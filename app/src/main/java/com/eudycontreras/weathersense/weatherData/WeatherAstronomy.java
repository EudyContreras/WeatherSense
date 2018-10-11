
package com.eudycontreras.weathersense.weatherData;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * This class is JSON object holder
 * which holds all values for a specified weather
 * attribute or element
 */
public class WeatherAstronomy implements JSONObjectPacker {

    public static final String ASTRONOMY = "moon_phase";

    private CurrentTime currentTime = new CurrentTime();
    private Sunrise sunrise;
    private Sunset sunset;

    public final Sunset getSunset() {
        return sunset;
    }

    public final Sunrise getSunrise() {
        return sunrise;
    }

    public final CurrentTime getCurrentTime() {
        return currentTime;
    }


    @Override
    public void populate(JSONObject data) {
        if(data==null)
            return;

        currentTime = new CurrentTime();
        currentTime.populate(data.optJSONObject(CurrentTime.CURRENT_TIME));

        sunrise = new Sunrise();
        sunrise.populate(data.optJSONObject(Sunrise.SUNRISE));

        sunset = new Sunset();
        sunset.populate(data.optJSONObject(Sunset.SUNSET));
    }

    @Override
    public JSONObject toJSON() {

        JSONObject data = new JSONObject();

        try {

            data.put(CurrentTime.CURRENT_TIME, getCurrentTime().toJSON());
            data.put(Sunrise.SUNRISE, getSunrise().toJSON());
            data.put(Sunset.SUNSET, getSunset().toJSON());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return data;
    }

    public class CurrentTime implements JSONObjectPacker{
        public final static String CURRENT_TIME = "current_time";

        private final static String HOUR = "hour";
        private final static String MINUTE = "minute";

        private String hour;
        private String minute;

        public  final String getHour() {
            return hour;
        }

        public final String getMinute() {
            return minute;
        }

        @Override
        public void populate(JSONObject data) {
            hour = data.optString(HOUR);
            minute = data.optString(MINUTE);
        }

        @Override
        public JSONObject toJSON() {

            JSONObject data = new JSONObject();

            try {
                data.put(HOUR,getHour());
                data.put(MINUTE,getMinute());

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return data;
        }
    }

    public class Sunrise implements JSONObjectPacker{
        public final static String SUNRISE = "sunrise";

        private final static String HOUR = "hour";
        private final static String MINUTE = "minute";

        private String hour;
        private String minute;

        public final String getHour() {
            return hour;
        }

        public final String getMinute() {
            return minute;
        }

        public final String getFormattedTime(){
            return hour+":"+minute;
        }

        @Override
        public void populate(JSONObject data) {
            hour = data.optString(HOUR);
            minute = data.optString(MINUTE);
        }

        @Override
        public JSONObject toJSON() {

            JSONObject data = new JSONObject();

            try {
                data.put(HOUR,getHour());
                data.put(MINUTE,getMinute());

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return data;
        }
    }

   public class Sunset implements JSONObjectPacker{
        public final static String SUNSET = "sunset";

        private final static String HOUR = "hour";
        private final static String MINUTE = "minute";

        private String hour;
        private String minute;

        public final String getHour() {
            return hour;
        }

        public final String getMinute() {
            return minute;
        }

        public final String getFormattedTime(){
            return hour+":"+minute;
        }

        @Override
        public void populate(JSONObject data) {
            hour = data.optString(HOUR);
            minute = data.optString(MINUTE);
        }

        @Override
        public JSONObject toJSON() {

            JSONObject data = new JSONObject();

            try {
                data.put(HOUR,getHour());
                data.put(MINUTE,getMinute());

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return data;
        }
    }
}
