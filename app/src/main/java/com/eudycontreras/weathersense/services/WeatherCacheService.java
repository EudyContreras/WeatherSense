
package com.eudycontreras.weathersense.services;

import android.content.Context;
import android.os.AsyncTask;

import com.eudycontreras.weathersense.R;
import com.eudycontreras.weathersense.dataHolders.CachedWeatherData;
import com.eudycontreras.weathersense.listeners.CachedDataListener;

import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author  Eudy Contreras
 * Service which saves weather data to the
 * device in case no weather data can be retrieve.
 *
 */
public class WeatherCacheService {
    public static final String WEATHER_CONDITION = "weatherCondition.data";
    public static final String WEATHER_ASTRONOMY = "weatherAstronomy.data";
    public static final String WEATHER_FORECAST_DAILY = "weatherForecastDaily.data";
    public static final String WEATHER_FORECAST_HOURLY = "weatherForecastHourly.data";
    private Context context;
    private Exception error;

    public WeatherCacheService(Context context) {
        this.context = context;
    }

    public void saveCache(JSONObject cachedData, CachedDataListener listener, String fileName) {

        AsyncTask<JSONObject, Void, Boolean> task = new AsyncTask<JSONObject, Void, Boolean>() {

            @Override
            protected Boolean doInBackground(JSONObject[] channels) {
                JSONObject cachedData = channels[0];

                if(cachedData==null){
                    return false;
                }

                FileOutputStream outputStream;

                try {
                    outputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
                    outputStream.write(cachedData.toString().getBytes());
                    outputStream.close();

                } catch (IOException e) {
                    e.printStackTrace();
                    error = new CacheException(context.getString(R.string.cache_exception_save));
                }

                return true;
            }

            @Override
            protected void onPostExecute(Boolean state) {
                if(listener!=null){
                    if(state){
                        listener.onDataCachedSuccess(cachedData);
                    }else{
                        listener.onDataCachedFailure(error);
                    }
                }
            }

        };
        task.execute(cachedData);
    }

    public void loadCache(final CachedDataListener listener,RequestType requestType, String fileName) {

        new AsyncTask<CachedDataListener, Void, CachedWeatherData>() {

            private CachedDataListener weatherListener;

            @Override
            protected CachedWeatherData doInBackground(CachedDataListener[] serviceListeners) {

                weatherListener = serviceListeners[0];

                try {
                    FileInputStream inputStream = context.openFileInput(fileName);

                    if(inputStream==null){
                        return null;
                    }
                    StringBuilder cache = new StringBuilder();

                    int content;

                    while ((content = inputStream.read()) != -1) {
                        cache.append((char) content);
                    }

                    inputStream.close();

                    JSONObject jsonCache = new JSONObject(cache.toString());

                    CachedWeatherData cachedData = new CachedWeatherData();

                    cachedData.setCachedData(jsonCache);
                    cachedData.setRequestType(requestType);

                    return cachedData;

                } catch (FileNotFoundException e) {
                    error = new CacheException(context.getString(R.string.cache_exception_get));
                    e.printStackTrace();
                } catch (Exception e) {
                    error = e;
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(CachedWeatherData cachedData) {
                if (cachedData == null || error != null) {
                    weatherListener.onDataRetrieveFailure(error);
                } else {
                    weatherListener.onDataRetrieveSuccess(cachedData);
                }
            }
        }.execute(listener);
    }

    public void deleteCachedData(CachedDataListener listener) {

        new AsyncTask<Void, Void, Boolean>() {

            @Override
            protected Boolean doInBackground(Void[] channels) {
                boolean success[] = {
                        context.deleteFile(WEATHER_CONDITION),
                        context.deleteFile(WEATHER_ASTRONOMY),
                        context.deleteFile(WEATHER_FORECAST_DAILY),
                        context.deleteFile(WEATHER_FORECAST_HOURLY)};

                for (boolean state : success) {
                    if (!state) {
                        error = new CacheException("No data exist at this location!");
                        return false;
                    }
                }
                return true;
            }

            @Override
            protected void onPostExecute(Boolean state) {
                if(listener!=null){
                    listener.onCachedDataDeleted(state);
                }
            }

        }.execute();
    }

    public class CacheException extends Exception {
        public CacheException(String detailMessage) {
            super(detailMessage);
        }
    }
}
