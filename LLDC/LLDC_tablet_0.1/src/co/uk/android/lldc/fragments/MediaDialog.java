package co.uk.android.lldc.fragments;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import co.uk.android.lldc.R;
import co.uk.android.lldc.custom.MyGallery;
import co.uk.android.lldc.models.EventMediaModel;

public class MediaDialog extends DialogFragment {

	Button btnAddToCart, btnBuy;
	String mTitle;
	TextView txtTitle, txtClose, txt_product_details;
	EditText txtProductNo;

	public MediaDialog(String title) {
		// TODO Auto-generated constructor stub
		mTitle = title;
		setStyle(DialogFragment.STYLE_NORMAL, R.style.MY_DIALOG);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.dialog_media, container,
				false);
		ArrayList<EventMediaModel> socialList = new ArrayList<EventMediaModel>();

		final MyGallery gallery = (MyGallery) rootView
				.findViewById(R.id.mediaGallery);

		RelativeLayout rlClose = (RelativeLayout) rootView
				.findViewById(R.id.rlClose);
		gallery.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				// ivLeft.setVisibility(View.VISIBLE);
				// ivRight.setVisibility(View.VISIBLE);
				// if(arg2 == adapter.getCount()){
				// ivRight.setVisibility(View.GONE);
				// ivLeft.setVisibility(View.VISIBLE);
				// }else if(arg2 == 0){
				// ivLeft.setVisibility(View.GONE);
				// ivRight.setVisibility(View.VISIBLE);
				// }
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});

		// ivLeft.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View arg0) {
		// // TODO Auto-generated method stub
		// gallery.setSelection(gallery.getSelectedItemPosition() - 1);
		// if(gallery.getSelectedItemPosition() == 0){
		// ivLeft.setVisibility(View.GONE);
		// ivRight.setVisibility(View.VISIBLE);
		// }
		// }
		// });
		//
		// ivRight.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View arg0) {
		// // TODO Auto-generated method stub
		// gallery.setSelection(gallery.getSelectedItemPosition() + 1);
		// if(gallery.getSelectedItemPosition() == adapter.getCount()){
		// ivRight.setVisibility(View.GONE);
		// ivLeft.setVisibility(View.VISIBLE);
		// }
		// }
		// });

		rlClose.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				getDialog().dismiss();
			}
		});
		// dialog.setCanceledOnTouchOutside(false);

		return rootView;
	}

	public void onShowSelectedData() {
	}

}