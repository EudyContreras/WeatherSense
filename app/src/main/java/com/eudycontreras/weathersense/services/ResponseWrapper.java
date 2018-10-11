package com.eudycontreras.weathersense.services;

import com.eudycontreras.weathersense.dataHolders.WeatherData;

/**
 * Created by Eudy on 2/1/2017.
 */

public class ResponseWrapper {

    private String location;
    private boolean isCached;
    private RequestType requestType;
    private WeatherData weatherData;

    public ResponseWrapper(RequestType requestType, WeatherData weatherData, String location, boolean isCached) {
        this.location = location;
        this.requestType = requestType;
        this.weatherData = weatherData;
        this.isCached = isCached;
    }

    public RequestType getRequestType() {
        return requestType;
    }

    public boolean isCached(){
        return isCached;
    }

    public void setRequestType(RequestType requestType) {
        this.requestType = requestType;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public WeatherData getWeatherData() {
        return weatherData;
    }
}
