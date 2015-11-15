package com.hoteltrip.android.gallery;

import java.util.ArrayList;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hoteltrip.android.R;
import com.hoteltrip.android.adapters.HotelDataAdapter;
import com.hoteltrip.android.gallery.Constants.Extra;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

public class ImageGridActivity extends AbsListViewBaseActivity {

	ArrayList<String> listImageURL = new ArrayList<String>();

	DisplayImageOptions options;
	ImageLoader imageLoader;

	String m_szHotelID = "";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.photogallery);
		super.init("Photo Gallery");
		btnMap.setVisibility(View.GONE);

		if (getIntent().getExtras() != null) {
			m_szHotelID = getIntent().getExtras().getString("hotelid");
		}

		imageLoader = ImageLoader.getInstance();
		// new JSONParse().execute();

		options = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.drawable.ic_empty)
				.showImageOnFail(R.drawable.ic_error).cacheInMemory(true)
				.cacheOnDisc(true).bitmapConfig(Bitmap.Config.RGB_565).build();

		listView = (GridView) findViewById(R.id.gv_images);
		listImageURL = HotelDataAdapter.mapHotelImagesList.get(m_szHotelID);
		if (listImageURL != null) {
			Log.d("", "Image length:: " + String.valueOf(listImageURL.size()));
			((GridView) listView).setAdapter(new ImageAdapter());
		} else {
			TextView tv_nophotos = (TextView) findViewById(R.id.tv_nophotos);
			tv_nophotos.setVisibility(View.VISIBLE);
		}

		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				startImagePagerActivity(position);
			}
		});
	}

	private void startImagePagerActivity(int position) {
		Intent intent = new Intent(this, ImagePagerActivity.class);
		intent.putExtra(Extra.IMAGES, listImageURL);
		intent.putExtra(Extra.IMAGE_POSITION, position);
		startActivity(intent);
	}

	public class ImageAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			return listImageURL.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final ViewHolder holder;
			View view = convertView;
			if (view == null) {

				view = getLayoutInflater().inflate(R.layout.item_grid_image,
						parent, false);
				holder = new ViewHolder();
				assert view != null;
				holder.imageView = (ImageView) view.findViewById(R.id.image);

				holder.progressBar = (ProgressBar) view
						.findViewById(R.id.progress);
				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}

			imageLoader.displayImage(listImageURL.get(position),
					holder.imageView, options,
					new SimpleImageLoadingListener() {
						@Override
						public void onLoadingStarted(String imageUri, View view) {
							holder.progressBar.setProgress(0);
							holder.progressBar.setVisibility(View.VISIBLE);
						}

						@Override
						public void onLoadingFailed(String imageUri, View view,
								FailReason failReason) {
							holder.progressBar.setVisibility(View.GONE);
						}

						@Override
						public void onLoadingComplete(String imageUri,
								View view, Bitmap loadedImage) {
							holder.progressBar.setVisibility(View.GONE);
						}
					}, new ImageLoadingProgressListener() {
						@Override
						public void onProgressUpdate(String imageUri,
								View view, int current, int total) {
							holder.progressBar.setProgress(Math.round(100.0f
									* current / total));
						}
					});

			return view;
		}

		class ViewHolder {
			ImageView imageView;
			ProgressBar progressBar;
		}
	}

	// private class JSONParse extends AsyncTask<String, String, JSONObject> {
	// private ProgressDialog pDialog;
	//
	// @Override
	// protected void onPreExecute() {
	// super.onPreExecute();
	//
	// pDialog = new ProgressDialog(ImageGridActivity.this);
	// pDialog.setMessage("Loading...");
	// pDialog.setIndeterminate(false);
	// pDialog.setCancelable(true);
	// pDialog.show();
	//
	// }
	//
	// @Override
	// protected JSONObject doInBackground(String... args) {
	// JsonParser jParser = new JsonParser();
	// // Getting JSON from URL
	// JSONObject json = jParser
	// .getJSONFromUrl("http://dev.pocketapp.co.uk/dev/hotel-trip/index.php/dataget/gethoteldetail/MA1305000008");
	// return json;
	// }
	//
	// @Override
	// protected void onPostExecute(JSONObject json) {
	//
	// try {
	//
	// Log.d("JSON output", json.toString());
	//
	// JSONObject c = json.getJSONObject("GetHotelDetail_Response");
	//
	// JSONArray jarray = c.getJSONArray("Images");
	//
	// imageUrls = new String[jarray.length()];
	// for (int i = 0; i < jarray.length(); i++) {
	// JSONObject jaatr = jarray.getJSONObject(i);
	// JSONObject jpath = jaatr.getJSONObject("@attributes");
	//
	// String jpathattr = jpath.getString("path");
	// Log.d("Image Url", jpathattr);
	//
	// imageUrls[i] = jpathattr;
	//
	// Log.d("image length inside",
	// String.valueOf(imageUrls.length));
	// }
	//
	// int len = imageUrls.length;
	//
	// Log.d("image length", String.valueOf(len));
	//
	// if (len > 0) {
	// ((GridView) listView).setAdapter(new ImageAdapter());
	// }
	// pDialog.dismiss();
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }
	// }

}