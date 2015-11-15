package uk.co.pocketapp.whotel.customview;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Pramod on 9/5/13.
 */
public class CustomTextView_italicfont extends TextView {

	public CustomTextView_italicfont(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public CustomTextView_italicfont(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public CustomTextView_italicfont(Context context) {
		super(context);
		init();
	}

	public void init() {
		Typeface tf_italic = Typeface.createFromAsset(getContext().getAssets(),
				"WSansNew-BoldItalic.otf");
		setTypeface(tf_italic, 2);
		// this.setIncludeFontPadding(false);
	}

}
