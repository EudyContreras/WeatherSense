package com.eudycontreras.weathersense.controllers;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import com.eudycontreras.weathersense.R;
import com.eudycontreras.weathersense.settings.SettingsActivity;
import com.eudycontreras.weathersense.utilities.multiThreading.ThreadTimers.Period;
import com.eudycontreras.weathersense.utilities.multiThreading.ThreadTimers.TimePeriod;
import com.eudycontreras.weathersense.utilities.multiThreading.ThreadTools.ThreadBuffer;
import com.eudycontreras.weathersense.weather.WeatherActivity;

/**
 * Sensor controller class which is in charge of managing the logic
 * which allows registrations and un-registrations to happen. This
 * class is also in charge of handling sensor data.
 */

public class SensorController implements SensorEventListener {

    private static final String BOUND = "SERVICE_BOUND" + SensorController.class.getSimpleName();
    private static final String CURRENT = "CURRENT_SENSOR" + SensorController.class.getSimpleName();
    private static final String REGISTERED = "SENSOR_REGISTERED" + SensorController.class.getSimpleName();
    private static final String TEMPERATURE_SENSOR  ="TEMPERATURE_SENSOR" + SensorController.class.getSimpleName();
    private static final String PRESSURE_SENSOR = "PRESSURE_SENSOR" + SensorController.class.getSimpleName();
    private static final String HUMIDITY_SENSOR = "HUMIDITY_SENSOR" + SensorController.class.getSimpleName();
    private static final String TEMPERATURE_SENSOR_MISSING = "Ambient Temperature Sensor is not available!";
    private static final String PRESSURE_SENSOR_MISSING = "Pressure Sensor is not available!";
    private static final String HUMIDITY_SENSOR_MISSING = "Relative Humidity Sensor is not available!";

    private boolean hasTemperatureSensor = false;
    private boolean hasPressureSensor = false;
    private boolean hasHumiditySensor = false;
    private boolean registered = false;
    private boolean bound = false;

    private Period reportFrequency;
    private ThreadBuffer<SensorEvent> sensorEvents;
    private SensorManager sensorManager;
    private Sensor temperatureSensor;
    private Sensor pressureSensor;
    private Sensor humiditySensor;
    private WeatherActivity activity;
    private ChangeListener sensorChangeListener;
    private Bundle savedState;
    private String current;

    /**
     * Constructor which constructs this class with an instant of
     * the main activity and a bundle. In the constructor sensor manager
     * is created and sensor listening service is bound.
     *
     * @param activity
     * @param savedInstanceState
     */
    public SensorController(WeatherActivity activity, Bundle savedInstanceState) {
        this.activity = activity;
        this.savedState = savedInstanceState;

        this.sensorManager = (SensorManager) activity.getSystemService(Context.SENSOR_SERVICE);
        this.sensorEvents = new ThreadBuffer<>(100);

        this.checkSensorAvailability();

        if (savedInstanceState != null) {

            current = savedInstanceState.getString(CURRENT);
            bound = savedInstanceState.getBoolean(BOUND);
            registered = savedInstanceState.getBoolean(REGISTERED);
            hasHumiditySensor = savedInstanceState.getBoolean(HUMIDITY_SENSOR);
            hasTemperatureSensor = savedInstanceState.getBoolean(TEMPERATURE_SENSOR);
            hasPressureSensor = savedInstanceState.getBoolean(PRESSURE_SENSOR);

        }

        setReportFrequency(TimePeriod.millis(150));

        sensorChangeListener = new ChangeListener(() -> {
            SensorEvent sensorNotification = null;
            try {
                sensorNotification = getSensorEvents();
            } catch (InterruptedException e) {
            }

            boolean showSensorReadings = activity.getPreferenceManager().getSavedSettings(activity.getString(R.string.pref_show_readings_comparison),true);

            if(showSensorReadings==false){
                readFromSensors(false);
            }
            handleSensorEvents(sensorNotification);
        });

        sensorChangeListener.startListener();

        resume();

    }

