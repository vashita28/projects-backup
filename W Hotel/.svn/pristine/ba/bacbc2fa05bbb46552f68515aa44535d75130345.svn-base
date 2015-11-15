package uk.co.pocketapp.whotel.adapters;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import uk.co.pocketapp.whotel.FragmentLoad;
import uk.co.pocketapp.whotel.R;
import uk.co.pocketapp.whotel.customview.RecyclingImageView;
import uk.co.pocketapp.whotel.fragments.HotelFragment;
import uk.co.pocketapp.whotel.fragments.WebViewFragment;
import uk.co.pocketapp.whotel.util.CustomHotelData;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mblox.engage.Message;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

public class HotelAdapter extends BaseAdapter implements Filterable {
	Context context;
	LayoutInflater inflater;
	private SimpleDateFormat dateFormat = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm");
	private static final String TAG = "uk.co.pocketapp.whotel.adapter.HotelAdapter";

	ImageLoader imageLoader;

	DisplayImageOptions options;

	int nPosition = 0;
	HotelFragment hotelfragment;

	List<Message> filteredMessage;
	List<CustomHotelData> hotelDataList, orginalhotelDataList;

	boolean bIsOrginalHotelList = true;

	FragmentLoad fragmentLoad;

	public static boolean isCategoryFilterSearch = false;

	// static final List<String> displayedImages = Collections
	// .synchronizedList(new LinkedList<String>());

	// static HashMap<String, Bitmap> mapImages = new HashMap<String, Bitmap>();

	public HotelAdapter(ArrayList<CustomHotelData> hotelDataList,
			Context context, HotelFragment hotelFragment,
			FragmentLoad fragmentLoad) {
		// TODO Auto-generated constructor stub

		this.hotelDataList = hotelDataList;
		this.orginalhotelDataList = hotelDataList;
		this.context = context;
		inflater = LayoutInflater.from(context);
		imageLoader = ImageLoader.getInstance();
		this.hotelfragment = hotelFragment;
		this.fragmentLoad = fragmentLoad;

		options = new DisplayImageOptions.Builder()
				// .displayer(new FadeInBitmapDisplayer(500))
				.cacheInMemory(true).cacheOnDisc(true)
				.showStubImage(R.drawable.placeholderportrait)
				.showImageForEmptyUri(R.drawable.placeholderportrait)
				.showImageOnFail(R.drawable.placeholderportrait)
				.bitmapConfig(Bitmap.Config.RGB_565).build(); // ARGB_8888

	}

	@Override
	public int getCount() {

		if (bIsOrginalHotelList) {
			if ((hotelDataList.size() - 1) % 2 == 0)
				return (hotelDataList.size() - 1) / 2;
			else
				return ((hotelDataList.size() - 1) / 2) + 1;
		} else {
			if (hotelDataList.size() % 2 == 0)
				return hotelDataList.size() / 2;
			else
				return (hotelDataList.size() / 2) + 1;
		}
	}

