package com.pyozer.keskonsmar;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

public class AddJdmActivity extends AppCompatActivity {

    private EditText mInputJdm;

    private RelativeLayout mAddJdmLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_jdm);

        mAddJdmLayout = (RelativeLayout) findViewById(R.id.add_jdm_layout);

        mInputJdm = (EditText) findViewById(R.id.add_input_jdm);

        Button mSubmitButton = (Button) findViewById(R.id.add_action_submit);
        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewJdm();
            }
        });

    }

    private void addNewJdm() {
        String jdm = mInputJdm.getText().toString().trim();

        if(jdm.length() > 0) {
            // TODO: Envoyer le JDM au serveur
        } else {
            // TODO: Afficher une Snackbar avec un msg d'erreur
        }
    }
}