package com.example.cabapppassenger.customview;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class HelveticaNeueUltraLightTextView extends TextView {

	public HelveticaNeueUltraLightTextView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public HelveticaNeueUltraLightTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public HelveticaNeueUltraLightTextView(Context context) {
		super(context);
		init();
	}

	public void init() {
		Typeface tf_book = Typeface.createFromAsset(getContext().getAssets(),
				"fonts/HelveticaNeueUltraLight.ttf");
		setTypeface(tf_book, 0);
		this.setIncludeFontPadding(false);
	}

}