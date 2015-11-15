package co.uk.android.lldc.adapters;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import co.uk.android.lldc.R;
import co.uk.android.lldc.models.MediaModel;
import co.uk.android.lldc.models.MediaModelAsymm;
import co.uk.android.lldc.tablet.LLDCApplication;

import com.felipecsl.asymmetricgridview.library.widget.AsymmetricGridView;
import com.felipecsl.asymmetricgridview.library.widget.AsymmetricGridViewAdapter;
import com.nostra13.universalimageloader.core.ImageLoader;

public class MediaAsymmetricListAdapter extends
		AsymmetricGridViewAdapter<MediaModelAsymm> {

	ArrayList<MediaModel> mediaList;

	public MediaAsymmetricListAdapter(final Context context,
			final AsymmetricGridView listView, final List<MediaModelAsymm> items) {
		super(context, listView, items);
	}

	@Override
	public View getActualView(final int position, final View convertView,
			final ViewGroup parent) {
		ImageView v;
		mediaList = LLDCApplication.selectedModel.getMediaList();
		MediaModelAsymm item = getItem(position);

		if (convertView == null) {
			v = new ImageView(context);
			v.setPadding(3, 3, 3, 3);
			v.setBackgroundColor(Color.parseColor("#ffffff"));
			v.setScaleType(ImageView.ScaleType.CENTER_CROP);
			v.setImageResource(R.drawable.qeop_placeholder);
			// R.drawable.text_view_background_selector));

			v.setId(item.getPosition());
		} else
			v = (ImageView) convertView;

		try {
			String szImageUrl = mediaList.get(position).getImageUrl();
			// Log.e("MediaAsymmetricListAdapter", "szImageUrl::> " +
			// szImageUrl);

			ImageLoader.getInstance().displayImage(szImageUrl, v);

			// v.setImageUrl(szImageUrl, ImageCacheManager.getInstance()
			// .getImageLoader());
			// v.setDefaultImageResId(R.drawable.qeop_placeholder);
			// v.setErrorImageResId(R.drawable.imgnt_placeholder);
			// v.setAdjustViewBounds(true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return v;
	}

}