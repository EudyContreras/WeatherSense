
package com.eudycontreras.weathersense.listeners;


import com.eudycontreras.weathersense.services.ResponseWrapper;

/**
 * @Author Eudy Contreras
 * Listener used for notifying the result of a request
 */

public interface WeatherServiceListener {

    void serviceSuccess(ResponseWrapper response);

    void serviceFailure(Exception exception);
}
