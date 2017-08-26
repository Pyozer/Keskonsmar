package com.pyozer.keskonsmar;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class AccountJDMActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_jdm);

        SessionManager session = new SessionManager(getApplicationContext());

        FragmentManager mFragmentManager = getSupportFragmentManager();

        JDMFragment mJdmFragment = JDMFragment.newInstance(AppConfig.TYPE_DATA_USER, session.getPseudo());

        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.replace(R.id.account_jdm_content, mJdmFragment).commit();
    }
}