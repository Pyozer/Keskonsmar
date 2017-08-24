package com.pyozer.keskonsmar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {

    private EditText mInputUser;
    private EditText mInputPassword;
    private EditText mInputPasswordConf;

    private ProgressDialog pDialog;

    private ScrollView mRegisterLayout;

    private Snackbar mSnackbar;

    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mRegisterLayout = (ScrollView) findViewById(R.id.register_view);

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

        TextView mLinkToLogin = (TextView) findViewById(R.id.register_to_login);
        mLinkToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // Progress dialog
        pDialog = new ProgressDialog(RegisterActivity.this, R.style.Theme_AppCompat_Dialog);
        pDialog.setIndeterminate(true);
        pDialog.setMessage("Inscription...");
        pDialog.setCancelable(false);

        // Session manager
        session = new SessionManager(getApplicationContext());

        // Check if user is already logged in or not
        /*if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }*/
    }

    private void attemptRegister() {

        String user = mInputUser.getText().toString().trim();
        String pass = mInputPassword.getText().toString().trim();
        String passConf = mInputPasswordConf.getText().toString().trim();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(passConf)) {
            mInputPasswordConf.setError(getString(R.string.error_field_required));
            focusView = mInputPasswordConf;
            cancel = true;
        }
        if (TextUtils.isEmpty(pass)) {
            mInputPassword.setError(getString(R.string.error_field_required));
            focusView = mInputPassword;
            cancel = true;
        }
        if (TextUtils.isEmpty(user)) {
            mInputUser.setError(getString(R.string.error_field_required));
            focusView = mInputUser;
            cancel = true;
        }
        if (pass.length() < AppConfig.MIN_PASS_LENGTH) {
            mInputPassword.setError(getString(R.string.register_mdp_tcourt));
            focusView = mInputPassword;
            cancel = true;
        }
        if (!pass.equals(passConf)) {
            mInputPasswordConf.setError(getString(R.string.register_mdp_dif));
            focusView = mInputPasswordConf;
            cancel = true;
        }

        if (cancel) focusView.requestFocus();
        else check_register(user, pass);

    }

    private void check_register(final String user, final String password) {

        showDialog();

        String url = AppConfig.ADDR_SERVER + "register.php?user=" + user + "&password=" + password;

        JsonObjectRequest mAuthTask = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // the response is already constructed as a JSONObject!
                        hideDialog();

                        try {
                            boolean isRegisterOk = response.getBoolean("status");

                            if (isRegisterOk) {

                                session.login(response.getInt("msg"), user, password);

                                Intent in = new Intent(RegisterActivity.this, MainActivity.class);
                                startActivity(in);
                            } else {
                                mSnackbar = Snackbar.make(mRegisterLayout, response.getString("msg"), Snackbar.LENGTH_LONG);
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

                        hideDialog();

                        mSnackbar = Snackbar.make(mRegisterLayout, getString(R.string.error_http), Snackbar.LENGTH_LONG);
                        mSnackbar.show();
                    }
                });

        Volley.newRequestQueue(this).add(mAuthTask);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

}
