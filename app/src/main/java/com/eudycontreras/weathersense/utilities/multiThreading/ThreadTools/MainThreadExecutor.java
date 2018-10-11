package com.eudycontreras.weathersense.utilities.multiThreading.ThreadTools;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;

/**
 * @author  Eudy Contreras
 */

public class MainThreadExecutor implements Executor {

    private final Handler handler = new Handler(Looper.getMainLooper());

    @Override
    public void execute(Runnable runnable) {
        handler.post(runnable);
    }
}