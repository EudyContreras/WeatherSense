
package com.eudycontreras.weathersense.weather;

import android.animation.Animator;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.SensorEvent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.Toast;

import com.eudycontreras.weathersense.R;
import com.eudycontreras.weathersense.controllers.FragmentController;
import com.eudycontreras.weathersense.controllers.SensorController;
import com.eudycontreras.weathersense.customView.SnackBar;
import com.eudycontreras.weathersense.dataHolders.CachedWeatherData;
import com.eudycontreras.weathersense.dataHolders.WeatherData;
import com.eudycontreras.weathersense.dialogs.DialogConfirmation;
import com.eudycontreras.weathersense.dialogs.DialogInformation;
import com.eudycontreras.weathersense.listeners.CachedDataListener;
import com.eudycontreras.weathersense.listeners.GeocodeServiceListener;
import com.eudycontreras.weathersense.listeners.WeatherServiceListener;
import com.eudycontreras.weathersense.services.BackgroundUpdateService;
import com.eudycontreras.weathersense.services.CachedDataHandler;
import com.eudycontreras.weathersense.services.GeoLocationService;
import com.eudycontreras.weathersense.services.GoogleGeocodeService;
import com.eudycontreras.weathersense.services.RequestHandler;
import com.eudycontreras.weathersense.services.RequestType;
import com.eudycontreras.weathersense.services.RequestValidator;
import com.eudycontreras.weathersense.services.ResponseWrapper;
import com.eudycontreras.weathersense.services.VolleyRequestHandler;
import com.eudycontreras.weathersense.services.WeatherCacheService;
import com.eudycontreras.weathersense.settings.SettingsActivity;
import com.eudycontreras.weathersense.splash.SplashActivity;
import com.eudycontreras.weathersense.utilities.miscellaneous.BlurUtility;
import com.eudycontreras.weathersense.utilities.multiThreading.ThreadManagers.ThreadManager;
import com.eudycontreras.weathersense.utilities.multiThreading.ThreadManagers.TimerThread;
import com.eudycontreras.weathersense.utilities.multiThreading.ThreadTimers.TimePeriod;
import com.eudycontreras.weathersense.utilities.multiThreading.ThreadTools.ValueTask;
import com.eudycontreras.weathersense.utilities.preferences.PreferenceManager;
import com.eudycontreras.weathersense.weatherData.subAtributes.Address;

import org.json.JSONObject;

import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.INTERNET;

/**
 * @author Eudy Contreras
 *         The main activity of the weather app. This activity
 *         is in charge of initializing all elements used by the app
 */
public class WeatherActivity extends AppCompatActivity implements WeatherApp, WeatherServiceListener, GeocodeServiceListener, CachedDataListener{

    private static final String ICON_PATH = "drawable/weather_icon_";

    public static final int PERMISSION_REQUEST_CODE = 11;
    public static final int REQUEST_SPLASH = 12;
    public static final int REQUEST_SETTINGS = 13;

    private GoogleGeocodeService geocodeService;
    private WeatherCacheService cachedDataService;
    private GeoLocationService geoLocationService;
    private VolleyRequestHandler volleyRequestHandler;
    private CachedDataHandler cachedDataHandler;
    private RequestHandler requestHandler;
    private SensorController sensorController;

    private SnackBar snackBar;
    private Bundle savedInstanceState;
    private ImageView dimOverlay;
    private ImageView blurredBackground;
    private BackgroundUpdateService updateService;
    private FragmentController controller;
    private PreferenceManager preferenceManager;

