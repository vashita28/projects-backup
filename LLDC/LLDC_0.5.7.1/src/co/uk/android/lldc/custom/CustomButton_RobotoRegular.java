package co.uk.android.lldc.custom;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

public class CustomButton_RobotoRegular extends Button {

	public CustomButton_RobotoRegular(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public CustomButton_RobotoRegular(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public CustomButton_RobotoRegular(Context context) {
		super(context);
		init();
	}

	public void init() {
		Typeface tf_bold = Typeface.createFromAsset(getContext().getAssets(),
				"ROBOTO-REGULAR.TTF");
		setTypeface(tf_bold, 0);
		this.setIncludeFontPadding(false);
	}

}
