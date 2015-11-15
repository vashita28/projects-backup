/*
 * Coded By Manpreet
 */

package com.example.cabapppassenger.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PlaceJSONParser {

	/** Receives a JSONObject and returns a list */
	public List<HashMap<String, String>> parse(JSONObject jObject) {

		JSONArray jPlaces = null;
		try {
			/** Retrieves all the elements in the 'places' array */
			jPlaces = jObject.getJSONArray("predictions");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		/**
		 * Invoking getPlaces with the array of json object where each json
		 * object represent a place
		 */
		return getPlaces(jPlaces);
	}

	private List<HashMap<String, String>> getPlaces(JSONArray jPlaces) {
		int placesCount = jPlaces.length();
		List<HashMap<String, String>> placesList = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> place = null;

		/** Taking each place, parses and adds to list object */
		for (int i = 0; i < placesCount; i++) {
			try {
				/** Call getPlace with place JSON object to parse the place */
				place = getPlace((JSONObject) jPlaces.get(i));
				placesList.add(place);

			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		return placesList;
	}

	/** Parsing the Place JSON object */
	private HashMap<String, String> getPlace(JSONObject jPlace) {

		HashMap<String, String> place = new HashMap<String, String>();

		String id = "";
		String reference = "";
		String description = "";
		String placeId = "";
		String withoutCommaDescription = "";
		try {

			description = jPlace.getString("description");
			id = jPlace.getString("id");
			reference = jPlace.getString("reference");
			placeId = jPlace.getString("place_id");
			withoutCommaDescription = description;
			while (description.lastIndexOf(",") != -1) {
				withoutCommaDescription = description.substring(0,
						description.lastIndexOf(","));
				if (withoutCommaDescription.indexOf(",") == -1) {
					withoutCommaDescription = description;
				}
				description = description.substring(0,
						description.lastIndexOf(","));
			}

			place.put("description", withoutCommaDescription);
			place.put("_id", id);
			place.put("placeId", placeId);
			place.put("reference", reference);

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return place;
	}
}
