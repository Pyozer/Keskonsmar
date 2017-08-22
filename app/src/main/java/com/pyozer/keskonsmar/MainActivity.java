package com.pyozer.keskonsmar;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private JeuDeMotAdapter mJdmAdapter;
    private List<JeuDeMot> mListJdm = new ArrayList<>();

    private Snackbar mSnackbar;

    private LinearLayout mMainActivityLayout;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_recent:
                    //mTextMessage.setText(R.string.title_recent);
                    return true;
                case R.id.navigation_trending:
                    //mTextMessage.setText(R.string.title_trending);
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMainActivityLayout = (LinearLayout) findViewById(R.id.main_activity_layout);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        mJdmAdapter = new JeuDeMotAdapter(mListJdm);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        mRecyclerView.setAdapter(mJdmAdapter);

        mRecyclerView.addOnItemTouchListener(new JeuDeMotTouch(getApplicationContext(), mRecyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                JeuDeMot data = mListJdm.get(position);

                // TODO: Faire quelques chose quand on clique sur un JDM
            }

            @Override
            public void onLongClick(View view, int position) {
            }
        }));

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.md_red_500, R.color.md_indigo_500, R.color.md_lime_500, R.color.md_orange_500);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (checkInternet()) loadData();
                else mSwipeRefreshLayout.setRefreshing(false);
            }
        });

        loadData();
    }

    private void loadData() {
        mSwipeRefreshLayout.setRefreshing(true);

        String url = Constants.ADDR_SERVER + "get_data.php";

        JsonArrayRequest jsonRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // the response is already constructed as a JSONObject!
                        mListJdm.clear();

                        try {
                            response = response.getJSONArray(0);

                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jdmObject = response.getJSONObject(i);

                                int id_jdm = jdmObject.getInt("id_jdm");
                                String text_jdm = jdmObject.getString("text_jdm");
                                String auteur_jdm = jdmObject.getString("pseudo_user");
                                String date_jdm = jdmObject.getString("date_jdm");

                                mListJdm.add(new JeuDeMot(id_jdm, text_jdm, auteur_jdm, date_jdm));
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        mJdmAdapter.notifyDataSetChanged();

                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();

                        mSnackbar = Snackbar.make(mMainActivityLayout, getString(R.string.error_http), Snackbar.LENGTH_LONG);
                        mSnackbar.show();
                    }
                });

        Volley.newRequestQueue(this).add(jsonRequest);

    }

    // Permet de vérifier la connexion internet
    public boolean checkInternet() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            mSnackbar.dismiss();
            return true;
        } else {
            mSnackbar = Snackbar
                    .make(mMainActivityLayout, getString(R.string.no_internet), Snackbar.LENGTH_INDEFINITE)
                    .setAction("Réessayer", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            MainActivity.this.finish();
                            startActivity(new Intent(MainActivity.this, MainActivity.class));
                        }
                    });
            mSnackbar.setActionTextColor(Color.YELLOW);
            mSnackbar.show();

            return false;
        }
    }

}
