package com.eudycontreras.weathersense.utilities.multiThreading.ThreadManagers;


import android.app.Activity;

import com.eudycontreras.weathersense.utilities.multiThreading.ThreadTimers.Period;

public class TimerThread {

	public static void schedule(final Activity activity, final Period delay, final Runnable script){
		ThreadManager.performScript(() -> {
            waitTime(delay.getDuration());
            activity.runOnUiThread(script);
        });
	}

	public static void scheduleAtRate(final Activity activity, final Period delay, final Period rate, final Runnable script){
		ThreadManager.performScript(() -> {
			waitTime(delay.getDuration());
			while(true){
				waitTime(rate.getDuration());
				script.run();
			}
		});
	}

	public static void intervalIterate(final int start, final int count, final Period period, final Period delay, final IterateWrapper wrapper){
		ThreadManager.performScript(() -> {
            waitTime(delay.getDuration());
            int counter = start;
            while(counter<=count){
				wrapper.onIteration(counter);
                waitTime(period.getDuration());
                counter++;
            }
        });
	}

	private static void waitTime(long millis){
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public interface IterateWrapper{
		void onIteration(int index);
	}

}
