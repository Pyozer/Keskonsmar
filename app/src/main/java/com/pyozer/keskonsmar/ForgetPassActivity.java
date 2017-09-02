package com.pyozer.keskonsmar;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPassActivity extends BaseActivity {

    private EditText inputEmail;
    private Button btnReset, btnBack;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pass);

        inputEmail = findViewById(R.id.forgot_pass_email);
        btnReset = findViewById(R.id.forgot_pass_submit);
        btnBack = findViewById(R.id.forgot_pass_back);

        auth = FirebaseAuth.getInstance();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ForgetPassActivity.this, LoginActivity.class));
                finish();
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputEmail.setError(null);

                String email = inputEmail.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    inputEmail.setError(getString(R.string.error_field_required));
                } else {
                    showProgressDialog(getString(R.string.dialog_loading));

                    auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            hideProgressDialog();

                            if (task.isSuccessful()) {
                                Toast.makeText(ForgetPassActivity.this, getString(R.string.forgot_pass_sent), Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(ForgetPassActivity.this, getString(R.string.forgot_pass_sent_failed), Toast.LENGTH_LONG).show();
                            }
                        }
                    });

                }
            }
        });
    }

}