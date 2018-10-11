package com.eudycontreras.weathersense.customView;

import android.app.Activity;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eudycontreras.weathersense.R;


/**
 * Class which creates a useful notification
 * element.
 */

public class SnackBar {

    public static final int LENGTH_LONG = 2300;
    public static final int LENGTH_NORMAL = 1200;
    public static final int LENGTH_SHORT = 750;

    private LinearLayout snackBarLayout;
    private TextView snackBarText;
    private TextView snackBarIndicator;
    private Activity activity;
    private Position position;


    /**
     * Constructor which constructs this class
     * with a given activity and the relative
     * position to which the snackBar will be
     * attached to.
     * @param activity
     * @param position
     */
    public SnackBar(Activity activity, Position position){
        this.position = position;
        this.activity = activity;
        this.initViews(position);
    }

    /**
     * Method initializes the views used
     * by the snackBar based on a given position.
     * @param position
     */
    private void initViews(Position position){
        ViewTreeObserver observer;
        switch (position) {
            case TOP:
                snackBarLayout = (LinearLayout) activity.findViewById(R.id.snackbar_top_layout);
                snackBarText = (TextView) activity.findViewById(R.id.snackbar_top_text);
                snackBarIndicator = (TextView) activity.findViewById(R.id.snackbar_top_indicator);
                observer = snackBarLayout.getViewTreeObserver();

                /**
                 * Observer which observes layout changes an notifies
                 * once the global layout is created. Useful for fetching
                 * dimension data for views that are not yet attached.
                 */
                observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                            snackBarLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        } else {
                            snackBarLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        }
                        int height = snackBarLayout.getMeasuredHeight();
                        snackBarLayout.setTranslationY(-height);
                    }
                });

                break;
            case BOTTOM:
                snackBarLayout = (LinearLayout) activity.findViewById(R.id.snackbar_bottom_layout);
                snackBarText = (TextView) activity.findViewById(R.id.snackbar_bottom_text);
                snackBarIndicator = (TextView) activity.findViewById(R.id.snackbar_bottom_indicator);
                observer = snackBarLayout.getViewTreeObserver();

                /**
                 * Observer which observes layout changes an notifies
                 * once the global layout is created. Useful for fetching
                 * dimension data for views that are not yet attached.
                 */
                observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                            snackBarLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        } else {
                            snackBarLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        }
                        int height = snackBarLayout.getMeasuredHeight();
                        snackBarLayout.setTranslationY(height);
                    }
                });
                break;
        }
    }

    /**
     * Method which shows a notification
     * with a given text.
     * @param text
     */
    public void showSnackBar(String text){
        showSnackBar(text,LENGTH_NORMAL,R.color.color_primary);
    }

    public void showSnackBar(String text, long duration){
        showSnackBar(text,duration,R.color.color_primary);
    }


    /**
     * Method which shows a notification with
     * a given text, duration, and text color
     * @param text
     * @param duration
     * @param textColorID
     */
    public void showSnackBar(String text, final long duration, int textColorID){
        if(snackBarText ==null)
            return;

        snackBarText.setText(text);
        snackBarIndicator.setBackgroundColor(ContextCompat.getColor(activity,textColorID));
        snackBarLayout.animate()
                .translationY(0)
                .setDuration(150)
                .setInterpolator(new DecelerateInterpolator());
        new android.os.Handler().postDelayed(
                () -> snackBarLayout.animate()
                        .translationY(position == Position.BOTTOM ? snackBarLayout.getHeight(): -snackBarLayout.getHeight())
                        .setInterpolator(new DecelerateInterpolator())
                        .setDuration(250), duration);
    }

    public enum Position{
        TOP, BOTTOM
    }
}
