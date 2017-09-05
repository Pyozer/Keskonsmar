package com.pyozer.keskonsmar.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.pyozer.keskonsmar.R;
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