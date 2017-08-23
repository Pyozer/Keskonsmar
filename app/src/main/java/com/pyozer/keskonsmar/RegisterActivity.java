package com.pyozer.keskonsmar;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private EditText mInputUser;
    private EditText mInputPassword;
    private EditText mInputPasswordConf;
    private View mProgressView;
    private View mLoginFormView;

    private LinearLayout mRegisterLayout;
    private Snackbar mSnackbar;
    private JsonObjectRequest mAuthTask = null;

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
                    check_register(user , pass);
                } else {
                    mSnackbar = Snackbar.make(mRegisterLayout, getString(R.string.register_mdp_dif), Snackbar.LENGTH_LONG);
                    mSnackbar.show();
                }
            } else {
                mSnackbar = Snackbar.make(mRegisterLayout, getString(R.string.register_mdp_tcourt), Snackbar.LENGTH_LONG);
                mSnackbar.show();
            }
        } else {
            mSnackbar = Snackbar.make(mRegisterLayout, getString(R.string.register_champ_vide), Snackbar.LENGTH_LONG);
            mSnackbar.show();
        }

    }

    private void check_register(final String user, final String password) {

        String url = Constants.ADDR_SERVER + "register.php";

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
                                mSnackbar = Snackbar.make(mRegisterLayout, "INSCRIPTION REUSSI", Snackbar.LENGTH_LONG);
                                mSnackbar.show();
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
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                // the POST parameters:
                params.put("user", user);
                params.put("password", password);
                return params;
            }
        };

        Volley.newRequestQueue(this).add(mAuthTask);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

}
