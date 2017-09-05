package com.pyozer.keskonsmar;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.pyozer.keskonsmar.fragments.JDMFragment;
import com.pyozer.keskonsmar.fragments.UserJdmFragment;

public class AccountJDMActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_jdm);

        super.redirectToLogin = true;

        FragmentManager mFragmentManager = getSupportFragmentManager();

        JDMFragment mJdmFragment = UserJdmFragment.newInstance(FirebaseAuth.getInstance().getCurrentUser().getUid());

        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.replace(R.id.account_jdm_content, mJdmFragment).commit();
    }
}