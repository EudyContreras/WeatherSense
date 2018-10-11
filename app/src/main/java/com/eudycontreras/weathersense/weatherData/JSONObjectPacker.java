
package com.eudycontreras.weathersense.weatherData;

import org.json.JSONObject;

/**
 * Interface used for populating objects
 * made from JSONObjects
 */
public interface JSONObjectPacker {

    void populate(JSONObject data);

    JSONObject toJSON();
}
