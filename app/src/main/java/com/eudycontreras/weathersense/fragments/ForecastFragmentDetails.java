package com.eudycontreras.weathersense.fragments;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eudycontreras.weathersense.R;
import com.eudycontreras.weathersense.utilities.animation.ViewAnimationUtils;
import com.eudycontreras.weathersense.weather.WeatherActivity;
import com.eudycontreras.weathersense.weatherData.WeatherConditions;
import com.eudycontreras.weathersense.weatherData.WeatherForecastDaily;
import com.eudycontreras.weathersense.weatherData.WeatherForecastHourly;

/**
 * Created by Eudy on 1/30/2017.
 */

public class ForecastFragmentDetails {

    private FrameLayout detailsContainer;
    private FrameLayout detailsLayout;
    private LinearLayout detailsCompilation_v1;
    private LinearLayout detailsCompilation_v2;
    private LinearLayout sensorReadingsLayout;
    private WeatherActivity activity;
    private TextView currentDate;
    private TextView lastUpdate;
    private TextView pressure;
    private TextView humidity;
    private TextView windSpeed;
    private TextView windDirection;
    private TextView uvIndex;
    private TextView altitude;
    private TextView visibility;
    private TextView temperature;
    private TextView weatherDescription;
    private TextView sensorAltitude;
    private TextView sensorTemperature;
    private TextView sensorHumidity;
    private TextView sensorPressure;
    private Bundle savedInstanceState;
    private boolean allowConversion = false;
    private boolean expanded = true;
    private int absoluteHeight;
    private int sensorLayoutWidth;

    public ForecastFragmentDetails(WeatherActivity activity, Bundle savedInstanceState){
        this.activity = activity;
        this.savedInstanceState = savedInstanceState;
        this.initComponents();
        this.performSetupAnimation();
        this.addListener();
    }

    private  void initComponents(){
        this.detailsCompilation_v2 = (LinearLayout) activity.findViewById(R.id.details_compilation_v2);
        this.detailsCompilation_v1 = (LinearLayout) activity.findViewById(R.id.details_compilation_v1);
        this.sensorReadingsLayout = (LinearLayout) activity.findViewById(R.id.sensors_reading_container);
        this.detailsLayout = (FrameLayout) activity.findViewById(R.id.weather_details_layout) ;
        this.detailsContainer = (FrameLayout) activity.findViewById(R.id.weather_details_container);
        this.lastUpdate = (TextView) activity.findViewById(R.id.weather_details_last_update);
        this.currentDate = (TextView) activity.findViewById(R.id.weather_details_date);
        this.weatherDescription = (TextView) activity.findViewById(R.id.weather_details_description);
        this.sensorAltitude = (TextView) activity.findViewById(R.id.weather_details_altitude_sensor);
        this.sensorHumidity = (TextView) activity.findViewById(R.id.weather_details_humidity_sensor);
        this.sensorTemperature = (TextView) activity.findViewById(R.id.weather_details_temperature_sensor);
        this.sensorPressure = (TextView) activity.findViewById(R.id.weather_details_pressure_sensor);
        this.sensorReadingsLayout.post(() -> sensorLayoutWidth = sensorReadingsLayout.getMeasuredWidth());

        boolean readingSensors = activity.getPreferenceManager().getSavedSettings(activity.getString(R.string.pref_show_readings_comparison),true);

        showSensorReadings(readingSensors);
    }

    private void loadFromParent(ViewGroup group){
        this.visibility = (TextView) group.findViewById(R.id.weather_details_visibility);
        this.windDirection = (TextView) group.findViewById(R.id.weather_details_wind_dir);
        this.windSpeed = (TextView) group.findViewById(R.id.weather_details_wind_speed);
        this.pressure = (TextView) group.findViewById(R.id.weather_details_pressure);
        this.humidity = (TextView) group.findViewById(R.id.weather_details_humidity);
        this.altitude = (TextView) group.findViewById(R.id.weather_details_altitude);
        this.uvIndex = (TextView) group.findViewById(R.id.weather_details_uv_index);
        this.temperature = (TextView) group.findViewById(R.id.weather_details_temperature);
    }

    public void setForecastDetails(WeatherForecastHourly forecast) {
        if(forecast.getHourForecasts().isEmpty())
            return;
        lastUpdate.setText(forecast.getHourForecasts().get(0).getTimeInfo().getFormattedTimeStamp());
        currentDate.setText(forecast.getHourForecasts().get(0).getTimeInfo().getFormattedDate());
        humidity.setText(forecast.getHourForecasts().get(0).getHumidity());
        uvIndex.setText(forecast.getHourForecasts().get(0).getUVIndex());
        windDirection.setText(forecast.getHourForecasts().get(0).getWindDirection().getDirection());

        int tempUnit = activity.getPreferenceManager().getSavedSettings(activity.getString(R.string.pref_selected_unit_index), 0);

        if(tempUnit == 0) {
            windSpeed.setText(forecast.getHourForecasts().get(0).getWindSpeed().getKph());
        }else{
            windSpeed.setText(forecast.getHourForecasts().get(0).getWindSpeed().getMph());
        }
    }

