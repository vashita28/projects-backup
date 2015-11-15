package co.uk.android.lldc.adapters;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import co.uk.android.lldc.R;
import co.uk.android.lldc.models.ServerModel;

import com.nostra13.universalimageloader.core.ImageLoader;

@SuppressLint("InflateParams")
public class ExploreAdapter extends BaseAdapter {
	Context context;
	LayoutInflater inflater;
	ArrayList<ServerModel> listExplore;

	ImageLoader imageLoader;

	public ExploreAdapter(Context _context, ArrayList<ServerModel> _listExplore) {
		this.context = _context;
		this.listExplore = _listExplore;
		imageLoader = ImageLoader.getInstance();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listExplore.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return listExplore.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@SuppressLint("DefaultLocale")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			// convertView = inflater.inflate(R.layout.row_explorelist, parent,
			// true);
			if (inflater == null)
				inflater = (LayoutInflater) context
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			if (convertView == null)
				convertView = inflater.inflate(R.layout.row_explorelist, null);

			holder.ivImage = (ImageView) convertView
					.findViewById(R.id.ivExploreImage);
			holder.tvHeading = (TextView) convertView
					.findViewById(R.id.tvExploreHeading);
			holder.tvTime = (TextView) convertView
					.findViewById(R.id.tvExploreTime);
			holder.tvDesc = (TextView) convertView
					.findViewById(R.id.tvExploreDesc);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		try {
			String szImageUrl = listExplore.get(position).getLargeImage()
					+ "&width=200&height=200&crop-to-fit";
			Log.e("ExplorerAdapter", "szImageUrl::> " + szImageUrl);
			ImageLoader.getInstance().displayImage(szImageUrl, holder.ivImage);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		holder.tvHeading.setText(listExplore.get(position).getName()
				.toUpperCase());
		holder.tvTime.setText(listExplore.get(position).getActiveDays());
		holder.tvDesc.setText(listExplore.get(position).getShortDesc());
		holder.tvHeading.setTextColor(Color.parseColor(listExplore
				.get(position).getColor()));
		holder.tvTime.setTextColor(Color.parseColor(listExplore.get(position)
				.getColor()));
		return convertView;
	}

	class ViewHolder {
		ImageView ivImage;
		TextView tvHeading, tvTime, tvDesc;

	}

}
