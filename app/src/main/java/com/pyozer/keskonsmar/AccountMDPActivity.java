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
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class AccountMDPActivity extends AppCompatActivity {

    private EditText mActualMdp;
    private EditText mNewMdp;
    private EditText mNewMdpConf;

    private ProgressDialog pDialog;

    private Snackbar mSnackbar;
    private LinearLayout mAccountMdpLayout;

    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_mdp);

        session = new SessionManager(getApplicationContext());
        if (!session.isLoggedIn()) {
            session.logout();

            Intent intent = new Intent(AccountMDPActivity.this, LoginActivity.class);
            intent.putExtra(AppConfig.INTENT_EXTRA_KEY, getString(R.string.snackbar_not_login));
            startActivity(intent);
            finish();
        }

        mAccountMdpLayout = (LinearLayout) findViewById(R.id.account_mdp_layout);

        mActualMdp = (EditText) findViewById(R.id.account_mdp_actual);
        mNewMdp = (EditText) findViewById(R.id.account_mdp_new);
        mNewMdpConf = (EditText) findViewById(R.id.account_mdp_new_conf);

        Button mEditSubmit = (Button) findViewById(R.id.account_mdp_submit);
        mEditSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editPassword();
            }
        });

        TextView mLinkToMain = (TextView) findViewById(R.id.account_mdp_cancel);
        mLinkToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // Progress dialog
        pDialog = new ProgressDialog(AccountMDPActivity.this);
        pDialog.setIndeterminate(true);
        pDialog.setMessage(getString(R.string.account_mdp_loader));
        pDialog.setCancelable(false);

    }

    private void editPassword() {
        String actualMdp = mActualMdp.getText().toString().trim();
        String newMdp = mNewMdp.getText().toString().trim();
        String newMdpConf = mNewMdpConf.getText().toString().trim();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(newMdpConf)) {
            mNewMdpConf.setError(getString(R.string.error_field_required));
            focusView = mNewMdpConf;
            cancel = true;
        }
        if (TextUtils.isEmpty(newMdp)) {
            mNewMdp.setError(getString(R.string.error_field_required));
            focusView = mNewMdp;
            cancel = true;
        }
        if (TextUtils.isEmpty(actualMdp)) {
            mActualMdp.setError(getString(R.string.error_field_required));
            focusView = mActualMdp;
            cancel = true;
        }
        if (newMdp.length() < AppConfig.MIN_PASS_LENGTH) {
            mNewMdp.setError(getString(R.string.register_mdp_tcourt));
            focusView = mNewMdp;
            cancel = true;
        }
        if (!newMdp.equals(newMdpConf)) {
            mNewMdpConf.setError(getString(R.string.register_mdp_dif));
            focusView = mNewMdpConf;
            cancel = true;
        }

        SessionManager session = new SessionManager(getApplicationContext());
        if (!actualMdp.equals(session.getPassword())) {
            mActualMdp.setError(getString(R.string.account_mdp_actual_wrong));
            focusView = mActualMdp;
            cancel = true;
        }

        if (cancel) focusView.requestFocus();
        else change_password(newMdp);

    }

    private void change_password(final String newMdp) {
        showDialog();

        String url = AppConfig.ADDR_SERVER + "change_password.php?user=" + session.getPseudo() + "&newpassword=" + newMdp;

        JsonObjectRequest mAuthTask = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // the response is already constructed as a JSONObject!
                        hideDialog();

                        try {
                            boolean isChangeMdpOk = response.getBoolean("status");

                            if (isChangeMdpOk) {

                                session.login(response.getInt("msg"), session.getPseudo(), newMdp);

                                mSnackbar = Snackbar.make(mAccountMdpLayout, getString(R.string.account_mdp_success), Snackbar.LENGTH_LONG);
                                mSnackbar.show();

                                mActualMdp.setText("");
                                mNewMdp.setText("");
                                mNewMdpConf.setText("");
                            } else {
                                mSnackbar = Snackbar.make(mAccountMdpLayout, response.getString("msg"), Snackbar.LENGTH_LONG);
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

                        mSnackbar = Snackbar.make(mAccountMdpLayout, getString(R.string.error_http), Snackbar.LENGTH_LONG);
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
