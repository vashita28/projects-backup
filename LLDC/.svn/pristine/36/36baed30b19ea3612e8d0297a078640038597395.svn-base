package co.uk.android.lldc.custom;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.AutoCompleteTextView;

public class CustomAutoCompleteView extends AutoCompleteTextView {

	public CustomAutoCompleteView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public CustomAutoCompleteView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public CustomAutoCompleteView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	// this is how to disable AutoCompleteTextView filter
	@Override
	protected void performFiltering(final CharSequence text, final int keyCode) {

		Typeface tf_bold = Typeface.createFromAsset(getContext().getAssets(),
				"ROBOTO-BOLD.TTF");
		setTypeface(tf_bold, 0);
		this.setIncludeFontPadding(false);

		String filterText = "";
		super.performFiltering(filterText, keyCode);
	}
}