    public void setWeatherForecast(WeatherForecastDaily weatherForecast) {
        int tempUnit = activity.getPreferenceManager().getSavedSettings(activity.getString(R.string.pref_selected_unit_index), 0);

        if(tempUnit==0) {
            weatherDescription.setText(weatherForecast.getDailyForecast().get(0).getDescriptionC());
        }else{
            weatherDescription.setText(weatherForecast.getDailyForecast().get(0).getDescriptionF());
        }
    }

    public void weatherConditions(WeatherConditions weatherDetails) {

        int tempUnit = activity.getPreferenceManager().getSavedSettings(activity.getString(R.string.pref_selected_unit_index), 0);

        altitude.setText(weatherDetails.getLocationData().getElevation());

        if(tempUnit == 0) {
            temperature.setText(weatherDetails.getTempC());
            visibility.setText(weatherDetails.getVisibilityKm());
            pressure.setText(weatherDetails.getPressureMB());
            if(!allowConversion)
                return;

            if(isNumeric(weatherDetails.getPressureMB())){
                float pressure = Float.parseFloat(weatherDetails.getPressureMB());
                String altitudeValue = Float.toString(SensorManager.getAltitude(SensorManager.PRESSURE_STANDARD_ATMOSPHERE,pressure));
                altitude.setText(altitudeValue);
            }
        }else{
            temperature.setText(weatherDetails.getTempF());
            visibility.setText(weatherDetails.getVisibilityMi());
            pressure.setText(weatherDetails.getPressureIN());
            if(!allowConversion)
                return;
            if(isNumeric(weatherDetails.getPressureIN())) {
                float pressure = Float.parseFloat(weatherDetails.getPressureIN());
                String altitudeValue = Float.toString(SensorManager.getAltitude(SensorManager.PRESSURE_STANDARD_ATMOSPHERE, pressure));
                altitude.setText(altitudeValue);
            }
        }
    }

    public void handleSensorReadings(SensorEvent sensorEvent){

        float readings = sensorEvent.values[0];

        switch (sensorEvent.sensor.getType()){
            case Sensor.TYPE_PRESSURE:
                float altitude = SensorManager.getAltitude(SensorManager.PRESSURE_STANDARD_ATMOSPHERE, sensorEvent.values[0]);
                activity.runOnUiThread(()-> sensorAltitude.setText(Float.toString(altitude)));
                activity.runOnUiThread(()-> sensorPressure.setText(Float.toString(readings)));
                break;
            case Sensor.TYPE_TEMPERATURE:
                activity.runOnUiThread(()-> sensorTemperature.setText(convertTemperature(readings)));
                break;
            case Sensor.TYPE_RELATIVE_HUMIDITY:
                activity.runOnUiThread(()-> sensorHumidity.setText(Float.toString(readings)));
                break;
        }
    }

    private char degreeSign = (char)0x00B0;

    private String convertTemperature(Float temperature){
        int tempUnit = activity.getPreferenceManager().getSavedSettings(activity.getString(R.string.pref_selected_unit_index), 0);

        if(tempUnit==0){
            return Float.toString(temperature)+degreeSign;
        }else{
            return Float.toString((9/5) * temperature + 32) +degreeSign;

        }
    }
    public void performSetupAnimation() {

    }

    public static boolean isNumeric(String str)
    {
        try
        {
            double d = Double.parseDouble(str);
        }
        catch(NumberFormatException nfe)
        {
            return false;
        }
        return true;
    }

    public void saveState(Bundle outState) {

    }

    private void addListener() {
        detailsContainer.setOnClickListener(view -> collapse());
        collapse();
    }

    public void expand(){
        if(!expanded) {
            ViewAnimationUtils.expandVertically(detailsLayout);
            expanded = true;
        }
    }

    public void collapse(){
        if(expanded) {
            ViewAnimationUtils.collapseVertically(detailsLayout);
            expanded = false;
        }
    }

    public void showSensorReadings(boolean state){
        if(state){
            loadFromParent(detailsCompilation_v1);
            detailsCompilation_v1.setVisibility(View.VISIBLE);
            detailsCompilation_v2.setVisibility(View.GONE);
       }else{
            loadFromParent(detailsCompilation_v2);
            detailsCompilation_v1.setVisibility(View.GONE);
            detailsCompilation_v2.setVisibility(View.VISIBLE);
        }
    }

    public void notifyChanges() {

    }
}
