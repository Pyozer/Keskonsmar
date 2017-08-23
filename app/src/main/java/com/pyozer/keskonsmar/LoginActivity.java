package com.pyozer.keskonsmar;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private JsonObjectRequest mAuthTask = null;

    // UI references.
    private EditText mInputUser;
    private EditText mInputPassword;
    private View mProgressView;
    private View mLoginFormView;

    private Snackbar mSnackbar;

    private RelativeLayout mLoginLayout;

    private SharedPreferences autolog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mLoginLayout = (RelativeLayout) findViewById(R.id.login_view);

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

        Button mRegisterButton = (Button) findViewById(R.id.login_register);
        mRegisterButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLoginRegister();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        autolog = getSharedPreferences(Constants.PREF_KEY_ACCOUNT, Context.MODE_PRIVATE);

        String userGetPref = autolog.getString(Constants.PREF_KEY_ACCOUNT_PSEUDO, null);
        String passGetPref = autolog.getString(Constants.PREF_KEY_ACCOUNT_PASSWORD, null);

        /*if (userGetPref != null && passGetPref != null) {
            mInputUser.setText(userGetPref);
            mInputPassword.setText(passGetPref);

            check_login(userGetPref, passGetPref);
        }*/
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made
     */

    private void attemptLoginRegister(){
        Intent in = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(in);
    }
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

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

        showProgress(true);

        String url = Constants.ADDR_SERVER + "check_login.php?user=" + user + "&password=" + password;

        mAuthTask = new JsonObjectRequest
                (Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // the response is already constructed as a JSONObject!
                        mAuthTask = null;
                        showProgress(false);

                        try {
                            boolean isLoginOk = response.getBoolean("status");

                            if (isLoginOk) {
                                SharedPreferences.Editor editor = autolog.edit();
                                editor.putInt(Constants.PREF_KEY_ACCOUNT_ID, response.getInt("msg"));
                                editor.putString(Constants.PREF_KEY_ACCOUNT_PSEUDO, user);
                                editor.putString(Constants.PREF_KEY_ACCOUNT_PASSWORD, password);
                                editor.apply();

                                Intent in = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(in);

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

                        mAuthTask = null;
                        showProgress(false);

                        mSnackbar = Snackbar.make(mLoginLayout, getString(R.string.error_http), Snackbar.LENGTH_LONG);
                        mSnackbar.show();
                    }
                });

        Volley.newRequestQueue(this).add(mAuthTask);
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    private void showProgress(final boolean show) {
        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
    }
}