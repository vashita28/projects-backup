package com.example.cabapppassenger.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cabapppassenger.R;
import com.example.cabapppassenger.task.DeleteFavourites;
import com.example.cabapppassenger.util.Constant;

public class FavouritesAdapter extends BaseAdapter {

	Context mContext;
	ArrayList<String> arr_favourites = new ArrayList<String>();
	ArrayList<Boolean> arr_edit_favourites = new ArrayList<Boolean>();
	Handler handler;
	ViewHolder viewHolder;

	public FavouritesAdapter(Context context, ArrayList<String> list_fav,
			ArrayList<Boolean> list_edit, Handler handler) {
		this.mContext = context;
		arr_favourites = list_fav;
		arr_edit_favourites = list_edit;
		this.handler = handler;

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return arr_favourites.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return arr_favourites.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	int pos;

	public void onLongPress(int position) {

		for (int i = 0; i < arr_edit_favourites.size(); i++) {
			arr_edit_favourites.set(i, false);
		}

		arr_edit_favourites.set(position, true);

		this.notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		pos = position;
		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.favourites_recent_row,
					parent, false);
			viewHolder = new ViewHolder(convertView);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		if (arr_favourites.get(position) != null)
			viewHolder.tv_address.setText(arr_favourites.get(position));

		if (arr_edit_favourites.get(position)) {
			viewHolder.rel_delete.setVisibility(View.VISIBLE);
			Log.e("GVVPosition", "" + position);
		} else {
			viewHolder.rel_delete.setVisibility(View.GONE);
			Log.e("GVGPosition", "" + position);
		}

		viewHolder.rel_favsrow.setTag(position + "");
		viewHolder.tv_yes_delete.setTag(position + "");

		return convertView;

	}

	public class ViewHolder {

		TextView tv_address, tv_yes_delete, tv_no_delete;
		RelativeLayout rel_favsrow, rel_delete;

		public ViewHolder(final View v) {
			tv_address = (TextView) v.findViewById(R.id.tv_address);
			tv_address.setText("");
			rel_delete = (RelativeLayout) v.findViewById(R.id.rel_delete);
			rel_favsrow = (RelativeLayout) v.findViewById(R.id.rel_favsrow);
			tv_yes_delete = (TextView) v.findViewById(R.id.tv_pick_up_top_yes);
			tv_no_delete = (TextView) v.findViewById(R.id.tv_pick_up_top_no);
			tv_no_delete.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					dontdelete();
				}
			});
			tv_yes_delete.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

					String address = arr_favourites.get(Integer.parseInt(v
							.getTag().toString()));
					if (address != null) {
						String favid = Constant.hash_favid_favourites
								.get(address);

						if (favid != null) {
							DeleteFavourites fav = new DeleteFavourites(
									mContext, handler, Constant.arr_favourites
											.get(Integer.parseInt(v.getTag()
													.toString())));
							fav.favourite_id = favid;
							fav.execute("");
						}
					} else {
						dontdelete();
						Toast.makeText(
								mContext,
								"Unable to delete the favourite item, Please try again.",
								Toast.LENGTH_SHORT).show();
					}
				}
			});

		}
	}

	public void dontdelete() {
		for (int i = 0; i < arr_edit_favourites.size(); i++) {
			arr_edit_favourites.set(i, false);
			this.notifyDataSetChanged();
		}
	}
}