    private boolean clearWeatherCache = false;
    private boolean receivedAstronomyData = false;
    private boolean receivedConditionData = false;
    private boolean receivedDailyForecastData = false;
    private boolean receivedHourlyForecastData = false;
    private boolean showSensorReadings = true;
    private boolean allowAutoUpdates = false;
    private boolean useVolley = true;
    private int updateFrequency = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        this.blurredBackground = (ImageView) findViewById(R.id.blurred_background);
        this.dimOverlay = (ImageView) findViewById(R.id.dim_overlay);
        this.savedInstanceState = savedInstanceState;
        initialize();
        showSplash();

    }

    private void showSplash() {
        Intent intent = new Intent(this, SplashActivity.class);
        startActivityForResult(intent,REQUEST_SPLASH);
        TimerThread.schedule(this, TimePeriod.millis(200), () -> applyPreferences());
    }

    private void initialize(){
        initComponents();
    }

    @Override
    public void initComponents() {
        snackBar = new SnackBar(this, SnackBar.Position.BOTTOM);
        requestHandler = new RequestHandler(this,this);
        volleyRequestHandler = new VolleyRequestHandler(this, this);
        cachedDataHandler = new CachedDataHandler(this, this);
        preferenceManager = new PreferenceManager(this, getString(R.string.pref_preference_name));
        updateService = new BackgroundUpdateService(TimePeriod.minutes(5), savedInstanceState, "Weather Background Update");
        controller = new FragmentController(this, savedInstanceState);
        geocodeService = new GoogleGeocodeService(this, this);
        cachedDataService = new WeatherCacheService(this);
        geoLocationService = new GeoLocationService(this);
        sensorController = new SensorController(this, savedInstanceState);
        blurredBackground.setVisibility(View.GONE);
        blurredBackground.setAlpha(0f);
        dimOverlay.setVisibility(View.GONE);
        dimOverlay.setAlpha(0f);
        controller.getForecastFragmentDetails().collapse();
    }

    @Override
    public void restorePreferences() {
        clearWeatherCache = preferenceManager.getSavedSettings(getString(R.string.pref_clear_weather_cache),false);
        allowAutoUpdates = preferenceManager.getSavedSettings(getString(R.string.pref_auto_updates_enabled),false);
        updateFrequency = preferenceManager.getSavedSettings(getString(R.string.pref_selected_interval_index), 0);
        useVolley = preferenceManager.getSavedSettings(getString(R.string.pref_use_volley_service), true);
        showSensorReadings = preferenceManager.getSavedSettings(getString(R.string.pref_show_readings_comparison),true);
        preferenceManager.saveSettings(getString(R.string.pref_clear_weather_cache),false);

        if(clearWeatherCache){
            cachedDataService.deleteCachedData(this);
        }
        if(showSensorReadings){
            controller.getForecastFragmentDetails().showSensorReadings(true);
            sensorController.readFromSensors(true);
        }else{
            controller.getForecastFragmentDetails().showSensorReadings(false);
            sensorController.readFromSensors(false);
        }

        if(allowAutoUpdates){
            startUpdateService();
        }else {
            fetchWeatherData();
        }
    }

    private void fetchWeatherData() {

        if(!geoLocationService.isNetworkAvailable()){
            cachedDataService.loadCache(this,RequestType.WEATHER_CONDITION, WeatherCacheService.WEATHER_CONDITION);
            cachedDataService.loadCache(this,RequestType.WEATHER_ASTRONOMY, WeatherCacheService.WEATHER_ASTRONOMY);
            cachedDataService.loadCache(this,RequestType.WEATHER_FORECAST_DAILY, WeatherCacheService.WEATHER_FORECAST_DAILY);
            cachedDataService.loadCache(this,RequestType.WEATHER_FORECAST_HOURLY, WeatherCacheService.WEATHER_FORECAST_HOURLY);
            return;
        }

        if (preferenceManager.getSavedSettings(getString(R.string.pref_geolocation_enabled), true)) {
            getWeatherFromCurrentLocation();
        } else {
            getWeatherFromSavedLocation();
        }
    }

    private void getWeatherFromDefaultLocation(){
        String location = getString(R.string.pref_default_location);
        performRequest(location);
    }

    private void getWeatherFromSavedLocation(){
        String location = preferenceManager.getSavedSettings(getString(R.string.pref_manual_location),getString(R.string.pref_default_location));
        performRequest(location);
    }

    public void performRefresh() {
        applyPreferences();
    }

    private void getWeatherFromCurrentLocation() {
        if(!arePermissionsGranted())
            return;

        geoLocationService.getWeatherFromCurrentLocation();

        String city = null;
        String country = null;

        if(geoLocationService.getCityName()!=null){
            city = Normalizer.normalize(geoLocationService.getCityName(), Normalizer.Form.NFKD).replaceAll("\\p{M}", "");
        }

        if(geoLocationService.getCountryName()!=null){
            country =  Normalizer.normalize(geoLocationService.getCountryName(), Normalizer.Form.NFKD).replaceAll("\\p{M}", "");
        }

        String location = city + ", " + country;

//        String location = "55.6166364,13.0546487";

        if(city!=null && country!=null) {
            performRequest(location);
            runOnUiThread(() -> {
                print(location, SnackBar.LENGTH_SHORT);
            });
        }else{
            runOnUiThread(() -> {
                Log.d("Location: ", location );
                print("Current Location not available!", Toast.LENGTH_LONG);
            });
            String locationCache = preferenceManager.getSavedSettings(getString(R.string.pref_cached_location), null);

            if (locationCache == null) {

                android.location.Location bestLocation = geoLocationService.getLastBestLocation();

                if(bestLocation!=null) {
                    geocodeService.refreshLocation(bestLocation);
                }else{
                    getWeatherFromDefaultLocation();
                }
            } else {
                performRequest(locationCache);
            }
        }
    }

    private void performRequest(String location){

        RequestValidator.VALIDATE_REQUEST(location, valid -> {
            if(valid){
                useVolley = preferenceManager.getSavedSettings(getString(R.string.pref_use_volley_service), true);
                if(!useVolley) {
                    requestHandler.performRequest(location, RequestType.WEATHER_CONDITION);
                    requestHandler.performRequest(location, RequestType.WEATHER_ASTRONOMY);
                    requestHandler.performRequest(location, RequestType.WEATHER_FORECAST_DAILY);
                    requestHandler.performRequest(location, RequestType.WEATHER_FORECAST_HOURLY);
                }else{
                    volleyRequestHandler.performRequest(location, RequestType.WEATHER_CONDITION);
                    volleyRequestHandler.performRequest(location, RequestType.WEATHER_ASTRONOMY);
                    volleyRequestHandler.performRequest(location, RequestType.WEATHER_FORECAST_DAILY);
                    volleyRequestHandler.performRequest(location, RequestType.WEATHER_FORECAST_HOURLY);
                }
            }
        });
    }

    public String getCurrentTimeStamp(){
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String currentDateTime = dateFormat.format(new Date());

            return currentDateTime;
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }

    private void applyPreferences() {
        restorePreferences();
    }

    private void startUpdateService() {
        updateService.setFrequency(updateFrequency);
        updateService.performTask(() -> fetchWeatherData());
    }

    public void stopUpdateService() {
        updateService.cancel();
        sensorController.finalize();
    }

    public void print(String text, int length) {
        Log.d("Weather Sense:",text);
        snackBar.showSnackBar(text, length==Toast.LENGTH_SHORT ? SnackBar.LENGTH_NORMAL : SnackBar.LENGTH_LONG);
//        Toast.makeText(this, text, length).show();
    }

    public void print(String text, int length, int color) {
        Log.d("Weather Sense:",text);
        snackBar.showSnackBar(text, length, color);
//        Toast.makeText(this, text, length).show();
    }

    private void startSettingsActivity() {
        updateService.setRunning(false);

        Intent intent = new Intent(WeatherActivity.this, SettingsActivity.class);
        startActivityForResult(intent, REQUEST_SETTINGS);

        overridePendingTransition(R.anim.fast_overlay_enter_from_right_animation, R.anim.slow_under_exit_to_left_animation);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                startSettingsActivity();
                return true;
            case android.R.id.home:
                getWeatherFromCurrentLocation();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        controller.onSaveInstanceState(outState);
        updateService.saveServiceState(outState);
        sensorController.storeSettings(outState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorController.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorController.pause();
    }

    @Override
    protected void onStop(){
        super.onStop();
        sensorController.pause();
    }

    @Override
    protected void onDestroy() {
        stopUpdateService();
        super.onDestroy();
    }

    private void performLayoutAnimation(){
        controller.performSetupAnimation();
    }

    @Override
    public synchronized void serviceSuccess(ResponseWrapper response) {
       switch (response.getRequestType()){
           case WEATHER_CONDITION:
               controller.setWeatherConditions(response.getWeatherData().getWeatherCondition());
               saveWeatherData(response.getWeatherData(),RequestType.WEATHER_CONDITION, response.getLocation());
               Log.d("Weather Sense: ", "Saving weather condition data");
               receivedConditionData = true;
               break;
           case WEATHER_ASTRONOMY:
               controller.setWeatherAstronomy(response.getWeatherData().getWeatherAstronomy());
               saveWeatherData(response.getWeatherData(),RequestType.WEATHER_ASTRONOMY, response.getLocation());
               Log.d("Weather Sense: ", "Saving weather astronomy data");
               receivedAstronomyData = true;
               break;
           case WEATHER_FORECAST_DAILY:
               controller.setDailyForecastValues(response.getWeatherData().getWeatherForecastDaily());
               saveWeatherData(response.getWeatherData(),RequestType.WEATHER_FORECAST_DAILY, response.getLocation());
               Log.d("Weather Sense: ", "Saving weather daily forecast data");
               receivedDailyForecastData = true;
               break;
           case WEATHER_FORECAST_HOURLY:
               controller.setHourlyForecastValues(response.getWeatherData().getWeatherForecastHourly());
               saveWeatherData(response.getWeatherData(),RequestType.WEATHER_FORECAST_HOURLY, response.getLocation());
               Log.d("Weather Sense: ", "Saving weather hourly forecast data");
               receivedHourlyForecastData = true;
               break;
       }

        if(response.getLocation()!=null)
        preferenceManager.saveSettings(getString(R.string.pref_cached_location), response.getLocation());

    }

    private boolean assertCompleteResponse(){
        return receivedConditionData &&
                receivedAstronomyData &&
                receivedDailyForecastData &&
                receivedHourlyForecastData;
    }

    private boolean assertNoResponse(){
        return !receivedConditionData &&
                !receivedAstronomyData &&
                !receivedDailyForecastData &&
                !receivedHourlyForecastData;
    }

    private void resetResponseResults(){
        receivedConditionData =  false;
        receivedAstronomyData = false;
        receivedDailyForecastData = false;
        receivedHourlyForecastData = false;
    }

    @Override
    public synchronized void serviceFailure(Exception exception) {
        print(exception.getMessage(), Toast.LENGTH_SHORT);
        if(!assertNoResponse()){
            cachedDataService.loadCache(this,RequestType.WEATHER_CONDITION, WeatherCacheService.WEATHER_CONDITION);
            cachedDataService.loadCache(this,RequestType.WEATHER_ASTRONOMY, WeatherCacheService.WEATHER_ASTRONOMY);
            cachedDataService.loadCache(this,RequestType.WEATHER_FORECAST_DAILY, WeatherCacheService.WEATHER_FORECAST_DAILY);
            cachedDataService.loadCache(this,RequestType.WEATHER_FORECAST_HOURLY, WeatherCacheService.WEATHER_FORECAST_HOURLY);
        }
    }

    @Override
    public void geocodeSuccess(Address location) {
        runOnUiThread(() -> {
            print(location.getLocation(),Toast.LENGTH_SHORT);
            Log.d("Location: ",location.getLocation());
        });
        performRequest(location.getLocation());
    }

    @Override
    public void geocodeFailure(Exception exception) {
        if(assertNoResponse()){
            cachedDataService.loadCache(this,RequestType.WEATHER_CONDITION, WeatherCacheService.WEATHER_CONDITION);
            cachedDataService.loadCache(this,RequestType.WEATHER_ASTRONOMY, WeatherCacheService.WEATHER_ASTRONOMY);
            cachedDataService.loadCache(this,RequestType.WEATHER_FORECAST_DAILY, WeatherCacheService.WEATHER_FORECAST_DAILY);
            cachedDataService.loadCache(this,RequestType.WEATHER_FORECAST_HOURLY, WeatherCacheService.WEATHER_FORECAST_HOURLY);
        }
    }

    @Override
    public void onDataCachedFailure(Exception error) {
        Log.d("Cached Weather Service:","Cached Failure");
        resetResponseResults();
    }

    @Override
    public void onDataCachedSuccess(JSONObject data) {
        Log.d("Cached Weather Service:","Cached Success");
        resetResponseResults();
    }

    @Override
    public void onDataRetrieveSuccess(CachedWeatherData data) {
        loadWeatherData(data);
        Log.d("Weather Sense:","Loading weather data");
    }

    @Override
    public void onDataRetrieveFailure(Exception error) {
        Log.d("Weather Sense:","Cache data retrieval failure: "+error.getMessage());
    }

    @Override
    public void onCachedDataDeleted(boolean success){
        runOnUiThread(() -> print("Cached data deleted..",Toast.LENGTH_LONG));
    }

    public void reportSensorEvent(SensorEvent event) {
        if(event==null)
            return;

        Log.d("Report",event.sensor.getName()+" Value: "+Float.toString(event.values[0]));
        controller.postSensorEvent(event);
    }


    public void reportMissingSensor(String sensor) {
        TimerThread.schedule(this, TimePeriod.seconds(1), () -> {
            runOnUiThread(() -> Toast.makeText(WeatherActivity.this, sensor, Toast.LENGTH_SHORT).show());
        });

    }

    private void saveWeatherData(WeatherData data, RequestType requestType, String location){
        switch (requestType) {
            case WEATHER_CONDITION:
                cachedDataService.saveCache(data.getConditionObject(), this, WeatherCacheService.WEATHER_CONDITION);
                break;
            case WEATHER_ASTRONOMY:
                cachedDataService.saveCache(data.getAstronomyObject(), this, WeatherCacheService.WEATHER_ASTRONOMY);
                break;
            case WEATHER_FORECAST_DAILY:
                cachedDataService.saveCache(data.getDailyForecastObject(), this, WeatherCacheService.WEATHER_FORECAST_DAILY);
                break;
            case WEATHER_FORECAST_HOURLY:
                cachedDataService.saveCache(data.getHourlyForecastObject(), this, WeatherCacheService.WEATHER_FORECAST_HOURLY);
                break;
        }
    }

    private void loadWeatherData(CachedWeatherData data){
        print("Loading cached weather information",Toast.LENGTH_LONG);
        cachedDataHandler.performRequest(data ,data.getRequestType());
        Log.d("Cached Weather Service:","Cached data retrieved");
    }

    public boolean arePermissionsGranted() {
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), INTERNET);
        int result2 = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_FINE_LOCATION);
        int result3 = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_COARSE_LOCATION);

        return result1 == PackageManager.PERMISSION_GRANTED && result2 == PackageManager.PERMISSION_GRANTED && result3 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION, INTERNET}, PERMISSION_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SETTINGS) {
            if (resultCode == RESULT_OK) {
                applyPreferences();
            }
        }
        if(requestCode == REQUEST_SPLASH){
            if(resultCode == RESULT_OK){
                if(!arePermissionsGranted()) {
                    TimerThread.schedule(this, TimePeriod.millis(1500), this::requestPermission);
                }

                TimerThread.schedule(this,TimePeriod.millis(500),this::performLayoutAnimation);

            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {

            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {

                    boolean locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean coarseAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean internetAccepted = grantResults[2] == PackageManager.PERMISSION_GRANTED;

                    if (!locationAccepted || !coarseAccepted || !internetAccepted){
                        snackBar.showSnackBar("The permissions required for this application to function were not granted!", SnackBar.LENGTH_LONG);
                    }
                    if(coarseAccepted && locationAccepted){
                        geoLocationService.getLocationInformation();
                        controller.getForecastFragmentDetails().collapse();
                        applyPreferences();
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(ACCESS_FINE_LOCATION)) {
                            showPermissionDialog(getString(R.string.retry_getting_location), () -> {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    requestPermissions(new String[]{ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION, INTERNET}, PERMISSION_REQUEST_CODE);
                                }
                            });
                            return;
                        }else{
                            if(!arePermissionsGranted()) {
                                DialogInformation dialog = new DialogInformation(
                                        this,
                                        R.style.NotificationDialogTheme,
                                        getString(R.string.needs_location_permissions));
                                dialog.setListener(new DialogInformation.DialogEventListener() {
                                    @Override
                                    public void onDialogShown() {
                                        showOverlay(true);
                                    }

                                    @Override
                                    public void onDialogHidden() {
                                        showOverlay(false);
                                    }
                                });
                                dialog.show();
                            }
                        }
                    }
                }
                break;
        }
    }


    private void showPermissionDialog(String message, Runnable runnable){
        DialogConfirmation dialog = new DialogConfirmation(
                this,
                R.style.NotificationDialogTheme,
                message,
                -1);
        dialog.setListener(new DialogConfirmation.DialogEventListener() {
            @Override
            public void onDialogShown() {
                showOverlay(true);
            }

            @Override
            public void onDialogHidden() {
                showOverlay(false);
            }
        });
        dialog.setYesScript(runnable);
        dialog.show();
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


    public GoogleGeocodeService getGeocodeService() {
        return geocodeService;
    }

    public FragmentController getController() {
        return controller;
    }

    public PreferenceManager getPreferenceManager() {
        return preferenceManager;
    }

    public SnackBar getSnackBar() {
        return snackBar;
    }
}
