package com.pyozer.keskonsmar;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class CompteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compte);

        SessionManager session = new SessionManager(getApplicationContext());
        if(!session.isLoggedIn()) {
            session.logout();

            Intent intent = new Intent(CompteActivity.this, LoginActivity.class);
            intent.putExtra(AppConfig.INTENT_EXTRA_KEY, getString(R.string.snackbar_not_login));
            startActivity(intent);
            finish();
        }

        TextView pseudo = (TextView) findViewById(R.id.pseudo);
        pseudo.setText(session.getPseudo());
    }
}
