
package com.eudycontreras.weathersense.services;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.util.Log;

import com.eudycontreras.weathersense.dataHolders.WeatherData;
import com.eudycontreras.weathersense.listeners.WeatherServiceListener;
import com.eudycontreras.weathersense.weather.WeatherActivity;
import com.eudycontreras.weathersense.weatherData.WeatherAstronomy;
import com.eudycontreras.weathersense.weatherData.WeatherConditions;
import com.eudycontreras.weathersense.weatherData.WeatherForecastDaily;
import com.eudycontreras.weathersense.weatherData.WeatherForecastHourly;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * @Author Eudy Contreras
 * Service class in charge of making requests to
 * the Yahoo Weather API and returning a handle response
 */
public class RequestHandler {

    private static final String RESPONSE = "response";
    private WeatherServiceListener listener;
    private WeatherActivity activity;
    private RequestFormatter requestFormatter;
    private Exception exception;

    public RequestHandler(WeatherActivity activity, WeatherServiceListener listener) {
        this.activity = activity;
        this.listener = listener;
        this.requestFormatter = new RequestFormatter(activity);
    }

    @SuppressLint("StaticFieldLeak")
    public void performRequest(String location, RequestType requestType) {

        new AsyncTask<String, Void, WeatherData>() {
            @Override
            protected WeatherData doInBackground(String[] locations) {

                String location = locations[0];

                WeatherData weatherData = new WeatherData();

                try {
                    URL url = new URL(requestFormatter.createRequest(location, requestType));

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

                    String error = null;
                    if (data.optJSONObject(RESPONSE).optJSONObject(RequestError.ERROR) != null) {
                        error = data.optJSONObject(RESPONSE).optJSONObject(RequestError.ERROR).optString(RequestError.DESCRIPTION);
                    }

                    if (error != null) {
                        Log.d("RESPONSE:", error.toString());
                        exception = new LocationWeatherException(error);
                        return null;
                    }

                    switch (requestType){
                        case WEATHER_CONDITION:
                            JSONObject conditions = data.optJSONObject(WeatherConditions.WEATHER_CONDITION);

                            if(conditions == null){
                                exception =  new LocationWeatherException("Could not retrieve weather information for "+location);
                                return null;
                            }

                            weatherData.populate(requestType,conditions);
                            weatherData.setConditionObject(data);
                            break;
                        case WEATHER_ASTRONOMY:
                            JSONObject details = data.optJSONObject(WeatherAstronomy.ASTRONOMY);

                            if(details == null){
                                exception =  new LocationWeatherException("Could not retrieve weather information for "+location);
                                return null;
                            }

                            weatherData.populate(requestType,details);
                            weatherData.setAstronomyObject(data);
                            break;
                        case WEATHER_FORECAST_DAILY:
                            JSONObject forecast_daily = data.optJSONObject(WeatherForecastDaily.WEATHER_FORECAST);

                            if(forecast_daily == null){
                                exception =  new LocationWeatherException("Could not retrieve weather information for "+location);
                                return null;
                            }

                            weatherData.populate(requestType,forecast_daily);
                            weatherData.setDailyForecastObject(data);
                            break;
                        case WEATHER_FORECAST_HOURLY:
                            JSONArray forecast_hourly = data.optJSONArray(WeatherForecastHourly.HOURLY_FORECAST);

                            if(forecast_hourly == null){
                                exception =  new LocationWeatherException("Could not retrieve weather information for "+location);
                                return null;
                            }

                            weatherData.populate(requestType,forecast_hourly);
                            weatherData.setHourlyForecastObject(data);
                            break;
                    }


                    return weatherData;

                } catch (Exception e) {
                    exception = e;
                }
                return null;
            }

            @Override
            protected void onPostExecute(WeatherData weatherData) {
                if (weatherData == null || exception != null) {
                    listener.serviceFailure(exception);
                    exception.printStackTrace();
                } else {
                    listener.serviceSuccess(new ResponseWrapper(requestType, weatherData, location, false));
                }

            }

        }.execute(location);
    }

    private class LocationWeatherException extends Exception {
        LocationWeatherException(String detailMessage) {
            super(detailMessage);
        }
    }
}
