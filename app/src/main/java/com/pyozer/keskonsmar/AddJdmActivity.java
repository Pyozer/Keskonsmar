package com.pyozer.keskonsmar;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.pyozer.keskonsmar.models.JeuDeMot;
import com.pyozer.keskonsmar.models.User;

import java.util.HashMap;
import java.util.Map;

public class AddJdmActivity extends BaseActivity {

    private final static String TAG = "AddJdmActivity";

    private EditText mInputJdm;
    private Button mSubmitButton;

    private LinearLayout mAddJdmLayout;

    private Snackbar mSnackbar;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_jdm);

        super.redirectToLogin = true;

        mAddJdmLayout = findViewById(R.id.add_jdm_layout);

        mInputJdm = findViewById(R.id.add_input_jdm);

        mSubmitButton = findViewById(R.id.add_action_submit);
        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewJdm();
            }
        });

        findViewById(R.id.add_action_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    private void addNewJdm() {
        mInputJdm.setError(null);

        String jdm = mInputJdm.getText().toString().trim();

        if (TextUtils.isEmpty(jdm)) {
            mInputJdm.setError(getString(R.string.error_field_required));
        } else {
            sendNewJdm(jdm);
        }
    }

    private void sendNewJdm(final String jdm) {
        setEditingEnabled(false);

        final String userId = mAuth.getCurrentUser().getUid();

        mDatabase.child("users").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get user value
                User user = dataSnapshot.getValue(User.class);

                if (user == null) {
                    // User is null, error out
                    Log.e(TAG, "User " + userId + " is unexpectedly null");

                    mSnackbar = Snackbar.make(mAddJdmLayout, getString(R.string.error_account_unknown), Snackbar.LENGTH_LONG);
                    mSnackbar.show();
                } else {
                    // Write new post
                    writeNewPost(userId, user, jdm);
                }

                // Finish this Activity, back to the stream
                setEditingEnabled(true);

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "getUser:onCancelled", databaseError.toException());

                setEditingEnabled(true);
            }
        });
    }

    private void writeNewPost(String userId, User user, String jdm) {
        String key = mDatabase.child("posts").push().getKey();
        JeuDeMot jeuDeMot = new JeuDeMot(userId, user.username, jdm);

        Map<String, Object> jdmValues = jeuDeMot.toMap();
        jdmValues.put("timestamp", ServerValue.TIMESTAMP);

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/posts/" + key, jdmValues);

        mDatabase.updateChildren(childUpdates);
    }

    private void setEditingEnabled(boolean enabled) {
        mInputJdm.setEnabled(enabled);
        if (enabled) {
            mSubmitButton.setVisibility(View.VISIBLE);
        } else {
            mSubmitButton.setVisibility(View.GONE);
        }
    }
}