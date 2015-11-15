package com.hoteltrip.android;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.hoteltrip.android.adapters.FavouriteDataAdapter;
import com.hoteltrip.android.util.DataBaseHandler;
import com.hoteltrip.android.util.FavouriteGetterSetter;

public class MyFavouriteActivity extends BaseActivity {

	TextView hotelNameText, tv_title;
	FavouriteDataAdapter favoritesTable;
	Context mContext = this;
	FavouriteGetterSetter fav;
	DataBaseHandler dbHelper;
	List<FavouriteGetterSetter> hotelList;
	TextView tv_nofavadded;
	ListView resultsList;
	FavouriteDataAdapter adapter;
	Handler handler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_myfavourites);
		super.init("My Favourites");
		btnMap.setVisibility(View.GONE);
		btnBack.setVisibility(View.VISIBLE);
		resultsList = (ListView) findViewById(R.id.myfavList);
		hotelNameText = (TextView) findViewById(R.id.hotelNameText);
		/*
		 * resultsList.setOnItemClickListener(new OnItemClickListener() {
		 * 
		 * @Override public void onItemClick(AdapterView<?> adapter, View View,
		 * int pos, long id) { /`/ TODO Auto-generated method stub
		 * Log.e("********************", "******CLICKED*************IDD  " + id
		 * + " POSss " + pos); Intent hoteldetailsIntent = new Intent(
		 * MyFavouriteActivity.this, HotelDetailsActivity.class);
		 * hoteldetailsIntent.putExtra("id", id);
		 * hoteldetailsIntent.putExtra("pos", pos);
		 * startActivity(hoteldetailsIntent); } });
		 */

		// Button btn_back = (Button) findViewById(R.id.btn_back);
		// btn_back.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method
		// finish();
		// }
		// });

		handler = new Handler() {
			@Override
			public void handleMessage(android.os.Message msg) {

				// dynamically update list when hotel is removed from myfavs
				hotelList = new ArrayList<FavouriteGetterSetter>();
				hotelList.clear();
				getAllHotels();
				adapter.setData(hotelList);
				adapter.notifyDataSetChanged();

				if (hotelList == null || hotelList.size() == 0) {
					tv_nofavadded = (TextView) findViewById(R.id.tv_nofavadded);
					tv_nofavadded.setVisibility(View.VISIBLE);
				}
			}
		};

		hotelList = new ArrayList<FavouriteGetterSetter>();
		hotelList.clear();

		getAllHotels();

		if (hotelList != null && hotelList.size() > 0) {
			// if list is not null then iterate
			adapter = new FavouriteDataAdapter(mContext, hotelList, handler);
			resultsList.setAdapter(adapter);
		} else {
			// if list is null show no fav added text
			resultsList.setAdapter(null);
			tv_nofavadded = (TextView) findViewById(R.id.tv_nofavadded);
			tv_nofavadded.setVisibility(View.VISIBLE);
		}

	}

	public List<FavouriteGetterSetter> getAllHotels() {

		// Select All Query.
		String selectQuery = "SELECT  * FROM " + DataBaseHandler.TABLE_FAVS;

		dbHelper = new DataBaseHandler(mContext);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor != null && cursor.moveToFirst()) {
			Log.i("getAllHotels", "List Size:: " + cursor.getCount());
			do {
				fav = new FavouriteGetterSetter();
				fav.setHotelid(cursor.getString(cursor
						.getColumnIndex(DataBaseHandler.HOTEL_ID)));

				fav.setHotelname(cursor.getString(cursor
						.getColumnIndex(DataBaseHandler.NAME)));
				fav.setHoteladdress(cursor.getString(cursor
						.getColumnIndex(DataBaseHandler.ADDRESS)));
				fav.setPrice(Float.parseFloat(cursor.getString(cursor
						.getColumnIndex(DataBaseHandler.PRICE))));
				fav.setImgpath(cursor.getString(cursor
						.getColumnIndex(DataBaseHandler.IMAGEURL)));
				fav.setRating(cursor.getString(cursor
						.getColumnIndex(DataBaseHandler.RATING_STAR)));
				fav.setReviewcount(cursor.getString(cursor
						.getColumnIndex(DataBaseHandler.REVIEW_COUNT)));
				fav.setReviewimageurl(cursor.getString(cursor
						.getColumnIndex(DataBaseHandler.REVIEW_IMAGEURL)));
				fav.setCurrency(cursor.getString(cursor
						.getColumnIndex(DataBaseHandler.CURRENCY)));
				// Adding hotels to list
				hotelList.add(fav);

			} while (cursor.moveToNext());

		}

		return hotelList;
	}
}