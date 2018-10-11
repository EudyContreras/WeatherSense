package com.eudycontreras.weathersense.listeners;

import com.eudycontreras.weathersense.dataHolders.CachedWeatherData;

import org.json.JSONObject;

/**
 * Created by Eudy on 1/29/2017.
 */

public interface CachedDataListener {

    void onDataCachedFailure(Exception error);
    void onDataCachedSuccess(JSONObject data);
    void onDataRetrieveSuccess(CachedWeatherData data);
    void onDataRetrieveFailure(Exception error);
    void onCachedDataDeleted(boolean success);
}
