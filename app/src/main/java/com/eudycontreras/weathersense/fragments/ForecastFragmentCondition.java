package com.eudycontreras.weathersense.fragments;

import android.animation.ValueAnimator;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eudycontreras.weathersense.R;
import com.eudycontreras.weathersense.customView.VerticalOverScrollView;
import com.eudycontreras.weathersense.utilities.animation.BackgroundAnimator;
import com.eudycontreras.weathersense.utilities.multiThreading.ThreadManagers.ThreadManager;
import com.eudycontreras.weathersense.utilities.multiThreading.ThreadManagers.TimerThread;
import com.eudycontreras.weathersense.utilities.multiThreading.ThreadTimers.TimePeriod;
import com.eudycontreras.weathersense.weather.WeatherActivity;
import com.eudycontreras.weathersense.weatherData.WeatherAstronomy;
import com.eudycontreras.weathersense.weatherData.WeatherConditions;
import com.eudycontreras.weathersense.weatherData.WeatherForecastDaily;

import java.util.Calendar;

import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;
import me.everything.android.ui.overscroll.VerticalOverScrollBounceEffectDecorator;
import me.everything.android.ui.overscroll.adapters.ScrollViewOverScrollDecorAdapter;

/**
 * Created by Eudy on 1/30/2017.
 */

public class ForecastFragmentCondition implements WeatherSegment {

    private static final String LAST_POSITION = "lastPosition" + WeatherActivity.class.getSimpleName();
    private static final String ICON_PATH = "@drawable/";
    private static final String DAY_TIME = "_day";
    private static final String NIGHT_TIME = "_night";

    private Toolbar topToolBar;
    private ImageView highTemp;
    private ImageView lowTemp;
    private ImageView background;
    private ImageView tempUnit;
    private TextView lowTempText;
    private TextView highTempText;
    private TextView sunriseText;
    private TextView sunsetText;
    private FrameLayout lowTempContainer;
    private FrameLayout highTempContainer;
    private TextView locationTextView;
    private LinearLayout refreshArea;
    private VerticalOverScrollView scroller;
    private WeatherActivity activity;
    private ImageView weatherIconImageView;
    private TextView temperatureTextView;
    private TextView conditionTextView;
    private Bundle savedInstanceState;
    private Drawable weatherIcon;
    private WeatherAstronomy weatherAstronomy;
    private WeatherConditions weatherConditions;
    private Animation fadeInAnimation = new AlphaAnimation(0, 1);
    private Animation fadeOutAnimation = new AlphaAnimation(1, 0);
    private TranslateAnimation translateIn = new TranslateAnimation(0,0,-100,0);
    private TranslateAnimation translateOut = new TranslateAnimation(0,0,0,-100);
    private AnimationSet animSetEnter = new AnimationSet(true);
    private AnimationSet animSetExit = new AnimationSet(true);
    private Calendar time = Calendar.getInstance();
    private char degreeSign = (char)0x00B0;

    private boolean firstTime = true;
    private boolean fadingIn = false;
    private boolean fadingOut = false;
    private int scrollPosition = 0;

    public ForecastFragmentCondition(WeatherActivity activity, Bundle savedInstanceState) {
        this.activity = activity;
        this.savedInstanceState = savedInstanceState;
        this.initComponents();
        this.applyAnimations();
        this.createBackgroundTransition();
        this.scrollListener();
    }

