package com.pyozer.keskonsmar.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ScrollView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthEmailException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.pyozer.keskonsmar.AppConfig;
import com.pyozer.keskonsmar.R;

public class LoginActivity extends BaseActivity {

    private final static String TAG = "LoginActivity";

    // UI references.
    private EditText mInputEmail;
    private EditText mInputPassword;

    private Snackbar mSnackbar;
    private ScrollView mLoginLayout;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mLoginLayout = findViewById(R.id.login_view);

        // Set up the login form.
        mInputEmail = findViewById(R.id.login_input_email);
        mInputPassword = findViewById(R.id.login_input_password);

        findViewById(R.id.login_action_submit).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                validateForm();
            }
        });
        findViewById(R.id.login_to_register).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
                finish();
            }
        });
        findViewById(R.id.login_forget_pass).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ForgetPassActivity.class));
            }
        });

        // Session manager
        mAuth = FirebaseAuth.getInstance();

        String extrasValue = getIntent().getStringExtra(AppConfig.INTENT_EXTRA_KEY);
        if (extrasValue != null) {
            mSnackbar = Snackbar.make(mLoginLayout, extrasValue, Snackbar.LENGTH_LONG);
            mSnackbar.show();
        }
    }

    private void validateForm() {
        // Reset errors.
        mInputEmail.setError(null);
        mInputPassword.setError(null);

        // Store values at the time of the login attempt.
        String user = mInputEmail.getText().toString().trim();
        String password = mInputPassword.getText().toString().trim();

        boolean cancel = false;

        if (TextUtils.isEmpty(user)) {
            mInputEmail.setError(getString(R.string.error_field_required));
            cancel = true;
        }
        if (TextUtils.isEmpty(password)) {
            mInputPassword.setError(getString(R.string.error_field_required));
            cancel = true;
        }

        if (!cancel) loginUser(user, password);
    }

    private void loginUser(final String username, final String password) {

        showProgressDialog(getString(R.string.login_loader));

        mAuth.signInWithEmailAndPassword(username, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                hideProgressDialog();

                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success");

                    onAuthSuccess(mAuth.getCurrentUser());
                } else {
                    String error = getString(R.string.error_login_failed);
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        error = getString(R.string.error_login_failed);
                    } catch (FirebaseAuthInvalidUserException e) {
                        error = getString(R.string.error_account_unknown);
                    } catch (FirebaseAuthEmailException e) {
                        error = getString(R.string.error_not_email);
                    } catch (Exception e) {
                        error = e.getMessage();
                    }
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.getException());

                    mSnackbar = Snackbar.make(mLoginLayout, error, Snackbar.LENGTH_LONG);
                    mSnackbar.show();
                }
            }
        });
    }
}