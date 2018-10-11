package com.eudycontreras.weathersense.settings;

import android.animation.Animator;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;

import com.eudycontreras.weathersense.R;
import com.eudycontreras.weathersense.customView.CustomAutoCompleteTextView;
import com.eudycontreras.weathersense.customView.SnackBar;
import com.eudycontreras.weathersense.dialogs.DialogConfirmation;
import com.eudycontreras.weathersense.services.AutoCompleteService;
import com.eudycontreras.weathersense.utilities.animation.AnimationUtility;
import com.eudycontreras.weathersense.utilities.animation.ResizeAnimation;
import com.eudycontreras.weathersense.utilities.miscellaneous.BlurUtility;
import com.eudycontreras.weathersense.utilities.multiThreading.ThreadManagers.ThreadManager;
import com.eudycontreras.weathersense.utilities.multiThreading.ThreadTools.ValueTask;
import com.eudycontreras.weathersense.utilities.preferences.PreferenceManager;
import com.eudycontreras.weathersense.weather.WeatherApp;

import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;

/**
 * @author  Eudy Contreras
 * Activity which allows the user to specify a
 * set of preferences which can be saved, retrieved and
 * used by the app.
 */
public class SettingsActivity extends AppCompatActivity implements WeatherApp, DialogConfirmation.DialogEventListener{

    public static final int MS_10 = 10;
    public static final int MS_100 = 100;
    public static final int MS_150 = 150;
    public static final int MS_250 = 250;

    private Toolbar topToolBar;
    private Spinner unitsSpinner;
    private Spinner intervalSpinner;
    private Button clearCache;
    private SnackBar snackBar;
    private CheckBox frequency10;
    private CheckBox frequency100;
    private CheckBox frequency150;
    private CheckBox frequency250;
    private SwitchCompat geoLocationSwitch;
    private SwitchCompat allowAutoUpdates;
    private SwitchCompat showComparison;
    private SwitchCompat useVolley;
    private ScrollView scrollView;
    private ImageView dimOverlay;
    private ImageView blurredBackground;
    private LinearLayout locationLayout;
    private LinearLayout intervalLayout;
    private LinearLayout sensorFrequencyLayout;
    private CustomAutoCompleteTextView manualLocation;
    private PreferenceManager preferenceManager;
    private AutoCompleteService cityService;
    private String[] units = {"C","F"};
    private String[] intervals = {"10 Minutes", "30 Minutes", "1 Hour","2 Hours","4 Hours","8 Hours"};

