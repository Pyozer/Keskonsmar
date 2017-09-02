package com.pyozer.keskonsmar;

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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pyozer.keskonsmar.models.User;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends BaseActivity {

    private final static String TAG = "LoginActivity";

    // UI references.
    private EditText mInputUser;
    private EditText mInputPassword;

    private Snackbar mSnackbar;

    private ScrollView mLoginLayout;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mLoginLayout = (ScrollView) findViewById(R.id.login_view);

        // Set up the login form.
        mInputUser = (EditText) findViewById(R.id.login_input_user);
        mInputPassword = (EditText) findViewById(R.id.login_input_password);

        findViewById(R.id.login_action_submit).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                validateForm();
            }
        });
        findViewById(R.id.login_to_register).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // Session manager
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        String extrasValue = getIntent().getStringExtra(AppConfig.INTENT_EXTRA_KEY);
        if (extrasValue != null) {
            mSnackbar = Snackbar.make(mLoginLayout, extrasValue, Snackbar.LENGTH_LONG);
            mSnackbar.show();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        onAuthSuccess(currentUser);
    }

    private void validateForm() {
        // Reset errors.
        mInputUser.setError(null);
        mInputPassword.setError(null);

        // Store values at the time of the login attempt.
        String user = mInputUser.getText().toString().trim();
        final String password = mInputPassword.getText().toString().trim();

        boolean cancel = false;

        if (TextUtils.isEmpty(user)) {
            mInputUser.setError(getString(R.string.error_field_required));
            cancel = true;
        }
        if (TextUtils.isEmpty(password)) {
            mInputPassword.setError(getString(R.string.error_field_required));
            cancel = true;
        }

        if (!cancel) {
            user += "@keskonsmar.fr";
            loginUser(user, password);
        }
    }

    private void loginUser(final String username, final String password) {

        showProgressDialog(getString(R.string.login_loader));

        mAuth.signInWithEmailAndPassword(username, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        hideProgressDialog();

                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");

                            FirebaseUser user = mAuth.getCurrentUser();
                            onAuthSuccess(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());

                            mSnackbar = Snackbar.make(mLoginLayout, getString(R.string.login_failed), Snackbar.LENGTH_LONG);
                            mSnackbar.show();
                        }
                    }
                });
    }

    private String usernameFromEmail(String email) {
        if (email.contains("@")) {
            return email.split("@")[0];
        } else {
            return email;
        }
    }

    private void onAuthSuccess(FirebaseUser user) {
        if (user != null) {
            String username = usernameFromEmail(user.getEmail());

            // Write new user
            writeNewUser(user.getUid(), username, user.getEmail());

            // Go to MainActivity
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }
    }

    private void writeNewUser(String userId, String name, String email) {
        User user = new User(name, email);

        mDatabase.child("users").child(userId).setValue(user);
    }
}