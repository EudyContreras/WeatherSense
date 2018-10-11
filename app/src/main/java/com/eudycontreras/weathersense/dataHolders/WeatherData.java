package com.eudycontreras.weathersense.dataHolders;

import com.eudycontreras.weathersense.services.RequestType;
import com.eudycontreras.weathersense.weatherData.WeatherAstronomy;
import com.eudycontreras.weathersense.weatherData.WeatherConditions;
import com.eudycontreras.weathersense.weatherData.WeatherForecastDaily;
import com.eudycontreras.weathersense.weatherData.WeatherForecastHourly;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Eudy on 1/31/2017.
 */

public class WeatherData {


    private WeatherAstronomy weatherAstronomy;
    private WeatherConditions weatherCondition;
    private WeatherForecastDaily weatherForecastDaily;
    private WeatherForecastHourly weatherForecastHourly;

    private JSONObject astronomyObject;
    private JSONObject dailyForecastObject;
    private JSONObject conditionObject;
    private JSONObject hourlyForecastObject;

    public WeatherData(){
        this.weatherCondition = new WeatherConditions();
        this.weatherAstronomy = new WeatherAstronomy();
        this.weatherForecastDaily = new WeatherForecastDaily();
        this.weatherForecastHourly = new WeatherForecastHourly();
    }

    public void populate(RequestType requestType, JSONObject object){
        switch (requestType){
            case WEATHER_CONDITION:
                weatherCondition.populate(object);
                break;
            case WEATHER_ASTRONOMY:
                weatherAstronomy.populate(object);
                break;
            case WEATHER_FORECAST_DAILY:
                weatherForecastDaily.populate(object);
                break;
        }
    }

    public void populate(RequestType requestType, JSONArray array){
        weatherForecastHourly.populate(array);
    }

    public WeatherForecastHourly getWeatherForecastHourly() {
        return weatherForecastHourly;
    }

    public WeatherForecastDaily getWeatherForecastDaily() {
        return weatherForecastDaily;
    }

    public WeatherConditions getWeatherCondition() {
        return weatherCondition;
    }

    public WeatherAstronomy getWeatherAstronomy() {
        return weatherAstronomy;
    }

    public void setAstronomyObject(JSONObject astronomyObject) {
        this.astronomyObject = astronomyObject;
    }

    public void setDailyForecastObject(JSONObject dailyForecastObject) {
        this.dailyForecastObject = dailyForecastObject;
    }

    public void setHourlyForecastObject(JSONObject hourlyForecastObject) {
        this.hourlyForecastObject = hourlyForecastObject;
    }

    public void setConditionObject(JSONObject conditionObject) {
        this.conditionObject = conditionObject;
    }

    public JSONObject getAstronomyObject() {
        return astronomyObject;
    }

    public JSONObject getDailyForecastObject() {
        return dailyForecastObject;
    }

    public JSONObject getConditionObject() {
        return conditionObject;
    }

    public JSONObject getHourlyForecastObject() {
        return hourlyForecastObject;
    }
}
