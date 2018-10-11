package com.eudycontreras.weathersense.parsers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author  Eudy Contreras
 * Parser used for parsing JSON data into
 * a hash-maps which can be used within a
 * autoCompleteTextView.
 */
public class PlaceJSONParser {
	private static final String NAME = "name";
	private static final String COUNTRY = "c";
	private static final String LATITUDE = "lat";
	private static final String LONGITUDE = "lon";
	private static final String PREDICTIONS ="RESULTS";

	public List<HashMap<String,String>> parse(JSONObject jObject){		
		
		JSONArray places = null;
		try {

			places = jObject.getJSONArray(PREDICTIONS);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return getPlaces(places);
	}
	
	
	private List<HashMap<String, String>> getPlaces(JSONArray places){

		int placesCount = places.length();

		List<HashMap<String, String>> placesList = new ArrayList<>();

		HashMap<String, String> place;

		for(int i=0; i<placesCount;i++){
			try {
				place = getPlace((JSONObject)places.get(i));
				placesList.add(place);

			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
		return placesList;
	}

	private HashMap<String, String> getPlace(JSONObject places){

		HashMap<String, String> place = new HashMap<>();
		
		String longitude;
		String latitude;
		String description;
		
		try {
			
			description = places.getString(NAME);
			
			place.put(NAME,description);

			
		} catch (JSONException e) {			
			e.printStackTrace();
		}		
		return place;
	}
}
