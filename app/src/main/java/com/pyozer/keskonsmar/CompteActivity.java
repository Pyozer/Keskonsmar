package com.pyozer.keskonsmar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class CompteActivity extends AppCompatActivity {

    private SessionManager session;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compte);

        TextView pseudo = (TextView) findViewById(R.id.pseudo);
        pseudo.setText(session.getPseudo());
    }
}
