package com.eudycontreras.weathersense.utilities.animation;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;


/**
 * @author  Eudy Contreras
 * Class which helps with the implementation
 * of various basic animation.
 */

public class AnimationUtility {

    public static void scaleView(View view, float startScale, float endScale, int duration) {
        Animation anim = new ScaleAnimation(
                1f, 1f,
                startScale, endScale,
                Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 1f);

        anim.setDuration(duration);
        anim.setFillAfter(true);
        view.startAnimation(anim);
    }

    public static void createAnimationStack(View view, Animation... animations) {
        AnimationSet animationSet = new AnimationSet(true);

        for (int i = 0; i < animations.length; i++) {
            animationSet.addAnimation(animations[i]);
        }
        view.startAnimation(animationSet);
    }

    public static Animation scaleView(View view, float startScale, float endScale, int duration, int delay, final ValueAnimator animator) {
        Animation anim = new ScaleAnimation(
                startScale, endScale, // Start and end values for the X axis scaling
                startScale, endScale, // Start and end values for the Y axis scaling
                Animation.RELATIVE_TO_SELF, 0.5f, // Pivot point of X scaling
                Animation.RELATIVE_TO_SELF, 0.5f); // Pivot point of Y scaling

        anim.setInterpolator(new DecelerateInterpolator());
        anim.setDuration(duration);
        anim.setFillAfter(true);
        anim.setStartOffset(delay);

        view.startAnimation(anim);

        return anim;
    }

    public static ValueAnimator changeColor(final View view, int from, int to, int duration) {
        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), from, to);

        colorAnimation.setDuration(duration);
        colorAnimation.addUpdateListener(animator -> view.setBackgroundColor((int) animator.getAnimatedValue()));
        return colorAnimation;
    }

    public static void resizeView(final View view, double startDimension, double targetDimension, ResizeAnimation.ResizeAxis resizeAxis, int duration) {
        ResizeAnimation resizeAnimation = new ResizeAnimation(
                view,
                startDimension,
                targetDimension,
                resizeAxis
        );
        resizeAnimation.setInterpolator(new DecelerateInterpolator());
        resizeAnimation.setDuration(duration);
        resizeAnimation.setFillAfter(true);
        resizeAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
//                view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.startAnimation(resizeAnimation);
    }
}
