package com.eudycontreras.weathersense.adapters;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eudycontreras.weathersense.R;
import com.eudycontreras.weathersense.fragments.ForecastFragmentDaily;
import com.eudycontreras.weathersense.utilities.miscellaneous.DimensionUtility;
import com.eudycontreras.weathersense.weather.WeatherActivity;
import com.eudycontreras.weathersense.weatherData.WeatherForecastDaily;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author  Eudy Contreras
 * Adapter used to populate the forecast.
 */
public class AdapterForecastDaily extends ArrayAdapter<WeatherForecastDaily.ForecastDay> {

    private static final String ICON_PATH = "@drawable/";
    private static final String DAY_TIME = "_day";
    private static final String NIGHT_TIME = "_night";
    private int tempUnit = 0;
    private char degreeSign = (char)0x00B0;
    private final WeatherActivity context;
    private Drawable weatherIcon;
    private ForecastFragmentDaily fragmentDaily;
    private FrameLayout container;

    public AdapterForecastDaily(WeatherActivity context, ForecastFragmentDaily fragmentDaily, ArrayList<WeatherForecastDaily.ForecastDay> rowData) {
        super(context, R.layout.fragment_forecast_daily, rowData);
        this.fragmentDaily = fragmentDaily;
        this.context = context;
        this.weatherIcon = ContextCompat.getDrawable(context, R.drawable.unknown_day);
    }

    public void setContainer(FrameLayout listView){
        this.container = listView;
    }

    @Override
    public void add(@Nullable WeatherForecastDaily.ForecastDay object) {
        super.add(object);
    }

    @Override
    public void addAll(@NonNull Collection<? extends WeatherForecastDaily.ForecastDay> collection) {
        super.addAll(collection);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.height =  collection.size() * (int)DimensionUtility.convertDpToPixel(40,context);
        container.setLayoutParams(layoutParams);
        container.requestLayout();
    }

    @Override
    public void addAll(WeatherForecastDaily.ForecastDay... items) {
        super.addAll(items);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.height =  items.length * (int)DimensionUtility.convertDpToPixel(40,context);
        container.setLayoutParams(layoutParams);
        container.requestLayout();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        WeatherForecastDaily.ForecastDay rowData = getItem(position);
        ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = context.getLayoutInflater();
            convertView = inflater.inflate(R.layout.list_item_forecast_daily, null, true);

            viewHolder.holder = (RelativeLayout) convertView.findViewById(R.id.forecast_item_container_daily);
            viewHolder.condition = (ImageView) convertView.findViewById(R.id.forecast_condition_field_daily);
            viewHolder.day = (TextView) convertView.findViewById(R.id.forecast_day_field_daily);
            viewHolder.high = (TextView) convertView.findViewById(R.id.forecast_high_field_daily);
            viewHolder.low = (TextView) convertView.findViewById(R.id.forecast_low_field_daily);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if(rowData == null) {
            return convertView;
        }else{
            if(rowData.getIcon() == null){
                rowData.setIcon("unknown");
            }
        }

        if(fragmentDaily.getWeatherAstronomy()!=null ){
            int time = Integer.parseInt(fragmentDaily.getWeatherAstronomy().getCurrentTime().getHour());

            if (time > 18 || time < 4) {
                  Log.d("Path:: ", ICON_PATH+rowData.getIcon()+ NIGHT_TIME);
                weatherIcon = context.getDrawable(context.getResources().getIdentifier(ICON_PATH+rowData.getIcon()+ NIGHT_TIME, null, context.getPackageName()));
            } else {
                Log.d("Path:",ICON_PATH+rowData.getIcon() + DAY_TIME);
                weatherIcon = context.getDrawable(context.getResources().getIdentifier(ICON_PATH+rowData.getIcon() + DAY_TIME, null, context.getPackageName()));
            }
        }

        tempUnit = context.getPreferenceManager().getSavedSettings(context.getString(R.string.pref_selected_unit_index), 0);
        weatherIcon.setColorFilter(ContextCompat.getColor(context,R.color.white), PorterDuff.Mode.SRC_ATOP);

        viewHolder.condition.setImageDrawable(weatherIcon);

        if(!rowData.isDummyData()) {
            if (tempUnit == 0) {
                viewHolder.low.setText(rowData.getLow().getCelsius() + degreeSign);
                viewHolder.high.setText(rowData.getHigh().getCelsius() + degreeSign);
            } else {
                viewHolder.low.setText(rowData.getLow().getFahrenheit() + degreeSign);
                viewHolder.high.setText(rowData.getHigh().getFahrenheit() + degreeSign);
            }

            viewHolder.day.setText(rowData.getDate().getWeekday());
        }

        return convertView;
    }

    private static class ViewHolder{
        RelativeLayout holder;
        ImageView condition;
        TextView day;
        TextView high;
        TextView low;
    }

}