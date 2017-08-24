package com.pyozer.keskonsmar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class AddJdmActivity extends AppCompatActivity {

    private EditText mInputJdm;

    private RelativeLayout mAddJdmLayout;

    private Snackbar mSnackbar;

    private JsonObjectRequest mAuthTask = null;


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

        Button mCancelButton = (Button) findViewById(R.id.add_action_cancel);
        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), JDMFragment.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void addNewJdm() {
        String jdm = mInputJdm.getText().toString().trim();

        SharedPreferences sharedPref = getSharedPreferences(Constants.PREF_KEY_ACCOUNT, MODE_PRIVATE);
        int idUser = sharedPref.getInt(Constants.PREF_KEY_ACCOUNT_ID, -1);

        if(idUser != -1) {
            if (jdm.length() > 0) {
                check_jdm(idUser, jdm);
            } else {
                mSnackbar = Snackbar.make(mAddJdmLayout, getString(R.string.add_error), Snackbar.LENGTH_LONG);
                mSnackbar.show();
            }
        } else {
            mSnackbar = Snackbar.make(mAddJdmLayout, getString(R.string.error_account_unknown), Snackbar.LENGTH_LONG);
            mSnackbar.show();
        }

    }

    private void check_jdm(final int idUser, final String jdm) {

        String url = Constants.ADDR_SERVER + "add_jdm.php?user=" + idUser + "&jdm=" + jdm;

        mAuthTask = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // the response is already constructed as a JSONObject!
                        mAuthTask = null;

                        try {
                            boolean isJdmOk = response.getBoolean("status");

                            if (isJdmOk) {
                                mSnackbar = Snackbar.make(mAddJdmLayout, getString(R.string.add_send_success), Snackbar.LENGTH_LONG);
                                mSnackbar.show();
                                Intent in = new Intent(AddJdmActivity.this, MainActivity.class);
                                startActivity(in);
                            } else {
                                mSnackbar = Snackbar.make(mAddJdmLayout, response.getString("msg"), Snackbar.LENGTH_LONG);
                                mSnackbar.show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();

                        mAuthTask = null;

                        mSnackbar = Snackbar.make(mAddJdmLayout, getString(R.string.error_http), Snackbar.LENGTH_LONG);
                        mSnackbar.show();
                    }
                });

        Volley.newRequestQueue(this).add(mAuthTask);
    }

}