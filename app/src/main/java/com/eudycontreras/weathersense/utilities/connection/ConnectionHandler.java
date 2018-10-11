package com.eudycontreras.weathersense.utilities.connection;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Eudy on 1/27/2017.
 */

public class ConnectionHandler extends AsyncTask<ConnectionParameters, Void, ConnectionResponse> {

    private ResponseHandler responseHandler;
    private String connectUrl;

    public void setResponseHandler(ResponseHandler responseHandler){
        this.responseHandler = responseHandler;
    }

    protected void onPostExecute(ConnectionResponse response) {
        responseHandler.handleResponse(response);
    }

    @Override
    protected ConnectionResponse doInBackground(ConnectionParameters... info) {
        connectUrl = info[0].getUrl();
        try {
            URL url = new URL(connectUrl);

            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

                StringBuilder stringBuilder = new StringBuilder();

                String line;

                while ((line = bufferedReader.readLine()) != null) {

                    stringBuilder.append(line).append("\n");
                }

                bufferedReader.close();

                return new ConnectionResponse(info[0],stringBuilder.toString());

            } finally {

                urlConnection.disconnect();
            }
        } catch (Exception e){
            Log.e("ERROR", e.getMessage(), e);
            return null;
        }
    }
}
