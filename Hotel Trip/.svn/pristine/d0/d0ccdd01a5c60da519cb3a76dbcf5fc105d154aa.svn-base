package com.hoteltrip.android.customview;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Malik on 1/21/14.
 */
public class HelveticaNeueMediumTextView extends TextView {

	public HelveticaNeueMediumTextView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public HelveticaNeueMediumTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public HelveticaNeueMediumTextView(Context context) {
		super(context);
		init();
	}

	public void init() {
		Typeface tf_book = Typeface.createFromAsset(getContext().getAssets(),
				"fonts/HelveticaNeueMedium.ttf");
		setTypeface(tf_book, 0);
		this.setIncludeFontPadding(false);
	}

}