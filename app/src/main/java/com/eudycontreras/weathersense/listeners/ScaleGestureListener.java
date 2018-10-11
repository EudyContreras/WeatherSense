package com.eudycontreras.weathersense.listeners;

import android.app.Activity;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.Toast;

/**
 * Created by Eudy on 2/1/2017.
 */

public class ScaleGestureListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {


    private Activity activity;
    private float onScaleBegin = 0;
    private float onScaleEnd = 0;
    private float scale = 1f;
    private View view;

    public ScaleGestureListener(Activity activity, View view ){
        this.activity = activity;
        this.view = view;
    }
    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        scale *= detector.getScaleFactor();
        view.setScaleX(scale);
        view.setScaleY(scale);
        return true;
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {

        Toast.makeText(activity.getApplicationContext(),"Scale Begin" ,Toast.LENGTH_SHORT).show();
        onScaleBegin = scale;

        return true;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {
        float scaleFactor;
        onScaleEnd = scale;

        if (onScaleEnd > onScaleBegin){
            scaleFactor = onScaleEnd / onScaleBegin;
         }

        if (onScaleEnd < onScaleBegin){
            scaleFactor = onScaleEnd / onScaleBegin;
        }

        super.onScaleEnd(detector);
    }
}