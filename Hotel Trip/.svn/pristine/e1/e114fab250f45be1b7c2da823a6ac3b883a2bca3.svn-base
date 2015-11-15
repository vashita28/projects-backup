package com.hoteltrip.android.customview;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Pramod on 1/20/14.
 */
public class HelveticaNeueBoldTextView extends TextView {

	public HelveticaNeueBoldTextView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public HelveticaNeueBoldTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public HelveticaNeueBoldTextView(Context context) {
		super(context);
		init();
	}

	public void init() {
		Typeface tf_book = Typeface.createFromAsset(getContext().getAssets(),
				"fonts/HelveticaNeueBold.ttf");
		setTypeface(tf_book, 0);
		this.setIncludeFontPadding(false);
	}

}
