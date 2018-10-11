package com.eudycontreras.weathersense.utilities.animation;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;

import com.eudycontreras.weathersense.utilities.multiThreading.ThreadTimers.Period;

import java.util.Random;

/**
 * Created by Eudy on 1/29/2017.
 */

public class BackgroundAnimator implements Runnable {

    private static final int MIN = 4000;
    private static final int MAX = 10000;

    private Activity context;
    private Random random;
    private ImageView image;
    private Drawable[] layers;
    private Period speed;

    public BackgroundAnimator(Activity activity, ImageView image, int... layers) {
        this.context = activity;
        this.image = image;
        this.random = new Random();
        this.layers = new Drawable[layers.length];
        for (int i = 0; i<layers.length; i++){
            this.layers[i] =  ContextCompat.getDrawable(context, layers[i]);
        }
    }

    public void run() {

        Handler handler = new Handler(context.getMainLooper());
        boolean reverse = false;

        while (true) {

            if (!reverse) {
                final int time = randInt(MIN, MAX);
                for (int i = 0; i < layers.length - 1; i++) {
                    Drawable layers[] = new Drawable[2];
                    layers[0] = this.layers[i];
                    layers[1] = this.layers[i + 1];

                    final TransitionDrawable crossFade = new TransitionDrawable(layers);
                    crossFade.setCrossFadeEnabled(false);

                    Runnable transitionRunnable = () -> {
                        image.setImageDrawable(crossFade);
                        crossFade.startTransition(time);
                    };

                    handler.post(transitionRunnable);

                    try {
                        Thread.sleep(time);
                    } catch (Exception e) {
                    }
                }

                reverse = true;
            } else if (reverse) {

                final int time = randInt(MIN, MAX);

                for (int i = layers.length - 1; i > 0; i--) {

                    Drawable layers[] = new Drawable[2];
                    layers[0] = this.layers[i];
                    layers[1] = this.layers[i - 1];

                    final TransitionDrawable crossFade = new TransitionDrawable(layers);
                    crossFade.setCrossFadeEnabled(true);

                    Runnable transitionRunnable = () -> {
                        image.setImageDrawable(crossFade);
                        crossFade.startTransition(time);
                    };

                    handler.post(transitionRunnable);

                    try {
                        Thread.sleep(time);
                    } catch (Exception e) {
                    }
                }

                reverse = false;
            }
        }
    }
    private int randInt(int min, int max) {
        return random.nextInt((max - min) + 1) + min;
    }
}
