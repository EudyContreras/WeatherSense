package com.eudycontreras.weathersense.utilities.miscellaneous;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

/**
 * Created by Eudy on 10/13/2016.
 */

public class JsonUtility {

    public static String toJason(String[] keys, String[] values) {
        JSONObject jsonObject = new JSONObject();

        for (int i = 0; i < keys.length; i++) {
            try {
                jsonObject.put(keys[i],values[i]);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return jsonObject.toString();
    }

    public static String toJasonArray(String id, String[] jsons) {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();

        try {
            for (int i = 0; i < jsons.length; i++) {
                JSONObject jsonItem = new JSONObject(jsons[i]);
                jsonArray.put(jsonItem);
            }
            jsonObject.put(id, jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject.toString();
    }

    public static HashMap<String,String> fromJason(String json, String[] keys) {

        JSONObject jsonObject;
        HashMap<String,String> data = new HashMap<>();
        try {
            jsonObject = new JSONObject(json);

            for (int i = 0; i < keys.length; i++) {
                data.put(keys[i], jsonObject.getString(keys[i]));
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return data;
    }
    public static HashMap<String,String> fromJason(JSONObject jsonObject, String[] keys) {

        HashMap<String,String> data = new HashMap<>();
        try {

            for(int i = 0; i<keys.length; i++) {
                data.put(keys[i],jsonObject.getString(keys[i]));
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return data;
    }
    public static LinkedList<JSONObject> fromJasonArray(String id, String json) {
        LinkedList<JSONObject> data = new LinkedList<>();
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.getJSONArray(id);

            for (int i = 0; i < jsonArray.length(); i++) {
                data.add(jsonArray.getJSONObject(i));
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return data;
    }

    public static boolean isValidJson(String test) {
        try {
            new JSONObject(test);
        } catch (JSONException ex) {
            try {
                new JSONArray(test);
            } catch (JSONException ex1) {
                return false;
            }
        }
        return true;
    }
    public String parseToJson(HashMap<String, String> mappings) {

        String start = "{";
        String end = "}";
        String valueKeySeparator = ":";
        String mappingSeparator = ",";
        String quotations = "\"";
        String tempJsonString = "";
        String jsonString;

        for (int i = 0; i < mappings.size(); i++) {

        }
        Iterator<Map.Entry<String, String>> iterator = mappings.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<String, String> entry = iterator.next();
            tempJsonString += quotations + entry.getKey() + quotations + valueKeySeparator + quotations + entry.getValue()
                    + quotations + mappingSeparator;
        }

        jsonString = start + tempJsonString.subSequence(0, tempJsonString.length() - 1) + end;

        return jsonString;
    }

    public String parseToJsonArray(LinkedList<String> jsonObjects, String arrayID){
        String quotations = "\"";
        String arrayStart = ":[";
        String arrayEnd = "]";
        String arraySeparator = ",";
        String jsonArray;
        String tempJsonArray = quotations+arrayID+quotations+arrayStart;

        for(int i = 0; i<jsonObjects.size(); i++){
            tempJsonArray += jsonObjects.get(i)+arraySeparator;
        }

        jsonArray = tempJsonArray.subSequence(0, tempJsonArray.length()-1)+arrayEnd;

        return jsonArray;
    }

    public HashMap<String, String> parseFromJson(String message) {

        HashMap<String, String> mappings = new HashMap<>();

        String original = message;

        String[] pairs;

        String[] keys;

        String[] values;

        String bracketsRemoved = original.subSequence(1, original.length() - 1).toString();

        pairs = bracketsRemoved.split(",");

        if (pairs.length > 0) {

            keys = new String[pairs.length];
            values = new String[pairs.length];

            for (int i = 0; i < pairs.length; i++) {
                keys[i] = pairs[i].split(":")[0].subSequence(1, pairs[i].split(":")[0].length() - 1).toString();
                values[i] = pairs[i].split(":")[1].subSequence(1, pairs[i].split(":")[1].length() - 1).toString();
            }

        } else {
            keys = new String[1];
            values = new String[1];

            keys[0] = bracketsRemoved.split(":")[0].subSequence(1, bracketsRemoved.split(":")[0].length() - 1)
                    .toString();
            values[0] = bracketsRemoved.split(":")[1].subSequence(1, bracketsRemoved.split(":")[1].length() - 1)
                    .toString();
        }

        for (int i = 0; i < keys.length; i++) {
            mappings.put(keys[i], values[i]);
        }

        return mappings;
    }

    public static String[] jsonKeys(String... keys){
        return keys;
    }

    public static String[] jsonValues(String... values){
        return values;
    }
}

