package com.hoteltrip.android.adapters;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.maps.model.LatLng;
import com.hoteltrip.android.R;
import com.hoteltrip.android.SearchResultsActivity;
import com.hoteltrip.android.util.AppValues;
import com.hoteltrip.android.util.FavouriteTableActivity;
import com.hoteltrip.android.util.HotelData;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

public class HotelDataAdapter extends BaseAdapter implements Filterable {

	private Context mContext;
	LayoutInflater inflater;
	private ArrayList<HotelData> hotels;
	private int textViewResourceId;
	String m_szHotelID = "";
	JSONArray jRoomCategArray;
	ViewHolder viewHolder = null;
	JSONObject jRoomCategObject;
	String hotelPriceText;
	String img;
	String hotelTitleText, hotelAddressText, tripAdvisorRatingCountText,
			currency;
	String hotelImageURL;
	float hotelRatingStar;
	String ratingcount, reviewimageurl;
	// A copy of the original mObjects array, initialized from and then used
	// instead as soon as
	// the mFilter ArrayFilter is used. mObjects will then only contain the
	// filtered values.
	private ArrayList<HotelData> mOriginalValues;

	private ArrayList<HotelData> searchValues;
	private boolean bIsSearchOngoing;

	private HotelsFilter mFilter;

	int pos;
	String id;
	/**
	 * Lock used to modify the content of hotels. Any write operation performed
	 * on the array should be synchronized on this lock. This lock is also used
	 * by the filter (see {@link #getFilter()} to make a synchronized copy of
	 * the original array of data.
	 */
	private final Object mLock = new Object();

	/**
	 * Indicates whether or not {@link #notifyDataSetChanged()} must be called
	 * whenever {@link hotels} is modified.
	 */
	private boolean mNotifyOnChange = true;

	public static HashMap<String, Bitmap> mapImages = new HashMap<String, Bitmap>();
	public static HashMap<String, String> mapAddress = new HashMap<String, String>();
	public static HashMap<String, String> mapDistance = new HashMap<String, String>();
	public static HashMap<String, ArrayList<String>> mapHotelImagesList = new HashMap<String, ArrayList<String>>();
	public static HashMap<String, String> mapHotelDetailsResponse = new HashMap<String, String>();

	boolean listIsScrolling = false;

	public HotelDataAdapter(Context context, int textViewResourceId,
			ArrayList<HotelData> hotels) {
		super();
		this.mContext = context;
		this.hotels = hotels;
		this.textViewResourceId = textViewResourceId;
		inflater = LayoutInflater.from(context);
	}

	public void setListScrollingState(boolean bIsScrolling) {
		listIsScrolling = bIsScrolling;
	}

	@Override
	public int getCount() {
		return hotels.size();
	}

