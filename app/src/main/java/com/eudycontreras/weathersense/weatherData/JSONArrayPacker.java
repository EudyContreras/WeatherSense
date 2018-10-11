
package com.eudycontreras.weathersense.weatherData;

import org.json.JSONArray;

/**
 * Interface used for populating the
 * objects made from JSONArrays
 */
public interface JSONArrayPacker {

    void populate(JSONArray data);

    JSONArray toJSONArray();
}
