
package com.eudycontreras.weathersense.services;

import android.annotation.SuppressLint;
import android.location.Location;
import android.os.AsyncTask;

import com.eudycontreras.weathersense.R;
import com.eudycontreras.weathersense.listeners.GeocodeServiceListener;
import com.eudycontreras.weathersense.weather.WeatherActivity;
import com.eudycontreras.weathersense.weatherData.subAtributes.Address;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * @author  Eudy Contreras
 * Service class in charge of making requests to
 * the google GeoCoding API and returning a handle response.
 */
public class GoogleGeocodeService {

    private static final String RESULTS = "results";

    private GeocodeServiceListener listener;
    private WeatherActivity activity;
    private Exception error;

    public GoogleGeocodeService(WeatherActivity activity, GeocodeServiceListener listener) {
        this.activity = activity;
        this.listener = listener;
    }

    @SuppressLint("StaticFieldLeak")
    public void refreshLocation(Location location) {

        new AsyncTask<Location, Void, Address>() {
            @Override
            protected Address doInBackground(Location... locations) {

                String API_KEY = activity.getString(R.string.Google_GeoCoding_Key);

                Location location = locations[0];

                String endpoint = String.format("https://maps.googleapis.com/maps/api/geocode/json?latlng=%s,%s&key=%s", location.getLatitude(), location.getLongitude(), API_KEY);

                try {
                    URL url = new URL(endpoint);

                    URLConnection connection = url.openConnection();

                    connection.setUseCaches(false);

                    InputStream inputStream = connection.getInputStream();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

                    StringBuilder result = new StringBuilder();

                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    JSONObject data = new JSONObject(result.toString());

                    JSONArray results = data.optJSONArray(RESULTS);

                    if (results.length() == 0) {
                        error = new ReverseGeolocationException(activity.getString(R.string.cannot_reverse) + location.getLatitude() + ", " + location.getLongitude());

                        return null;
                    }

                    Address locationResult = new Address();

                    locationResult.populate(results.optJSONObject(9));

                    return locationResult;

                } catch (Exception e) {
                    error = e;
                }

                return null;
            }

            @Override
            protected void onPostExecute(Address location) {

                if (location == null || error != null) {
                    listener.geocodeFailure(error);
                } else {
                    listener.geocodeSuccess(location);
                }

            }

        }.execute(location);
    }

    private class ReverseGeolocationException extends Exception {
         ReverseGeolocationException(String detailMessage) {
            super(detailMessage);
        }
    }
}
