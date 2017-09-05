package com.pyozer.keskonsmar;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ScrollView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthEmailException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pyozer.keskonsmar.models.User;

public class RegisterActivity extends BaseActivity {

    private final static String TAG = "RegisterActivity";

    private EditText mInputUser;
    private EditText mInputEmail;
    private EditText mInputPassword;
    private EditText mInputPasswordConf;

    private ScrollView mRegisterLayout;

    private Snackbar mSnackbar;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mRegisterLayout = findViewById(R.id.register_view);

        mInputUser = findViewById(R.id.register_input_user);
        mInputEmail = findViewById(R.id.register_input_email);
        mInputPassword = findViewById(R.id.register_input_password);
        mInputPasswordConf = findViewById(R.id.register_input_password_conf);

        findViewById(R.id.register_action_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateForm();
            }
        });
        findViewById(R.id.register_to_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    private void validateForm() {

        mInputUser.setError(null);
        mInputEmail.setError(null);
        mInputPassword.setError(null);
        mInputPasswordConf.setError(null);

        String user = mInputUser.getText().toString().trim();
        String email = mInputEmail.getText().toString().trim();
        String pass = mInputPassword.getText().toString().trim();
        String passConf = mInputPasswordConf.getText().toString().trim();

        boolean cancel = false;

        if (TextUtils.isEmpty(user)) {
            mInputUser.setError(getString(R.string.error_field_required));
            cancel = true;
        }
        if (TextUtils.isEmpty(email)) {
            mInputEmail.setError(getString(R.string.error_field_required));
            cancel = true;
        }
        if (TextUtils.isEmpty(pass)) {
            mInputPassword.setError(getString(R.string.error_field_required));
            cancel = true;
        }
        if (TextUtils.isEmpty(passConf)) {
            mInputPasswordConf.setError(getString(R.string.error_field_required));
            cancel = true;
        }

        if (pass.length() < AppConfig.MIN_PASS_LENGTH) {
            mInputPassword.setError(String.format(getString(R.string.register_mdp_tcourt), AppConfig.MIN_PASS_LENGTH));
            cancel = true;
        }
        if (!pass.equals(passConf)) {
            mInputPasswordConf.setError(getString(R.string.register_mdp_dif));
            cancel = true;
        }

        if (!cancel) registerUser(user, email, pass);
    }

    private void registerUser(final String username, final String email, final String password) {

        showProgressDialog(getString(R.string.register_loader));

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                hideProgressDialog();

                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success");
                    FirebaseUser userFirebase = mAuth.getCurrentUser();

                    // On enregistre l'utilisateur sur la Database (pour avoir son pseudo)
                    writeNewUser(userFirebase.getUid(), username, userFirebase.getEmail());

                    onAuthSuccess(userFirebase);
                } else {
                    String error = getString(R.string.register_failed);
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthWeakPasswordException e) {
                        error = getString(R.string.error_weak_password);
                    } catch (FirebaseAuthUserCollisionException e) {
                        error = getString(R.string.error_user_exists);
                    } catch (FirebaseAuthEmailException e) {
                        error = getString(R.string.error_not_email);
                    } catch (Exception e) {
                        error = e.getMessage();
                    }
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.getException());

                    mSnackbar = Snackbar.make(mRegisterLayout, error, Snackbar.LENGTH_LONG);
                    mSnackbar.show();
                }
            }
        });
    }

    private void writeNewUser(String userId, String name, String email) {
        User user = new User(name, email);

        mDatabase.child("users").child(userId).setValue(user);
    }
}