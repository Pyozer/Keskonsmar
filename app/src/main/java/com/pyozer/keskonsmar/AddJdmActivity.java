package com.pyozer.keskonsmar;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
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

import java.util.HashMap;
import java.util.Map;

public class AddJdmActivity extends AppCompatActivity {

    private EditText mInputJdm;

    private RelativeLayout mAddJdmLayout;

    private Snackbar mSnackbar;

    private JsonObjectRequest mAuthTask = null;

    private View mProgressView;
    private View mLoginFormView;



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

        String url = Constants.ADDR_SERVER + "add_jdm.php";

        if(jdm.length() > 0) {
            // TODO: Envoyer le JDM au serveur
        } else {
            mSnackbar = Snackbar.make(mAddJdmLayout, getString(R.string.add_error), Snackbar.LENGTH_LONG);
            mSnackbar.show();
        }
    }

    private void check_jdm(final String user, final String password) {

        String url = Constants.ADDR_SERVER + "add_jdm.php";

        mAuthTask = new JsonObjectRequest
                (Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // the response is already constructed as a JSONObject!
                        mAuthTask = null;
                        showProgress(false);

                        try {
                            boolean isJDMOk = response.getBoolean("status");

                            if (isJDMOk) {
                                mSnackbar = Snackbar.make(mAddJdmLayout, getString(R.string.error_http), Snackbar.LENGTH_LONG);
                                mSnackbar.show();
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
                        showProgress(false);

                        mSnackbar = Snackbar.make(mAddJdmLayout, getString(R.string.error_http), Snackbar.LENGTH_LONG);
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