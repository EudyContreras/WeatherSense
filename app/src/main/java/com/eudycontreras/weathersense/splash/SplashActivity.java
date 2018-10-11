package com.eudycontreras.weathersense.splash;

/**
 * Created by Eudy on 10/14/2016.
 */

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.eudycontreras.weathersense.R;


/**
 * @author  Eudy Contreras
 * Simple splash screen
 */

public class SplashActivity extends Activity {

    private ImageView logo;

    public void onStart(){
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initContent();
    }

    private void initContent(){
        logo = (ImageView) findViewById(R.id.splash_image);
        ValueAnimator anim = ValueAnimator.ofArgb(ContextCompat.getColor(this,R.color.blue), ContextCompat.getColor(this,R.color.cyan));
        anim.setEvaluator(new ArgbEvaluator());
        anim.addUpdateListener(valueAnimator -> logo.setColorFilter((Integer)valueAnimator.getAnimatedValue()));
        anim.setInterpolator(new LinearInterpolator());
        anim.setDuration(1000);
        anim.start();
        new android.os.Handler().postDelayed(() -> startTheApp(), 1200);
    }

    private void startTheApp() {
        setResult(RESULT_OK);
        finish();
        overridePendingTransition(R.anim.fade_in_animation, R.anim.fade_out_animation);
    }

}
