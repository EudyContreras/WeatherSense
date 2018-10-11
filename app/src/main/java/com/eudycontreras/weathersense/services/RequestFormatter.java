package com.eudycontreras.weathersense.services;

import com.eudycontreras.weathersense.R;
import com.eudycontreras.weathersense.weather.WeatherActivity;

/**
 * Created by Eudy on 1/31/2017.
 */

public class RequestFormatter {

    private WeatherActivity activity;
    private String MY_API_KEY;

    public RequestFormatter(WeatherActivity activity){
        this.activity = activity;
        this.MY_API_KEY = activity.getString(R.string.WEATHER_UNDERGROUND_API_KEY);
    }

    public String createRequest(String location, RequestType requestType){
        switch (requestType){
            case WEATHER_CONDITION:
                return createConditionsRequest(location);
            case WEATHER_ASTRONOMY:
                return createAstronomyRequest(location);
            case WEATHER_FORECAST_DAILY:
                return createForecastDailyRequest(location);
            case WEATHER_FORECAST_HOURLY:
                return createForecastHourlyRequest(location);
        }
        return null;
    }

    private String createConditionsRequest(String location){
        String endpoint = String.format("https://api.wunderground.com/api/"+ MY_API_KEY+"/conditions/q/"+location+".json");
        return endpoint;
    }

    private String createAstronomyRequest(String location){
        String endpoint = String.format("https://api.wunderground.com/api/"+MY_API_KEY+"/astronomy/q/"+location+".json");
        return endpoint;
    }

    private String createForecastDailyRequest(String location){
        String endpoint = String.format("https://api.wunderground.com/api/"+MY_API_KEY+"/forecast10day/q/"+location+".json");
        return endpoint;
    }

    private String createForecastHourlyRequest(String location){
        String endpoint = String.format("https://api.wunderground.com/api/"+MY_API_KEY+"/hourly/q/"+location+".json");
        return endpoint;
    }
}
