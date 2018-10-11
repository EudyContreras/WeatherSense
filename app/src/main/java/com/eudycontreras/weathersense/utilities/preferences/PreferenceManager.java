package com.eudycontreras.weathersense.utilities.preferences;

import android.content.Context;
import android.content.SharedPreferences;


/**
 * @author  Eudy Contreras
 * Class which helps with the managing
 * of preferences.
 */
public class PreferenceManager {

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    private int PRIVATE_MODE = 0;

    public PreferenceManager(Context context, String name) {
        this.preferences = context.getSharedPreferences(name, PRIVATE_MODE);
        this.editor = preferences.edit();
    }

    public void setFirstTimeLaunch(boolean isFirstTime, String tag) {
        editor.putBoolean(tag, isFirstTime);
        editor.commit();
    }

    public void saveSettings(String tag, boolean settings){
        editor.putBoolean(tag, settings);
        editor.commit();
    }

    public void saveSettings(String tag, String settings){
        editor.putString(tag, settings);
        editor.commit();
    }

    public void saveSettings(String tag, float settings){
        editor.putFloat(tag, settings);
        editor.commit();
    }

    public void saveSettings(String tag, int settings){
        editor.putInt(tag, settings);
        editor.commit();
    }

    public void saveSettings(String tag, long settings){
        editor.putLong(tag, settings);
        editor.commit();
    }

    public String getSavedSettings(String tag, String defaultValue){
        return preferences.getString(tag,defaultValue);
    }

    public long getSavedSettings(String tag, long defaultValue){
        return preferences.getLong(tag,defaultValue);
    }

    public int getSavedSettings(String tag, int defaultValue){
        return preferences.getInt(tag,defaultValue);
    }

    public float getSavedSettings(String tag, float defaultValue){
        return preferences.getFloat(tag,defaultValue);
    }

    public boolean getSavedSettings(String tag, boolean defaultSettings){
        return preferences.getBoolean(tag,defaultSettings);
    }

    public boolean isFirstTimeLaunch(String tag) {
        return preferences.getBoolean(tag, true);
    }

    public void removePreference(String tag){
        editor.remove(tag);
        editor.commit();
    }

    public void clearPreferences(){
        editor.clear();
        editor.commit();
    }
}
