package com.pyozer.keskonsmar;

import android.os.AsyncTask;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpRequest extends AsyncTask<String, Void, String> {

    private MainActivity mainActivity = null;
    private LoginActivity loginActivity = null;

    HttpRequest(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    HttpRequest(LoginActivity loginActivity) {
        this.loginActivity = loginActivity;
    }

    @Override
    protected String doInBackground(String... urls) {
        try {
            return downloadUrl(urls);
        } catch (IOException e) {
            if(mainActivity != null)
                mainActivity.doInBackgroundError();
            else if(loginActivity != null)
                loginActivity.doInBackgroundError();

            return null;
        }
    }

    @Override
    protected void onPreExecute() {
        if (mainActivity != null)
            mainActivity.onPreExecute();

    }

    @Override
    protected void onPostExecute(String result) {
        if(mainActivity != null)
            mainActivity.onPostExecute(result);
        else if(loginActivity != null)
            loginActivity.onPostExecute(result);

    }

    @Override
    protected void onCancelled() {
        if (loginActivity != null)
            loginActivity.onCancelled();
    }

    /**
     * On effectue la requête et récupère le contenu
     * @param params Paramètres, URL, timeout
     * @return résultat requête
     * @throws IOException IOException
     */
    private String downloadUrl(String... params) throws IOException {
        InputStream is = null;
        int readTimeout = 15000;
        int connectTimeout = 15000;

        try {
            URL url = new URL(params[0]);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(readTimeout);
            conn.setConnectTimeout(connectTimeout);
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