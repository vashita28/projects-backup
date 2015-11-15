package uk.co.pocketapp.whotel.customview;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class CustomTextView_boldfont extends TextView {

	public CustomTextView_boldfont(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public CustomTextView_boldfont(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public CustomTextView_boldfont(Context context) {
		super(context);
		init();
	}

	public void init() {
		Typeface tf_bold = Typeface.createFromAsset(getContext().getAssets(),
				"WSansNew-Bold.otf");
        setTypeface(tf_bold, 0);
        this.setIncludeFontPadding(false);
	}

}
