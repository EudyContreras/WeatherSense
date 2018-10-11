package com.eudycontreras.weathersense.services;

import android.support.annotation.NonNull;
import android.util.Log;

import com.eudycontreras.weathersense.utilities.multiThreading.ThreadManagers.ThreadManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by Eudy on 2/1/2017.
 */

public class RequestValidator {

    private static final String RESULTS = "RESULTS";

    public static void VALIDATE_REQUEST(String request, @NonNull ValidationListener listener){

        ThreadManager.performTask(() -> {
            String input="";

            try {
                input = "query=" + URLEncoder.encode(request, "utf-8");
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            }
            String url = "http://autocomplete.wunderground.com/aq?"+input;

            try {
                String response = downloadResponse(url);

                if(response==null || response.length()==0)
                    return;

                Log.d("Results for: "+request,response);

                JSONObject object = new JSONObject(response);
                JSONArray array = object.optJSONArray(RESULTS);

                if(array!=null){
                    if(array.length()<=0){
                        listener.isValidRequest(false);
                    }else{
                        listener.isValidRequest(true);
                    }
                }else{
                    listener.isValidRequest(false);
                }

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        });
    }

    private static String downloadResponse(String strUrl) throws IOException {

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

            while(( line = bufferedReader.readLine())  != null){
                stringBuffer.append(line);
            }

            data = stringBuffer.toString();

            bufferedReader.close();

        }catch(Exception e){

            Log.d("Exception", e.toString());

        }finally{
            if(inputStream!=null)
            inputStream.close();
            if(connection!=null)
            connection.disconnect();
        }
        return data;
    }

    public interface ValidationListener{
        void isValidRequest(boolean state);
    }
}
