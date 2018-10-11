package com.eudycontreras.weathersense.services;

import android.os.Bundle;

import com.eudycontreras.weathersense.utilities.multiThreading.ThreadManagers.ThreadManager;
import com.eudycontreras.weathersense.utilities.multiThreading.ThreadTimers.Period;
import com.eudycontreras.weathersense.utilities.multiThreading.ThreadTimers.TimePeriod;
import com.eudycontreras.weathersense.utilities.multiThreading.ThreadTools.TaskWrapper;
import com.eudycontreras.weathersense.utilities.multiThreading.ThreadTools.WorkerThread;

/**
 * @author  Eudy Contreras
 * Class which provides a background service for
 * periodical updates
 */

public class BackgroundUpdateService {

    private boolean running = false;

    private static final String TYPE = BackgroundUpdateService.class.getName();
    private static final String STATE = TYPE+"state";
    private Period frequency;

    private ThreadManager manager;

    private String name;

    public BackgroundUpdateService(Period frequency, Bundle savedState, String name){
        this.manager = new ThreadManager();
        this.frequency = frequency;
        this.name = name;
        this.running = false;
        if(savedState!=null){
            running = savedState.getBoolean(STATE+name);
        }
    }

    public void setFrequency(int index){
        switch(index){
            case 0:
                frequency = TimePeriod.minutes(10);
                break;
            case 1:
                frequency = TimePeriod.minutes(30);
                break;
            case 2:
                frequency = TimePeriod.hours(1);
                break;
            case 3:
                frequency = TimePeriod.hours(2);
                break;
            case 4:
                frequency = TimePeriod.hours(4);
                break;
            case 5:
                frequency = TimePeriod.hours(8);
                break;
        }
    }

    public void performTask(TaskWrapper update) {
        if(manager.containsTask(update))
            return;

        this.manager.submitThread(name, WorkerThread.TaskType.CONTINUOUS, frequency, TimePeriod.millis(0),update);
        this.manager.setDaemon(name,true);
        this.manager.start(name);
    }

    public String getName(){
        return name;
    }

    public boolean isRunning() {
        if(!manager.containsTask(name))
            return false;

        return this.manager.isRunning(name);
    }

    public void setRunning(boolean running) {
        if(!manager.containsTask(name))
            return;

        if(running){
            this.manager.resume(name);
        }else{
            this.manager.pause(name);
        }
    }

    public boolean cancel() {
        if(!manager.containsTask(name))
            return false;

        this.manager.stop(name);
        return false;
    }

    public void saveServiceState(Bundle outState) {
        outState.putBoolean(STATE+name,running);
    }

}
