package com.eudycontreras.weathersense.fragments;

import android.os.Bundle;

/**
 * Created by Eudy on 1/30/2017.
 */

public interface WeatherSegment {

    void resume();
    void pause();
    void destroy();
    void saveState(Bundle savedState);
}
