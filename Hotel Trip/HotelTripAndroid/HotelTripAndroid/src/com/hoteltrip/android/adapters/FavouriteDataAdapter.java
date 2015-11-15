package com.hoteltrip.android.adapters;

import java.io.IOException;
import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.hoteltrip.android.R;
import com.hoteltrip.android.util.FavouriteGetterSetter;
import com.hoteltrip.android.util.FavouriteTableActivity;
import com.nostra13.universalimageloader.core.ImageLoader;

public class FavouriteDataAdapter extends BaseAdapter {
	List<FavouriteGetterSetter> favList;
	private Context context;
	String hname, haddress, hreviews, hrating, hurl, hreviewurl, currency, hid;
	Float hprice;
	Handler handler;

	public FavouriteDataAdapter(Context context,
			List<FavouriteGetterSetter> hotelList, Handler handler) {
		this.context = context;
		favList = hotelList;
		this.handler = handler;

	}

	public void setData(List<FavouriteGetterSetter> hotelList) {
		this.favList = hotelList;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return favList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return favList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	int pos;

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder;
		pos = position;
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.searchresultslist_row_new,
					parent, false);
			viewHolder = new ViewHolder(convertView);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		hid = favList.get(position).getHotelid();
		hname = favList.get(position).getHotelname();
		haddress = favList.get(position).getHoteladdress();
		hreviews = favList.get(position).getReviewcount();
		hprice = favList.get(position).getPrice();
		hurl = favList.get(position).getImgpath();
		hreviewurl = favList.get(position).getReviewimageurl();
		hrating = favList.get(position).getRating();
		currency = favList.get(position).getCurrency();

		Log.i("Image URL", " HOTEL ID:: " + favList.get(position).getHotelid());

		viewHolder.hotelname.setText(hname);
		viewHolder.hoteladdress.setText(haddress);
		String text = "from <font size=24 color=#f47c21><b>%s %s </b></font> /night";

		try {

			viewHolder.hotelprice.setText(Html.fromHtml(String.format(text,
					hprice, currency)));
		} catch (Exception e) {
			viewHolder.hotelprice.setText(Html.fromHtml(String.format(text,
					"$", "0")));
		}
		String text1 = "<b>%s</b> Reviews";
		// viewHolder.hoteltreviews.setText(hreviews);

		viewHolder.hotelreviews.setText(Html.fromHtml(String.format(text1,
				hreviews)));
		viewHolder.starRating.setRating(Float.parseFloat(hrating));
		ImageLoader.getInstance().displayImage(hurl, viewHolder.hotelimage);
		ImageLoader.getInstance().displayImage(hreviewurl,
				viewHolder.rewiewimage);
		viewHolder.imgbtn_nextarrow.setVisibility(View.GONE);
		viewHolder.togglebtn_favorite.setChecked(false);
		viewHolder.togglebtn_favorite
				.setOnClickListener(new togglebuttonClickListener(pos, hid));
		return convertView;
	}

	class ViewHolder {

		TextView hotelname;
		TextView hoteladdress;
		TextView hotelprice;
		TextView hotelreviews;
		ImageView hotelimage, rewiewimage;
		RatingBar starRating;
		ImageButton imgbtn_nextarrow;
		ToggleButton togglebtn_favorite;

		public ViewHolder(final View v) {
			hotelname = (TextView) v.findViewById(R.id.hotelNameText);
			hoteladdress = (TextView) v.findViewById(R.id.streetNameText);
			hotelprice = (TextView) v.findViewById(R.id.priceText);
			hotelreviews = (TextView) v.findViewById(R.id.tv_reviews_count);
			hotelimage = (ImageView) v.findViewById(R.id.iv_hotel);
			starRating = (RatingBar) v.findViewById(R.id.starRating);
			rewiewimage = (ImageView) v.findViewById(R.id.iv_tripadvisor);
			togglebtn_favorite = (ToggleButton) v
					.findViewById(R.id.togglebtn_favorite);
			imgbtn_nextarrow = (ImageButton) v
					.findViewById(R.id.imgbtn_nextarrow);

			hotelname.setText("");
			hoteladdress.setText("");
			hotelprice.setText("");
			hotelreviews.setText("");
			hotelimage.setImageBitmap(null);
		}
	}

	int post;
	String hotelid;

	public class togglebuttonClickListener implements OnClickListener {

		int position;
		String szHotelID;

		public togglebuttonClickListener(int position, String szHotelID) {

			this.position = position;
			this.szHotelID = szHotelID;
		}

		FavouriteTableActivity data;

		public void onClick(View v) {
			{
				post = position;
				hotelid = szHotelID;

				Log.i("onClick******************", "" + post + szHotelID);
				try {
					data = new FavouriteTableActivity(context);
					data.open();
					data.deleteRow(szHotelID);
					handler.sendEmptyMessage(0);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					if (data != null)
						data.close();
				}

			}
		}
	}
}