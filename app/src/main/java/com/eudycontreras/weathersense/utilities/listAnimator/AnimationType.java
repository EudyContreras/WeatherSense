package com.eudycontreras.weathersense.utilities.listAnimator;

/**
 * Created by Eudy on 1/27/2017.
 */

import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

import com.eudycontreras.weathersense.utilities.animation.ResizeAnimation;

/**
 * Sample class containing common animations.
 * Use case: The class can be called statically and it
 * will return an animation.
 */
public class AnimationType{

    private static final Animation getFadeAnimation(float from, float to, long duration){
        Animation animation = new AlphaAnimation(from, to);
        animation.setDuration(duration);
        return animation;
    }

    private static final Animation getScaleAnimation(float fromX, float toX, float fromY, float toY, long duration){
        Animation animation = new ScaleAnimation(fromX,toX,fromY,toY);
        animation.setDuration(duration);
        return animation;
    }

    private static final Animation getRotateAnimation(float fromDegrees, float toDegrees, long duration){
        Animation animation = new RotateAnimation(fromDegrees,toDegrees);
        animation.setDuration(duration);
        return animation;
    }

    private static final Animation getTranslateAnimation(float fromX, float toX, float fromY, float toY, long duration){
        Animation animation = new TranslateAnimation(fromX,toX,fromY,toY);
        animation.setDuration(duration);
        return animation;
    }

    private static final Animation getResizeAnimation(View view, double from, double to, long duration, ResizeAnimation.ResizeAxis resizeAxis){
        Animation animation = new ResizeAnimation(view, from,to,resizeAxis);
        animation.setDuration(duration);
        return animation;
    }
}