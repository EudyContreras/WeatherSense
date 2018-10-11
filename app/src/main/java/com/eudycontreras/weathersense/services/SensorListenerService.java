package com.eudycontreras.weathersense.services;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.eudycontreras.weathersense.utilities.multiThreading.ThreadTools.ThreadBuffer;

/**
 * Created by Eudy on 1/23/2017.
 */

public class SensorListenerService extends Service implements SensorEventListener {

    private ThreadBuffer<SensorEvent> sensorEvents;


    public class LocalService extends Binder {
        public SensorListenerService getService() {
            return SensorListenerService.this;
        }
    }
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new LocalService();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent!=null) {
            this.sensorEvents = new ThreadBuffer<>(100);
        }
        return Service.START_NOT_STICKY;
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
}
