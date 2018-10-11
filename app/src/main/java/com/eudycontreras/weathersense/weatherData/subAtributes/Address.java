
package com.eudycontreras.weathersense.weatherData.subAtributes;

import com.eudycontreras.weathersense.weatherData.JSONObjectPacker;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class is JSON object holder
 * which holds all values for a specified weather
 * attribute or element
 */
public class Address implements JSONObjectPacker {

    private static final String ADDRESS = "formatted_address";
    private String address;

    public String getLocation() {
        return address;
    }

    @Override
    public void populate(JSONObject data) {
        address = data.optString(ADDRESS);
    }

    @Override
    public JSONObject toJSON() {
        JSONObject data = new JSONObject();

        try {
            data.put(ADDRESS, getLocation());

        } catch (JSONException e) {}

        return data;
    }
}
