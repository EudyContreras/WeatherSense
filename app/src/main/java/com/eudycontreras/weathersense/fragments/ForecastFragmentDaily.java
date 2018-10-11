package com.eudycontreras.weathersense.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.eudycontreras.weathersense.R;
import com.eudycontreras.weathersense.adapters.AdapterForecastDaily;
import com.eudycontreras.weathersense.controllers.FragmentController;
import com.eudycontreras.weathersense.weather.WeatherActivity;
import com.eudycontreras.weathersense.weatherData.WeatherAstronomy;
import com.eudycontreras.weathersense.weatherData.WeatherForecastDaily;

import java.util.ArrayList;

import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;

/**
 * @author  Eudy Contreras
 * A simple {@link Fragment} subclass.
 * Fragment used to show a weather forecast
 */
public class ForecastFragmentDaily {


    private WeatherActivity activity;
    private FrameLayout forecastContainer;
    private FragmentController controller;
    private WeatherAstronomy weatherAstronomy;
    private AdapterForecastDaily adapter;
    private Bundle onSaveInstanceState;
    private ListView listView;

    public ForecastFragmentDaily(WeatherActivity activity, Bundle savedInstance) {
        this.activity = activity;
        this.onSaveInstanceState = savedInstance;
        this.initializeComponents();
    }

    public void setWeatherForecast(WeatherForecastDaily forecast){
        adapter.clear();
        adapter.addAll(forecast.getDailyForecast());
        adapter.notifyDataSetChanged();
    }

    public void initializeComponents() {
        controller = activity.getController();
        adapter = new AdapterForecastDaily(activity, this, new ArrayList<>());
        listView = (ListView) activity.findViewById(R.id.forecast_list_daily);
        forecastContainer = (FrameLayout) activity.findViewById(R.id.forecast_container);
        OverScrollDecoratorHelper.setUpOverScroll(listView);
        adapter.setContainer(forecastContainer);
        createDummyData();
    }

    private void createDummyData(){
        for(int i = 0; i<7; i++){
            WeatherForecastDaily.ForecastDay dummy = new WeatherForecastDaily.ForecastDay();
            dummy.setDummyData(true);
            adapter.add(dummy);
        }
        listView.setAdapter(adapter);
    }

    public void notifyChanges() {
        adapter.notifyDataSetChanged();
    }

    public void setWeatherAstronomy(WeatherAstronomy weatherAstronomy) {
        this.weatherAstronomy = weatherAstronomy;
    }

    public WeatherAstronomy getWeatherAstronomy(){
        return  weatherAstronomy;
    }
}
