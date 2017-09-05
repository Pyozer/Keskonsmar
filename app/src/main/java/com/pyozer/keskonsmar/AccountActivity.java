package com.pyozer.keskonsmar;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pyozer.keskonsmar.models.User;

public class AccountActivity extends BaseActivity {

    private final static String TAG = "AccountActivity";

    private Snackbar mSnackbar;
    private LinearLayout mAccountLayout;

    private AlertDialog.Builder builder;

    private FirebaseUser mUser;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        super.redirectToLogin = true; // On spécifie qu'il faut être connecté pour accéder ici

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mAccountLayout = findViewById(R.id.account_layout);

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

        mUser = mAuth.getCurrentUser();
        DatabaseReference myRef = mDatabase.child("users").child(mUser.getUid());
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);

                if (user != null) {
                    TextView pseudoText = findViewById(R.id.account_pseudo_text);
                    TextView emailText = findViewById(R.id.account_email_text);

                    pseudoText.setText(user.username);
                    emailText.setText(user.email);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    private void editPseudo() {
        builder = new AlertDialog.Builder(this);
        builder.setTitle("Modification du pseudo");

        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_edit_pseudo, null);
        builder.setView(dialogView);

        final EditText inputPseudo = dialogView.findViewById(R.id.input_new_pseudo);

        // Set up the buttons
        builder.setPositiveButton("Valider", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newPseudo = inputPseudo.getText().toString().trim();
                if (TextUtils.isEmpty(newPseudo)) {
                    mSnackbar = Snackbar.make(mAccountLayout, getString(R.string.error_field_required), Snackbar.LENGTH_LONG);
                    mSnackbar.show();
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

        final EditText inputEmail = dialogView.findViewById(R.id.input_new_email);

        // Set up the buttons
        builder.setPositiveButton("Valider", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String newEmail = inputEmail.getText().toString().trim();

                if (TextUtils.isEmpty(newEmail)) {
                    mSnackbar = Snackbar.make(mAccountLayout, getString(R.string.error_field_required), Snackbar.LENGTH_LONG);
                    mSnackbar.show();
                } else {
                    changeEmail(newEmail);
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

    private void changeEmail(final String newEmail) {
        mUser.updateEmail(newEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    mDatabase.child("users").child(mUser.getUid()).child("email").setValue(newEmail);

                    mSnackbar = Snackbar.make(mAccountLayout, getString(R.string.account_new_email_success), Snackbar.LENGTH_LONG);
                    mSnackbar.show();

                    Log.d(TAG, "User email address updated.");
                } else {
                    String error;
                    try {
                        throw task.getException();
                    } catch (Exception e) {
                        error = e.getMessage();
                    }
                    mSnackbar = Snackbar.make(mAccountLayout, error, Snackbar.LENGTH_LONG);
                    mSnackbar.show();
                }
            }
        });
    }

    private void editPassword() {

        builder = new AlertDialog.Builder(this);
        builder.setTitle("Modification du mot de passe");

        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_edit_password, null);
        builder.setView(dialogView);

        final EditText inputMdp = dialogView.findViewById(R.id.input_new_mdp);
        final EditText inputMdpConf = dialogView.findViewById(R.id.input_new_mdp_conf);

        // Set up the buttons
        builder.setPositiveButton("Valider", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                checkPasswordForm(inputMdp, inputMdpConf);
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

    private void checkPasswordForm(EditText inputMdp, EditText inputMdpConf) {
        inputMdp.setError(null);
        inputMdpConf.setError(null);

        String newMdp = inputMdp.getText().toString().trim();
        String newMdpConf = inputMdpConf.getText().toString().trim();

        boolean isCancel = false;

        if (TextUtils.isEmpty(newMdp)) {
            mSnackbar = Snackbar.make(mAccountLayout, getString(R.string.error_field_required), Snackbar.LENGTH_LONG);
            isCancel = true;
        }
        if (TextUtils.isEmpty(newMdpConf)) {
            mSnackbar = Snackbar.make(mAccountLayout, getString(R.string.error_field_required), Snackbar.LENGTH_LONG);
            isCancel = true;
        }
        if (newMdp.length() < AppConfig.MIN_PASS_LENGTH) {
            mSnackbar = Snackbar.make(mAccountLayout, getString(R.string.register_mdp_tcourt), Snackbar.LENGTH_LONG);
            isCancel = true;
        }
        if (!newMdp.equals(newMdpConf)) {
            mSnackbar = Snackbar.make(mAccountLayout, getString(R.string.register_mdp_dif), Snackbar.LENGTH_LONG);
            isCancel = true;
        }

        if (isCancel) {
            mSnackbar.show();
        } else {
            changePassword(newMdp);
        }
    }

    private void changePassword(String newPassword) {
        mUser.updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    mSnackbar = Snackbar.make(mAccountLayout, getString(R.string.account_new_mdp_success), Snackbar.LENGTH_LONG);
                    mSnackbar.show();
                    Log.d(TAG, "User password updated.");
                } else {
                    String error;
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthWeakPasswordException e) {
                        error = getString(R.string.error_weak_password);
                    } catch (Exception e) {
                        error = e.getMessage();
                    }
                    mSnackbar = Snackbar.make(mAccountLayout, error, Snackbar.LENGTH_LONG);
                    mSnackbar.show();
                }
            }
        });
    }
}