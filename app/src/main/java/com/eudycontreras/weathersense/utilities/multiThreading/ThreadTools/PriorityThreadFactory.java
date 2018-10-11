package com.eudycontreras.weathersense.utilities.multiThreading.ThreadTools;
import android.os.Process;

import java.util.concurrent.ThreadFactory;

/**
 * Created by Eudy Contreras on 10/14/2016.
 */


public class PriorityThreadFactory implements ThreadFactory {

    private final int threadPriority;

    public PriorityThreadFactory(int threadPriority) {
    	this.threadPriority = threadPriority;
    }

    @Override
    public Thread newThread(final Runnable runnable) {
        Runnable wrapperRunnable = () -> {
            try {
                Process.setThreadPriority(threadPriority);
            } catch (Throwable t) {

            }
            runnable.run();
        };
        return new Thread(wrapperRunnable);
    }

}