    private void initComponents() {
//        weatherFont = Typeface.createFromAsset(activity.getAssets(), "fonts/weather.ttf");
        weatherIconImageView = (ImageView) activity.findViewById(R.id.condition_icon);
        temperatureTextView = (TextView) activity.findViewById(R.id.condition_temperatur);
        conditionTextView = (TextView) activity.findViewById(R.id.condition_weather);
        lowTemp = (ImageView) activity.findViewById(R.id.condition_temp_med_low_icon);
        highTemp = (ImageView) activity.findViewById(R.id.condition_temp_med_high_icon);
        lowTempText = (TextView) activity.findViewById(R.id.condition_temp_low) ;
        highTempText = (TextView) activity.findViewById(R.id.condition_temp_high) ;
        sunriseText = (TextView) activity.findViewById(R.id.condition_sunrise) ;
        sunsetText = (TextView) activity.findViewById(R.id.condition_sunset);
        tempUnit = (ImageView) activity.findViewById(R.id.condition_temp_unit);
        highTempContainer = (FrameLayout) activity.findViewById(R.id.high_temp_container);
        lowTempContainer = (FrameLayout) activity.findViewById(R.id.low_temp_container);
        locationTextView = (TextView) activity.findViewById(R.id.toolbar_title);
        refreshArea = (LinearLayout) activity.findViewById(R.id.refresh_area);
        scroller = (VerticalOverScrollView) activity.findViewById(R.id.weather_activity_scroller);
        background = (ImageView) activity.findViewById(R.id.background);
        topToolBar = (Toolbar) activity.findViewById(R.id.home_toolbar);
        topToolBar.setNavigationIcon(R.drawable.ic_action_location_on);
        topToolBar.setTitle("");

        firstTime = activity.getPreferenceManager().getSavedSettings(activity.getString(R.string.pref_first_run),true);

        weatherIcon = ContextCompat.getDrawable(activity,R.drawable.unknown_day);

        OverScrollDecoratorHelper.setUpOverScroll(scroller);

        if (savedInstanceState != null) {
            scrollPosition = savedInstanceState.getInt(LAST_POSITION);
        }

        TimerThread.schedule(activity, TimePeriod.millis(80), () -> activity.runOnUiThread(() -> resetViewProperties()));

        scroller.post(() -> scroller.scrollTo(0, scrollPosition));

        activity.setSupportActionBar(topToolBar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activity.getSupportActionBar().setDisplayShowHomeEnabled(true);

        weatherIconImageView.setScaleX(0);
        weatherIconImageView.setScaleY(0);
    }

    private void resetViewProperties() {
        highTempContainer.setTranslationY(-(highTempContainer.getY() - highTempContainer.getMeasuredHeight()));
        highTempContainer.setAlpha(0f);

        lowTempContainer.setTranslationY(-(lowTempContainer.getY() - lowTempContainer.getMeasuredHeight()));
        lowTempContainer.setAlpha(0f);

        weatherIconImageView.setScaleY(0);
        weatherIconImageView.setScaleX(0);
        weatherIconImageView.setAlpha(0f);
    }

    private void createBackgroundTransition() {
        final int[] drawables = new int[7];
        drawables[0] = R.drawable.bg_animated_1;
        drawables[1] = R.drawable.bg_animated_2;
        drawables[2] = R.drawable.bg_animated_3;
        drawables[3] = R.drawable.bg_animated_4;
        drawables[4] = R.drawable.bg_animated_5;
        drawables[5] = R.drawable.bg_animated_6;
        drawables[6] = R.drawable.bg_animated_7;

        BackgroundAnimator animator = new BackgroundAnimator(activity, background, drawables);
        ThreadManager.performScript(animator);

//        gradientBackgroundPainter = new GradientBackgroundPainter(background, drawables);
//        gradientBackgroundPainter.start();
    }

    float scale = 1;
    float alpha = 1;
    float lastOffset = 0;
    boolean animating = false;
    private void scrollListener() {
        VerticalOverScrollBounceEffectDecorator decor = new VerticalOverScrollBounceEffectDecorator(new ScrollViewOverScrollDecorAdapter(scroller));

        decor.setOverScrollUpdateListener((decor1, state, offset) -> {
            if(offset>lastOffset && offset>100){
                alpha-=0.15f;
                scale-=0.15f;
            }
            if(offset<lastOffset && !fadingIn){
                alpha+=0.15f;
                scale+=0.15f;
            }

            if(scale>=1){scale=1;}
            if(scale<=0){scale=0;}
            if(alpha>=1){alpha=1;}
            if(alpha<=0){alpha=0;}

            setParallax(scale,alpha);
            if (offset > 200) {
                if (!fadingIn) {
                    refreshArea.startAnimation(animSetEnter);
                    fadingIn = true;
                    fadingOut = false;
                    activity.getController().performRefreshAnimation();
                }
            } else if (offset <= 200){
                if (!fadingOut && fadingIn) {
                    refreshArea.startAnimation(animSetExit);
                    performSetupAnimation();
                    alpha = 1;
                    scale = 1;
                    fadingOut = true;
                    fadingIn = false;
                }
            }
            lastOffset = offset;
        });
    }

    private void setParallax(float scale, float alpha){
        weatherIconImageView.setAlpha(alpha);
        weatherIconImageView.setScaleX(scale);
        weatherIconImageView.setScaleY(scale);
        lowTempContainer.setAlpha(alpha);
        highTempContainer.setAlpha(alpha);
    }

    private void applyAnimations() {
        ValueAnimator highTempAnimation = ValueAnimator.ofArgb(ContextCompat.getColor(activity, R.color.orange_light), ContextCompat.getColor(activity, R.color.orange));
        highTempAnimation.addUpdateListener(valueAnimator -> highTemp.setColorFilter((Integer) valueAnimator.getAnimatedValue()));
        highTempAnimation.setDuration(1200);
        highTempAnimation.setRepeatMode(ValueAnimator.REVERSE);
        highTempAnimation.setRepeatCount(ValueAnimator.INFINITE);
        highTempAnimation.start();

        ValueAnimator lowTempAnimation = ValueAnimator.ofArgb(ContextCompat.getColor(activity, R.color.cyan), ContextCompat.getColor(activity, R.color.cyan_light));
        lowTempAnimation.addUpdateListener(valueAnimator -> lowTemp.setColorFilter((Integer) valueAnimator.getAnimatedValue()));
        lowTempAnimation.setDuration(1200);
        lowTempAnimation.setRepeatMode(ValueAnimator.REVERSE);
        lowTempAnimation.setRepeatCount(ValueAnimator.INFINITE);
        lowTempAnimation.start();

        animSetEnter = new AnimationSet(true);
        animSetEnter.setFillAfter(true);
        animSetEnter.setDuration(350);
        animSetEnter.setInterpolator(new DecelerateInterpolator());
        animSetEnter.addAnimation(fadeInAnimation);
        animSetEnter.addAnimation(translateIn);

        animSetExit = new AnimationSet(true);
        animSetExit.setFillAfter(true);
        animSetExit.setDuration(150);
        animSetExit.setInterpolator(new DecelerateInterpolator());
        animSetExit.addAnimation(fadeOutAnimation);
        animSetExit.addAnimation(translateOut);

        refreshArea.startAnimation(animSetExit);
        refreshArea.setTranslationY(-50f);

        animSetExit.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                activity.performRefresh();
                //activity.print("Weather Refreshed..", Toast.LENGTH_SHORT);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

    public void performSetupAnimation() {
        weatherIconImageView.setScaleY(0);
        weatherIconImageView.setScaleX(0);
        weatherIconImageView.setAlpha(0f);
        weatherIconImageView.animate()
                .scaleY(1)
                .scaleX(1)
                .setDuration(500)
                .setInterpolator(new BounceInterpolator())
                .alpha(1)
                .start();

        lowTempContainer.setTranslationY(-(lowTempContainer.getY() - lowTempContainer.getMeasuredHeight()));
        lowTempContainer.setAlpha(0f);
        lowTempContainer.animate()
                .setInterpolator(new BounceInterpolator())
                .setDuration(400)
                .translationY(0)
                .translationX(0)
                .alpha(1)
                .start();

        highTempContainer.setTranslationY(-(highTempContainer.getY() - highTempContainer.getMeasuredHeight()));
        highTempContainer.setAlpha(0f);
        highTempContainer.animate()
                .setInterpolator(new BounceInterpolator())
                .setDuration(400)
                .translationY(0)
                .translationX(0)
                .alpha(1)
                .start();
    }

    public void weatherConditions(WeatherConditions weatherConditions) {
        this.weatherConditions = weatherConditions;
        setLocation(weatherConditions.getLocation());
        int tempUnit = activity.getPreferenceManager().getSavedSettings(activity.getString(R.string.pref_selected_unit_index), 0);

        if(weatherAstronomy!=null ){
            int time = Integer.parseInt(weatherAstronomy.getCurrentTime().getHour());

            if (time > 18 || time < 4) {
                weatherIcon = ContextCompat.getDrawable(activity, activity.getResources().getIdentifier(ICON_PATH+this.weatherConditions.getIcon() + NIGHT_TIME, null, activity.getPackageName()));
            } else {
                weatherIcon = ContextCompat.getDrawable(activity, activity.getResources().getIdentifier(ICON_PATH+this.weatherConditions.getIcon() + DAY_TIME, null, activity.getPackageName()));
            }
        }

        if(tempUnit == 0){
            setTempUnit(ContextCompat.getDrawable(activity,R.drawable.celsius_icon));
            this.setTemperature(weatherConditions.getTempC());
        }else{
            setTempUnit(ContextCompat.getDrawable(activity,R.drawable.fahrenheit_icon));
            this.setTemperature(weatherConditions.getTempF());
        }
        this.weatherIcon.setColorFilter(ContextCompat.getColor(activity,R.color.white), PorterDuff.Mode.SRC_ATOP);
        this.setWeatherIcon(weatherIcon);

        this.setCondition(weatherConditions.getWeather());
    }

    public void setWeatherAstronomy(WeatherAstronomy weatherAstronomy) {
        this.weatherAstronomy = weatherAstronomy;

        if(weatherConditions!=null ){
            int time = Integer.parseInt(weatherAstronomy.getCurrentTime().getHour());

            if (time > 18 || time < 4) {
                weatherIcon = ContextCompat.getDrawable(activity, activity.getResources().getIdentifier(ICON_PATH+weatherConditions.getIcon() + NIGHT_TIME, null, activity.getPackageName()));
            } else {
                weatherIcon = ContextCompat.getDrawable(activity, activity.getResources().getIdentifier(ICON_PATH+weatherConditions.getIcon() + DAY_TIME, null, activity.getPackageName()));
            }
        }
        this.weatherIcon.setColorFilter(ContextCompat.getColor(activity,R.color.white), PorterDuff.Mode.SRC_ATOP);
        setWeatherIcon(weatherIcon);

        if(weatherAstronomy.getSunrise()!=null) {
            setSunrise(weatherAstronomy.getSunrise().getFormattedTime());
            setSunset(weatherAstronomy.getSunset().getFormattedTime());
        }
    }

    public void setWeatherForecast(WeatherForecastDaily weatherForecastDaily){
        int tempUnit = activity.getPreferenceManager().getSavedSettings(activity.getString(R.string.pref_selected_unit_index), 0);

        if(weatherForecastDaily.getDailyForecast().isEmpty())
            return;

        if(tempUnit==0) {
            setLowTemp(weatherForecastDaily.getDailyForecast().get(0).getLow().getCelsius()+degreeSign);
            setHighTemp(weatherForecastDaily.getDailyForecast().get(0).getHigh().getCelsius()+degreeSign);
        }else{
            setLowTemp(weatherForecastDaily.getDailyForecast().get(0).getLow().getFahrenheit()+degreeSign);
            setHighTemp(weatherForecastDaily.getDailyForecast().get(0).getHigh().getFahrenheit()+degreeSign);
        }
    }

    public void fadeToRefresh() {

    }

    public ImageView getWeatherIcon() {
        return weatherIconImageView;
    }

    public void setWeatherIcon(Drawable icon){
        weatherIconImageView.setImageDrawable(icon);
    }

    public void setTemperature(String temperature){
        temperatureTextView.setText(temperature);
    }

    public void setCondition(String condition){
        conditionTextView.setText(condition);
    }

    private void setTempUnit(Drawable drawable){
        this.tempUnit.setImageDrawable(drawable);
    }

    private void setSunrise(String sunrise) {
        this.sunriseText.setText(sunrise);
    }

    private void setSunset(String sunset) {
        this.sunsetText.setText(sunset);
    }

    private void setLowTemp(String lowTemp) {
        this.lowTempText.setText(lowTemp);
    }

    private void setHighTemp(String highTemp) {
        this.highTempText.setText(highTemp);
    }

    public void setLocation(String location) {
        locationTextView.setText(location);
    }

    @Override
    public void saveState(Bundle savedInstanceState) {
        savedInstanceState.putInt(LAST_POSITION, scroller.getScrollY());
    }

    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {

    }

    public void notifyChanges() {

    }

}
