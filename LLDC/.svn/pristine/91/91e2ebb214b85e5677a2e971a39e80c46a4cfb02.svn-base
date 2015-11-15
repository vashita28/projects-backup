package co.uk.android.lldc.custom;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class CustomTextView_VenueFacilityFont extends TextView {

	public CustomTextView_VenueFacilityFont(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public CustomTextView_VenueFacilityFont(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public CustomTextView_VenueFacilityFont(Context context) {
		super(context);
		init();
	}

	public void init() {
		Typeface tf_bold = Typeface.createFromAsset(getContext().getAssets(),
				"fontello.ttf");
		setTypeface(tf_bold, 0);
		this.setIncludeFontPadding(false);
	}

	// @Override
	// protected void onFocusChanged(boolean focused, int direction,
	// Rect previouslyFocusedRect) {
	// if (focused)
	// super.onFocusChanged(focused, direction, previouslyFocusedRect);
	// }
	//
	// @Override
	// public void onWindowFocusChanged(boolean focused) {
	// if (focused)
	// super.onWindowFocusChanged(focused);
	// }
	//
	// @Override
	// public boolean isFocused() {
	// return true;
	// }

}
