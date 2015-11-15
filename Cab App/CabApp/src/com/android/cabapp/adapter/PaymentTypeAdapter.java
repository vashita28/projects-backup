package com.android.cabapp.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.cabapp.R;
import com.android.cabapp.datastruct.json.Card;

public class PaymentTypeAdapter extends BaseAdapter {
	Context context;
	List<Card> cardsList;

	public PaymentTypeAdapter(Context mcontext, List<Card> cardsList) {
		this.context = mcontext;
		this.cardsList = cardsList;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return cardsList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return cardsList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;

		LayoutInflater mInflater = (LayoutInflater) context
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		if (convertView == null) {
			convertView = mInflater
					.inflate(R.layout.row_spinner_item, null);
			holder = new ViewHolder();
			holder.txtType = (TextView) convertView
					.findViewById(R.id.tvContent);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		if (cardsList.get(position).getTruncatedPan() != null
				&& !cardsList.get(position).getTruncatedPan().equals(""))
			holder.txtType.setText(cardsList.get(position).getBrand() + "-"
					+ cardsList.get(position).getTruncatedPan());
		else
			holder.txtType.setText(cardsList.get(position).getBrand());

		return convertView;
	}

	private class ViewHolder {
		TextView txtType;
	}

}
