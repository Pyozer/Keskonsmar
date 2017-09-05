package com.pyozer.keskonsmar;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.pyozer.keskonsmar.fragments.LastJdmFragment;
import com.pyozer.keskonsmar.fragments.TopJdmFragment;
import com.pyozer.keskonsmar.fragments.WorstJdmFragment;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Fragment mFragment;
    private FragmentManager mFragmentManager;

    private FirebaseAuth mAuth;

    private int lastFragmentLoad = R.id.navigation_recent;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_recent:
                    if (item.getItemId() != lastFragmentLoad)
                        mFragment = new LastJdmFragment();
                    break;
                case R.id.navigation_trending:
                    if (item.getItemId() != lastFragmentLoad)
                        mFragment = new TopJdmFragment();
                    break;
                case R.id.navigation_worst:
                    if (item.getItemId() != lastFragmentLoad)
                        mFragment = new WorstJdmFragment();
                    break;
            }
            lastFragmentLoad = item.getItemId();
            loadFragment(mFragment);
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        super.redirectToLogin = true; // On spécifie qu'il faut être connecté pour accéder ici

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mFragmentManager = getSupportFragmentManager();

        CoordinatorLayout mCoordLayout = findViewById(R.id.coordinatorlayout);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(MainActivity.this, AddJdmActivity.class);
                startActivity(in);
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mAuth = FirebaseAuth.getInstance();

        loadFragment(new LastJdmFragment());

        String extrasValue = getIntent().getStringExtra(AppConfig.INTENT_EXTRA_KEY);
        if (extrasValue != null) {
            Snackbar mSnackbar = Snackbar.make(mCoordLayout, extrasValue, Snackbar.LENGTH_LONG);
            mSnackbar.show();
        }
    }

    private void loadFragment(Fragment fragment) {
        final FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.replace(R.id.main_content, fragment).commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_about) {
            Intent intent = new Intent(MainActivity.this, AboutActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_account_jdm) {
            Intent intent = new Intent(MainActivity.this, AccountJDMActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_account_mdp) {
            Intent intent = new Intent(MainActivity.this, AccountActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_logout) {
            mAuth.signOut();

            showProgressDialog(getString(R.string.logout_loader));

            // On met un timer de 1,5sec
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    intent.putExtra(AppConfig.INTENT_EXTRA_KEY, getString(R.string.snackbar_logout_success));
                    startActivity(intent);
                    finish();
                }
            }, 1000);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}