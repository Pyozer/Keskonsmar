package com.pyozer.keskonsmar;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private Fragment fragment;
    private FragmentManager fragmentManager;
    private int lastFragmentLoad = 0;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_recent:
                    if (item.getItemId() != lastFragmentLoad) {
                        fragment = JDMFragment.newInstance(false);
                    }
                    break;
                case R.id.navigation_trending:
                    if (item.getItemId() != lastFragmentLoad) {
                        fragment = JDMFragment.newInstance(true);
                    }
                    break;
                case R.id.navigation_account:
                    if (item.getItemId() != lastFragmentLoad) {
                        fragment = new AccountFragment();
                    }
                    break;
            }
            lastFragmentLoad = item.getItemId();
            loadFragment(fragment);
            return true;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentManager = getSupportFragmentManager();

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

        loadFragment(JDMFragment.newInstance(false));

    }

    private void loadFragment(Fragment fragment) {
        final FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.main_content, fragment).commit();
    }

}