	@Override
	public Object getItem(int position) {
		return hotelDataList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {

			convertView = inflater.inflate(R.layout.row_dealslist, parent,
					false);

			holder = new ViewHolder();
			holder.imageview_hotels1 = (RecyclingImageView) convertView
					.findViewById(R.id.imageview_deals1);
			holder.imageview_hotels2 = (RecyclingImageView) convertView
					.findViewById(R.id.imageview_deals2);

			holder.textview_hotel_header1 = (TextView) convertView
					.findViewById(R.id.tv_deals_header1);
			holder.textview_hotel_header2 = (TextView) convertView
					.findViewById(R.id.tv_deals_header2);
			holder.textview_hotel_desc1 = (TextView) convertView
					.findViewById(R.id.tv_deals_desc1);
			holder.textview_hotel_desc2 = (TextView) convertView
					.findViewById(R.id.tv_deals_desc2);

			holder.textview_read_more1 = (TextView) convertView
					.findViewById(R.id.textview_read_more_1);
			holder.textview_read_more2 = (TextView) convertView
					.findViewById(R.id.textview_read_more_2);

			holder.textview_read_more1.setText(context
					.getString(R.string.read_more) + " ");
			holder.textview_read_more2.setText(context
					.getString(R.string.read_more) + " ");

			// holder.imageview_read_more1 = (ImageView) convertView
			// .findViewById(R.id.imageview_read_more1);
			// holder.imageview_read_more2 = (ImageView) convertView
			// .findViewById(R.id.imageview_read_more2);

			holder.rel_left = (RelativeLayout) convertView
					.findViewById(R.id.rel_left);
			holder.rel_right = (RelativeLayout) convertView
					.findViewById(R.id.rel_right);

			// holder.progress_left = (ProgressBar) convertView
			// .findViewById(R.id.progress_left);
			// holder.progress_right = (ProgressBar) convertView
			// .findViewById(R.id.progress_right);

			convertView.setTag(holder); // set the View holder

		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.imageview_hotels1.setVisibility(View.INVISIBLE);
		// holder.textview_deals_header1.setVisibility(View.GONE);
		// holder.textview_deals_desc1.setVisibility(View.GONE);
		// holder.textview_read_more1.setVisibility(View.GONE);
		// holder.imageview_read_more1.setVisibility(View.GONE);
		holder.rel_left.setVisibility(View.INVISIBLE);

		holder.imageview_hotels2.setVisibility(View.INVISIBLE);
		// holder.textview_deals_header2.setVisibility(View.GONE);
		// holder.textview_deals_desc2.setVisibility(View.GONE);
		// holder.textview_read_more2.setVisibility(View.GONE);
		// holder.imageview_read_more2.setVisibility(View.GONE);
		holder.rel_right.setVisibility(View.INVISIBLE);

		if (hotelDataList.isEmpty()) {
			// row1.setText("No active messages");
		} else {

			if (bIsOrginalHotelList)
				getViewOriginal(position, holder);
			else
				getViewAfterSearch(position, holder);
		}
		return convertView;
	}

	void getViewAfterSearch(int position, ViewHolder holder) {
		CustomHotelData msg1 = null, msg2 = null;
		String szBannerImageURL = "";

		// Log.d(TAG, " publish Results Position " + position);

		nPosition = position + 1;
		try {
			msg1 = hotelDataList.get((nPosition * 2) - 2);
			// holder.textview_read_more1
			// .setOnClickListener(new textViewClickListener(
			// ((nPosition * 2) - 2), hotspotDataList.get(
			// ((nPosition * 2) - 2)).getId(),
			// hotspotDataList.get(((nPosition * 2) - 2))
			// .getPage_url()));
			holder.imageview_hotels1
					.setOnClickListener(new textViewClickListener(
							((nPosition * 2) - 2), hotelDataList.get(
									((nPosition * 2) - 2)).getId(),
							hotelDataList.get(((nPosition * 2) - 2))
									.getPage_url(), hotelDataList.get(
									((nPosition * 2) - 2)).getH_lati(),
							hotelDataList.get(((nPosition * 2) - 2))
									.getH_long(), hotelDataList.get(
									((nPosition * 2) - 2)).getIs_whotel()));
			// Log.d("TAG",
			// "BANNER POS111 :: "
			// + String.valueOf((nPosition * 2) - 1));
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			msg2 = hotelDataList.get((nPosition * 2) - 1);
			// holder.textview_read_more2
			// .setOnClickListener(new textViewClickListener(
			// ((nPosition * 2) - 1), hotspotDataList.get(
			// ((nPosition * 2) - 1)).getId(),
			// hotspotDataList.get(((nPosition * 2) - 1))
			// .getPage_url()));
			holder.imageview_hotels2
					.setOnClickListener(new textViewClickListener(
							((nPosition * 2) - 1), hotelDataList.get(
									((nPosition * 2) - 1)).getId(),
							hotelDataList.get(((nPosition * 2) - 1))
									.getPage_url(), hotelDataList.get(
									((nPosition * 2) - 1)).getH_lati(),
							hotelDataList.get(((nPosition * 2) - 1))
									.getH_long(), hotelDataList.get(
									((nPosition * 2) - 1)).getIs_whotel()));
			// Log.d("TAG",
			// "BANNER POS222 :: " + String.valueOf(nPosition * 2));
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (msg1 != null) {
			try {
				szBannerImageURL = msg1.getRectsmall().toString();
				// .get("banner_image").toString();
				// Log.d(TAG, "BANNER11111:: " + szBannerImageURL);
				// Log.d(TAG, "app_data11111:: "
				// + msg1.getMessageBody().get("app_data"));

				// if (szBannerImageURL != null
				// && !szBannerImageURL.equals("")) {
				holder.imageview_hotels1.setVisibility(View.VISIBLE);
				// holder.textview_deals_header1
				// .setVisibility(View.VISIBLE);
				// holder.textview_deals_desc1
				// .setVisibility(View.VISIBLE);
				// holder.textview_read_more1
				// .setVisibility(View.VISIBLE);
				// holder.imageview_read_more1
				// .setVisibility(View.VISIBLE);
				holder.rel_left.setVisibility(View.VISIBLE);

				// if (mapImages.get(szBannerImageURL) == null)
				imageLoader.displayImage(szBannerImageURL,
						holder.imageview_hotels1, new ImageLoadListener());// holder.progress_left
				// else
				// holder.imageview_hotspot1.setImageBitmap(mapImages
				// .get(szBannerImageURL));

				holder.textview_hotel_header1.setText(msg1.getH_name()
						.toString());
				holder.textview_hotel_desc1
						.setText(msg1.getH_desc().toString());
				// }
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (msg2 != null) {
			try {
				szBannerImageURL = msg2.getRectsmall().toString();
				// .get("banner_image").toString();
				// Log.d(TAG, "BANNER22222:: " + szBannerImageURL);
				// Log.d(TAG,
				// "app_data22222:: "
				// + msg2.getMessageBody().get("app_data"));
				// if (szBannerImageURL != null
				// && !szBannerImageURL.equals("")) {
				holder.imageview_hotels2.setVisibility(View.VISIBLE);
				// holder.textview_deals_header2
				// .setVisibility(View.VISIBLE);
				// holder.textview_deals_desc2
				// .setVisibility(View.VISIBLE);
				// holder.textview_read_more2
				// .setVisibility(View.VISIBLE);
				// holder.imageview_read_more2
				// .setVisibility(View.VISIBLE);
				holder.rel_right.setVisibility(View.VISIBLE);

				// if (mapImages.get(szBannerImageURL) == null)
				imageLoader.displayImage(szBannerImageURL,
						holder.imageview_hotels2, new ImageLoadListener());// holder.progress_right
				// else
				// holder.imageview_hotspot2.setImageBitmap(mapImages
				// .get(szBannerImageURL));

				holder.textview_hotel_header2.setText(msg2.getH_name()
						.toString());
				holder.textview_hotel_desc2
						.setText(msg2.getH_desc().toString());
				// }
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	void getViewOriginal(int position, ViewHolder holder) {

		CustomHotelData msg1 = null, msg2 = null;
		String szBannerImageURL = "";

		// Log.d(TAG, " publish Results Position " + position);

		nPosition = position + 1;
		try {
			msg1 = hotelDataList.get((nPosition * 2) - 1);
			// holder.textview_read_more1
			// .setOnClickListener(new textViewClickListener(
			// (nPosition * 2) - 1, hotelDataList.get(
			// (nPosition * 2) - 1).getId(),
			// hotelDataList.get((nPosition * 2) - 1)
			// .getPage_url()));
			holder.imageview_hotels1
					.setOnClickListener(new textViewClickListener(
							(nPosition * 2) - 1, hotelDataList.get(
									(nPosition * 2) - 1).getId(), hotelDataList
									.get((nPosition * 2) - 1).getPage_url(),
							hotelDataList.get((nPosition * 2) - 1).getH_lati(),
							hotelDataList.get((nPosition * 2) - 1).getH_long(),
							hotelDataList.get((nPosition * 2) - 1)
									.getIs_whotel()));
			// Log.d("TAG",
			// "BANNER POS111 :: "
			// + String.valueOf((nPosition * 2) - 1));
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			msg2 = hotelDataList.get(nPosition * 2);
			// holder.textview_read_more2
			// .setOnClickListener(new textViewClickListener(
			// nPosition * 2, hotelDataList.get(nPosition * 2)
			// .getId(), hotelDataList.get(
			// nPosition * 2).getPage_url()));
			holder.imageview_hotels2
					.setOnClickListener(new textViewClickListener(
							nPosition * 2, hotelDataList.get(nPosition * 2)
									.getId(), hotelDataList.get(nPosition * 2)
									.getPage_url(), hotelDataList.get(
									nPosition * 2).getH_lati(), hotelDataList
									.get(nPosition * 2).getH_long(),
							hotelDataList.get(nPosition * 2).getIs_whotel()));
			// Log.d("TAG",
			// "BANNER POS222 :: " + String.valueOf(nPosition * 2));
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (msg1 != null) {
			try {
				szBannerImageURL = msg1.getRectsmall().toString();

				// if (szBannerImageURL != null
				// && !szBannerImageURL.equals("")) {
				holder.imageview_hotels1.setVisibility(View.VISIBLE);
				// holder.textview_deals_header1
				// .setVisibility(View.VISIBLE);
				// holder.textview_deals_desc1
				// .setVisibility(View.VISIBLE);
				// holder.textview_read_more1
				// .setVisibility(View.VISIBLE);
				// holder.imageview_read_more1
				// .setVisibility(View.VISIBLE);
				holder.rel_left.setVisibility(View.VISIBLE);

				// if (mapImages.get(szBannerImageURL) == null)
				imageLoader.displayImage(szBannerImageURL,
						holder.imageview_hotels1, new ImageLoadListener());// holder.progress_left
				// else
				// holder.imageview_hotels1.setImageBitmap(mapImages
				// .get(szBannerImageURL));

				holder.textview_hotel_header1.setText(msg1.getH_name()
						.toString());
				holder.textview_hotel_desc1
						.setText(msg1.getH_desc().toString());
				// }
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (msg2 != null) {
			try {
				szBannerImageURL = msg2.getRectsmall().toString();
				// .get("banner_image").toString();
				// Log.d(TAG, "BANNER22222:: " + szBannerImageURL);
				// Log.d(TAG,
				// "app_data22222:: "
				// + msg2.getMessageBody().get("app_data"));
				// if (szBannerImageURL != null
				// && !szBannerImageURL.equals("")) {
				holder.imageview_hotels2.setVisibility(View.VISIBLE);
				// holder.textview_deals_header2
				// .setVisibility(View.VISIBLE);
				// holder.textview_deals_desc2
				// .setVisibility(View.VISIBLE);
				// holder.textview_read_more2
				// .setVisibility(View.VISIBLE);
				// holder.imageview_read_more2
				// .setVisibility(View.VISIBLE);
				holder.rel_right.setVisibility(View.VISIBLE);

				// if (mapImages.get(szBannerImageURL) == null)
				imageLoader.displayImage(szBannerImageURL,
						holder.imageview_hotels2, new ImageLoadListener());// holder.progress_right
				// else
				// holder.imageview_hotels2.setImageBitmap(mapImages
				// .get(szBannerImageURL));

				holder.textview_hotel_header2.setText(msg2.getH_name()
						.toString());
				holder.textview_hotel_desc2
						.setText(msg2.getH_desc().toString());
				// }
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	class ViewHolder {
		ImageView imageview_hotels1, imageview_hotels2;
		TextView textview_hotel_header1, textview_hotel_header2;
		TextView textview_hotel_desc1, textview_hotel_desc2;
		TextView textview_read_more1, textview_read_more2;
		// ImageView imageview_read_more1, imageview_read_more2;
		RelativeLayout rel_left, rel_right;
		// ProgressBar progress_left, progress_right;
	}

	class textViewClickListener implements OnClickListener {
		int position;
		String messageID;
		String url;
		String szLat, szLng;
		String szIsWHotel;

		public textViewClickListener(int pos, String szId, String szURL,
				String szLat, String szLng, String szIsWHotel) {
			this.position = pos;
			this.messageID = szId;
			this.url = szURL;
			this.szLat = szLat;
			this.szLng = szLng;
			this.szIsWHotel = szIsWHotel;
		}

		public void onClick(View v) {
			{
				// Log.d(TAG, " CLICKED*** " + position);
				Fragment mFragment_webview = WebViewFragment.newInstance();
				WebViewFragment.fragmentLoad = fragmentLoad;
				Bundle bundleArgs = new Bundle();
				bundleArgs.putString("webURL", url);
				bundleArgs.putString("messageid", messageID);
				bundleArgs.putString("lat", szLat);
				bundleArgs.putString("lng", szLng);
				bundleArgs.putString("is_whotel", szIsWHotel);
				mFragment_webview.setArguments(bundleArgs);

				Log.d("textViewClickListener", "Message ID is: " + messageID);
				hotelfragment.switchFragment(mFragment_webview);

				// Log.d(TAG, " CLICKED*** WEBURL " +
				// textview_read_more.getTag());
			}
		}
	}

	@Override
	public Filter getFilter() {
		// TODO Auto-generated method stub

		Filter filter = new Filter() {

			@SuppressWarnings("unchecked")
			@Override
			protected void publishResults(CharSequence constraint,
					FilterResults results) {

				hotelDataList = (ArrayList<CustomHotelData>) results.values;
				// Log.d(TAG, " publish Results " + messages);
				// Log.d(TAG, " publish Results size " + messages.size());
				notifyDataSetChanged();

				if (hotelDataList != null && hotelDataList.size() == 0) {
					fragmentLoad.noMatchFound();
				}
			}

			@Override
			protected FilterResults performFiltering(CharSequence constraint) {

				FilterResults results;
				ArrayList<CustomHotelData> filteredMessage;

				if (isCategoryFilterSearch) {
					results = new FilterResults();
					filteredMessage = new ArrayList<CustomHotelData>();
					if (constraint.toString().equals("ALL")
							|| constraint.toString().equals("all")) {
						results.count = orginalhotelDataList.size();
						results.values = orginalhotelDataList;
						bIsOrginalHotelList = true;
						Log.d("Constraint Category Search",
								constraint.toString() + " SIZE "
										+ results.count);
					} else {
						Log.d("Constraint Category Search", " Category"
								+ constraint.toString());

						for (int i = 0; i < orginalhotelDataList.size(); i++) {
							String szCategory;
							try {
								szCategory = orginalhotelDataList.get(i)
										.getH_category().toString();
								if (szCategory.toString().equalsIgnoreCase(
										constraint.toString())) {
									filteredMessage.add(orginalhotelDataList
											.get(i));
								}
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}

						results.count = filteredMessage.size();
						results.values = filteredMessage;
						bIsOrginalHotelList = false;
						// Log.d(TAG, "VALUES RESULT COUNT" + results.count);
						// Log.d(TAG, "VALUES RESULT " +
						// results.values.toString());
					}
					return results;
				} else {
					results = new FilterResults();
					filteredMessage = new ArrayList<CustomHotelData>();
					if (constraint == null || constraint.length() == 0) {
						results.count = orginalhotelDataList.size();
						results.values = orginalhotelDataList;
						bIsOrginalHotelList = true;
					} else {
						constraint = constraint.toString().toLowerCase();
						boolean bIsFirstAdded = false;
						for (int i = 0; i < orginalhotelDataList.size(); i++) {
							String dataNames;
							try {
								dataNames = orginalhotelDataList.get(i)
										.getH_name().toString();
								// .getMessageBody().get("title").toString();
								if (dataNames.toLowerCase().contains(
										constraint.toString())) {
									filteredMessage.add(orginalhotelDataList
											.get(i));
									if (!bIsFirstAdded) {
										filteredMessage
												.add(orginalhotelDataList
														.get(i));
										bIsFirstAdded = true;
									}
								}
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}

						results.count = filteredMessage.size();
						results.values = filteredMessage;
						bIsOrginalHotelList = false;
						// Log.d(TAG, "VALUES RESULT COUNT" + results.count);
						// Log.d(TAG, "VALUES RESULT " +
						// results.values.toString());
					}
				}
				return results;
			}
		};

		return filter;
	}

	private class ImageLoadListener extends SimpleImageLoadingListener {

		// ProgressBar progressBar;

		// ImageLoadListener(ProgressBar progressBar) {
		// this.progressBar = progressBar;
		// }

		@Override
		public void onLoadingComplete(String imageUri, View view,
				Bitmap loadedImage) {
			if (loadedImage != null) {
				ImageView imageView = (ImageView) view;
				// boolean firstDisplay = !displayedImages.contains(imageUri);
				// if (firstDisplay) {
				// FadeInBitmapDisplayer.animate(imageView, 500);
				// displayedImages.add(imageUri);
				// mapImages.put(imageUri, loadedImage);
				// }
				// else{
				imageView.setImageBitmap(loadedImage);
				// }
			}
			// progressBar.setVisibility(View.GONE);
		}

		@Override
		public void onLoadingStarted(String imageUri, View view) {
			// boolean firstDisplay = !displayedImages.contains(imageUri);
			// if (firstDisplay)
			// progressBar.setVisibility(View.VISIBLE);
		}

		@Override
		public void onLoadingFailed(String imageUri, View view,
				FailReason failReason) {
			// progressBar.setVisibility(View.GONE);
		}

	}
}
