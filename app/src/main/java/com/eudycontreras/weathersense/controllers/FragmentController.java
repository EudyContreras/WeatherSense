package com.eudycontreras.weathersense.controllers;


import android.hardware.SensorEvent;
import android.os.Bundle;

import com.eudycontreras.weathersense.fragments.ForecastFragmentCondition;
import com.eudycontreras.weathersense.fragments.ForecastFragmentDaily;
import com.eudycontreras.weathersense.fragments.ForecastFragmentDetails;
import com.eudycontreras.weathersense.fragments.ForecastFragmentHourly;
import com.eudycontreras.weathersense.weather.WeatherActivity;
import com.eudycontreras.weathersense.weatherData.WeatherAstronomy;
import com.eudycontreras.weathersense.weatherData.WeatherConditions;
import com.eudycontreras.weathersense.weatherData.WeatherForecastDaily;
import com.eudycontreras.weathersense.weatherData.WeatherForecastHourly;

/**
 * @author  Eudy Contreras
 * This class is the controller that manages communication
 * among fragments and it facilitates the communication between other
 * classes and activities with the fragments and vice-versa
 * */
public class FragmentController {

    private static final String TYPE_TAG = FragmentController.class.getName();

    public static final String FORECAST_FRAGMENT = TYPE_TAG+ForecastFragmentDaily.class.getName();

    private Bundle savedInstanceState;
    private WeatherActivity activity;

    private PagerController pagerController;
    private ForecastFragmentDaily forecastFragmentDaily;
    private ForecastFragmentDetails forecastFragmentDetails;
    private ForecastFragmentCondition forecastFragmentCondition;
    private ForecastFragmentHourly forecastFragmentHourly;

    public FragmentController(WeatherActivity activity, Bundle savedInstanceState ) {
        this.activity = activity;
        this.savedInstanceState = savedInstanceState;
        initialize();
    }

    private void initialize(){
//        pagerController = new PagerController(activity);
        forecastFragmentDetails = new ForecastFragmentDetails(activity,savedInstanceState);
        forecastFragmentHourly = new ForecastFragmentHourly(activity,savedInstanceState);
        forecastFragmentCondition = new ForecastFragmentCondition(activity,savedInstanceState);
        forecastFragmentDaily = new ForecastFragmentDaily(activity, savedInstanceState);
    }

    public void setDailyForecastValues(WeatherForecastDaily forecast){
        forecastFragmentDaily.setWeatherForecast(forecast);
        forecastFragmentCondition.setWeatherForecast(forecast);
        forecastFragmentDetails.setWeatherForecast(forecast);
    }

    public void setHourlyForecastValues(WeatherForecastHourly forecast){
        forecastFragmentHourly.setWeatherForecast(forecast);
        forecastFragmentDetails.setForecastDetails(forecast);
    }

    public void setWeatherConditions(WeatherConditions weatherDetails){
        forecastFragmentDetails.weatherConditions(weatherDetails);
        forecastFragmentCondition.weatherConditions(weatherDetails);
    }

    public void setWeatherAstronomy(WeatherAstronomy weatherAstronomy){
        forecastFragmentCondition.setWeatherAstronomy(weatherAstronomy);
        forecastFragmentHourly.setWeatherAstronomy(weatherAstronomy);
        forecastFragmentDaily.setWeatherAstronomy(weatherAstronomy);
    }

    public ForecastFragmentDaily getForecastFragmentDaily() {
        return forecastFragmentDaily;
    }

    public ForecastFragmentDetails getForecastFragmentDetails() {
        return forecastFragmentDetails;
    }

    public void onSaveInstanceState(Bundle outState) {
        forecastFragmentCondition.saveState(outState);
        forecastFragmentHourly.saveState(outState);
    }

    public void performRefreshAnimation() {
        forecastFragmentCondition.fadeToRefresh();
        forecastFragmentHourly.fadeToRefresh();
    }

    public void performSetupAnimation() {
        forecastFragmentCondition.performSetupAnimation();
//        forecastFragmentHourly.performSetupAnimation();
    }

    public void notifyChanges() {
        forecastFragmentHourly.notifyChanges();
        forecastFragmentDaily.notifyChanges();
        forecastFragmentCondition.notifyChanges();
        forecastFragmentDetails.notifyChanges();
    }

    public void postSensorEvent(SensorEvent event) {
        forecastFragmentDetails.handleSensorReadings(event);
    }

    public ForecastFragmentHourly getForecastFragmentHourly() {
        return forecastFragmentHourly;
    }
}
