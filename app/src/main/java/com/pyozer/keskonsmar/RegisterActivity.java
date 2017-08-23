package com.pyozer.keskonsmar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
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

public class RegisterActivity extends AppCompatActivity {

    private EditText mInputUser;
    private EditText mInputPassword;
    private EditText mInputPasswordConf;

    private View mProgressView;
    private View mRegisterFormView;
    private RelativeLayout mRegisterLayout;

    private Snackbar mSnackbar;

    private JsonObjectRequest mAuthTask = null;

    private SharedPreferences autolog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mRegisterLayout = (RelativeLayout) findViewById(R.id.register_view);

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

        mRegisterFormView = findViewById(R.id.register_form);
        mProgressView = findViewById(R.id.register_progress);

        autolog = getSharedPreferences(Constants.PREF_KEY_ACCOUNT, MODE_PRIVATE);
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
        if (pass.length() < Constants.MIN_PASS_LENGTH) {
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

        showProgress(true);

        String url = Constants.ADDR_SERVER + "register.php?user=" + user + "&password=" + password;

        mAuthTask = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // the response is already constructed as a JSONObject!
                        mAuthTask = null;
                        showProgress(false);

                        try {
                            boolean isRegisterOk = response.getBoolean("status");

                            if (isRegisterOk) {

                                SharedPreferences.Editor editor = autolog.edit();
                                editor.putInt(Constants.PREF_KEY_ACCOUNT_ID, response.getInt("msg"));
                                editor.putString(Constants.PREF_KEY_ACCOUNT_PSEUDO, user);
                                editor.putString(Constants.PREF_KEY_ACCOUNT_PASSWORD, password);
                                editor.apply();

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

                        mAuthTask = null;
                        showProgress(false);

                        mSnackbar = Snackbar.make(mRegisterLayout, getString(R.string.error_http), Snackbar.LENGTH_LONG);
                        mSnackbar.show();
                    }
                });

        Volley.newRequestQueue(this).add(mAuthTask);
    }

    private void showProgress(final boolean show) {
        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
    }

}
