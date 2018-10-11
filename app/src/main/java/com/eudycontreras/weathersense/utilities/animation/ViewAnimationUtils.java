package com.eudycontreras.weathersense.utilities.animation;

import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Transformation;

/**
 * Created by Eudy on 2/1/2017.
 */

public class ViewAnimationUtils {

    public static void expandVertically(final View view) {
        view.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        final int targetHeight = view.getMeasuredHeight();

        view.getLayoutParams().height = 0;
        view.setVisibility(View.VISIBLE);
        Animation animation = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation transformation) {
                view.getLayoutParams().height = interpolatedTime == 1
                        ? ViewGroup.LayoutParams.WRAP_CONTENT
                        : (int) (targetHeight * interpolatedTime);
                view.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };
        animation.setInterpolator(new DecelerateInterpolator());
        animation.setDuration((int) (targetHeight / view.getContext().getResources().getDisplayMetrics().density));
        view.startAnimation(animation);
    }

    public static void collapseVertically(final View view) {
        final int initialHeight = view.getMeasuredHeight();

        Animation animation = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation transformation) {
                if (interpolatedTime == 1) {
                    view.setVisibility(View.GONE);
                } else {
                    view.getLayoutParams().height = initialHeight - (int) (initialHeight * interpolatedTime);
                    view.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        animation.setInterpolator(new DecelerateInterpolator());
        animation.setDuration((int) (initialHeight / view.getContext().getResources().getDisplayMetrics().density));
        view.startAnimation(animation);
    }

    public static void expandHorizontally(final View view) {
        view.measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);

        final int targetWidth = view.getMeasuredWidth();

        view.getLayoutParams().width = 0;
        view.setVisibility(View.VISIBLE);

        Animation animation = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation transformation) {
                view.getLayoutParams().width = interpolatedTime == 1
                        ? ViewGroup.LayoutParams.WRAP_CONTENT
                        : (int) (targetWidth * interpolatedTime);
                view.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        animation.setInterpolator(new DecelerateInterpolator());
        animation.setDuration((int) (targetWidth / view.getContext().getResources().getDisplayMetrics().density));
        view.startAnimation(animation);
    }

    public static void collapseHorizontally(final View view) {
        final int initialWidth = view.getMeasuredWidth();

        Animation animation = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation transformation) {
                if (interpolatedTime == 1) {
                    view.setVisibility(View.GONE);
                } else {
                    view.getLayoutParams().width = initialWidth - (int) (initialWidth * interpolatedTime);
                    view.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        animation.setDuration((int) (initialWidth / view.getContext().getResources().getDisplayMetrics().density));
        view.startAnimation(animation);
    }
}