
package com.eudycontreras.weathersense.weatherData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * This class is JSON object holder
 * which holds all values for a specified weather
 * attribute or element
 */
public class WeatherForecastDaily implements JSONObjectPacker {

    public static final String WEATHER_FORECAST = "forecast";

    private TextForecast textForecast = new TextForecast();
    private SimpleForecast simpleForecast = new SimpleForecast();
    private ArrayList<ForecastDay> forecastDays = new ArrayList<>();


    public ArrayList<ForecastDay> getDailyForecast() {
        return forecastDays;
    }

    @Override
    public void populate(JSONObject data) {
        if(data==null)
            return;

        textForecast.populate(data.optJSONObject(TextForecast.TEXT_FORECAST));

        simpleForecast.populate(data.optJSONObject(SimpleForecast.SIMPLE_FORECAST));
    }

    @Override
    public JSONObject toJSON() {

        JSONObject data = new JSONObject();

        try {
            data.put(TextForecast.TEXT_FORECAST, textForecast.toJSON());
            data.put(SimpleForecast.SIMPLE_FORECAST, simpleForecast.toJSON());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return data;
    }

    public class TextForecast implements JSONObjectPacker {

        static final String TEXT_FORECAST = "txt_forecast";

        @Override
        public void populate(JSONObject data) {

            forecastDays.clear();

            for (int i = 0; i < 10; i++) {
                forecastDays.add(new ForecastDay());
            }

            JSONArray arrayData = data.optJSONArray(ForecastDay.FORECAST_DAY);

            int indexOne = 0;

            for (int i = 0; i < forecastDays.size(); i++) {
                forecastDays.get(i).populateOne(arrayData.optJSONObject(indexOne));
                indexOne+=2;
            }
        }

        @Override
        public JSONObject toJSON() {

            JSONObject data = new JSONObject();
            JSONArray arrayData = new JSONArray();

            try {

                for (int i = 0; i < forecastDays.size(); i++) {
                    JSONObject jsonItem = new JSONObject();
                    jsonItem.put(ForecastDay.FORECAST_DAY, forecastDays.get(i).toJSON());
                    arrayData.put(jsonItem);
                }
                data.put(ForecastDay.FORECAST_DAY, arrayData);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return data;
        }
    }

    public class SimpleForecast implements JSONObjectPacker {

        static final String SIMPLE_FORECAST = "simpleforecast";

        @Override
        public void populate(JSONObject data) {

            JSONArray arrayData = data.optJSONArray(ForecastDay.FORECAST_DAY);

            for (int i = 0; i < forecastDays.size(); i++) {
                forecastDays.get(i).populateTwo(arrayData.optJSONObject(i));
            }
        }

        @Override
        public JSONObject toJSON() {

            JSONObject data = new JSONObject();
            JSONArray arrayData = new JSONArray();

            try {

                for (int i = 0; i < forecastDays.size() * 2; i++) {
                    JSONObject jsonItem = new JSONObject();
                    jsonItem.put(ForecastDay.FORECAST_DAY, forecastDays.get(i).toJSON());
                    arrayData.put(jsonItem);
                }
                data.put(ForecastDay.FORECAST_DAY, arrayData);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return data;
        }
    }

    public static class ForecastDay implements JSONObjectPacker {

        public static final String FORECAST_DAY = "forecastday";

        private static final String PERIOD = "period";
        private static final String ICON = "icon";
        private static final String TITLE = "title";
        private static final String DESCRIPTION_F = "fcttext";
        private static final String DESCRIPTION_C = "fcttext_metric";
        private static final String CONDITION = "conditions";
        private static final String AVERAGE_HUMIDITY = "avehumidity";
        private static final String MAX_HUMIDITY = "maxhumidity";
        private static final String MIN_HUMIDITY = "minhumidity";

        private Date date = new Date();
        private Low low = new Low();
        private High high = new High();
        private SnowDay snowDay = new SnowDay();
        private SnowNight snowNight = new SnowNight();
        private SnowAllDay snowAllDay = new SnowAllDay();
        private MaxWind maxWind = new MaxWind();
        private AveWind aveWind = new AveWind();

        private String period;
        private String icon = "unknown";
        private String title;
        private String descriptionF;
        private String descriptionC;
        private String condition;
        private String averageHumidity;
        private String maxHumidity;
        private String minHumidity;

        private boolean isDummyData = false;

        public void setIcon(String icon) {
            this.icon = icon;
        }
        public boolean isDummyData() {
            return isDummyData;
        }

        public void setDummyData(boolean dummyData) {
            isDummyData = dummyData;
        }

        public final String getTitle() {
            return title;
        }

        public final Date getDate() {
            return date;
        }

        public final Low getLow() {
            return low;
        }

        public final High getHigh() {
            return high;
        }

        public AveWind getAveWind() {
            return aveWind;
        }

        public MaxWind getMaxWind() {
            return maxWind;
        }

        public SnowAllDay getSnowAllDay() {
            return snowAllDay;
        }

        public SnowNight getSnowNight() {
            return snowNight;
        }

        public SnowDay getSnowDay() {
            return snowDay;
        }

        public final String getPeriod() {
            return period;
        }

        public final String getIcon() {
            return icon;
        }

        public String getMaxHumidity() {
            return maxHumidity;
        }

        public String getMinHumidity() {
            return minHumidity;
        }

        public String getAverageHumidity() {
            return averageHumidity;
        }

        public final String getDescriptionF() {
            return descriptionF;
        }

        public final String getDescriptionC() {
            return descriptionC;
        }

        public final String getCondition() {
            return condition;
        }

        public void populateOne(JSONObject data) {
            period = data.optString(PERIOD);
            title = data.optString(TITLE);
            descriptionF = data.optString(DESCRIPTION_F);
            descriptionC = data.optString(DESCRIPTION_C);
        }

        public void populateTwo(JSONObject data) {
            date.populate(data.optJSONObject(Date.DATE));
            high.populate(data.optJSONObject(High.HIGH));
            low.populate(data.optJSONObject(Low.LOW));
            maxWind.populate(data.optJSONObject(MaxWind.MAX_WIND));
            aveWind.populate(data.optJSONObject(AveWind.AVE_WIND));
            snowAllDay.populate(data.optJSONObject(SnowAllDay.SNOW_ALL_DAY));
            snowDay.populate(data.optJSONObject(SnowDay.SNOW_DAY));
            snowNight.populate(data.optJSONObject(SnowNight.SNOW_NIGHT));

            icon = data.optString(ICON);
            condition = data.optString(CONDITION);
            maxHumidity = data.optString(MAX_HUMIDITY);
            averageHumidity = data.optString(AVERAGE_HUMIDITY);
            minHumidity = data.optString(MIN_HUMIDITY);
        }
        @Override
        public void populate(JSONObject data) {
            date.populate(data.optJSONObject(Date.DATE));
            high.populate(data.optJSONObject(High.HIGH));
            low.populate(data.optJSONObject(Low.LOW));
            maxWind.populate(data.optJSONObject(MaxWind.MAX_WIND));
            aveWind.populate(data.optJSONObject(AveWind.AVE_WIND));
            snowAllDay.populate(data.optJSONObject(SnowAllDay.SNOW_ALL_DAY));
            snowDay.populate(data.optJSONObject(SnowDay.SNOW_DAY));
            snowNight.populate(data.optJSONObject(SnowNight.SNOW_NIGHT));

            period = data.optString(PERIOD);
            icon = data.optString(ICON);
            title = data.optString(TITLE);
            descriptionF = data.optString(DESCRIPTION_F);
            descriptionC = data.optString(DESCRIPTION_C);
            condition = data.optString(CONDITION);
            maxHumidity = data.optString(MAX_HUMIDITY);
            averageHumidity = data.optString(AVERAGE_HUMIDITY);
            minHumidity = data.optString(MIN_HUMIDITY);
        }

        @Override
        public JSONObject toJSON() {
            JSONObject data = new JSONObject();

            try {
                data.put(Date.DATE, getDate().toJSON());
                data.put(High.HIGH, getHigh().toJSON());
                data.put(Low.LOW, getLow().toJSON());
                data.put(SnowAllDay.SNOW_ALL_DAY, getSnowAllDay().toJSON());
                data.put(SnowDay.SNOW_DAY, getSnowDay().toJSON());
                data.put(SnowNight.SNOW_NIGHT, getSnowNight().toJSON());
                data.put(MaxWind.MAX_WIND, getMaxWind().toJSON());
                data.put(AveWind.AVE_WIND, getAveWind().toJSON());

                data.put(PERIOD, getPeriod());
                data.put(ICON, getIcon());
                data.put(TITLE, getTitle());
                data.put(DESCRIPTION_F, getDescriptionF());
                data.put(DESCRIPTION_C, getDescriptionC());
                data.put(CONDITION, getCondition());
                data.put(MAX_HUMIDITY, getMaxHumidity());
                data.put(MIN_HUMIDITY, getMinHumidity());
                data.put(AVERAGE_HUMIDITY, getAverageHumidity());

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return data;
        }

        public class Date implements JSONObjectPacker {
            public static final String DATE = "date";


            private static final String DAY = "day";
            private static final String MONTH = "month";
            private static final String HOUR = "hour";
            private static final String MINUTE = "minute";
            private static final String MONTH_NAME = "monthname";
            private static final String WEEKDAY_SHORT = "weekday_short";
            private static final String WEEKDAY = "weekday";

            private String day;
            private String month;
            private String hour;
            private String minute;
            private String monthName;
            private String weekdayShort;
            private String weekday;

            public final String getHour() {
                return hour;
            }

            public final String getMonth() {
                return month;
            }

            public final String getDay() {
                return day;
            }

            public final String getMonthName() {
                return monthName;
            }

            public final String getMinute() {
                return minute;
            }

            public final String getWeekdayShort() {
                return weekdayShort;
            }

            public final String getWeekday() {
                return weekday;
            }

            @Override
            public void populate(JSONObject data) {
                day = data.optString(DAY);
                month = data.optString(MONTH);
                hour = data.optString(HOUR);
                minute = data.optString(MINUTE);
                monthName = data.optString(MONTH_NAME);
                weekdayShort = data.optString(WEEKDAY_SHORT);
                weekday = data.optString(WEEKDAY);
            }

            @Override
            public JSONObject toJSON() {
                JSONObject data = new JSONObject();

                try {
                    data.put(DAY, getDay());
                    data.put(MONTH, getMonth());
                    data.put(HOUR, getHour());
                    data.put(MINUTE, getMinute());
                    data.put(MONTH_NAME, getMonthName());
                    data.put(WEEKDAY_SHORT, getWeekdayShort());
                    data.put(WEEKDAY, getWeekday());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return data;
            }
        }

        public class High implements JSONObjectPacker {

            public static final String HIGH = "high";

            private static final String FAHRENHEIT = "fahrenheit";
            private static final String CELSIUS = "celsius";

            private String fahrenheit;
            private String celsius;

            public final String getFahrenheit() {
                return fahrenheit;
            }

            public final String getCelsius() {
                return celsius;
            }

            @Override
            public void populate(JSONObject data) {
                fahrenheit = data.optString(FAHRENHEIT);
                celsius = data.optString(CELSIUS);
            }

            @Override
            public JSONObject toJSON() {
                JSONObject data = new JSONObject();
                try {
                    data.put(FAHRENHEIT, getFahrenheit());
                    data.put(CELSIUS, getCelsius());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return data;
            }
        }

       public class Low implements JSONObjectPacker {

            public static final String LOW = "low";

            private static final String FAHRENHEIT = "fahrenheit";
            private static final String CELSIUS = "celsius";

            private String fahrenheit;
            private String celsius;

            public final String getFahrenheit() {
                return fahrenheit;
            }

            public final String getCelsius() {
                return celsius;
            }

            @Override
            public void populate(JSONObject data) {
                fahrenheit = data.optString(FAHRENHEIT);
                celsius = data.optString(CELSIUS);
            }

            @Override
            public JSONObject toJSON() {
                JSONObject data = new JSONObject();
                try {
                    data.put(FAHRENHEIT, getFahrenheit());
                    data.put(CELSIUS, getCelsius());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return data;
            }
        }

        public class SnowDay implements JSONObjectPacker {

            public static final String SNOW_DAY = "snow_day";

            private static final String INCHES = "in";
            private static final String CENTIMETERS = "cm";

            private String inches;
            private String centimeters;

            public final String getInches() {
                return inches;
            }

            public final String getCentimeters() {
                return centimeters;
            }

            @Override
            public void populate(JSONObject data) {
                inches = data.optString(INCHES);
                centimeters = data.optString(CENTIMETERS);
            }

            @Override
            public JSONObject toJSON() {
                JSONObject data = new JSONObject();
                try {
                    data.put(INCHES, getInches());
                    data.put(CENTIMETERS, getCentimeters());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return data;
            }
        }

        public class SnowNight implements JSONObjectPacker {

            public static final String SNOW_NIGHT = "snow_night";

            private static final String INCHES = "in";
            private static final String CENTIMETERS = "cm";

            private String inches;
            private String centimeters;

            public final String getInches() {
                return inches;
            }

            public final String getCentimeters() {
                return centimeters;
            }

            @Override
            public void populate(JSONObject data) {
                inches = data.optString(INCHES);
                centimeters = data.optString(CENTIMETERS);
            }

            @Override
            public JSONObject toJSON() {
                JSONObject data = new JSONObject();
                try {
                    data.put(INCHES, getInches());
                    data.put(CENTIMETERS, getCentimeters());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return data;
            }
        }

        public class SnowAllDay implements JSONObjectPacker {

            public static final String SNOW_ALL_DAY = "snow_allday";

            private static final String INCHES = "in";
            private static final String CENTIMETERS = "cm";

            private String inches;
            private String centimeters;

            public final String getInches() {
                return inches;
            }

            public final String getCentimeters() {
                return centimeters;
            }

            @Override
            public void populate(JSONObject data) {
                inches = data.optString(INCHES);
                centimeters = data.optString(CENTIMETERS);
            }

            @Override
            public JSONObject toJSON() {
                JSONObject data = new JSONObject();
                try {
                    data.put(INCHES, getInches());
                    data.put(CENTIMETERS, getCentimeters());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return data;
            }
        }

       public class MaxWind implements JSONObjectPacker {

            public static final String MAX_WIND = "maxwind";

            private static final String DIRECTION = "dir";
            private static final String DEGREES = "degrees";
            private static final String KPH = "kph";
            private static final String MPH = "mph";

            private String direction;
            private String degrees;
            private String mph;
            private String kph;

            public final String getDegrees() {
                return degrees;
            }

            public final String getMph() {
                return mph;
            }

            public final String getKph() {
                return kph;
            }

            public final String getDirection() {
                return direction;
            }

            @Override
            public void populate(JSONObject data) {
                direction = data.optString(DIRECTION);
                degrees = data.optString(DEGREES);
                kph = data.optString(KPH);
                mph = data.optString(MPH);
            }

            @Override
            public JSONObject toJSON() {
                JSONObject data = new JSONObject();
                try {
                    data.put(DIRECTION, getDirection());
                    data.put(DEGREES, getDegrees());
                    data.put(KPH, getKph());
                    data.put(MPH, getMph());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return data;
            }
        }

       public class AveWind implements JSONObjectPacker {

            public static final String AVE_WIND = "avewind";

            private static final String DIRECTION = "dir";
            private static final String DEGREES = "degrees";
            private static final String KPH = "kph";
            private static final String MPH = "mph";

            private String direction;
            private String degrees;
            private String mph;
            private String kph;

            public final String getDegrees() {
                return degrees;
            }

            public final String getMph() {
                return mph;
            }

            public final String getKph() {
                return kph;
            }

            public final String getDirection() {
                return direction;
            }

            @Override
            public void populate(JSONObject data) {
                direction = data.optString(DIRECTION);
                degrees = data.optString(DEGREES);
                kph = data.optString(KPH);
                mph = data.optString(MPH);
            }

            @Override
            public JSONObject toJSON() {
                JSONObject data = new JSONObject();
                try {
                    data.put(DIRECTION, getDirection());
                    data.put(DEGREES, getDegrees());
                    data.put(KPH, getKph());
                    data.put(MPH, getMph());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return data;
            }
        }
    }
}
