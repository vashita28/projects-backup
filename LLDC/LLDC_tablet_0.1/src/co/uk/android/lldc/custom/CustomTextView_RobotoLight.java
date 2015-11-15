package co.uk.android.lldc.custom;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class CustomTextView_RobotoLight extends TextView {

	public CustomTextView_RobotoLight(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public CustomTextView_RobotoLight(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public CustomTextView_RobotoLight(Context context) {
		super(context);
		init();
	}

	public void init() {
		Typeface tf_bold = Typeface.createFromAsset(getContext().getAssets(),
				"ROBOTO-LIGHT.TTF");
		setTypeface(tf_bold, 0);
		this.setIncludeFontPadding(false);
	}

}
