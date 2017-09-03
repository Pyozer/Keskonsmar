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

public class AccountJDMActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_jdm);

        mAuth = FirebaseAuth.getInstance();

        FragmentManager mFragmentManager = getSupportFragmentManager();

        JDMFragment mJdmFragment = UserJdmFragment.newInstance(mAuth.getCurrentUser().getUid());

        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.replace(R.id.account_jdm_content, mJdmFragment).commit();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser == null) {
            Intent intent = new Intent(AccountJDMActivity.this, LoginActivity.class);
            intent.putExtra(AppConfig.INTENT_EXTRA_KEY, getString(R.string.snackbar_not_login));
            startActivity(intent);
            finish();
        }
    }
}