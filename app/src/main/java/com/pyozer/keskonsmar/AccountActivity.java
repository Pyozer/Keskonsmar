package com.pyozer.keskonsmar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthEmailException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AccountActivity extends BaseActivity {

    private final static String TAG = "AccountActivity";

    private Snackbar mSnackbar;
    private LinearLayout mAccountLayout;

    private AlertDialog.Builder builder;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mAccountLayout = findViewById(R.id.account_mdp_layout);

        findViewById(R.id.account_pseudo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editPseudo();
            }
        });

        findViewById(R.id.account_email).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editEmail();
            }
        });
        findViewById(R.id.account_password).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editPassword();
            }
        });

        builder = new AlertDialog.Builder(this);

        DatabaseReference myRef = mDatabase.child("users");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);

                Log.d(TAG, "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        mUser = mAuth.getCurrentUser();
        if (mUser == null) {
            Intent intent = new Intent(AccountActivity.this, LoginActivity.class);
            intent.putExtra(AppConfig.INTENT_EXTRA_KEY, getString(R.string.snackbar_not_login));
            startActivity(intent);
            finish();
        }
    }

    private void editPseudo() {
        builder = new AlertDialog.Builder(this);
        builder.setTitle("Modification du pseudo");

        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_edit_pseudo, null);
        builder.setView(dialogView);

        final EditText inputPseudo = findViewById(R.id.input_new_pseudo);

        // Set up the buttons
        builder.setPositiveButton("Valider", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newPseudo = inputPseudo.getText().toString();
                if (TextUtils.isEmpty(newPseudo)) {
                    inputPseudo.setError(getString(R.string.error_field_required));
                } else {
                    mDatabase.child("users").child(mUser.getUid()).child("username").setValue(newPseudo);
                }
            }
        });
        builder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void editEmail() {
        builder = new AlertDialog.Builder(this, R.style.Theme_AppCompat_Dialog_Alert);
        builder.setTitle("Modification de l'adresse mail");

        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_edit_email, null);
        builder.setView(dialogView);

        final EditText inputEmail = findViewById(R.id.input_new_email);

        // Set up the buttons
        builder.setPositiveButton("Valider", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newEmail = inputEmail.getText().toString();

                if (TextUtils.isEmpty(newEmail)) {
                    inputEmail.setError(getString(R.string.error_field_required));
                } else {
                    mUser.updateEmail(newEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                mSnackbar = Snackbar.make(mAccountLayout, getString(R.string.account_new_email_success), Snackbar.LENGTH_LONG);
                                mSnackbar.show();
                                Log.d(TAG, "User email address updated.");
                            } else {
                                try {
                                    throw task.getException();
                                } catch (FirebaseAuthEmailException e) {
                                    mSnackbar = Snackbar.make(mAccountLayout, getString(R.string.error_not_email), Snackbar.LENGTH_LONG);
                                    mSnackbar.show();
                                } catch (Exception e) {
                                    Log.e(TAG, e.getMessage());
                                }
                            }
                        }
                    });
                }
            }
        });
        builder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void editPassword() {

        builder = new AlertDialog.Builder(this);
        builder.setTitle("Modification du mot de passe");

        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_edit_password, null);
        builder.setView(dialogView);

        final EditText inputMdp = findViewById(R.id.input_new_mdp);
        final EditText inputMdpConf = findViewById(R.id.input_new_mdp_conf);

        // Set up the buttons
        builder.setPositiveButton("Valider", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                inputMdp.setError(null);
                inputMdpConf.setError(null);

                String newMdp = inputMdp.getText().toString().trim();
                String newMdpConf = inputMdpConf.getText().toString().trim();

                boolean isCancel = false;

                if (TextUtils.isEmpty(newMdpConf)) {
                    inputMdp.setError(getString(R.string.error_field_required));
                    isCancel = true;
                }
                if (TextUtils.isEmpty(newMdp)) {
                    inputMdpConf.setError(getString(R.string.error_field_required));
                    isCancel = true;
                }
                if (newMdp.length() < AppConfig.MIN_PASS_LENGTH) {
                    inputMdp.setError(getString(R.string.register_mdp_tcourt));
                    isCancel = true;
                }
                if (!newMdp.equals(newMdpConf)) {
                    inputMdpConf.setError(getString(R.string.register_mdp_dif));
                    isCancel = true;
                }

                if (!isCancel) {
                    mUser.updatePassword(newMdp).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                mSnackbar = Snackbar.make(mAccountLayout, getString(R.string.account_new_mdp_success), Snackbar.LENGTH_LONG);
                                mSnackbar.show();
                                Log.d(TAG, "User password updated.");
                            } else {
                                try {
                                    throw task.getException();
                                } catch (FirebaseAuthWeakPasswordException e) {
                                    mSnackbar = Snackbar.make(mAccountLayout, getString(R.string.error_weak_password), Snackbar.LENGTH_LONG);
                                    mSnackbar.show();
                                } catch (Exception e) {
                                    Log.e(TAG, e.getMessage());
                                }
                            }
                        }
                    });
                }
            }
        });
        builder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }
}