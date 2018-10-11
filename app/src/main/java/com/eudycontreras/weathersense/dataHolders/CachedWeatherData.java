package com.eudycontreras.weathersense.dataHolders;

import com.eudycontreras.weathersense.services.RequestType;

import org.json.JSONObject;

/**
 * Created by Eudy on 1/31/2017.
 */

public class CachedWeatherData {

    private JSONObject cachedData;
    private RequestType requestType;
    private String location;

//    public CachedWeatherData(String location) {
//        this.location = location;
//    }
//
//    public String getLocation() {
//        return location;
//    }

    public JSONObject getCachedData() {
        return cachedData;
    }

    public void setCachedData(JSONObject object) {
        this.cachedData = object;
    }

    public RequestType getRequestType() {
        return requestType;
    }

    public void setRequestType(RequestType requestType) {
        this.requestType = requestType;
    }
}
