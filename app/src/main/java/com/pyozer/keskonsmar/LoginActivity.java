package com.pyozer.keskonsmar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
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

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    // UI references.
    private EditText mInputUser;
    private EditText mInputPassword;

    private ProgressDialog pDialog;

    private Snackbar mSnackbar;

    private ScrollView mLoginLayout;

    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mLoginLayout = (ScrollView) findViewById(R.id.login_view);

        // Set up the login form.
        mInputUser = (EditText) findViewById(R.id.login_input_user);
        mInputPassword = (EditText) findViewById(R.id.login_input_password);

        Button mSubmitButton = (Button) findViewById(R.id.login_action_submit);
        mSubmitButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        TextView mLinkToRegister = (TextView) findViewById(R.id.login_to_register);
        mLinkToRegister.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // Progress dialog
        pDialog = new ProgressDialog(LoginActivity.this, R.style.Theme_AppCompat_Dialog);
        pDialog.setIndeterminate(true);
        pDialog.setMessage("Connexion...");
        pDialog.setCancelable(false);

        // Session manager
        session = new SessionManager(getApplicationContext());

        // Check if user is already logged in or not
        /*if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }*/
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made
     */
    private void attemptLogin() {

        // Reset errors.
        mInputUser.setError(null);
        mInputPassword.setError(null);

        // Store values at the time of the login attempt.
        String user = mInputUser.getText().toString();
        String password = mInputPassword.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password.
        if (TextUtils.isEmpty(password)) {
            mInputPassword.setError(getString(R.string.error_field_required));
            focusView = mInputPassword;
            cancel = true;
        }

        // Check for a valid pseudo
        if (TextUtils.isEmpty(user)) {
            mInputUser.setError(getString(R.string.error_field_required));
            focusView = mInputUser;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            check_login(user, password);
        }
    }

    private void check_login(final String user, final String password) {

        showDialog();

        String url = AppConfig.ADDR_SERVER + "check_login.php?user=" + user + "&password=" + password;

        JsonObjectRequest mAuthTask = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        hideDialog();

                        try {
                            boolean isLoginOk = response.getBoolean("status");

                            if (isLoginOk) {

                                session.login(response.getInt("msg"), user, password);

                                Intent in = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(in);
                                finish();

                            } else {
                                mSnackbar = Snackbar.make(mLoginLayout, response.getString("msg"), Snackbar.LENGTH_LONG);
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

                        mSnackbar = Snackbar.make(mLoginLayout, getString(R.string.error_http), Snackbar.LENGTH_LONG);
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