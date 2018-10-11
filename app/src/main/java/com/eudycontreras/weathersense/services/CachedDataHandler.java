
package com.eudycontreras.weathersense.services;

import android.annotation.SuppressLint;
import android.os.AsyncTask;

import com.eudycontreras.weathersense.dataHolders.CachedWeatherData;
import com.eudycontreras.weathersense.dataHolders.WeatherData;
import com.eudycontreras.weathersense.listeners.WeatherServiceListener;
import com.eudycontreras.weathersense.weather.WeatherActivity;
import com.eudycontreras.weathersense.weatherData.WeatherAstronomy;
import com.eudycontreras.weathersense.weatherData.WeatherConditions;
import com.eudycontreras.weathersense.weatherData.WeatherForecastDaily;
import com.eudycontreras.weathersense.weatherData.WeatherForecastHourly;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * @Author Eudy Contreras
 * Service class in charge of making requests to
 * the Yahoo Weather API and returning a handle response
 */
public class CachedDataHandler {


    private WeatherServiceListener listener;
    private WeatherActivity activity;
    private Exception exception;

    public CachedDataHandler(WeatherActivity activity, WeatherServiceListener listener) {
        this.activity = activity;
        this.listener = listener;
    }

    @SuppressLint("StaticFieldLeak")
    public void performRequest(CachedWeatherData cachedWeatherData, RequestType requestType) {

        new AsyncTask<Void, Void, WeatherData>() {
            @Override
            protected WeatherData doInBackground(Void[] locations) {

                WeatherData weatherData = new WeatherData();
                JSONObject data = cachedWeatherData.getCachedData();
                try {
                    switch (requestType) {
                        case WEATHER_CONDITION:
                            JSONObject conditions = data.optJSONObject(WeatherConditions.WEATHER_CONDITION);
                            weatherData.populate(requestType, conditions);
                            weatherData.setConditionObject(data);
                            break;
                        case WEATHER_ASTRONOMY:
                            JSONObject details = data.optJSONObject(WeatherAstronomy.ASTRONOMY);
                            weatherData.populate(requestType, details);
                            weatherData.setAstronomyObject(data);
                            break;
                        case WEATHER_FORECAST_DAILY:
                            JSONObject forecast_daily = data.optJSONObject(WeatherForecastDaily.WEATHER_FORECAST);
                            weatherData.populate(requestType, forecast_daily);
                            weatherData.setDailyForecastObject(data);
                            break;
                        case WEATHER_FORECAST_HOURLY:
                            JSONArray forecast_hourly = data.optJSONArray(WeatherForecastHourly.HOURLY_FORECAST);
                            weatherData.populate(requestType, forecast_hourly);
                            weatherData.setHourlyForecastObject(data);
                            break;
                    }

                    return weatherData;

                } catch (Exception e) {
                    exception = new Exception("Weather cached data redistribution failure!");
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(WeatherData weatherData) {
                if (weatherData == null || exception != null) {
                    listener.serviceFailure(exception);
                } else {
                    listener.serviceSuccess(new ResponseWrapper(requestType, weatherData,null, true));
                }

            }

        }.execute();
    }

}
