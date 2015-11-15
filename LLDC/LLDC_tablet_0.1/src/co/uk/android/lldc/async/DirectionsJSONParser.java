package co.uk.android.lldc.async;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import co.uk.android.lldc.tablet.LLDCApplication;

import com.google.android.gms.maps.model.LatLng;

public class DirectionsJSONParser {

	/**
	 * Receives a JSONObject and returns a list of lists containing latitude and
	 * longitude
	 */
	@SuppressWarnings("unchecked")
	public List<List<HashMap<String, String>>> parse(JSONObject jObject,
			int routeNo) {

		List<List<HashMap<String, String>>> routes = new ArrayList<List<HashMap<String, String>>>();
		JSONArray jRoutes = null;
		JSONArray jLegs = null;
		JSONArray jSteps = null;
		try {

			String temp = jObject.getString("status");

			if (!temp.equals("OK")) {
				throw new Exception("No Route");
			}

			jRoutes = jObject.getJSONArray("routes");

			if (jObject.has("route_id")) {
				LLDCApplication.navRouteId = jObject.getString("route_id");
			}

			if (routeNo > jRoutes.length() - 1) {
				routeNo = 0;
			}

			if (jRoutes.length() == 0) {
				throw new Exception("No Route");
			}

			LLDCApplication.NAVROUTEDURATION = 0f;
			LLDCApplication.NAVROUTEDISTANCE = 0f;
			/** Traversing all routes */
			// for (int i = 0; i < jRoutes.length(); i++) {
			jLegs = ((JSONObject) jRoutes.get(routeNo)).getJSONArray("legs");

			/** Traversing all legs */
			for (int j = 0; j < jLegs.length(); j++) {

				@SuppressWarnings("rawtypes")
				List path = new ArrayList<HashMap<String, String>>();

				jSteps = ((JSONObject) jLegs.get(j)).getJSONArray("steps");

				/** Traversing all steps */
				for (int k = 0; k < jSteps.length(); k++) {
					String polyline = "";
					polyline = (String) ((JSONObject) ((JSONObject) jSteps
							.get(k)).get("polyline")).get("points");
					
					LLDCApplication.ROUTEDURATION = ((JSONObject) jSteps
							.get(k)).getJSONObject("duration").getString(
							"text");
					LLDCApplication.onShowLogCat("Duration Before Add",
							LLDCApplication.TRIALROUTEDURATION + "");
					LLDCApplication.TRIALROUTEDURATION = LLDCApplication.TRIALROUTEDURATION
							+ ((JSONObject) jSteps.get(k)).getJSONObject(
									"duration").getDouble("value");
					LLDCApplication.onShowLogCat("Duration After Add",
							LLDCApplication.TRIALROUTEDURATION + "");
					LLDCApplication.onShowLogCat("Distance Before Add",
							LLDCApplication.TRIALROUTEDISTANCE + "");
					LLDCApplication.TRIALROUTEDISTANCE = LLDCApplication.TRIALROUTEDISTANCE
							+ ((JSONObject) jSteps.get(k)).getJSONObject(
									"distance").getDouble("value");
					LLDCApplication.onShowLogCat("Distance After Add",
							LLDCApplication.TRIALROUTEDISTANCE + "");

					LLDCApplication.onShowLogCat(
							"Navigation Duration Before Add",
							LLDCApplication.NAVROUTEDURATION + "");
					LLDCApplication.NAVROUTEDURATION = LLDCApplication.NAVROUTEDURATION
							+ ((JSONObject) jSteps.get(k)).getJSONObject(
									"duration").getDouble("value");
					LLDCApplication.onShowLogCat(
							"Navigation Duration After Add",
							LLDCApplication.NAVROUTEDURATION + "");
					LLDCApplication.onShowLogCat(
							"Navigation Distance Before Add",
							LLDCApplication.NAVROUTEDISTANCE + "");
					LLDCApplication.NAVROUTEDISTANCE = LLDCApplication.NAVROUTEDISTANCE
							+ ((JSONObject) jSteps.get(k)).getJSONObject(
									"distance").getDouble("value");
					LLDCApplication.onShowLogCat(
							"Navigation Distance After Add",
							LLDCApplication.NAVROUTEDISTANCE + "");
					
					if (!polyline.equals("")) {
						List<LatLng> list = decodePoly(polyline);

						/** Traversing all points */
						for (int l = 0; l < list.size(); l++) {
							HashMap<String, String> hm1 = new HashMap<String, String>();
							hm1.put("lat", Double.toString(((LatLng) list
									.get(l)).latitude));
							hm1.put("lng", Double.toString(((LatLng) list
									.get(l)).longitude));
							path.add(hm1);
						}
					} else {

						HashMap<String, String> hm = new HashMap<String, String>();

						String lat = (String) ((JSONObject) ((JSONObject) jSteps
								.get(k)).get("start_location")).get("lat");
						String lng = (String) ((JSONObject) ((JSONObject) jSteps
								.get(k)).get("start_location")).get("lng");

						hm.put("lat", lat);
						hm.put("lng", lng);
						path.add(hm);
						hm = new HashMap<String, String>();
						lat = (String) ((JSONObject) ((JSONObject) jSteps
								.get(k)).get("end_location")).get("lat");
						lng = (String) ((JSONObject) ((JSONObject) jSteps
								.get(k)).get("end_location")).get("lng");
						hm.put("lat", lat);
						hm.put("lng", lng);

						path.add(hm);
					}
				}
				routes.add(path);
			}

		} catch (JSONException e) {
			e.printStackTrace();
		} catch (Exception e) {
			return null;
		}

		return routes;
	}

	/**
	 * Method to decode polyline points Courtesy :
	 * jeffreysambells.com/2010/05/27
	 * /decoding-polylines-from-google-maps-direction-api-with-java
	 * */
	private List<LatLng> decodePoly(String encoded) {

		List<LatLng> poly = new ArrayList<LatLng>();
		int index = 0, len = encoded.length();
		int lat = 0, lng = 0;

		while (index < len) {
			int b, shift = 0, result = 0;
			do {
				b = encoded.charAt(index++) - 63;
				result |= (b & 0x1f) << shift;
				shift += 5;
			} while (b >= 0x20);
			int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
			lat += dlat;

			shift = 0;
			result = 0;
			do {
				b = encoded.charAt(index++) - 63;
				result |= (b & 0x1f) << shift;
				shift += 5;
			} while (b >= 0x20);
			int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
			lng += dlng;

			LatLng p = new LatLng((((double) lat / 1E5)),
					(((double) lng / 1E5)));
			poly.add(p);
		}

		return poly;
	}
}