	@Override
	public HotelData getItem(int position) {
		return hotels.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public void setIsSearchOnGoing(boolean bValue) {
		bIsSearchOngoing = bValue;
	}

	/**
	 * Sorts the content of this adapter using the specified comparator.
	 * 
	 * @param comparator
	 *            The comparator used to sort the objects contained in this
	 *            adapter.
	 */
	public void sort(Comparator<HotelData> comparator) {
		synchronized (this.mLock) {
			synchronized (mLock) {

				if (bIsSearchOngoing) {
					Collections.sort(searchValues, comparator);
				} else {
					if (mOriginalValues != null) {
						Collections.sort(mOriginalValues, comparator);
					} else {
						Collections.sort(hotels, comparator);
					}
				}
			}
			if (mNotifyOnChange)
				notifyDataSetChanged();
		}
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		System.out.println("pos in id=" + position);
		// System.out.println("pos in id***=" + getItemId(position));
		System.out.println("pos in id Value********="
				+ mapAddress.get(hotels.get(position).getHotelId()));

		if (convertView == null) {
			convertView = inflater.inflate(this.textViewResourceId, parent,
					false);
			viewHolder = new ViewHolder(convertView);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		viewHolder.hotelNameTextView.setText("");
		viewHolder.streetNameTextView.setText("");
		viewHolder.starRating.setRating(5f);
		viewHolder.priceTextView.setText("");
		viewHolder.distanceTextView.setText("");
		viewHolder.hotelImageView.setImageDrawable(mContext.getResources()
				.getDrawable(R.drawable.placeholder));
		viewHolder.hotelFavoriteToggleButton.setChecked(true);
		viewHolder.tripAdvisorImageRating.setImageDrawable(mContext
				.getResources().getDrawable(R.drawable.trip_advisor));
		viewHolder.reviewsCountText.setText("");

		if (hotels != null) {
			viewHolder.hotelNameTextView.setText(hotels.get(position)
					.getHotelName());

			if (mapAddress.get(hotels.get(position).getHotelId()) != null) {
				viewHolder.streetNameTextView.setText(mapAddress.get(hotels
						.get(position).getHotelId()));
			}

			if (mapDistance.get(hotels.get(position).getHotelId()) != null) {
				viewHolder.distanceTextView.setText((mapDistance.get(hotels
						.get(position).getHotelId())) + " from here");
			}

			viewHolder.starRating.setRating(hotels.get(position).getRating());

			// JsonElement jsonElement =
			// getItem(position).getObject("RoomCateg");
			// if (jsonElement.isJsonObject())
			// viewHolder.priceTextView.setText(jsonElement.getAsJsonObject()
			// .getAsJsonObject("@attributes").get("Price")
			// .getAsString());

			checkFavouriteAndSetImage(hotels.get(position).getHotelId(),
					viewHolder.hotelFavoriteToggleButton);

			viewHolder.hotelFavoriteToggleButton
					.setOnClickListener(new togglebuttonClickListener(position,
							hotels.get(position).getHotelId()));

			String text = "from <font size=24 color=#f47c21><b>%s %s </b></font> /night";

			try {
				viewHolder.priceTextView.setText((Html.fromHtml(String.format(
						text, hotels.get(position).getCurrency(),
						hotels.get(position).getObject("RoomCateg")
								.getJSONObject("@attributes")
								.getString("Price")))));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				try {
					viewHolder.priceTextView.setText((Html.fromHtml(String
							.format(text,
									hotels.get(position).getCurrency(),
									hotels.get(position).getArray("RoomCateg")
											.getJSONObject(0)
											.getJSONObject("@attributes")
											.getString("Price")))));
				} catch (Exception e1) {
				}
			}
			try {
				text = "<b>%s</b> Reviews";
				viewHolder.reviewsCountText.setText(Html.fromHtml(String
						.format(text,
								hotels.get(position)
										.getObject("trip_advisor_data")
										.getJSONArray("hotel_location")
										.getJSONObject(0)
										.getJSONObject("@attributes")
										.getString("reviewcnt"))));
				ImageLoader.getInstance().displayImage(
						hotels.get(position).getObject("trip_advisor_data")
								.getJSONArray("hotel_location")
								.getJSONObject(0).getJSONObject("@attributes")
								.getString("img_url"),
						viewHolder.tripAdvisorImageRating);
			} catch (Exception e) {
				// e.printStackTrace();
				viewHolder.reviewsCountText.setText(Html.fromHtml(String
						.format(text, "0")));
				viewHolder.tripAdvisorImageRating.setImageBitmap(null);
			}

			// viewHolder.distanceTextView.setText(String
			// .valueOf(getItem(position).getDistance()));
			// viewHolder.hotelImageView.setImageResource(getItem(position)
			// .getHotelImage());
			// viewHolder.hotelFavoriteToggleButton.setChecked(getItem(position)
			// .getHotelIsFavorite());

			// Drawable fDraw = viewHolder.hotelImageView.getDrawable();
			// Drawable sDraw = mContext.getResources().getDrawable(
			// R.drawable.placeholder);
			// Bitmap bitmap = ((BitmapDrawable) fDraw).getBitmap();
			// Bitmap bitmap2 = ((BitmapDrawable) sDraw).getBitmap();

			// if (bitmap == bitmap2) {
			if (mapImages.containsKey(hotels.get(position).getHotelId())) {
				viewHolder.hotelImageView.setImageBitmap(mapImages.get(hotels
						.get(position).getHotelId()));
			} else {

				viewHolder.hotelImageView.setTag(hotels.get(position)
						.getHotelId());
				viewHolder.streetNameTextView.setTag(hotels.get(position)
						.getHotelId());
				viewHolder.distanceTextView.setTag(hotels.get(position)
						.getHotelId());

				GetHotelDetailTask task = new GetHotelDetailTask(hotels.get(
						position).getHotelId(), viewHolder.hotelImageView,
						viewHolder.streetNameTextView,
						viewHolder.distanceTextView);
				task.execute();
			}
			// }

		}

		return convertView;
	}

	class ViewHolder {

		TextView hotelNameTextView;
		TextView streetNameTextView;
		RatingBar starRating;
		TextView priceTextView;
		TextView distanceTextView;
		ImageView hotelImageView;
		ToggleButton hotelFavoriteToggleButton;
		ImageView tripAdvisorImageRating;
		TextView reviewsCountText;

		public ViewHolder(final View v) {
			hotelNameTextView = (TextView) v.findViewById(R.id.hotelNameText);
			streetNameTextView = (TextView) v.findViewById(R.id.streetNameText);
			starRating = (RatingBar) v.findViewById(R.id.starRating);
			priceTextView = (TextView) v.findViewById(R.id.priceText);
			distanceTextView = (TextView) v.findViewById(R.id.distanceText);
			hotelImageView = (ImageView) v.findViewById(R.id.iv_hotel);
			hotelFavoriteToggleButton = (ToggleButton) v
					.findViewById(R.id.togglebtn_favorite);
			tripAdvisorImageRating = (ImageView) v
					.findViewById(R.id.iv_tripadvisor);
			reviewsCountText = (TextView) v.findViewById(R.id.tv_reviews_count);
		}
	}

	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
		mNotifyOnChange = true;
	}

	@Override
	public Filter getFilter() {
		// TODO Auto-generated method stub
		if (this.mFilter == null) {
			this.mFilter = new HotelsFilter();
		}
		return this.mFilter;
	}

	class HotelsFilter extends Filter {

		@Override
		protected FilterResults performFiltering(CharSequence constraint) {
			final FilterResults results = new FilterResults();

			if (HotelDataAdapter.this.mOriginalValues == null) {
				synchronized (HotelDataAdapter.this.mLock) {
					HotelDataAdapter.this.mOriginalValues = new ArrayList<HotelData>(
							HotelDataAdapter.this.hotels);
				}
			}

			if (constraint == null || constraint.length() == 0) {
				ArrayList<HotelData> list;
				synchronized (HotelDataAdapter.this.mLock) {
					list = new ArrayList<HotelData>(
							HotelDataAdapter.this.mOriginalValues);
					results.values = list;
					results.count = list.size();

					searchValues = list;
				}
			} else {
				String prefixString = constraint.toString().toLowerCase();

				ArrayList<HotelData> values;
				synchronized (HotelDataAdapter.this.mLock) {
					values = new ArrayList<HotelData>(
							HotelDataAdapter.this.mOriginalValues);
				}

				final int count = values.size();
				final ArrayList<HotelData> newValues = new ArrayList<HotelData>(
						count);

				for (int i = 0; i < count; i++) {
					final HotelData value = values.get(i);

					// this is the string that we want to filter, a.k.a
					// hotel name
					final String valueText = value.getHotelName().toString()
							.toLowerCase();

					// First match against the whole, non-splitted value
					if (valueText.startsWith(prefixString)) {
						newValues.add(value);
					} else {
						final String[] words = valueText.split(" ");
						final int wordCount = words.length;

						// Start at index 0, in case valueText starts with
						// space(s)
						for (int k = 0; k < wordCount; k++) {
							if (words[k].startsWith(prefixString)) {
								newValues.add(value);
								break;
							}
						}
					}
				}
				results.values = newValues;
				results.count = newValues.size();

				searchValues = newValues;
			}

			return results;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void publishResults(CharSequence constraint,
				FilterResults results) {
			// noinspection unchecked
			AppValues.hotelDataList = HotelDataAdapter.this.hotels = (ArrayList<HotelData>) results.values;
			if (results.count > 0) {
				notifyDataSetChanged();
			} else {
				notifyDataSetInvalidated();
			}
			SearchResultsActivity.toggleEmptyView(results.count);
		}

	}

	class GetHotelDetailTask extends AsyncTask<Void, String, String> {

		// http://dev.pocketapp.co.uk/dev/hotel-trip/index.php/dataget/gethoteldetail/WSMA0809090398'

		private String szURL = AppValues.ms_szURL + "dataget/gethoteldetail/%s";

		String szHotelID = "";
		ImageView hotelImageView;
		TextView streetNameTextView;
		TextView distanceTextView;

		public GetHotelDetailTask(String szHotelID, ImageView hotelImageView,
				TextView streetNameTextView, TextView distanceTextView) {
			// TODO Auto-generated constructor stub
			this.szHotelID = szHotelID;
			this.hotelImageView = hotelImageView;
			this.streetNameTextView = streetNameTextView;
			this.distanceTextView = distanceTextView;
		}

		@Override
		protected String doInBackground(Void... params) {
			// TODO Auto-generated method stub
			szURL = String.format(szURL, szHotelID);
			Log.e("SearchHotelTask", "URL GET HOTEL DETAILS:: " + szURL);
			String response = null;
			try {
				HttpClient client = new DefaultHttpClient();
				HttpParams httpParams = new BasicHttpParams();
				HttpConnectionParams.setConnectionTimeout(httpParams, 15000); // timeout
				HttpGet getMethod = new HttpGet(szURL);
				HttpResponse httpResponse = client.execute(getMethod);
				response = inputStreamToString(
						httpResponse.getEntity().getContent()).toString();
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}

			return response;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (result != null) {
				try {

					mapHotelDetailsResponse.put(szHotelID, result);

					JSONObject jsonObject = new JSONObject(result);
					jsonObject = jsonObject
							.getJSONObject("GetHotelDetail_Response");
					String szAddress = "";
					if (jsonObject.has("Address1")
							&& jsonObject.getString("Address1") != null
							&& !jsonObject.getString("Address1").equals("{}"))
						szAddress = jsonObject.getString("Address1");

					if (jsonObject.has("Address2")
							&& jsonObject.getString("Address2") != null
							&& !jsonObject.getString("Address2").equals("{}"))
						szAddress = szAddress + ", "
								+ jsonObject.getString("Address2");

					if (jsonObject.has("Address3")
							&& jsonObject.getString("Address3") != null
							&& !jsonObject.getString("Address3").equals("{}"))
						szAddress = szAddress + ", "
								+ jsonObject.getString("Address3");

					if (!listIsScrolling && !mapAddress.containsKey(szHotelID)) {
						streetNameTextView.setText(szAddress);
						mapAddress.put(szHotelID, szAddress);
					}

					JSONArray hotelArray = null;
					try {
						hotelArray = jsonObject.getJSONArray("Images");
					} catch (Exception e) {

					}

					ArrayList<String> listImages = new ArrayList<String>();

					if (hotelArray != null && hotelArray.length() > 0) {
						JSONObject jsonObjectHotel;
						JSONObject jsonObjectHotelValues;
						for (int i = 1; i < hotelArray.length(); i++) {
							jsonObjectHotel = hotelArray.getJSONObject(i);
							jsonObjectHotelValues = jsonObjectHotel
									.getJSONObject("@attributes");
							listImages.add(jsonObjectHotelValues
									.getString("path"));
						}

						if (!mapHotelImagesList.containsKey(szHotelID)) {
							mapHotelImagesList.put(szHotelID, listImages);
						}

						jsonObjectHotel = hotelArray.getJSONObject(1);
						jsonObjectHotelValues = jsonObjectHotel
								.getJSONObject("@attributes");

						if (hotelImageView.getTag() != null
								&& hotelImageView.getTag().toString()
										.equals(szHotelID)) {
							ImageLoader.getInstance().displayImage(
									jsonObjectHotelValues.getString("path"),
									hotelImageView,
									new SimpleImageLoadingListener() {
										@Override
										public void onLoadingComplete(
												String imageUri, View view,
												Bitmap loadedImage) {
											if (loadedImage != null) {
												// ImageView imageView =
												// (ImageView)
												// view;
												// if (!listIsScrolling)
												// imageView
												// .setImageBitmap(loadedImage);

												mapImages.put(szHotelID,
														loadedImage);
											}
										}

										@Override
										public void onLoadingStarted(
												String imageUri, View view) {
										}

										@Override
										public void onLoadingFailed(
												String imageUri, View view,
												FailReason failReason) {
											mapImages.remove(szHotelID);
										}
									});
						}
					}

					if (jsonObject.has("Location")
							&& jsonObject.getString("Location") != null
							&& !jsonObject.getString("Location").equals("{}"))
						szAddress = szAddress + ", "
								+ jsonObject.getString("Location");

					// if (!mapDistance.containsKey(szHotelID)) {
					// distanceTextView.setTag(szHotelID);
					// getLatLngFromAddressTask task = new
					// getLatLngFromAddressTask(
					// distanceTextView);
					// task.szAddress = szAddress + ", "
					// + AppValues.ms_szSelectedDestination;
					// task.szHotelID = szHotelID;
					// task.execute();
					// }

				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
					// ((TextView) view.findViewById(R.id.streetNameText))
					// .setText("");
					// ((ImageView) view.findViewById(R.id.iv_hotel))
					// .setImageDrawable(mContext.getResources()
					// .getDrawable(R.drawable.placeholder));
				}
			}
		}
	}

	private StringBuilder inputStreamToString(InputStream content)
			throws IOException {
		// TODO Auto-generated method stub
		String line = "";
		StringBuilder total = new StringBuilder();

		BufferedReader rd = new BufferedReader(new InputStreamReader(content));
		while ((line = rd.readLine()) != null) {
			total.append(line);
		}
		// Log.i("SearchHotelTask", "inputStreamToString()-result  " +
		// total);

		return total;
	}

	class getLatLngFromAddressTask extends AsyncTask<Void, String, String> {
		String szAddress;
		String szHotelID;

		// http://maps.googleapis.com/maps/api/geocode/json?address=1600+Amphitheatre+Parkway,+Mountain+View,+CA&sensor=true_or_false
		String szURL = "http://maps.googleapis.com/maps/api/geocode/json?address=%s&sensor=false";

		TextView distanceTextView;

		public getLatLngFromAddressTask(TextView distanceTextView) {
			// TODO Auto-generated constructor stub
			this.distanceTextView = distanceTextView;
		}

		@Override
		protected String doInBackground(Void... params) {
			// TODO Auto-generated method stub
			szURL = String.format(szURL, Uri.encode(szAddress));
			Log.e("getLatLngFromAddressTask", " getLatLngFromAddressTask :: "
					+ szURL);

			String response = null;
			try {
				HttpClient client = new DefaultHttpClient();
				HttpParams httpParams = new BasicHttpParams();
				HttpConnectionParams.setConnectionTimeout(httpParams, 5000); // timeout
				// value
				HttpGet request = new HttpGet();
				request.setURI(new URI(szURL));
				request.setParams(httpParams);
				HttpResponse httpResponse = client.execute(request);
				response = inputStreamToString(
						httpResponse.getEntity().getContent()).toString();

				Log.e("getLatLngFromAddressTask()", " RESULTTT RESPONSE ::"
						+ response);

			} catch (Exception e) {
				e.printStackTrace();
			}

			return response;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (result != null) {
				try {
					JSONObject jObject = new JSONObject(result);
					JSONArray resultsArray = jObject.getJSONArray("results");
					if (resultsArray != null && resultsArray.length() > 0) {
						jObject = resultsArray.getJSONObject(0)
								.getJSONObject("geometry")
								.getJSONObject("location");

						LatLng location = new LatLng(jObject.getDouble("lat"),
								jObject.getDouble("lng"));

						distanceTextView.setTag(szHotelID);
						calculateDistanceTask task = new calculateDistanceTask(
								distanceTextView);
						task.source = AppValues.getLocation(mContext);
						task.destination = location;
						task.szHotelID = szHotelID;
						task.execute();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

	}

	class calculateDistanceTask extends AsyncTask<Void, String, String> {

		public Location source;
		public LatLng destination;
		String szHotelID;
		TextView distanceTextView;
		String hotelPriceText;
		String img;
		String hotelTitleText, hotelAddressText, tripAdvisorRatingCountText;
		String hotelImageURL;
		float hotelRatingStar;

		public calculateDistanceTask(TextView distanceTextView) {
			// TODO Auto-generated constructor stub
			this.distanceTextView = distanceTextView;
		}

		@Override
		protected String doInBackground(Void... params) {
			// StringBuilder response = new StringBuilder();
			String stringUrl = getDirectionsUrl(source, destination);
			Log.e("calculateDistanceTask", " DISTANCE URL:: " + stringUrl);

			String response = null;
			try {
				HttpClient client = new DefaultHttpClient();
				HttpParams httpParams = new BasicHttpParams();
				HttpConnectionParams.setConnectionTimeout(httpParams, 5000); // timeout
				// value
				HttpGet request = new HttpGet();
				request.setURI(new URI(stringUrl));
				request.setParams(httpParams);
				HttpResponse httpResponse = client.execute(request);
				response = inputStreamToString(
						httpResponse.getEntity().getContent()).toString();

				Log.e("calculateDistanceTask()", " RESULTTT RESPONSE ::"
						+ response);

			} catch (Exception e) {
				e.printStackTrace();
			}

			return response;

		}

		protected void onPostExecute(String result) {

			if (result != null) {
				JSONObject jObject;
				try {
					jObject = new JSONObject(result);
					JSONArray routesArray = jObject.getJSONArray("routes");
					if (routesArray != null && routesArray.length() > 0) {
						JSONArray legsArray = ((JSONObject) routesArray.get(0))
								.getJSONArray("legs");
						String distance = legsArray.getJSONObject(0)
								.getJSONObject("distance").getString("text");
						if (!listIsScrolling
								&& !mapDistance.containsKey(szHotelID)) {
							if (distanceTextView.getTag() != null
									&& distanceTextView.getTag().toString()
											.equals(szHotelID)) {
								distanceTextView.setText(distance
										+ " from here");
							}
							mapDistance.put(szHotelID, distance);
						}
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					// e.printStackTrace();
				}
			}

		}
	}

	private String getDirectionsUrl(Location origin, LatLng dest) {

		// Origin of route
		String str_origin = "origin=" + origin.getLatitude() + ","
				+ origin.getLongitude();

		// Destination of route
		String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

		// Sensor enabled
		String sensor = "sensor=false";

		// Building the parameters to the web service
		String parameters = str_origin + "&" + str_dest + "&" + sensor;

		// Output format
		String output = "json";

		// Building the url to the web service
		String url = "https://maps.googleapis.com/maps/api/directions/"
				+ output + "?" + parameters;

		return url;
	}

	int post;
	String hotelid;

	public class togglebuttonClickListener implements OnClickListener {
		// toggle button onclicklistenere code
		int position;
		String szHotelID;

		public togglebuttonClickListener(int position, String szHotelID) {

			this.position = position;
			this.szHotelID = szHotelID;
		}

		public void onClick(View v) {
			{
				post = position;
				hotelid = szHotelID;
				ArrayList<String> listImageURL = HotelDataAdapter.mapHotelImagesList
						.get(szHotelID);
				// getting all the values at specific position
				jRoomCategArray = hotels.get(position).getArray("RoomCateg");
				if (jRoomCategArray != null)
					try {
						jRoomCategObject = jRoomCategArray.getJSONObject(0);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				else
					jRoomCategObject = hotels.get(position).getObject(
							"RoomCateg");
				hotelTitleText = hotels.get(position).getHotelName();
				hotelAddressText = HotelDataAdapter.mapAddress.get(szHotelID);

				try {
					hotelImageURL = listImageURL.get(0);
				} catch (Exception e) {

				}
				hotelRatingStar = hotels.get(position).getRating();

				try {
					// text = "from <font size=22><b>%s</b></font> Reviews";
					// text = "from %s Reviews";
					tripAdvisorRatingCountText = hotels.get(position)
							.getObject("trip_advisor_data")
							.getJSONArray("hotel_location").getJSONObject(0)
							.getJSONObject("@attributes")
							.getString("reviewcnt");

				} catch (Exception e) {
					// e.printStackTrace();
					tripAdvisorRatingCountText = ("0");
				}
				try {
					currency = hotels.get(position).getCurrency();
					// hotels.get(position).getObject("RoomCateg")
					// .getJSONObject("@attributes")
					// .getString("Price"));
					hotelPriceText = jRoomCategObject.getJSONObject(
							"@attributes").getString("Price");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					reviewimageurl = hotels.get(position)
							.getObject("trip_advisor_data")
							.getJSONArray("hotel_location").getJSONObject(0)
							.getJSONObject("@attributes").getString("img_url");
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				Log.i("Position", "" + hotelTitleText + hotelAddressText
						+ hotelPriceText + tripAdvisorRatingCountText
						+ hotelImageURL + hotelRatingStar + reviewimageurl
						+ tripAdvisorRatingCountText);

				try {
					if (!checkFavourites(szHotelID)) {
						addFavourites();
					} else {
						removeFavourites();
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		}

	}

	private boolean checkFavourites(String hotelID) throws IOException {
		FavouriteTableActivity favoritesTable = new FavouriteTableActivity(
				mContext);
		try {
			// check if hotel exist in db
			favoritesTable.open();
			Log.d("",
					"is Hotel in Favourite "
							+ favoritesTable.isHotelFavorite(hotelID));
			if (favoritesTable.isHotelFavorite(hotelID))
				return true;
			else
				return false;

		} finally {
			if (favoritesTable != null)
				favoritesTable.close();
		}
	}

	void checkFavouriteAndSetImage(String hotelID,
			ToggleButton hotelFavoriteToggleButton) {
		try {
			// check if hotel exist in db and set the image accordingly on myfav
			// toggle button
			if (checkFavourites(hotelID)) {
				hotelFavoriteToggleButton.setChecked(false);
			} else {
				hotelFavoriteToggleButton.setChecked(true);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@SuppressWarnings("deprecation")
	private void addFavourites() throws IOException {
		FavouriteTableActivity favoritesTable = new FavouriteTableActivity(
				mContext);
		try {
			// adding specific hotel in db
			favoritesTable.open();
			favoritesTable.insertRows(hotelid, hotelTitleText,
					hotelAddressText, hotelImageURL, hotelPriceText,
					hotelRatingStar, reviewimageurl,
					tripAdvisorRatingCountText, currency);
		} finally {
			if (favoritesTable != null)
				favoritesTable.close();
		}
	}

	@SuppressWarnings("deprecation")
	private void removeFavourites() throws IOException {
		FavouriteTableActivity favoritesTable = new FavouriteTableActivity(
				mContext);
		try {
			// removing hotel from favs
			favoritesTable.open();
			favoritesTable.deleteRow(hotelid);
			Toast.makeText(mContext, " Hotel removed from Favourites.",
					Toast.LENGTH_LONG).show();

		} finally {
			if (favoritesTable != null)
				favoritesTable.close();
		}
	}
}
