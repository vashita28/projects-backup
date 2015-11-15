package com.hoteltrip.android;

import java.util.Comparator;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.actionbarsherlock.app.ActionBar;
import com.hoteltrip.android.adapters.HotelDataAdapter;
import com.hoteltrip.android.domain.Hotel;
import com.hoteltrip.android.util.AppValues;
import com.hoteltrip.android.util.HotelData;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.PauseOnScrollListener;

public class SearchResultsActivity extends BaseActivity {

	private ListView resultsList;
	private int checkedItemId = 0;
	private EditText searchResultsTextBox;
	private HotelDataAdapter adapter;
	private List<Hotel> hotels;
	HotelDataAdapter dataadapter;
	static TextView emptyText;
	ToggleButton togglebtn_favorite;
	View sortLayout, filterLayout, optionsLayout;
	static Context mContext;

	public static Handler favoriteHandler;

	int SORT = 1;
	int FILTER = 2;
	int OPTIONS = 3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_results);
		super.init("Search Results");

		mContext = this;
		togglebtn_favorite = (ToggleButton) findViewById(R.id.togglebtn_favorite);
		resultsList = (ListView) findViewById(R.id.resultsList);

		sortLayout = (View) findViewById(R.id.view_sort);
		filterLayout = (View) findViewById(R.id.view_filter);
		optionsLayout = (View) findViewById(R.id.view_options);
		ActionBar actionBar = getSupportActionBar();
		actionBar.hide();
		LinearLayout customABView = (LinearLayout) getLayoutInflater().inflate(
				R.layout.results_custom_actionbar_view, null);
		customABView.setGravity(Gravity.RIGHT);
		actionBar.setCustomView(customABView, new ActionBar.LayoutParams(
				Gravity.RIGHT));
		actionBar.setDisplayShowCustomEnabled(true);
		Button mapBtn = (Button) findViewById(R.id.mapBtn);
		mapBtn.setOnClickListener(onMapClick);

		emptyText = (TextView) findViewById(R.id.emptyText);
		emptyText.setVisibility(View.GONE);
		searchResultsTextBox = (EditText) findViewById(R.id.searchResultsTextBox);
		searchResultsTextBox.addTextChangedListener(textWatcher);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

		searchResultsTextBox.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_ENTER) {
					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(
							searchResultsTextBox.getWindowToken(), 0);
					return true;
				}
				return false;
			}
		});

		filterLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent myfav = new Intent(SearchResultsActivity.this,
						FilterListingActivity.class);
				startActivity(myfav);
			}
		});

		sortLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent sortIntent = new Intent(SearchResultsActivity.this,
						SortingActivity.class);
				startActivityForResult(sortIntent, SORT);
			}
		});

		optionsLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent myfav = new Intent(SearchResultsActivity.this,
						NewOptionsActivity.class);
				startActivity(myfav);
			}
		});

		// mock data
		// Hotel hotel1 = new Hotel(
		// "Chinatown Inn",
		// "136 between Soi 16 to 14/1 , Lat-krabang Rd,. lat krabang Bangkok 10520, Thailand",
		// "address1", 10.00, 70, 3.13139, 101.63424, 3.5f, 50,
		// R.drawable.images1, true);
		// Hotel hotel2 = new Hotel("PJ Hilton",
		// "Lat Krabang Rd, Lat Krabang, Bangkok 10520, Thailand",
		// "address1", 80.80, 70, 3.12809, 101.63381, 2.5f, 40,
		// R.drawable.images2, true);
		// Hotel hotel3 = new Hotel(
		// "Grand Continental Hotel",
		// "136 between Soi 16 to 14/1 , Lat-krabang Rd,. lat krabang Bangkok 10520, Thailand",
		// "address1", 20.00, 70, 3.12522, 101.64435, 1.0f, 80,
		// R.drawable.images3, false);
		// Hotel hotel4 = new Hotel("Capitol Inn",
		// "Lat Krabang Rd, Lat Krabang, Bangkok 10520, Thailand",
		// "address1", 99.10, 70, 3.11786, 101.62972, 0.5f, 10,
		// R.drawable.images1, true);
		// Hotel hotel5 = new Hotel(
		// "Milennium Hotel",
		// "136 between Soi 16 to 14/1 , Lat-krabang Rd,. lat krabang Bangkok 10520, Thailand",
		// "address1", 99.99, 70, 3.1091, 101.6432, 4.5f, 14,
		// R.drawable.images2, false);
		// Hotel hotel6 = new Hotel("Bugger Hotel",
		// "Lat Krabang Rd, Lat Krabang, Bangkok 10520, Thailand",
		// "address1", 70.00, 70, 3.11674, 101.63634, 4.5f, 25,
		// R.drawable.images2, false);
		// Hotel hotel7 = new Hotel(
		// "Cendol Hotel",
		// "136 between Soi 16 to 14/1 , Lat-krabang Rd,. lat krabang Bangkok 10520, Thailand",
		// "address1", 99.90, 70, 3.11253, 101.65126, 4.5f, 44,
		// R.drawable.images3, false);
		// Hotel hotel8 = new Hotel("Empire Hotel",
		// "Lat Krabang Rd, Lat Krabang,  Bangkok 10520, Thailand",
		// "address1", 50.00, 70, 3.10768, 101.65192, 4.5f, 60,
		// R.drawable.images1, false);
		// Hotel hotel9 = new Hotel(
		// "Bugis Hotel",
		// "136 between Soi 16 to 14/1 , Lat-krabang Rd,. lat krabang Bangkok 10520, Thailand",
		// "address1", 100.00, 70, 3.1019, 101.6439, 4.5f, 9,
		// R.drawable.images2, true);
		// Hotel hotel10 = new Hotel("City Inn",
		// "Lat Krabang Rd, Lat Krabang, Bangkok 10520, Thailand",
		// "address1", 200.00, 70, 3.1031, 101.6737, 4.5f, 90,
		// R.drawable.images3, true);
		//
		// hotels = new ArrayList<Hotel>();
		// hotels.add(hotel1);
		// hotels.add(hotel2);
		// hotels.add(hotel3);
		// hotels.add(hotel4);
		// hotels.add(hotel5);
		// hotels.add(hotel6);
		// hotels.add(hotel7);
		// hotels.add(hotel8);
		// hotels.add(hotel9);
		// hotels.add(hotel10);

		adapter = new HotelDataAdapter(getApplicationContext(),
				R.layout.searchresultslist_row_new, AppValues.hotelDataList);

		resultsList.setAdapter(adapter);
		sortList(AppValues.nSortType);

		// TODO Auto-generated method stub

		if (adapter == null || adapter.getCount() == 0) {
			emptyText.setVisibility(View.VISIBLE);
		} else {
			emptyText.setVisibility(View.GONE);
		}

		resultsList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapter, View View, int pos,
					long id) {
				// TODO Auto-generated method stub
				Log.e("********************", "******CLICKED*************IDD  "
						+ id + " POSss " + pos);
				Intent hoteldetailsIntent = new Intent(
						SearchResultsActivity.this, HotelDetailsActivity.class);
				hoteldetailsIntent.putExtra("id", id);
				hoteldetailsIntent.putExtra("pos", pos);
				startActivity(hoteldetailsIntent);
			}
		});

		resultsList.setOnScrollListener(new PauseOnScrollListener(ImageLoader
				.getInstance(), true, true));

		resultsList.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				switch (scrollState) {
				case OnScrollListener.SCROLL_STATE_IDLE:
					// when list scrolling stops
					adapter.setListScrollingState(false);
					break;
				case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
					adapter.setListScrollingState(true);
					break;
				case OnScrollListener.SCROLL_STATE_FLING:
					adapter.setListScrollingState(false);
					break;
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub

			}
		});

		favoriteHandler = new Handler() {
			@Override
			public void handleMessage(android.os.Message msg) {
				if (adapter != null)
					adapter.notifyDataSetChanged();
			}
		};
	}

	// class HotelsAdapter extends BaseAdapter implements Filterable {
	//
	// private Context context;
	// private List<Hotel> hotels;
	// private int textViewResourceId;
	//
	// // A copy of the original mObjects array, initialized from and then used
	// // instead as soon as
	// // the mFilter ArrayFilter is used. mObjects will then only contain the
	// // filtered values.
	// private ArrayList<Hotel> mOriginalValues;
	//
	// private HotelsFilter mFilter;
	//
	// /**
	// * Lock used to modify the content of hotels. Any write operation
	// * performed on the array should be synchronized on this lock. This lock
	// * is also used by the filter (see {@link #getFilter()} to make a
	// * synchronized copy of the original array of data.
	// */
	// private final Object mLock = new Object();
	//
	// /**
	// * Indicates whether or not {@link #notifyDataSetChanged()} must be
	// * called whenever {@link hotels} is modified.
	// */
	// private boolean mNotifyOnChange = true;
	//
	// public HotelsAdapter(Context context, int textViewResourceId,
	// List<Hotel> hotels) {
	// super();
	// this.context = context;
	// this.hotels = hotels;
	// this.textViewResourceId = textViewResourceId;
	// }
	//
	// @Override
	// public int getCount() {
	// return hotels.size();
	// }
	//
	// @Override
	// public Hotel getItem(int position) {
	// return hotels.get(position);
	// }
	//
	// @Override
	// public long getItemId(int position) {
	// return position;
	// }
	//
	// /**
	// * Sorts the content of this adapter using the specified comparator.
	// *
	// * @param comparator
	// * The comparator used to sort the objects contained in this
	// * adapter.
	// */
	// public void sort(Comparator<Hotel> comparator) {
	// synchronized (this.mLock) {
	// synchronized (mLock) {
	// if (mOriginalValues != null) {
	// Collections.sort(mOriginalValues, comparator);
	// } else {
	// Collections.sort(hotels, comparator);
	// }
	// }
	// if (mNotifyOnChange)
	// notifyDataSetChanged();
	// }
	// }
	//
	// @Override
	// public View getView(int position, View convertView, ViewGroup parent) {
	//
	// View v = convertView;
	// ViewHolder viewHolder = null;
	//
	// if (viewHolder == null) {
	// final LayoutInflater inflater = (LayoutInflater) this.context
	// .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	// v = inflater.inflate(this.textViewResourceId, null);
	// viewHolder = new ViewHolder(v);
	// v.setTag(viewHolder);
	// }
	//
	// viewHolder = (ViewHolder) v.getTag();
	//
	// if (hotels != null) {
	// viewHolder.hotelNameTextView.setText(getItem(position)
	// .getName());
	// viewHolder.streetNameTextView.setText(getItem(position)
	// .getStreetName());
	// viewHolder.starRating.setRating(getItem(position)
	// .getStarRating());
	// viewHolder.priceTextView.setText(getItem(position)
	// .getPricePerNight().toString());
	// viewHolder.distanceTextView.setText(String.valueOf(getItem(
	// position).getDistance()));
	// viewHolder.hotelImageView.setImageResource(getItem(position)
	// .getHotelImage());
	// viewHolder.hotelFavoriteToggleButton.setChecked(getItem(
	// position).getHotelIsFavorite());
	// }
	//
	// return v;
	// }
	//
	// class ViewHolder {
	//
	// TextView hotelNameTextView;
	// TextView streetNameTextView;
	// RatingBar starRating;
	// TextView priceTextView;
	// TextView distanceTextView;
	// ImageView hotelImageView;
	// ToggleButton hotelFavoriteToggleButton;
	//
	// public ViewHolder(final View v) {
	// hotelNameTextView = (TextView) v
	// .findViewById(R.id.hotelNameText);
	// streetNameTextView = (TextView) v
	// .findViewById(R.id.streetNameText);
	// starRating = (RatingBar) v.findViewById(R.id.starRating);
	// priceTextView = (TextView) v.findViewById(R.id.priceText_old);
	// distanceTextView = (TextView) v.findViewById(R.id.distanceText);
	// hotelImageView = (ImageView) v.findViewById(R.id.iv_hotel);
	// hotelFavoriteToggleButton = (ToggleButton) v
	// .findViewById(R.id.togglebtn_favorite);
	// }
	// }
	//
	// @Override
	// public void notifyDataSetChanged() {
	// super.notifyDataSetChanged();
	// mNotifyOnChange = true;
	// }
	//
	// @Override
	// public Filter getFilter() {
	// if (this.mFilter == null) {
	// this.mFilter = new HotelsFilter();
	// }
	// return this.mFilter;
	// }
	//
	// class HotelsFilter extends Filter {
	//
	// @Override
	// protected FilterResults performFiltering(CharSequence constraint) {
	// final FilterResults results = new FilterResults();
	//
	// if (HotelsAdapter.this.mOriginalValues == null) {
	// synchronized (HotelsAdapter.this.mLock) {
	// HotelsAdapter.this.mOriginalValues = new ArrayList<Hotel>(
	// HotelsAdapter.this.hotels);
	// }
	// }
	//
	// if (constraint == null || constraint.length() == 0) {
	// ArrayList<Hotel> list;
	// synchronized (HotelsAdapter.this.mLock) {
	// list = new ArrayList<Hotel>(
	// HotelsAdapter.this.mOriginalValues);
	// results.values = list;
	// results.count = list.size();
	// }
	// } else {
	// String prefixString = constraint.toString().toLowerCase();
	//
	// ArrayList<Hotel> values;
	// synchronized (HotelsAdapter.this.mLock) {
	// values = new ArrayList<Hotel>(
	// HotelsAdapter.this.mOriginalValues);
	// }
	//
	// final int count = values.size();
	// final ArrayList<Hotel> newValues = new ArrayList<Hotel>(
	// count);
	//
	// for (int i = 0; i < count; i++) {
	// final Hotel value = values.get(i);
	//
	// // this is the string that we want to filter, a.k.a
	// // hotel name
	// final String valueText = value.getName().toString()
	// .toLowerCase();
	//
	// // First match against the whole, non-splitted value
	// if (valueText.startsWith(prefixString)) {
	// newValues.add(value);
	// } else {
	// final String[] words = valueText.split(" ");
	// final int wordCount = words.length;
	//
	// // Start at index 0, in case valueText starts with
	// // space(s)
	// for (int k = 0; k < wordCount; k++) {
	// if (words[k].startsWith(prefixString)) {
	// newValues.add(value);
	// break;
	// }
	// }
	// }
	// }
	// results.values = newValues;
	// results.count = newValues.size();
	// }
	//
	// return results;
	// }
	//
	// @SuppressWarnings("unchecked")
	// @Override
	// protected void publishResults(CharSequence constraint,
	// FilterResults results) {
	// // noinspection unchecked
	// HotelsAdapter.this.hotels = (List<Hotel>) results.values;
	// if (results.count > 0) {
	// notifyDataSetChanged();
	// } else {
	// notifyDataSetInvalidated();
	// }
	// }
	//
	// }
	//
	// }

	private TextWatcher textWatcher = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {

		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {

		}

		@Override
		public void afterTextChanged(Editable s) {

			if (s == null || s.equals(""))
				adapter.setIsSearchOnGoing(false);
			else
				adapter.setIsSearchOnGoing(true);

			if (adapter != null && adapter.getFilter() != null) {
				adapter.getFilter().filter(s);
			}
		}
	};

	OnClickListener onMapClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent mapIntent = new Intent(getApplicationContext(),
					SearchResultsMapActivity.class);
			startActivity(mapIntent);
		}
	};

	OnItemClickListener onResultClick = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View v, int position,
				long id) {

		}
	};

	// @Override
	// public boolean onCreateOptionsMenu(Menu menu) {
	// getSupportMenuInflater().inflate(R.menu.activity_search_results, menu);
	// return super.onCreateOptionsMenu(menu);
	// }
	//
	// @Override
	// public boolean onOptionsItemSelected(MenuItem item) {
	//
	// Intent intent = null;
	//
	// switch (item.getItemId()) {
	// case R.id.menu_sort:
	// registerForContextMenu(findViewById(R.id.menu_sort));
	// openContextMenu(findViewById(R.id.menu_sort));
	// break;
	// case R.id.menu_filter:
	// intent = new Intent(getApplicationContext(),
	// FilterListingActivity.class);
	// startActivity(intent);
	// break;
	// case R.id.menu_option:
	// intent = new Intent(getApplicationContext(), OptionsActivity.class);
	// startActivity(intent);
	// break;
	//
	// }
	// return true;
	// }
	//
	// @Override
	// public void onCreateContextMenu(ContextMenu menu, View v,
	// ContextMenuInfo menuInfo) {
	// super.onCreateContextMenu(menu, v, menuInfo);
	// // opening context menus uses the default menuinflater to inflate menus,
	// // nothing to do with compat libs
	// MenuInflater inflater = getMenuInflater();
	// inflater.inflate(R.menu.sort_context_menu, menu);
	// menu.setHeaderTitle("Sort by");
	//
	// for (int i = 0; i < menu.size(); i++) {
	//
	// }
	//
	// }
	//
	// @Override
	// public boolean onContextItemSelected(android.view.MenuItem item) {
	//
	// switch (item.getItemId()) {
	// case R.id.sort_by_distance:
	//
	// if (item.isChecked()) {
	// adapter.sort(new Comparator<HotelData>() {
	//
	// @Override
	// public int compare(HotelData lhs, HotelData rhs) {
	//
	// return 0;
	// }
	// });
	// }
	//
	// case R.id.sort_by_popularity:
	//
	// if (item.isChecked())
	// item.setChecked(false);
	// else
	// item.setChecked(true);
	//
	// return true;
	// case R.id.sort_by_hotelname:
	//
	// adapter.sort(new Comparator<HotelData>() {
	//
	// @Override
	// public int compare(HotelData lhs, HotelData rhs) {
	// return String.valueOf(lhs.getHotelName()).compareTo(
	// String.valueOf(rhs.getHotelName()));
	// }
	// });
	//
	// if (item.isChecked())
	// item.setChecked(false);
	// else
	// item.setChecked(true);
	// return true;
	// case R.id.sort_by_reviewscore:
	// if (item.isChecked())
	// item.setChecked(false);
	// else
	// item.setChecked(true);
	// return true;
	// case R.id.sort_by_rating_ascend:
	//
	// // tested working for sort by rating in ascending value
	// adapter.sort(new Comparator<HotelData>() {
	// @Override
	// public int compare(HotelData lhs, HotelData rhs) {
	// return Float.valueOf(lhs.getRating()).compareTo(
	// Float.valueOf(rhs.getRating()));
	// }
	// });
	//
	// if (item.isChecked())
	// item.setChecked(false);
	// else
	// item.setChecked(true);
	// return true;
	// case R.id.sort_by_rating_descend:
	//
	// adapter.sort(new Comparator<HotelData>() {
	//
	// @Override
	// public int compare(HotelData lhs, HotelData rhs) {
	//
	// int comparatorValue = Float.valueOf(lhs.getRating())
	// .compareTo(Float.valueOf(rhs.getRating()));
	//
	// return comparatorValue * (-1);
	// }
	// });
	//
	// if (item.isChecked())
	// item.setChecked(false);
	// else
	// item.setChecked(true);
	// return true;
	// case R.id.sort_by_price_ascend:
	//
	// adapter.sort(new Comparator<HotelData>() {
	//
	// @Override
	// public int compare(HotelData lhs, HotelData rhs) {
	// try {
	// return Double.valueOf(
	// lhs.getObject("RoomCateg")
	// .getJSONObject("@attributes")
	// .getString("Price")).compareTo(
	// Double.valueOf(rhs.getObject("RoomCateg")
	// .getJSONObject("@attributes")
	// .getString("Price")));
	// } catch (NumberFormatException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// } catch (JSONException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// return 0;
	// }
	//
	// });
	//
	// if (item.isChecked())
	// item.setChecked(false);
	// else
	// item.setChecked(true);
	// return true;
	// case R.id.sort_by_price_descend:
	//
	// adapter.sort(new Comparator<HotelData>() {
	//
	// @Override
	// public int compare(HotelData lhs, HotelData rhs) {
	// int comparatorValue = 0;
	// try {
	// comparatorValue = Double.valueOf(
	// lhs.getObject("RoomCateg")
	// .getJSONObject("@attributes")
	// .getString("Price")).compareTo(
	// Double.valueOf(rhs.getObject("RoomCateg")
	// .getJSONObject("@attributes")
	// .getString("Price")));
	// return comparatorValue * (-1);
	// } catch (NumberFormatException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// } catch (JSONException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// return 0;
	// }
	// });
	//
	// if (item.isChecked())
	// item.setChecked(false);
	// else
	// item.setChecked(true);
	// return true;
	// }
	// return super.onContextItemSelected(item);
	// }

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mContext = null;
	}

	public static void finishMe() {
		if (mContext != null)
			((Activity) mContext).finish();
	}

	public static void toggleEmptyView(int count) {

		if (count == 0) {
			emptyText.setVisibility(View.VISIBLE);
		} else {
			emptyText.setVisibility(View.GONE);
		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode == SORT) {

			if (resultCode == RESULT_OK) {
				int result = data.getExtras().getInt("result");
				sortList(result);
				if (resultCode == RESULT_CANCELED) {
					// Write your code if there's no result
				}
			}
		}// onActivityResult
	}

	void sortList(int sortType) {

		Handler handler = new Handler();
		Runnable r = new Runnable() {
			public void run() {
				resultsList.setSelection(0);
			}
		};
		handler.postDelayed(r, 100);

		if (adapter != null && adapter.getFilter() != null) {
			switch (sortType) {
			case R.id.cb_priceasc:
				// adapter.getFilter().filter("priceasc");
				adapter.sort(new Comparator<HotelData>() {
					@Override
					public int compare(HotelData lhs, HotelData rhs) {
						try {
							return Float.valueOf(
									lhs.getObject("RoomCateg")
											.getJSONObject("@attributes")
											.getString("Price")).compareTo(
									Float.valueOf(rhs.getObject("RoomCateg")
											.getJSONObject("@attributes")
											.getString("Price")));
						} catch (Exception e) {
							try {
								return Float.valueOf(
										lhs.getArray("RoomCateg")
												.getJSONObject(0)
												.getJSONObject("@attributes")
												.getString("Price")).compareTo(
										Float.valueOf(rhs.getArray("RoomCateg")
												.getJSONObject(0)
												.getJSONObject("@attributes")
												.getString("Price")));
							} catch (Exception e1) {
								// TODO Auto-generated catch block

								try {
									return Float
											.valueOf(
													lhs.getObject("RoomCateg")
															.getJSONObject(
																	"@attributes")
															.getString("Price"))
											.compareTo(
													Float.valueOf(rhs
															.getArray(
																	"RoomCateg")
															.getJSONObject(0)
															.getJSONObject(
																	"@attributes")
															.getString("Price")));
								} catch (Exception e2) {
									// TODO Auto-generated catch block
									try {
										return Float
												.valueOf(
														lhs.getArray(
																"RoomCateg")
																.getJSONObject(
																		0)
																.getJSONObject(
																		"@attributes")
																.getString(
																		"Price"))
												.compareTo(
														Float.valueOf(rhs
																.getObject(
																		"RoomCateg")
																.getJSONObject(
																		"@attributes")
																.getString(
																		"Price")));
									} catch (Exception e3) {
										// TODO Auto-generated catch
										// block
									}

								}

							}

						}
						return -1;
					}
				});
				break;
			case R.id.cb_pricedesc:
				// adapter.getFilter().filter("pricedesc");
				adapter.sort(new Comparator<HotelData>() {
					@Override
					public int compare(HotelData lhs, HotelData rhs) {
						try {
							return Float.valueOf(
									rhs.getObject("RoomCateg")
											.getJSONObject("@attributes")
											.getString("Price")).compareTo(
									Float.valueOf(lhs.getObject("RoomCateg")
											.getJSONObject("@attributes")
											.getString("Price")));
						} catch (Exception e) {
							try {
								return Float.valueOf(
										rhs.getArray("RoomCateg")
												.getJSONObject(0)
												.getJSONObject("@attributes")
												.getString("Price")).compareTo(
										Float.valueOf(lhs.getArray("RoomCateg")
												.getJSONObject(0)
												.getJSONObject("@attributes")
												.getString("Price")));
							} catch (Exception e1) {
								// TODO Auto-generated catch block

								try {
									return Float
											.valueOf(
													rhs.getObject("RoomCateg")
															.getJSONObject(
																	"@attributes")
															.getString("Price"))
											.compareTo(
													Float.valueOf(lhs
															.getArray(
																	"RoomCateg")
															.getJSONObject(0)
															.getJSONObject(
																	"@attributes")
															.getString("Price")));
								} catch (Exception e2) {
									// TODO Auto-generated catch block
									try {
										return Float
												.valueOf(
														rhs.getArray(
																"RoomCateg")
																.getJSONObject(
																		0)
																.getJSONObject(
																		"@attributes")
																.getString(
																		"Price"))
												.compareTo(
														Float.valueOf(lhs
																.getObject(
																		"RoomCateg")
																.getJSONObject(
																		"@attributes")
																.getString(
																		"Price")));
									} catch (Exception e3) {
										// TODO Auto-generated catch
										// block
									}

								}

							}

						}
						return -1;
					}
				});
				break;
			case R.id.cb_ratingascen:
				// adapter.getFilter().filter("ratingascen");
				adapter.sort(new Comparator<HotelData>() {
					@Override
					public int compare(HotelData lhs, HotelData rhs) {
						return Float.valueOf(lhs.getRating()).compareTo(
								Float.valueOf(rhs.getRating()));
					}
				});

				break;
			case R.id.cb_ratingdesc:
				// adapter.getFilter().filter("ratingdesc");
				adapter.sort(new Comparator<HotelData>() {
					@Override
					public int compare(HotelData lhs, HotelData rhs) {
						return Float.valueOf(rhs.getRating()).compareTo(
								Float.valueOf(lhs.getRating()));
					}
				});
				break;
			default:
				break;
			}
		}
	}
}