    private void checkSensorAvailability(){
        if(sensorManager.getDefaultSensor(Sensor.TYPE_TEMPERATURE)!=null){
            hasTemperatureSensor = true;
            temperatureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_TEMPERATURE);
        }else{
            reportMissing(TEMPERATURE_SENSOR_MISSING);
        }
        if(sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE)!=null){
            hasPressureSensor = true;
            pressureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        }else{
            reportMissing(PRESSURE_SENSOR_MISSING);
        }
        if(sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY)!=null){
            hasHumiditySensor = true;
            humiditySensor = sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY);
        }else{
            reportMissing(HUMIDITY_SENSOR_MISSING);
        }
    }

    /**
     * Method which stores relevant data held by
     * this class each time the main activity is destroyed.
     *
     * @param state
     */
    public void storeSettings(Bundle state) {
        if (state == null)
            return;

        state.putString(CURRENT, current);
        state.putBoolean(REGISTERED, registered);
        state.putBoolean(BOUND, bound);
        state.putBoolean(TEMPERATURE_SENSOR, hasTemperatureSensor);
        state.putBoolean(HUMIDITY_SENSOR, hasHumiditySensor);
        state.putBoolean(PRESSURE_SENSOR, hasPressureSensor);
    }

    /**
     * Method which is in charged of stopping threads, services
     * and un-registering sensor listeners,
     */
    public void finalize() {
        storeSettings(savedState);
        sensorManager.unregisterListener(this);
        sensorChangeListener.stopListener();
        sensorManager = null;
        bound = false;
    }

    public void resume(){
        int frequency = activity.getPreferenceManager().getSavedSettings(activity.getString(R.string.pref_frequency_selection), SettingsActivity.MS_150);

        setReportFrequency(TimePeriod.millis(frequency));

        if(hasTemperatureSensor){
            sensorManager.registerListener(this,temperatureSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
        if(hasPressureSensor){
            sensorManager.registerListener(this,pressureSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
        if(hasHumiditySensor){
            sensorManager.registerListener(this,humiditySensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
        if(sensorChangeListener!=null){
            sensorChangeListener.startListener();
        }
    }

    public void pause(){
        sensorManager.unregisterListener(this);
        if(sensorChangeListener!=null){
            sensorChangeListener.stopListener();
        }
    }

    /**
     * Method handles and reports sensor events
     * to the main activity.
     *
     * @param event
     */
    private void handleSensorEvents(SensorEvent event) {
        activity.reportSensorEvent(event);
    }

    private void reportMissing(String sensor) {
        activity.reportMissingSensor(sensor);
    }

    public void readFromSensors(boolean state) {
        if(state){
            resume();
        }else{
            pause();
        }
    }

    public SensorEvent getSensorEvents() throws InterruptedException{
        return sensorEvents.get();
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if(sensorEvent==null)
            return;
        sensorEvents.add(sensorEvent);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }

    /**
     * Change listening thread used for polling
     * sensor event data from a buffer.
     */
    private class ChangeListener {
        private ChangeUpdate update;
        private Worker workerHelper;
        private boolean running = false;

        public ChangeListener(ChangeUpdate update) {
            this.update = update;
        }

        public void startListener() {
            if(running)
                return;

            workerHelper = new Worker();
            workerHelper.setDaemon(true);
            workerHelper.start();
            running = true;
        }

        public void stopListener() {
            if (workerHelper != null) {
                running = false;
                workerHelper.interrupt();
                workerHelper = null;
            }
        }

        class Worker extends Thread {

            public void run() {
                while (!interrupted() && running) {
                    update.onUpdate();
                    waitTime();
                }
            }
        }
    }

    private void waitTime() {
        try {
            if (getReportFrequency() != null) {
                Thread.sleep(getReportFrequency().getDuration());
            } else {
                Thread.sleep(150);
            }
        } catch (InterruptedException e) {

        }
    }

    private void setReportFrequency(Period reportFrequency){
        this.reportFrequency = reportFrequency;
    }

    private Period getReportFrequency(){
        return reportFrequency;
    }
    /**
     * Interface used for change updates.
     */
    private interface ChangeUpdate {
        void onUpdate();
    }
}


