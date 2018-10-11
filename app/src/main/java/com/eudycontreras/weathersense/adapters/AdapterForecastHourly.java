package com.eudycontreras.weathersense.adapters;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eudycontreras.weathersense.R;
import com.eudycontreras.weathersense.fragments.ForecastFragmentHourly;
import com.eudycontreras.weathersense.weather.WeatherActivity;
import com.eudycontreras.weathersense.weatherData.WeatherForecastHourly;

import java.util.ArrayList;

/**
 * Created by Eudy on 1/30/2017.
 */

public class AdapterForecastHourly extends RecyclerView.Adapter<AdapterForecastHourly.CustomViewHolder> {

    private static final String ICON_PATH = "@drawable/";
    private static final String DAY_TIME = "_day";
    private static final String NIGHT_TIME = "_night";

    private ArrayList<WeatherForecastHourly.HourForecast> adapterData;
    private ForecastFragmentHourly fragmentHourly;
    private WeatherActivity context;
    private Drawable weatherIcon;
    private char degreeSign = (char)0x00B0;
    private int tempUnit = 0;

    public AdapterForecastHourly(WeatherActivity context, ForecastFragmentHourly fragmentHourly, ArrayList<WeatherForecastHourly.HourForecast> adapterData) {
        this.adapterData = adapterData;
        this.fragmentHourly = fragmentHourly;
        this.context = context;
        this.weatherIcon = ContextCompat.getDrawable(context, R.drawable.unknown_day);
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        WeatherForecastHourly.HourForecast rowData = adapterData.get(i);
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_forecast_hourly, null);


        CustomViewHolder customViewHolder = new CustomViewHolder(view, caller -> {
            context.getController().getForecastFragmentDetails().expand();
        });

        if(rowData == null) {
            return customViewHolder;
        }else{
            if(rowData.getIcon() == null){
                rowData.setIcon("unknown");
            }
        }
        if(fragmentHourly.getWeatherAstronomy()!=null ){
            int time = Integer.parseInt(fragmentHourly.getWeatherAstronomy().getCurrentTime().getHour());

            if (time > 18 || time < 4) {
                Log.d("Path:: ", ICON_PATH+rowData.getIcon()+ NIGHT_TIME);
                 weatherIcon = ContextCompat.getDrawable(context, context.getResources().getIdentifier(ICON_PATH+rowData.getIcon() + NIGHT_TIME, null, context.getPackageName()));
            } else {
                Log.d("Path:",ICON_PATH+rowData.getIcon() + DAY_TIME);
                weatherIcon = ContextCompat.getDrawable(context, context.getResources().getIdentifier(ICON_PATH+rowData.getIcon() + DAY_TIME, null, context.getPackageName()));
            }
        }

        tempUnit = context.getPreferenceManager().getSavedSettings(context.getString(R.string.pref_selected_unit_index), 0);

        weatherIcon.setColorFilter(ContextCompat.getColor(context,R.color.white), PorterDuff.Mode.SRC_ATOP);

        customViewHolder.condition.setImageDrawable(weatherIcon);

        if(rowData.isDummyData())
            return customViewHolder;

        customViewHolder.hour.setText(rowData.getTimeInfo().getFormattedTime());

        if(tempUnit == 0){
            customViewHolder.high.setText(rowData.getTemperature().getCelsius() + degreeSign);
            customViewHolder.low.setText(rowData.getFellsLike().getCelsius() + degreeSign);
        }else{
            customViewHolder.high.setText(rowData.getTemperature().getFahrenheit() + degreeSign);
            customViewHolder.low.setText(rowData.getFellsLike().getFahrenheit() + degreeSign);
        }
        return customViewHolder;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder customViewHolder, int i) {
        WeatherForecastHourly.HourForecast rowData = adapterData.get(i);

        if(fragmentHourly.getWeatherAstronomy()!=null ){
            int time = Integer.parseInt(fragmentHourly.getWeatherAstronomy().getCurrentTime().getHour());

            if (time > 18 || time < 4) {
                Log.d("Path:: ", ICON_PATH+rowData.getIcon()+ NIGHT_TIME);
                weatherIcon = context.getDrawable(context.getResources().getIdentifier(ICON_PATH+rowData.getIcon() + NIGHT_TIME, null, context.getPackageName()));
            } else {
                Log.d("Path:",ICON_PATH+rowData.getIcon() + DAY_TIME +" Package: " + context.getPackageName());
                weatherIcon = context.getDrawable( context.getResources().getIdentifier(ICON_PATH+rowData.getIcon() + DAY_TIME, null, context.getPackageName()));
            }
        }

        tempUnit = context.getPreferenceManager().getSavedSettings(context.getString(R.string.pref_selected_unit_index), 0);
        weatherIcon.setColorFilter(ContextCompat.getColor(context,R.color.white), PorterDuff.Mode.SRC_ATOP);
        customViewHolder.condition.setImageDrawable(weatherIcon);

        if(rowData.isDummyData())
            return;

        customViewHolder.hour.setText(rowData.getTimeInfo().getFormattedTime());

        if(tempUnit == 0){
            customViewHolder.high.setText(rowData.getTemperature().getCelsius()+degreeSign);
            customViewHolder.low.setText(rowData.getFellsLike().getCelsius()+degreeSign);
        }else{
            customViewHolder.high.setText(rowData.getTemperature().getFahrenheit()+degreeSign);
            customViewHolder.low.setText(rowData.getFellsLike().getFahrenheit()+degreeSign);
        }
    }

    @Override
    public int getItemCount() {
        return (null != adapterData ? adapterData.size() : 0);
    }

    public void clear() {
        int size = adapterData.size();
        notifyItemRangeRemoved(0, size);
    }

    class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public IMyViewHolderClicks listener;
        protected LinearLayout container;
        protected ImageView condition;
        protected TextView hour;
        protected TextView high;
        protected TextView low;

        public CustomViewHolder(View view, IMyViewHolderClicks listener) {
            super(view);
            this.listener = listener;
            this.container = (LinearLayout) view.findViewById(R.id.forecast_item_container_hourly);
            this.condition = (ImageView) view.findViewById(R.id.forecast_condition_field_hourly);
            this.hour = (TextView) view.findViewById(R.id.forecast_hour_field_hourly);
            this.high = (TextView) view.findViewById(R.id.forecast_high_field_hourly);
            this.low = (TextView) view.findViewById(R.id.forecast_low_field_hourly);
            this.condition.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            listener.event(v);
        }
    }

    public interface IMyViewHolderClicks {
        void event(View caller);
    }
}