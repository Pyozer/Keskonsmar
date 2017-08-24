package com.pyozer.keskonsmar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInstaller;
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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Fragment mFragment;
    private FragmentManager mFragmentManager;

    private int lastFragmentLoad = 0;

    private CoordinatorLayout mCoordLayout;
    private Snackbar mSnackbar;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_recent:
                    if (item.getItemId() != lastFragmentLoad) {
                        mFragment = JDMFragment.newInstance(false);
                    }
                    break;
                case R.id.navigation_trending:
                    if (item.getItemId() != lastFragmentLoad) {
                        mFragment = JDMFragment.newInstance(true);
                    }
                    break;
                case R.id.navigation_versus:
                    if (item.getItemId() != lastFragmentLoad) {
                        mFragment = new AccountFragment();
                    }
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
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SessionManager session = new SessionManager(getApplicationContext());
        if(!session.isLoggedIn()) {
            session.logout();

            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.putExtra(AppConfig.INTENT_EXTRA_KEY, getString(R.string.snackbar_not_login));
            startActivity(intent);
            finish();
        }

        mFragmentManager = getSupportFragmentManager();

        mCoordLayout = (CoordinatorLayout) findViewById(R.id.coordinatorlayout);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(MainActivity.this, AddJdmActivity.class);
                startActivity(in);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        loadFragment(JDMFragment.newInstance(false));

        String extrasValue = getIntent().getStringExtra(AppConfig.INTENT_EXTRA_KEY);
        if (extrasValue != null) {
            mSnackbar = Snackbar.make(mCoordLayout, extrasValue, Snackbar.LENGTH_LONG);
            mSnackbar.show();
        }
    }

    private void loadFragment(Fragment fragment) {
        final FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.replace(R.id.main_content, fragment).commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_account) {

        } else if (id == R.id.nav_about) {

        } else if(id == R.id.nav_logout) {
            SessionManager session = new SessionManager(getApplicationContext());

            session.logout();

            ProgressDialog pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setIndeterminate(true);
            pDialog.setMessage(getString(R.string.logout_loader));
            pDialog.setCancelable(false);
            pDialog.show();

            // On met un timer de 1,5sec
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    intent.putExtra(AppConfig.INTENT_EXTRA_KEY, getString(R.string.snackbar_logout_success));
                    startActivity(intent);
                    finish();
                }
            }, 1500);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}