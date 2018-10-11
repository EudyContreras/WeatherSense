package com.eudycontreras.weathersense.fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;

import com.eudycontreras.weathersense.R;
import com.eudycontreras.weathersense.adapters.AdapterForecastHourly;
import com.eudycontreras.weathersense.utilities.multiThreading.ThreadManagers.TimerThread;
import com.eudycontreras.weathersense.utilities.multiThreading.ThreadTimers.TimePeriod;
import com.eudycontreras.weathersense.weather.WeatherActivity;
import com.eudycontreras.weathersense.weatherData.WeatherAstronomy;
import com.eudycontreras.weathersense.weatherData.WeatherForecastHourly;

import java.util.ArrayList;

import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;

/**
 * Created by Eudy on 1/30/2017.
 */

public class ForecastFragmentHourly {

    private WeatherActivity activity;
    private Bundle savedInstanceState;
    private FrameLayout hourlyContainer;
    private RecyclerView recyclerView;
    private WeatherAstronomy weatherAstronomy;
    private ArrayList<WeatherForecastHourly.HourForecast> adapterData;
    private AdapterForecastHourly adapterForecastHourly;

    public ForecastFragmentHourly(WeatherActivity activity, Bundle savedInstanceState) {
        this.activity = activity;
        this.savedInstanceState = savedInstanceState;
        this.initComponents();
        this.createDummyData();
    }

    private void initComponents() {
        this.adapterData = new ArrayList<>();
        this.recyclerView = (RecyclerView) activity.findViewById(R.id.forecast_list_hourly);
        this.hourlyContainer = (FrameLayout) activity.findViewById(R.id.forecast_container_hourly) ;
        this.recyclerView.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));
        this.adapterForecastHourly = new AdapterForecastHourly(activity, this, adapterData);
        this.hourlyContainer.setClipChildren(false);

        OverScrollDecoratorHelper.setUpOverScroll(recyclerView, OverScrollDecoratorHelper.ORIENTATION_HORIZONTAL);
    }

    private void createDummyData(){
        for(int i = 0; i<12; i++){
            WeatherForecastHourly.HourForecast dummy = new WeatherForecastHourly.HourForecast();
            dummy.setDummyData(true);
            adapterData.add(dummy);
        }
        this.recyclerView.setAdapter(adapterForecastHourly);
        for (int index = 0; index<recyclerView.getChildCount(); index++) {
            fadeToRefresh(index,50);
        }
    }

    public void setWeatherForecast(WeatherForecastHourly forecast) {
        adapterData.clear();
        adapterData.addAll(forecast.getHourForecasts());
        adapterForecastHourly.notifyDataSetChanged();
    }

    public void performSetupAnimation() {

    }

    public void saveState(Bundle outState) {

    }

    public void fadeToRefresh() {

    }

    private void animateList() {

        TimerThread.IterateWrapper wrapper = index -> activity.runOnUiThread(() -> listItemFadeIn(index, 250));

        for (int index = 0; index<recyclerView.getChildCount(); index++) {
            final View view = recyclerView.getChildAt(index);
            view.setTranslationY(view.getHeight());
            view.setAlpha(0);
        }
        TimerThread.intervalIterate(0, recyclerView.getChildCount(), TimePeriod.millis(170), TimePeriod.millis(1), wrapper);
    }

    private void listItemFadeIn(final int index, long duration) {
        if (index >= 0 && index < recyclerView.getChildCount()) {
            final View view = recyclerView.getChildAt(index);
            view.animate()
                    .setDuration(duration)
                    .translationY(0)
                    .setInterpolator(new DecelerateInterpolator())
                    .alpha(1)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            view.animate().cancel();
                        }
                    });
        }
    }

    private void fadeToRefresh(final int index, long duration) {
        if (index >= 0 && index < recyclerView.getChildCount()) {
            final View view = recyclerView.getChildAt(index);
            view.animate()
                    .setDuration(duration)
                    .setInterpolator(new DecelerateInterpolator())
                    .alpha(0)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            view.setAlpha(0);
                        }
                    }).start();
        }
    }

    public void notifyChanges() {
        adapterForecastHourly.notifyDataSetChanged();
    }


    public void setWeatherAstronomy(WeatherAstronomy weatherAstronomy){
        this.weatherAstronomy = weatherAstronomy;
    }

    public WeatherAstronomy getWeatherAstronomy(){
        return  weatherAstronomy;
    }

}
