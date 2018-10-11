package com.eudycontreras.weathersense.services;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.eudycontreras.weathersense.R;
import com.eudycontreras.weathersense.weather.WeatherActivity;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * @author Eudy Contreras
 * Class which provides a location service which uses
 * different providers in order to achieve a somewhat
 * accurate location of the device.
 */

public class GeoLocationService {

    private static final int REQUEST_ACCESS_FINE_LOCATION = 1;
    private static final int REQUEST_ACCESS_COARSE_LOCATION = 2;
    private static final int TWO_MINUTES = 1000 * 60 * 2;

    private ConnectivityManager connectivityManager;
    private LocationManager locationManager;
    private Criteria locationCriteria;
    private WeatherActivity activity;
    private Location location;
    private Geocoder geocoder;

    private String locationName;
    private String provider;
    private String addressName;
    private String state;
    private String cityName;
    private String countryName;

    private boolean gpsEnabled;
    private boolean networkEnabled;

    private double longitude;
    private double latitude;

    public GeoLocationService(WeatherActivity activity) {
        this.activity = activity;
        initialize();
    }

    public void initialize() {
        locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        connectivityManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);

        geocoder = new Geocoder(activity, Locale.getDefault());
        locationCriteria = new Criteria();

        locationCriteria.setAccuracy(Criteria.ACCURACY_MEDIUM);

        try {
            gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        try {
            networkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if(activity.arePermissionsGranted()){
            getLocationInformation();
        }
    }

    public void getLocationInformation(){
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {}
        if (networkEnabled)
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, networkListener);

        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {}
        if (gpsEnabled)
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, gpsListener);

        provider = locationManager.getBestProvider(locationCriteria, true);

        location = getLastBestLocation();
        Log.d("Location Services","GPS ENABLED: "+gpsEnabled+" NETWORK ENABLED: "+networkEnabled);

        updateLocation(location);
    }

    private void updateLocation(Location location) {
        Toast.makeText(activity,"Updating location:",Toast.LENGTH_SHORT).show();

        if (location != null) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();

            List<Address> addresses = null;

            try {
                addresses = geocoder.getFromLocation(latitude, longitude, 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (addresses != null && !addresses.isEmpty()) {

                addressName = addresses.get(0).getAddressLine(0);

                state = addresses.get(0).getAdminArea();

                cityName = addresses.get(0).getLocality();

                countryName = addresses.get(0).getCountryName();

                locationName = cityName + ", " + countryName;

                Toast.makeText(activity," location: "+locationName,Toast.LENGTH_SHORT).show();
            }

            if (activity.getPreferenceManager().getSavedSettings(activity.getString(R.string.pref_geolocation_enabled), true)) {
                activity.getGeocodeService().refreshLocation(location);
            }
        }
    }

    public void stopRequests(String provider) {
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
        }
        locationManager.removeUpdates(getListener(provider));
    }

    LocationListener gpsListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            updateLocation( makeUseOfNewLocation(location));
        }
        @Override
        public void onStatusChanged(String provider, int i, Bundle bundle) {

        }
        @Override
        public void onProviderEnabled(String provider) {

        }
        @Override
        public void onProviderDisabled(String provider) {
//            stopRequests(provider);
        }
    };

    LocationListener networkListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            updateLocation( makeUseOfNewLocation(location));
        }
        @Override
        public void onStatusChanged(String provider, int i, Bundle bundle) {

        }
        @Override
        public void onProviderEnabled(String provider) {

        }
        @Override
        public void onProviderDisabled(String provider) {
//            stopRequests(provider);
        }
    };

    public void getWeatherFromCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
        }
        locationManager.requestSingleUpdate(provider, getListener(provider), null);
    }

    private LocationListener getListener(String provider){
        if(provider.contentEquals(LocationManager.GPS_PROVIDER)){
            return gpsListener;
        }else{
            return networkListener;
        }
    }

    private Location makeUseOfNewLocation(Location location) {
        if (isBetterLocation(location, this.location) ) {
            return location;
        }else{
            return this.location;
        }
    }

    public Location getLastBestLocation() {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_DENIED) {
        }
        Location locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        Location locationNet = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        long GPSLocationTime = 0;

        if (null != locationGPS) {
            GPSLocationTime = locationGPS.getTime();
        }

        long NetLocationTime = 0;

        if (null != locationNet) {
            NetLocationTime = locationNet.getTime();
        }

        if ( 0 < GPSLocationTime - NetLocationTime ) {
            return locationGPS;
        }
        else {
            return locationNet;
        }
    }

    private boolean isBetterLocation(Location location, Location currentBestLocation) {
        if (currentBestLocation == null) {
            return true;
        }

        long timeDelta = location.getTime() - currentBestLocation.getTime();

        boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
        boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;

        boolean isNewer = timeDelta > 0;

        if (isSignificantlyNewer) {
            return true;
        } else if (isSignificantlyOlder) {
            return false;
        }

        int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());

        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 200;

        boolean isFromSameProvider = isSameProvider(location.getProvider(),currentBestLocation.getProvider());

        if (isMoreAccurate) {
            return true;
        } else if (isNewer && !isLessAccurate) {
            return true;
        } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
            return true;
        }
        return false;
    }

    public boolean isNetworkAvailable() {
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
            return provider2 == null;
        }
        return provider1.equals(provider2);
    }

    public double getLatitude(){
        if(location != null){
             return location.getLatitude();
        }

        return Double.NaN;
    }

    public double getLongitude(){
        if(location != null){
            return location.getLongitude();
        }
        return Double.NaN;
    }

    public String getLocationName(){
        return locationName;
    }

    public String getCityName() {
        return cityName;
    }

    public String getState(){
        return state;
    }

    public String getAddressName() {
        return addressName;
    }

    public String getCountryName() {
        return countryName;
    }
}
