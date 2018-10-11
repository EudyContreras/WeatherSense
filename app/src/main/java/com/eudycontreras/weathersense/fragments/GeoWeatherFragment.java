package com.eudycontreras.weathersense.fragments;

import android.view.View;

/**
 * @author  Eudy Contreras
 * Interface which holds the default
 * methods for fragment.
 */

public interface GeoWeatherFragment {

    void initializeComponents(View view);
    void reinitializeCallback();
    void triggerInitialization();
    void resetAnimations();
    void updateEvent();
}

