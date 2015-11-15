package com.example.cabapppassenger.customview;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Pramod on 15/03/14.
 */
public class HelveticaNeueTextView extends TextView {

	public HelveticaNeueTextView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public HelveticaNeueTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public HelveticaNeueTextView(Context context) {
		super(context);
		init();
	}

	public void init() {
		Typeface tf_book = Typeface.createFromAsset(getContext().getAssets(),
				"fonts/HelveticaNeue.ttf");
		setTypeface(tf_book, 0);
		this.setIncludeFontPadding(false);
	}

}
