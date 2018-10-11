package com.eudycontreras.weathersense.services;

import android.os.AsyncTask;
import android.util.Log;

import com.eudycontreras.weathersense.adapters.AdapterSuggestions;
import com.eudycontreras.weathersense.customView.CustomAutoCompleteTextView;
import com.eudycontreras.weathersense.parsers.PlaceJSONParser;
import com.eudycontreras.weathersense.settings.SettingsActivity;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;

/**
 * @author  Eudy Contreras
 * Service class in charge of making requests to
 * the google places autoComplete API and returning a handle response
 */

public class AutoCompleteService {

    private static final String TYPES = "types=(cities)";
    private static final String SENSOR = "sensor=false";
    private static final String FORMAT = "json?";

    private PlacesTask placesTask;
    private ParserTask parserTask;
    private SettingsActivity activity;
    private CustomAutoCompleteTextView textView;

    public AutoCompleteService(SettingsActivity activity, CustomAutoCompleteTextView textView){
        this.activity = activity;
        this.textView = textView;
    }

    public void createTask(String letter){
        placesTask = new PlacesTask();
        placesTask.execute(letter);
    }

    private String downloadResponse(String strUrl) throws IOException {

        String data = "";
        InputStream inputStream = null;
        HttpURLConnection connection = null;

        try{
            URL url = new URL(strUrl);

            connection = (HttpURLConnection) url.openConnection();

            connection.connect();

            inputStream = connection.getInputStream();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            StringBuffer stringBuffer  = new StringBuffer();

            String line;

            while( ( line = bufferedReader.readLine())  != null){
                stringBuffer.append(line);
            }

            data = stringBuffer.toString();

            bufferedReader.close();

        }catch(Exception e){

            Log.d("Exception", e.toString());

        }finally{
            inputStream.close();
            connection.disconnect();
        }
        return data;
    }

    private class PlacesTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... place) {
            String data = "";

            String input="";

            try {
                input = "query=" + URLEncoder.encode(place[0], "utf-8");
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            }

            String url = "http://autocomplete.wunderground.com/aq?"+input;

            try{
                data = downloadResponse(url);

            }catch(Exception e){
                Log.d("CITY AUTOCOMPLETE",e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            parserTask = new ParserTask();

            parserTask.execute(result);
        }
    }

    private class ParserTask extends AsyncTask<String, Integer, List<HashMap<String,String>>>{

        JSONObject jObject;

        @Override
        protected List<HashMap<String, String>> doInBackground(String... jsonData) {

            List<HashMap<String, String>> places = null;

            PlaceJSONParser placeJsonParser = new PlaceJSONParser();

            try{
                jObject = new JSONObject(jsonData[0]);

                places = placeJsonParser.parse(jObject);

            }catch(Exception e){
                Log.d("Google Place Service",e.toString());
            }
            return places;
        }

        @Override
        protected void onPostExecute(List<HashMap<String, String>> result) {
            if(result==null)
                return;

            if(result.size()>4)
            result.subList(3, result.size()).clear();

            String[] from = new String[] { "name"};

            int[] to = new int[] { android.R.id.text1 };

            AdapterSuggestions adapter = new AdapterSuggestions(activity, result, android.R.layout.simple_list_item_1, from, to);

            textView.setAdapter(adapter);
        }
    }
}