package com.eudycontreras.weathersense.services;

import com.eudycontreras.weathersense.weatherData.JSONObjectPacker;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Eudy on 1/31/2017.
 */

public class RequestError implements JSONObjectPacker {

    public static final String ERROR = "error";

    public static final String TYPE = "type";
    public static final String DESCRIPTION = "description";

    private String type;
    private String description;

    public final String getType() {
        return type;
    }

    public final String getDescription() {
        return description;
    }

    @Override
    public void populate(JSONObject data) {
        type = data.optString(TYPE);
        description = data.optString(DESCRIPTION);
    }

    @Override
    public JSONObject toJSON() {
        JSONObject data = new JSONObject();
        try {
            data.put(TYPE, getType());
            data.put(DESCRIPTION, getDescription());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return data;
    }
}
