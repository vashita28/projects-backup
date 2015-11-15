package uk.co.pocketapp.whotel.fragments;

import uk.co.pocketapp.whotel.FragmentLoad;
import uk.co.pocketapp.whotel.R;
import uk.co.pocketapp.whotel.customview.RecyclingImageView;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

@SuppressLint("ValidFragment")
public class HotelInfoFragment extends SherlockFragment {

	ImageView imageViewInfo;
	TextView tvCategory, tvAddress, tvName, tvDesc;
	Button btnBookNow;
	FragmentLoad fragmentLoad;

	String category, name, address, desc, image_URL;
	ProgressBar progress_hotelinfo;

	Typeface font_bold;

	public HotelInfoFragment(FragmentLoad fragmentLoad) {
		// TODO Auto-generated constructor stub
		this.fragmentLoad = fragmentLoad;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Bundle bundleArgs = getArguments();
		if (bundleArgs != null) {
			category = (String) bundleArgs.get("category");
			name = (String) bundleArgs.getString("name");
			address = (String) bundleArgs.getString("address");
			desc = (String) bundleArgs.getString("desc");
			image_URL = (String) bundleArgs.getString("image_url");
		}

		font_bold = Typeface.createFromAsset(getActivity().getAssets(),
				"WSansNew-Bold.otf");

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_hotel_information,
				container, false);

		imageViewInfo = (RecyclingImageView) view
				.findViewById(R.id.imageview_deals_header);

		tvCategory = (TextView) view.findViewById(R.id.textview_category);
		tvAddress = (TextView) view.findViewById(R.id.textview_address);
		tvName = (TextView) view.findViewById(R.id.textview_name);
		tvDesc = (TextView) view.findViewById(R.id.textview_desc);
		progress_hotelinfo = (ProgressBar) view
				.findViewById(R.id.progress_hotelinfo);

		btnBookNow = (Button) view.findViewById(R.id.book_now);
		btnBookNow.setTypeface(font_bold);
		btnBookNow.setIncludeFontPadding(false);
		btnBookNow.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				String szBookNowURL = "www.watlantadowntown.com/wwwwwwwwwww";
				try {
					if (!szBookNowURL.startsWith("https://")
							&& !szBookNowURL.startsWith("http://")) {
						szBookNowURL = "http://" + szBookNowURL;
					}
					Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.setData(Uri.parse(szBookNowURL));
					startActivity(intent);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		return view;
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		tvCategory.setText(category);
		tvAddress.setText(address);
		tvName.setText(name);
		tvDesc.setText(desc);
		ImageLoader.getInstance().displayImage(image_URL, imageViewInfo,
				new SimpleImageLoadingListener() {
					@Override
					public void onLoadingStarted(String imageUri, View view) {
						progress_hotelinfo.setVisibility(View.VISIBLE);
					}

					@Override
					public void onLoadingFailed(String imageUri, View view,
							FailReason failReason) {
						progress_hotelinfo.setVisibility(View.GONE);
					}

					@Override
					public void onLoadingComplete(String imageUri, View view,
							Bitmap loadedImage) {
						progress_hotelinfo.setVisibility(View.GONE);
					}
				});

		fragmentLoad.onHotelInfoFragmentLoad(this);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		fragmentLoad.onHotelInfoFragmentClosed();
	}
}
