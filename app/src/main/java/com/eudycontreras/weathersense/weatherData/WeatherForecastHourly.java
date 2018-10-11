
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
public class WeatherForecastHourly implements JSONArrayPacker {

    public static final String HOURLY_FORECAST = "hourly_forecast";

    private ArrayList<HourForecast> hourForecasts;

    public WeatherForecastHourly() {
        hourForecasts = new ArrayList<>();
    }

    public ArrayList<HourForecast> getHourForecasts(){
        return hourForecasts;
    }

    @Override
    public void populate(JSONArray data) {
        hourForecasts.clear();

        if(data==null)
            return;

        for (int i = 0; i < 12; i++) {
            HourForecast hourForecast = new HourForecast();
            hourForecast.populate(data.optJSONObject(i));
            hourForecasts.add(hourForecast);
        }
    }

    @Override
    public JSONArray toJSONArray() {

        JSONArray arrayData = new JSONArray();

        try {
            for (HourForecast hourForecast : hourForecasts) {
                JSONObject jsonItem = new JSONObject();
                jsonItem.put(HourForecast.HOURLY_FORECAST, hourForecast.toJSON());
                arrayData.put(jsonItem);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return arrayData;
    }

    public static class HourForecast implements JSONObjectPacker {

        public static final String HOURLY_FORECAST = "hourly_forecast";

        private static final String CONDITION = "condition";
        private static final String ICON = "icon";
        private static final String UV_INDEX = "uvi";
        private static final String HUMIDITY = "humidity";

        private TimeInfo timeInfo = new TimeInfo();
        private Temperature temperature = new Temperature();
        private WindSpeed windSpeed = new WindSpeed();
        private WindDirection windDirection = new WindDirection();
        private FellsLike fellsLike = new FellsLike();

        private String condition;
        private String icon = "unknown";
        private String UVIndex;
        private String humidity;

        private boolean isDummyData = false;

        public boolean isDummyData() {
            return isDummyData;
        }

        public void setDummyData(boolean dummyData) {
            isDummyData = dummyData;
        }

        public TimeInfo getTimeInfo() {
            return timeInfo;
        }

        public Temperature getTemperature() {
            return temperature;
        }

        public WindSpeed getWindSpeed() {
            return windSpeed;
        }

        public WindDirection getWindDirection() {
            return windDirection;
        }

        public FellsLike getFellsLike() {
            return fellsLike;
        }

        public String getCondition() {
            return condition;
        }

        public String getIcon() {
            return icon;
        }

        public String getUVIndex() {
            return UVIndex;
        }

        public String getHumidity() {
            return humidity;
        }

        @Override
        public void populate(JSONObject data) {
            if(data==null)
                return;

            timeInfo.populate(data.optJSONObject(TimeInfo.TIME_INFO));
            temperature.populate(data.optJSONObject(Temperature.TEMPERATURE));
            windDirection.populate(data.optJSONObject(WindDirection.WIND_DIRECTION));
            windSpeed.populate(data.optJSONObject(WindSpeed.WIND_SPEED));
            fellsLike.populate(data.optJSONObject(FellsLike.FEELS_LIKE));

            condition = data.optString(CONDITION);
            icon = data.optString(ICON);
            UVIndex = data.optString(UV_INDEX);
            humidity = data.optString(HUMIDITY);

        }

        @Override
        public JSONObject toJSON() {

            JSONObject data = new JSONObject();

            try {
                data.put(TimeInfo.TIME_INFO, getTimeInfo());
                data.put(Temperature.TEMPERATURE, getTemperature());
                data.put(WindDirection.WIND_DIRECTION, getWindDirection());
                data.put(WindSpeed.WIND_SPEED, getWindSpeed());
                data.put(FellsLike.FEELS_LIKE, getFellsLike());

                data.put(CONDITION, getCondition());
                data.put(ICON, getIcon());
                data.put(UV_INDEX, getUVIndex());
                data.put(HUMIDITY, getHumidity());

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return data;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public class TimeInfo implements JSONObjectPacker {

            public static final String TIME_INFO = "FCTTIME";

            private static final String HOUR = "hour_padded";
            private static final String MINUTE = "min";
            private static final String MONTH = "mon_padded";
            private static final String MONTH_NAME = "month_name";
            private static final String YEAR = "year";
            private static final String MONTH_ABREV = "month_abbrev";
            private static final String TIME = "civil";
            private static final String WEEKDAY = "weekday_name";
            private static final String MONTH_DAY ="mday_padded";
            private static final String WEEKDAY_NGTH = "weekday_name_night";
            private static final String AM_PM = "ampm";

            private String hour;
            private String minute;
            private String month;
            private String monthName;
            private String weekDay;
            private String weekDayNight;
            private String am_pm;
            private String time;
            private String year;
            private String monthAbbrev;
            private String monthDay;

            public String getMonthDay() {
                return monthDay;
            }

            public String getAm_pm() {
                return am_pm;
            }

            public final String getYear() {
                return year;
            }

            public final String getMonthAbbrev() {
                return monthAbbrev;
            }

            public final String getHour() {
                return hour;
            }

            public final String getMinute() {
                return minute;
            }

            public final String getMonth() {
                return month;
            }

            public final String getMonthName() {
                return monthName;
            }

            public final String getWeekDay() {
                return weekDay;
            }

            public final String getWeekDayNight() {
                return weekDayNight;
            }

            public final String getAMPM() {
                return am_pm;
            }

            public final String getTime() {
                return time;
            }

            public String getFormattedTime(){
                return hour+":"+minute;
            }

            public String getFormattedTimeStamp(){
                return String.valueOf(year+"-"+month+"-"+monthDay+" "+ hour+":"+minute);
            }

            public String getFormattedDate(){
                return weekDay+", "+month+", "+year;
            }

            @Override
            public void populate(JSONObject data) {
                hour = data.optString(HOUR);
                minute = data.optString(MINUTE);
                month = data.optString(MONTH);
                monthName = data.optString(MONTH_NAME);
                time = data.optString(TIME);
                weekDay = data.optString(WEEKDAY);
                weekDayNight = data.optString(WEEKDAY_NGTH);
                monthAbbrev = data.optString(MONTH_ABREV);
                year = data.optString(YEAR);
                am_pm = data.optString(AM_PM);
                monthDay = data.optString(MONTH_DAY);

            }

            @Override
            public JSONObject toJSON() {

                JSONObject data = new JSONObject();

                try {
                    data.put(YEAR, getYear());
                    data.put(MONTH_ABREV, getMonthAbbrev());
                    data.put(HOUR, getHour());
                    data.put(MINUTE, getMinute());
                    data.put(MONTH, getMonth());
                    data.put(MONTH_NAME, getMonthName());
                    data.put(TIME, getTime());
                    data.put(WEEKDAY, getWeekDay());
                    data.put(WEEKDAY_NGTH, getWeekDayNight());
                    data.put(AM_PM, getAMPM());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return data;
            }
        }

        public class Temperature implements JSONObjectPacker {
            public static final String TEMPERATURE = "temp";

            private static final String FAHRENHEIT = "english";
            private static final String CELSIUS = "metric";

            private String fahrenheit;
            private String celsius;

            public final String getCelsius() {
                return celsius;
            }

            public final String getFahrenheit() {
                return fahrenheit;
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

        public class WindSpeed implements JSONObjectPacker {

            public static final String WIND_SPEED = "wspd";

            private static final String KPH = "metric";
            private static final String MPH = "english";

            private String kph;
            private String mph;

            public final String getMph() {
                return mph;
            }

            public final String getKph() {
                return kph;
            }

            @Override
            public void populate(JSONObject data) {
                mph = data.optString(MPH);
                kph = data.optString(KPH);
            }

            @Override
            public JSONObject toJSON() {
                JSONObject data = new JSONObject();

                try {
                    data.put(MPH, getMph());
                    data.put(KPH, getKph());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return data;
            }
        }

        public class WindDirection implements JSONObjectPacker {

            public static final String WIND_DIRECTION = "wdir";

            private static final String DIRECTION = "dir";
            private static final String DEGREES = "degrees";

            private String direction;
            private String degrees;

            public final String getDirection() {
                return direction;
            }

            public final String getDegrees() {
                return degrees;
            }

            @Override
            public void populate(JSONObject data) {
                direction = data.optString(DIRECTION);
                degrees = data.optString(DEGREES);
            }

            @Override
            public JSONObject toJSON() {
                JSONObject data = new JSONObject();

                try {
                    data.put(DIRECTION, getDirection());
                    data.put(DEGREES, getDegrees());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return data;
            }
        }

        public class FellsLike implements JSONObjectPacker {

            public static final String FEELS_LIKE = "feelslike";

            private static final String FAHRENHEIT = "english";
            private static final String CELSIUS = "metric";

            private String fahrenheit;
            private String celsius;

            public final String getCelsius() {
                return celsius;
            }

            public final String getFahrenheit() {
                return fahrenheit;
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
    }
}
