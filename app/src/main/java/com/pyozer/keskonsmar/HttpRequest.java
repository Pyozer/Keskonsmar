package com.pyozer.keskonsmar;

import android.os.AsyncTask;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpRequest {

    private MainActivity mainActivity = null;

    HttpRequest(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    class DownloadWebpageTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            try {
                return downloadUrl(urls);
            } catch (IOException e) {
                if(mainActivity != null) {
                    return "error";
                }
                return null;
            }
        }

        @Override
        protected void onPreExecute() {
            if (mainActivity != null) {
                mainActivity.swipeRefreshLayout.setRefreshing(true);
            }
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            if(mainActivity != null) {
                mainActivity.loadData(result);
            }
        }
    }

    /**
     * On effectue la requête et récupère le contenu
     * @param params Paramètres, URL, timeout
     * @return résultat requête
     * @throws IOException
     */
    private String downloadUrl(String... params) throws IOException {
        InputStream is = null;
        String ReadTimeout = params[1];
        String ConnectTimeout = params[2];

        try {
            URL url = new URL(params[0]);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(Integer.parseInt(ReadTimeout));
            conn.setConnectTimeout(Integer.parseInt(ConnectTimeout));
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();
            //int response = conn.getResponseCode();
            is = conn.getInputStream();

            return convertStreamToString(is);

        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    /**
     * Convertion du InputStream en String.
     * @param is InputStream à convertir
     * @return string
     * @throws IOException
     */
    private String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}