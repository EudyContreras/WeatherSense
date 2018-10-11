
package com.eudycontreras.weathersense.weatherData;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * This class is JSON object holder
 * which holds all values for a specified weather
 * attribute or element
 */
public class WeatherConditions implements JSONObjectPacker {

    public static final String WEATHER_CONDITION = "current_observation";

    private static final String LOCATION = "display_location";
    private static final String WEATHER = "weather";
    private static final String TEMPERATURE_F = "temp_f";
    private static final String TEMPERATURE_C = "temp_c";
    private static final String HUMIDITY = "relative_humidity";
    private static final String WIND_DIRECTION = "wind_dir";
    private static final String WIND_DEGREES = "wind_degrees";
    private static final String WIND_MPH = "wind_mph";
    private static final String WIND_KPH = "wind_kph";
    private static final String PRESSURE_MILLIBARS = "pressure_mb";
    private static final String PRESSURE_INCHES = "pressure_in";
    private static final String WIND_CHILL_F = "windchill_f";
    private static final String WIND_CHILL_C = "windchill_c";
    private static final String FEELS_LIKE_F = "feelslike_f";
    private static final String FEELS_LIKE_C = "feelslike_c";
    private static final String VISIBILITY_MI = "visibility_mi";
    private static final String VISIBILITY_KM = "visibility_km";
    private static final String UV_LIGHT = "UV";
    private static final String ICON = "icon";

    private DisplayLocation locationData = new DisplayLocation();
    private String weather;
    private String tempC;
    private String tempF;
    private String humidity;
    private String windDir;
    private String windDeg;
    private String windKph;
    private String windMph;
    private String pressureMB;
    private String pressureIN;
    private String windChillF;
    private String windChillC;
    private String feelsLikeC;
    private String feelsLikeF;
    private String visibilityMi;
    private String visibilityKm;
    private String location;
    private String uvLight;
    private String icon;

    public final String getWindChillF() {
        return windChillF;
    }

    public final DisplayLocation getLocationData() {
        return locationData;
    }

    public final String getWeather() {
        return weather;
    }

    public final String getTempC() {
        return tempC;
    }

    public final String getTempF() {
        return tempF;
    }

    public final String getHumidity() {
        return humidity;
    }

    public final String getWindDir() {
        return windDir;
    }

    public final String getWindDeg() {
        return windDeg;
    }

    public final String getWindKph() {
        return windKph;
    }

    public final String getWindMph() {
        return windMph;
    }

    public final String getPressureMB() {
        return pressureMB;
    }

    public final String getPressureIN() {
        return pressureIN;
    }

    public final String getWindChillC() {
        return windChillC;
    }

    public final String getFeelsLikeC() {
        return feelsLikeC;
    }

    public final String getFeelsLikeF() {
        return feelsLikeF;
    }

    public final String getVisibilityMi() {
        return visibilityMi;
    }

    public final String getVisibilityKm() {
        return visibilityKm;
    }

    public final String getLocation() {
        return location;
    }

    public final String getUvLight() {
        return uvLight;
    }

    public final String getIcon() {
        return icon;
    }

    @Override
    public void populate(JSONObject data) {
        if(data==null)
            return;

        locationData.populate(data.optJSONObject(LOCATION));
        location = String.format("%s, %s", locationData.getCity(), locationData.getStateName());

        weather = data.optString(WEATHER);
        tempC = data.optString(TEMPERATURE_C);
        tempF = data.optString(TEMPERATURE_F);
        humidity = data.optString(HUMIDITY);
        windDir = data.optString(WIND_DIRECTION);
        windDeg = data.optString(WIND_DEGREES);
        windKph = data.optString(WIND_KPH);
        windMph = data.optString(WIND_MPH);
        windChillC = data.optString(WIND_CHILL_C);
        windChillF = data.optString(WIND_CHILL_F);
        pressureMB = data.optString(PRESSURE_MILLIBARS);
        pressureIN = data.optString(PRESSURE_INCHES);
        feelsLikeC = data.optString(FEELS_LIKE_C);
        feelsLikeF = data.optString(FEELS_LIKE_F);
        visibilityKm = data.optString(VISIBILITY_KM);
        visibilityMi = data.optString(VISIBILITY_MI);
        uvLight = data.optString(UV_LIGHT);
        icon = data.optString(ICON);
    }

    @Override
    public JSONObject toJSON() {

        JSONObject data = new JSONObject();

        try {
            data.put(WEATHER, getWeather());
            data.put(TEMPERATURE_F, getTempF());
            data.put(TEMPERATURE_C, getTempC());
            data.put(WIND_DIRECTION, getWindDir());
            data.put(WIND_DEGREES, getWindDeg());
            data.put(WIND_MPH, getWindMph());
            data.put(WIND_KPH, getWindKph());
            data.put(HUMIDITY, getHumidity());
            data.put(PRESSURE_MILLIBARS, getPressureMB());
            data.put(PRESSURE_INCHES, getPressureIN());
            data.put(WIND_CHILL_C, getWindChillC());
            data.put(WIND_CHILL_F, getWindChillF());
            data.put(FEELS_LIKE_C, getFeelsLikeC());
            data.put(FEELS_LIKE_F, getFeelsLikeF());
            data.put(VISIBILITY_MI, getVisibilityMi());
            data.put(VISIBILITY_KM, getVisibilityKm());
            data.put(UV_LIGHT, getUvLight());
            data.put(ICON, getIcon());

            data.put(LOCATION, getLocationData().toJSON());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return data;
    }

    public class DisplayLocation implements JSONObjectPacker {

        private static final String FULL = "full";
        private static final String CITY = "city";
        private static final String STATE = "state";
        private static final String STATE_NAME = "state_name";
        private static final String COUNTRY = "country";
        private static final String ZIP = "zip";
        private static final String LATITUDE = "latitude";
        private static final String LONGITUDE = "longitude";
        private static final String ELEVATION = "elevation";

        private String full;
        private String city;
        private String state;
        private String stateName;
        private String country;
        private String zip;
        private String latitude;
        private String longitude;
        private String elevation;

        public final String getFull() {
            return full;
        }

        public final String getCity() {
            return city;
        }

        public final String getState() {
            return state;
        }

        public final String getStateName() {
            return stateName;
        }

        public final String getCountry() {
            return country;
        }

        public final String getZip() {
            return zip;
        }

        public final String getLongitude() {
            return longitude;
        }

        public final String getLattitude() {
            return latitude;
        }

        public final String getElevation() {
            return elevation;
        }

        @Override
        public void populate(JSONObject data) {
            full = data.optString(FULL);
            city = data.optString(CITY);
            state = data.optString(STATE);
            stateName = data.optString(STATE_NAME);
            country = data.optString(COUNTRY);
            zip = data.optString(ZIP);
            latitude = data.optString(LATITUDE);
            longitude = data.optString(LONGITUDE);
            elevation = data.optString(ELEVATION);
        }

        @Override
        public JSONObject toJSON() {
            JSONObject data = new JSONObject();

            try {
                data.put(FULL, getFull());
                data.put(CITY, getCity());
                data.put(STATE, getState());
                data.put(STATE_NAME, getStateName());
                data.put(COUNTRY, getCountry());
                data.put(ZIP, getZip());
                data.put(LATITUDE, getLattitude());
                data.put(LONGITUDE, getLongitude());
                data.put(ELEVATION, getElevation());

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return data;
        }
    }
}
