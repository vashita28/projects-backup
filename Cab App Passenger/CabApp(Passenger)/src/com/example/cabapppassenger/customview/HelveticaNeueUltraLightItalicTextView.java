package com.example.cabapppassenger.customview;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Malik on 15/03/14.
 */
public class HelveticaNeueUltraLightItalicTextView extends TextView {

	public HelveticaNeueUltraLightItalicTextView(Context context,
			AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public HelveticaNeueUltraLightItalicTextView(Context context,
			AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public HelveticaNeueUltraLightItalicTextView(Context context) {
		super(context);
		init();
	}

	public void init() {
		Typeface tf_book = Typeface.createFromAsset(getContext().getAssets(),
				"fonts/HelveticaNeueUltraLightItalic.ttf");
		setTypeface(tf_book, 0);
		this.setIncludeFontPadding(false);
	}

}