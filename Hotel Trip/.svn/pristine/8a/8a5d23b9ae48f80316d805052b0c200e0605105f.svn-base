package com.hoteltrip.android.customview;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class HelveticaNeueCondensedBoldTextView  extends TextView {

		public HelveticaNeueCondensedBoldTextView(Context context, AttributeSet attrs,
				int defStyle) {
			super(context, attrs, defStyle);
			init();
		}

		public HelveticaNeueCondensedBoldTextView(Context context, AttributeSet attrs) {
			super(context, attrs);
			init();
		}

		public HelveticaNeueCondensedBoldTextView(Context context) {
			super(context);
			init();
		}

		public void init() {
			Typeface tf_book = Typeface.createFromAsset(getContext().getAssets(),
					"fonts/HelveticaNeueCondensedBold.ttf");
			setTypeface(tf_book, 0);
			this.setIncludeFontPadding(false);
		}

	}