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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    protected SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private JeuDeMotAdapter jeuDeMotAdapter;
    private List<JeuDeMot> listJeuDeMot = new ArrayList<>();

    private Snackbar snackbar;
    private boolean SNACKBARSHOW = false;

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

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        jeuDeMotAdapter = new JeuDeMotAdapter(listJeuDeMot);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(jeuDeMotAdapter);

        recyclerView.addOnItemTouchListener(new JeuDeMotTouch(getApplicationContext(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                JeuDeMot data = listJeuDeMot.get(position);

                //TODO
            }

            @Override
            public void onLongClick(View view, int position) {}
        }));

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setColorSchemeResources(R.color.md_red_500, R.color.md_indigo_500, R.color.md_lime_500, R.color.md_orange_500);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(checkInternet()) {
                    String url = "http://127.0.0.1:8080/Keskonsmar/get_data.php";
                    HttpRequest HttpRequest = new HttpRequest(MainActivity.this);
                    HttpRequest.new DownloadWebpageTask().execute(url, "5000", "5000");
                } else {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });

        prepareData();
    }

    private void prepareData() {
        JeuDeMot jeuDeMot = new JeuDeMot("Teddy Ficile ggggggg gggggg gggg ggggggggg gggggggggggg gggg gggggg ggggg gggg ggggggggg ggggggggg", "Pyozer", "Il y a 5 minutes");
        listJeuDeMot.add(jeuDeMot);

        jeuDeMot = new JeuDeMot("Justin peu trop", "Pyozer", "Il y a 1 jour");
        listJeuDeMot.add(jeuDeMot);

        jeuDeMot = new JeuDeMot("Sans 50", "Pyozer", "Il y a 3 jours");
        listJeuDeMot.add(jeuDeMot);

        jeuDeMot = new JeuDeMot("Teddy Ficile ggggggg gggggg gggg ggggggggg gggggggggggg gggg gggggg ggggg gggg ggggggggg ggggggggg", "Pyozer", "Il y a 5 minutes");
        listJeuDeMot.add(jeuDeMot);

        jeuDeMot = new JeuDeMot("Justin peu trop", "Pyozer", "Il y a 1 jour");
        listJeuDeMot.add(jeuDeMot);

        jeuDeMot = new JeuDeMot("Sans 50", "Pyozer", "Il y a 3 jours");
        listJeuDeMot.add(jeuDeMot);

        jeuDeMot = new JeuDeMot("Teddy Ficile ggggggg gggggg gggg ggggggggg gggggggggggg gggg gggggg ggggg gggg ggggggggg ggggggggg", "Pyozer", "Il y a 5 minutes");
        listJeuDeMot.add(jeuDeMot);

        jeuDeMot = new JeuDeMot("Justin peu trop", "Pyozer", "Il y a 1 jour");
        listJeuDeMot.add(jeuDeMot);

        jeuDeMot = new JeuDeMot("Sans 50", "Pyozer", "Il y a 3 jours");
        listJeuDeMot.add(jeuDeMot);

        jeuDeMot = new JeuDeMot("Teddy Ficile ggggggg gggggg gggg ggggggggg gggggggggggg gggg gggggg ggggg gggg ggggggggg ggggggggg", "Pyozer", "Il y a 5 minutes");
        listJeuDeMot.add(jeuDeMot);

        jeuDeMot = new JeuDeMot("Justin peu trop", "Pyozer", "Il y a 1 jour");
        listJeuDeMot.add(jeuDeMot);

        jeuDeMot = new JeuDeMot("Sans 50", "Pyozer", "Il y a 3 jours");
        listJeuDeMot.add(jeuDeMot);

        jeuDeMotAdapter.notifyDataSetChanged();
    }

    public void loadData(String data) {
        final ObjectMapper objectMapper = new ObjectMapper();
        // the better way is to create a custom class with the correct format
        try {
            final Map<?, ?> decoded = objectMapper.readValue(data, Map.class);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Permet de vérifier la connexion internet
    public boolean checkInternet() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnected()) {
            if(SNACKBARSHOW) { // Si y'avais une snackbar on la supprime
                snackbar.dismiss();
            }
            return true;
        } else {
            displaySnackbar(findViewById(android.R.id.content), getString(R.string.no_internet));
            return false;
        }
    }

    public void displaySnackbar(View mainactivityLayout, String error) {
        snackbar = Snackbar
                .make(mainactivityLayout, error, Snackbar.LENGTH_INDEFINITE)
                .setAction("Réessayer", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        MainActivity.this.finish();
                        startActivity(new Intent(MainActivity.this, MainActivity.class));
                    }
                });
        snackbar.setActionTextColor(Color.YELLOW);
        snackbar.show();
        SNACKBARSHOW = true;
    }

}