    private boolean clearCacheState = false;
    private int locationsHeight;
    private int intervalsHeight;
    private int frequenciesHeight;
    private int frequencySelection;
    private int selectedUnit;
    private int selectedInterval;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        initComponents();
        registerListeners();
        restorePreferences();
    }

    @Override
    public void initComponents() {
        preferenceManager = new PreferenceManager(this,getString(R.string.pref_preference_name));
        snackBar = new SnackBar(this, SnackBar.Position.BOTTOM);
        blurredBackground = (ImageView) findViewById(R.id.blurred_background);
        dimOverlay = (ImageView) findViewById(R.id.setting_dim_overlay);
        scrollView = (ScrollView) findViewById(R.id.setting_activity_scroller);
        locationLayout = (LinearLayout) findViewById(R.id.manual_location_setting);
        intervalLayout = (LinearLayout) findViewById(R.id.update_Intervals);
        sensorFrequencyLayout = (LinearLayout) findViewById(R.id.settings_report_frequency);
        intervalSpinner = (Spinner) findViewById(R.id.time_intervals);
        unitsSpinner = (Spinner) findViewById(R.id.temperature_unit);
        showComparison = (SwitchCompat) findViewById(R.id.show_comparison);
        geoLocationSwitch = (SwitchCompat) findViewById(R.id.geolocation);
        allowAutoUpdates = (SwitchCompat) findViewById(R.id.autoUpdates);
        clearCache = (Button) findViewById(R.id.setting_clear_cache);
        frequency10 = (CheckBox) findViewById(R.id.settings_report_frequency_50) ;
        frequency100 = (CheckBox) findViewById(R.id.settings_report_frequency_100) ;
        frequency150 = (CheckBox) findViewById(R.id.settings_report_frequency_150) ;
        frequency250 = (CheckBox) findViewById(R.id.settings_report_frequency_250) ;
        useVolley = (SwitchCompat) findViewById(R.id.use_volley);
        manualLocation = (CustomAutoCompleteTextView) findViewById(R.id.manual_location);
        topToolBar = (Toolbar) findViewById(R.id.settings_toolbar);
        topToolBar.setTitle("");
        blurredBackground.setVisibility(View.GONE);
        blurredBackground.setAlpha(0f);
        dimOverlay.setVisibility(View.GONE);
        dimOverlay.setAlpha(0f);

        cityService = new AutoCompleteService(this,manualLocation);

        OverScrollDecoratorHelper.setUpOverScroll(scrollView);

        locationLayout.post(() ->locationsHeight = locationLayout.getMeasuredHeight());
        intervalLayout.post(() ->intervalsHeight = intervalLayout.getMeasuredHeight());
        sensorFrequencyLayout.post(() ->frequenciesHeight = sensorFrequencyLayout.getMeasuredHeight());

        setSupportActionBar(topToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void registerListeners(){
        clearCache.setOnClickListener(view -> {
            DialogConfirmation dialog = new DialogConfirmation(
                    this,
                    R.style.NotificationDialogTheme,
                    getString(R.string.clear_cache_confirmation),
                    -1);
            dialog.setListener(this);
            dialog.setYesScript(() -> {
                clearCacheState = true;
                runOnUiThread(() -> snackBar.showSnackBar("Cached data has been deleted!"));
            } );
            dialog.show();
        });

        geoLocationSwitch.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (isChecked) {
                manualLocation.setEnabled(false);
                hideLocationInput(locationLayout);
            } else {
                manualLocation.setEnabled(true);
                showLocationInput(locationLayout);
            }
        });
        allowAutoUpdates.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if(isChecked){
                intervalSpinner.setEnabled(true);
                showIntervalInput(intervalLayout);
            }else{
                intervalSpinner.setEnabled(false);
                hideIntervalInput(intervalLayout);
            }
        });
        showComparison.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if(isChecked){
                showComparison(true);
            }else{
                showComparison(false);
            }
        });

        useVolley.setOnCheckedChangeListener((compoundButton, isChecked) ->{} );

        unitsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedUnit = position;
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        intervalSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                selectedInterval = position;
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        manualLocation.setOnTouchListener((v, event) -> {
            manualLocation.showDropDown();
            return false;
        });

        addWatchers();
    }

    private void addWatchers(){
        manualLocation.setThreshold(2);

        manualLocation.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
               cityService.createTask(s.toString());
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,int after) {
                // TODO Auto-generated method stub
            }
            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }
        });

        frequency10.setOnCheckedChangeListener(changeChecker);
        frequency100.setOnCheckedChangeListener(changeChecker);
        frequency150.setOnCheckedChangeListener(changeChecker);
        frequency250.setOnCheckedChangeListener(changeChecker);

    }

    CompoundButton.OnCheckedChangeListener changeChecker = (buttonView, isChecked) -> {
        if (isChecked){
            if (buttonView == frequency10) {
                frequencySelection = MS_10;
                frequency100.setChecked(false);
                frequency150.setChecked(false);
                frequency250.setChecked(false);
            }
            if (buttonView == frequency100) {
                frequencySelection = MS_100;
                frequency10.setChecked(false);
                frequency150.setChecked(false);
                frequency250.setChecked(false);
            }
            if (buttonView == frequency150) {
                frequencySelection = MS_150;
                frequency10.setChecked(false);
                frequency100.setChecked(false);
                frequency250.setChecked(false);
            }
            if (buttonView == frequency250) {
                frequencySelection = MS_250;
                frequency10.setChecked(false);
                frequency100.setChecked(false);
                frequency150.setChecked(false);
            }
        }
    };

    public void saveChanges(){
        saveSettings();
        finish();
    }

    private void goBackHome(){
        Intent result = new Intent();
        setResult(RESULT_CANCELED, result);
        finish();
    }

    private void saveSettings(){
        preferenceManager.saveSettings(getString(R.string.pref_needs_setup),false);
        preferenceManager.saveSettings(getString(R.string.pref_clear_weather_cache),clearCacheState);
        preferenceManager.saveSettings(getString(R.string.pref_geolocation_enabled),geoLocationSwitch.isChecked());
        preferenceManager.saveSettings(getString(R.string.pref_auto_updates_enabled),allowAutoUpdates.isChecked());
        preferenceManager.saveSettings(getString(R.string.pref_use_volley_service), useVolley.isChecked());
        preferenceManager.saveSettings(getString(R.string.pref_show_readings_comparison),showComparison.isChecked());
        preferenceManager.saveSettings(getString(R.string.pref_manual_location),manualLocation.getText().toString());
        preferenceManager.saveSettings(getString(R.string.pref_frequency_selection),frequencySelection);
        preferenceManager.saveSettings(getString(R.string.pref_temperature_unit),units[selectedUnit]);
        preferenceManager.saveSettings(getString(R.string.pref_interval_value),intervals[selectedInterval]);
        preferenceManager.saveSettings(getString(R.string.pref_selected_interval_index),selectedInterval);
        preferenceManager.saveSettings(getString(R.string.pref_selected_unit_index),selectedUnit);

        Intent result = new Intent();
        setResult(RESULT_OK, result);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settings_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.saveChanges:
                saveChanges();
                return true;
            case android.R.id.home:
                goBackHome();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void restorePreferences() {
        geoLocationSwitch.setChecked(preferenceManager.getSavedSettings(getString(R.string.pref_geolocation_enabled),true));
        allowAutoUpdates.setChecked(preferenceManager.getSavedSettings(getString(R.string.pref_auto_updates_enabled),true));
        showComparison.setChecked(preferenceManager.getSavedSettings(getString(R.string.pref_show_readings_comparison),true));
        useVolley.setChecked(preferenceManager.getSavedSettings(getString(R.string.pref_use_volley_service),true));
        manualLocation.setText(preferenceManager.getSavedSettings(getString(R.string.pref_manual_location),getString(R.string.pref_default_location)));

        frequencySelection = preferenceManager.getSavedSettings(getString(R.string.pref_frequency_selection),MS_150);
        selectedInterval = preferenceManager.getSavedSettings(getString(R.string.pref_selected_interval_index),0);
        selectedUnit = preferenceManager.getSavedSettings(getString(R.string.pref_selected_unit_index),0);

        intervalSpinner.setSelection(selectedInterval);
        unitsSpinner.setSelection(selectedUnit);

        if(allowAutoUpdates.isChecked()){
            intervalSpinner.setEnabled(true);
            showIntervalInput(intervalLayout);
        }else{
            intervalSpinner.setEnabled(false);
            hideIntervalInput(intervalLayout);
        }
        performSelection(frequencySelection);

        if(!showComparison.isChecked()){
            showComparison(false);
        }else{
            showComparison(true);
        }

    }

    private void performSelection(int selection){
        switch (selection){
            case MS_10:
                frequency10.setChecked(true);
                break;
            case MS_100:
                frequency100.setChecked(true);
                break;
            case MS_150:
                frequency150.setChecked(true);
                break;
            case MS_250:
                frequency250.setChecked(true);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        saveChanges();
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    protected void onResume() {
        super.onResume();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slow_under_under_enter_from_left_animation, R.anim.slide_to_left_animation);
    }

    private void showComparison(boolean state) {
        showFrequencyPane(state);
    }

    private void blurBackground(boolean state){
        Bitmap map = BlurUtility.TAKE_SNAPSHOT(this);

        Drawable blurredImage =  ThreadManager.computeValue((ValueTask<Drawable>) () -> {
            Bitmap fast = BlurUtility.FAST_BLUR(map, 10);
            return new BitmapDrawable(getResources(),fast);

        });

        blurredBackground.setImageDrawable(blurredImage);

        if(state) {
            blurredBackground.setAlpha(0f);
            blurredBackground.setVisibility(View.VISIBLE);
            blurredBackground.animate()
                    .alpha(1)
                    .setInterpolator(new DecelerateInterpolator())
                    .setDuration(700)
                    .setListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animator) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animator) {
                            blurredBackground.setAlpha(1.0f);
                        }

                        @Override
                        public void onAnimationCancel(Animator animator) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animator) {

                        }
                    })
                    .start();
        }else{
            blurredBackground.setAlpha(1f);
            blurredBackground.animate()
                    .alpha(0)
                    .setInterpolator(new DecelerateInterpolator())
                    .setDuration(500)
                    .setListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animator) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animator) {
                            blurredBackground.setVisibility(View.GONE);
                            blurredBackground.setAlpha(0.0f);
                        }

                        @Override
                        public void onAnimationCancel(Animator animator) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animator) {

                        }
                    })
                    .start();
        }
    }

    private void showOverlay(boolean state){
//        blurBackground(state);
        if(state) {
            dimOverlay.setAlpha(0f);
            dimOverlay.setVisibility(View.VISIBLE);
            dimOverlay.animate()
                    .alpha(1)
                    .setInterpolator(new DecelerateInterpolator())
                    .setDuration(700)
                    .setListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animator) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animator) {
                            dimOverlay.setAlpha(1.0f);
                        }

                        @Override
                        public void onAnimationCancel(Animator animator) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animator) {

                        }
                    })
                    .start();
        }else{
            dimOverlay.setAlpha(1f);
            dimOverlay.animate()
                    .alpha(0)
                    .setInterpolator(new DecelerateInterpolator())
                    .setDuration(500)
                    .setListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animator) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animator) {
                            dimOverlay.setVisibility(View.GONE);
                            dimOverlay.setAlpha(0.0f);
                        }

                        @Override
                        public void onAnimationCancel(Animator animator) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animator) {

                        }
                    })
                    .start();
        }
    }

    private void showFrequencyPane(boolean state){
        if(state){
            sensorFrequencyLayout.setEnabled(true);
            if(frequenciesHeight>0)
            AnimationUtility.resizeView(sensorFrequencyLayout, 0,frequenciesHeight, ResizeAnimation.ResizeAxis.RESIZE_HEIGHT,250);
        }else{
            sensorFrequencyLayout.setEnabled(false);
            AnimationUtility.resizeView(sensorFrequencyLayout, frequenciesHeight,0, ResizeAnimation.ResizeAxis.RESIZE_HEIGHT,250);
       }
    }

    private void hideLocationInput(final View view) {
        AnimationUtility.resizeView(view, locationsHeight,0, ResizeAnimation.ResizeAxis.RESIZE_HEIGHT,250);
    }

    private void showLocationInput(final View view) {
        AnimationUtility.resizeView(view,0, locationsHeight,ResizeAnimation.ResizeAxis.RESIZE_HEIGHT,250);
    }

    private void hideIntervalInput(final View view) {
        AnimationUtility.resizeView(view, intervalsHeight,0, ResizeAnimation.ResizeAxis.RESIZE_HEIGHT,250);
    }

    private void showIntervalInput(final View view) {
        if(intervalsHeight>0)
        AnimationUtility.resizeView(view,0, intervalsHeight,ResizeAnimation.ResizeAxis.RESIZE_HEIGHT,250);
    }

    @Override
    public void onDialogShown() {
        showOverlay(true);
    }

    @Override
    public void onDialogHidden() {
        showOverlay(false);
    }
}
