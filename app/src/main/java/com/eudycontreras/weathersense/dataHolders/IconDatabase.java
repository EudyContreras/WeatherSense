package com.eudycontreras.weathersense.dataHolders;

import java.util.HashMap;

/**
 * Created by Eudy on 2/6/2017.
 */

public class IconDatabase {

    public static final String KEY_CHANCE_OF_FLURRIES = "chanceflurries";
    public static final String KEY_CHANCE_OF_RAIN = "chancerain";
    public static final String KEY_CHANCE_OF_FREEZING_RAIN = "chancesleet";
    public static final String KEY_CHANCE_OF_SNOW = "chancesnow";
    public static final String KEY_CHANCE_OF_THUNDER_STORM = "chancetstorms";
    public static final String KEY_CLEAR = "clear";
    public static final String KEY_CLOUDY = "Cloudy";
    public static final String KEY_FLURRIES = "flurries";
    public static final String KEY_FOG = "fog";
    public static final String KEY_HAZE = "hazy";
    public static final String KEY_MOSTLY_CLOUDY = "mostlycloudy";
    public static final String KEY_MOSTLY_SUNNY = "mostlysunny";
    public static final String KEY_PARTLY_CLOUDY = "partlycloudy";
    public static final String KEY_PARTLY_SUNNY = "partlysunny";
    public static final String KEY_RAIN = "rain";
    public static final String KEY_FREEZING_RAIN = "sleet";
    public static final String KEY_SNOW = "snow";
    public static final String KEY_SUNNY = "Sunny";
    public static final String KEY_THUNDER_STORMS = "tstorms";
    public static final String KEY_UNKNOWN = "unknown";

    private NighTime nighTime;
    private DayTime dayTime;

    public IconDatabase(){
        dayTime = new DayTime();
        nighTime = new NighTime();
    }

    public DayTime getDayTime() {
        return dayTime;
    }

    public NighTime getNighTime() {
        return nighTime;
    }

    public class NighTime{
        private HashMap<String, String> nighttimeIcons = new HashMap<>();

        public NighTime(){
            nighttimeIcons.put(KEY_CHANCE_OF_FLURRIES,"&#xf024");
            nighttimeIcons.put(KEY_CHANCE_OF_RAIN,"&#xf029");
            nighttimeIcons.put(KEY_CHANCE_OF_FREEZING_RAIN,"&#xf0b4");
            nighttimeIcons.put(KEY_CHANCE_OF_SNOW,"&#xf02a");
            nighttimeIcons.put(KEY_CHANCE_OF_THUNDER_STORM,"&#xf02d");
            nighttimeIcons.put(KEY_CLEAR,"&#xf02e");
            nighttimeIcons.put(KEY_CLOUDY,"&#xf086");
            nighttimeIcons.put(KEY_FLURRIES,"&#xf032");
            nighttimeIcons.put(KEY_FOG,"&#xf04a");
            nighttimeIcons.put(KEY_HAZE,"&#xf023");
            nighttimeIcons.put(KEY_MOSTLY_CLOUDY,"&#xf013");
            nighttimeIcons.put(KEY_MOSTLY_SUNNY,"&#xf031");
            nighttimeIcons.put(KEY_PARTLY_CLOUDY,"&#xf083");
            nighttimeIcons.put(KEY_PARTLY_SUNNY,"&#xf07e");
            nighttimeIcons.put(KEY_RAIN,"&#xf036");
            nighttimeIcons.put(KEY_FREEZING_RAIN,"&#xf034");
            nighttimeIcons.put(KEY_SNOW,"&#xf02a");
            nighttimeIcons.put(KEY_SUNNY,"&#xf02e");
            nighttimeIcons.put(KEY_THUNDER_STORMS,"&#xf01e");
            nighttimeIcons.put(KEY_UNKNOWN,"&#xf03d");

        }

        public HashMap<String, String> getNighttimeIcons(){
            return nighttimeIcons;
        }
    }

    public class DayTime{
        private HashMap<String, String> daytimeIcons = new HashMap<>();

        public DayTime(){
            daytimeIcons.put(KEY_CHANCE_OF_FLURRIES,"&#xf0b2");
            daytimeIcons.put(KEY_CHANCE_OF_RAIN,"&#xf009");
            daytimeIcons.put(KEY_CHANCE_OF_FREEZING_RAIN,"&#xf006");
            daytimeIcons.put(KEY_CHANCE_OF_SNOW,"&#xf00a");
            daytimeIcons.put(KEY_CHANCE_OF_THUNDER_STORM,"&#xf010");
            daytimeIcons.put(KEY_CLEAR,"&#xf00d");
            daytimeIcons.put(KEY_CLOUDY,"&#xf013");
            daytimeIcons.put(KEY_FLURRIES,"&#xf004");
            daytimeIcons.put(KEY_FOG,"&#xf003");
            daytimeIcons.put(KEY_HAZE,"&#xf0b6");
            daytimeIcons.put(KEY_MOSTLY_CLOUDY,"&#xf00c");
            daytimeIcons.put(KEY_MOSTLY_SUNNY,"&#xf00c");
            daytimeIcons.put(KEY_PARTLY_CLOUDY,"&#xf013");
            daytimeIcons.put(KEY_PARTLY_SUNNY,"&#xf002");
            daytimeIcons.put(KEY_RAIN,"&#xf008");
            daytimeIcons.put(KEY_FREEZING_RAIN,"&#xf0b2");
            daytimeIcons.put(KEY_SNOW,"&#xf076");
            daytimeIcons.put(KEY_SUNNY,"&#xf00d");
            daytimeIcons.put(KEY_THUNDER_STORMS,"&#xf010");
            daytimeIcons.put(KEY_UNKNOWN,"&#xf03d");
        }
        public HashMap<String, String> getDaytimeIcons(){
            return daytimeIcons;
        }

    }
}
