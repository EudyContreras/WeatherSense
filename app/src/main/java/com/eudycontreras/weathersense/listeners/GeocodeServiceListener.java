
package com.eudycontreras.weathersense.listeners;


import com.eudycontreras.weathersense.weatherData.subAtributes.Address;

/**
 * @Author Eudy Contreras
 * Listener used for notifying the result of a request
 */
public interface GeocodeServiceListener {

    void geocodeSuccess(Address location);

    void geocodeFailure(Exception exception);
}
