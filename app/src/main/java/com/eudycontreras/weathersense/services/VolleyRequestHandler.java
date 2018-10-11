
package com.eudycontreras.weathersense.services;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.eudycontreras.weathersense.R;
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
public class VolleyRequestHandler {

    private static final String RESPONSE = "response";
    private RequestQueue requestQueue;
    private WeatherServiceListener listener;
    private WeatherActivity activity;
    private Exception exception;
    private RequestFormatter requestFormatter;

    public VolleyRequestHandler(WeatherActivity activity, WeatherServiceListener listener) {
        this.activity = activity;
        this.listener = listener;
        this.requestQueue = Volley.newRequestQueue(activity);
        this.requestFormatter = new RequestFormatter(activity);
    }

    public void performRequest(String location, RequestType requestType) {
        exception = null;

        String endpoint = requestFormatter.createRequest(location, requestType);

        Log.d("End Point::::",endpoint);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, endpoint, response -> {

            WeatherData weatherData =  processResult(location, response, requestType);

            if (exception != null) {
                listener.serviceFailure(exception);
                exception.printStackTrace();
            }else{
                listener.serviceSuccess(new ResponseWrapper(requestType,weatherData, location, false));
            }

        }, error -> {
            if (exception != null) {
                error.printStackTrace();
                if(exception==null) {
                    exception = new LocationWeatherException(activity.getString(R.string.no_weather_found) + " " + location);
                }
                listener.serviceFailure(exception);
            }
        });

        requestQueue.add(stringRequest);
    }

    private WeatherData processResult(String location, String result, RequestType requestType) {
        WeatherData weatherData;

        try {
            weatherData = new WeatherData();
            JSONObject data = new JSONObject(result.toString());

            String error = null;
            if(data.optJSONObject(RESPONSE).optJSONObject(RequestError.ERROR)!=null) {
                error = data.optJSONObject(RESPONSE).optJSONObject(RequestError.ERROR).optString(RequestError.DESCRIPTION);
            }

            if(error!=null){
                Log.d("RESPONSE:",error.toString());
                exception =  new LocationWeatherException(error);
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



    private class LocationWeatherException extends Exception {
        LocationWeatherException(String detailMessage) {
            super(detailMessage);
        }
    }
}
