package com.pyozer.keskonsmar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

public class RegisterActivity extends AppCompatActivity {

    private EditText mInputUser;
    private EditText mInputPassword;
    private EditText mInputPasswordConf;

    private LinearLayout mRegisterLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mRegisterLayout = (LinearLayout) findViewById(R.id.register_view);

        mInputUser = (EditText) findViewById(R.id.register_input_user);
        mInputPassword = (EditText) findViewById(R.id.register_input_password);
        mInputPasswordConf = (EditText) findViewById(R.id.register_input_password_conf);

        Button mSubmitButton = (Button) findViewById(R.id.register_action_submit);
        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptRegister();
            }
        });
    }

    private void attemptRegister() {

        String user = mInputUser.getText().toString().trim();
        String pass = mInputPassword.getText().toString().trim();
        String passConf = mInputPasswordConf.getText().toString().trim();

        if(user.length() > 0 && pass.length() > 0 && passConf.length() > 0) {
            if(pass.length() >= Constants.MIN_PASS_LENGTH) {
                if(pass.equals(passConf)) {
                    // TODO: Faire la requete au serveur
                } else {
                    // TODO: Afficher erreur mot de passe non identiques
                }
            } else {
                // TODO: Afficher erreur mot de passe trop court
            }
        } else {
            // TODO: Afficher erreur champs non remplis
        }

    }
}
