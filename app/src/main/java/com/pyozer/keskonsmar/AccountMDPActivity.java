package com.pyozer.keskonsmar;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AccountMDPActivity extends BaseActivity {

    private EditText mNewMdp;
    private EditText mNewMdpConf;

    private Snackbar mSnackbar;
    private LinearLayout mAccountMdpLayout;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_mdp);

        mAuth = FirebaseAuth.getInstance();

        mAccountMdpLayout = (LinearLayout) findViewById(R.id.account_mdp_layout);

        mNewMdp = (EditText) findViewById(R.id.account_mdp_new);
        mNewMdpConf = (EditText) findViewById(R.id.account_mdp_new_conf);

        findViewById(R.id.account_mdp_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editPassword();
            }
        });

        findViewById(R.id.account_mdp_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Intent intent = new Intent(AccountMDPActivity.this, LoginActivity.class);
            intent.putExtra(AppConfig.INTENT_EXTRA_KEY, getString(R.string.snackbar_not_login));
            startActivity(intent);
            finish();
        }
    }

    private void editPassword() {

        mNewMdp.setError(null);
        mNewMdpConf.setError(null);

        String newMdp = mNewMdp.getText().toString().trim();
        String newMdpConf = mNewMdpConf.getText().toString().trim();

        boolean cancel = false;

        if (TextUtils.isEmpty(newMdpConf)) {
            mNewMdpConf.setError(getString(R.string.error_field_required));
            cancel = true;
        }
        if (TextUtils.isEmpty(newMdp)) {
            mNewMdp.setError(getString(R.string.error_field_required));
            cancel = true;
        }
        if (newMdp.length() < AppConfig.MIN_PASS_LENGTH) {
            mNewMdp.setError(getString(R.string.register_mdp_tcourt));
            cancel = true;
        }
        if (!newMdp.equals(newMdpConf)) {
            mNewMdpConf.setError(getString(R.string.register_mdp_dif));
            cancel = true;
        }

        if (!cancel) change_password(newMdp);

    }

    private void change_password(final String newMdp) {
        showProgressDialog(getString(R.string.account_mdp_loader));

    }

}