package com.eudycontreras.weathersense.utilities.listAnimator;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.ListView;

import com.eudycontreras.weathersense.utilities.multiThreading.ThreadManagers.TimerThread;
import com.eudycontreras.weathersense.utilities.multiThreading.ThreadTimers.TimePeriod;

/**
 * Created by Eudy on 1/27/2017.
 */

public class Template {

    private ListView yourList;
    private Activity activity;

    public Template(){

    }

    /**
     * Method used in order to animate items inside a list individually
     */
    private void animateList() {

        //We create an animation range in order to only animate the cells that are visible
        int visibleChildCount = (yourList.getLastVisiblePosition() - yourList.getFirstVisiblePosition()) + 1;

        /**
         * Assuming we need to set initial values: For example the starting position of an element
         * before an animation.
         */
        for (int i = yourList.getFirstVisiblePosition(); i < yourList.getLastVisiblePosition() + 1; i++) {
            final View view = yourList.getChildAt(i);

            //This are example initial values
            view.setTranslationX(view.getWidth());
            view.setAlpha(0);
        }

        /**
         * We use an iterate functional wrapper that returns the current index of an interval
         * iteration animation. The wrapper takes a runnable that is executed on the UI thread
         * and the passed index could be use to iterate through the list with intervals.
         */
        TimerThread.IterateWrapper wrapper = index -> activity.runOnUiThread(() -> listItemFadeIn(index, 200));

        /**
         * TimerThread intervalIterate allows us to enter a start index, an end index, a time period representing
         * the interval duration, and a time period representing the delay before the timer starts. We also
         * passed the previously created wrapper.
         */
        TimerThread.intervalIterate(yourList.getFirstVisiblePosition(), visibleChildCount, TimePeriod.millis(150), TimePeriod.millis(50), wrapper);
    }

    /**
     * Sample use case for the view
     * inline animations. The method takes an
     * index which is used to determine which cell
     * to animate. This animation will result in
     * a view becoming visible while it moves to the left.
     *
     * @param index
     */
    private void listItemFadeIn(final int index, long duration) {
        if (index >= 0 && index < yourList.getChildCount()) {
            final View view = yourList.getChildAt(index);
            view.setTranslationX(view.getWidth());
            view.setAlpha(0);
            view.animate()
                    .setDuration(duration)
                    .translationX(0)
                    .setInterpolator(new DecelerateInterpolator())
                    .alpha(1)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            view.animate().cancel();
                        }
                    });
        }
    }

    /**
     * Method which allows us to passed a pre-made animation
     * such as the ones that the AnimationType class can return.
     * @param index
     * @param animation
     */
    public void animateItem(final int index, Animation animation){
        if (index >= 0 && index < yourList.getChildCount()) {
            final View view = yourList.getChildAt(index);
            view.setAnimation(animation);
            view.startAnimation(animation);
        }
    }
    /**
     * Method which allows us to passed a pre-made animation
     * such as the ones that the AnimationType class can return.
     * the additional start time identifies when the animation will
     * start relative to execution.
     * @param index
     * @param animation
     * @param startTime
     */
    public void animateItem(final int index, long startTime, Animation animation){
        if (index >= 0 && index < yourList.getChildCount()) {
            final View view = yourList.getChildAt(index);
            animation.setStartTime(startTime);
            view.setAnimation(animation);
        }
    }
}
