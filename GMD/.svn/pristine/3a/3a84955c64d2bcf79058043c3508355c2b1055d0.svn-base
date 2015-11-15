package co.uk.pocketapp.gmd.util;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import co.uk.pocketapp.gmd.R;

public class CustomEditText extends EditText implements OnClickListener {
	public CustomEditText(Context context) {
		super(context);
		init();
	}

	public CustomEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public CustomEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	void init() {

		CustomEditText.this.setHintTextColor(CustomEditText.this.getResources()
				.getColor(R.color.hint_text_color));
		CustomEditText.this.setTextColor(CustomEditText.this.getResources()
				.getColor(R.color.textview_text_color));
		CustomEditText.this.setBackgroundColor(CustomEditText.this
				.getResources().getColor(R.color.editText_bg_text_color));

		// // if text changes, take care of the button
		// this.addTextChangedListener(new TextWatcher() {
		// @Override
		// public void onTextChanged(CharSequence s, int start, int before,
		// int count) {
		//
		// }
		//
		// @Override
		// public void afterTextChanged(Editable arg0) {
		// }
		//
		// @Override
		// public void beforeTextChanged(CharSequence s, int start, int count,
		// int after) {
		// }
		// });
	}

	// intercept Typeface change and set it with our custom font
	public void setTypeface(Typeface tf, int style) {
		// if (style == Typeface.BOLD) {
		// super.setTypeface(Typeface.createFromAsset(
		// getContext().getAssets(), "fonts/Vegur-B 0.602.otf"));
		// } else {
		// super.setTypeface(Typeface.createFromAsset(
		// getContext().getAssets(), "fonts/Vegur-R 0.602.otf"));
		// }
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		// CustomEditText.this.setFocusable(true);
		// CustomEditText.this.setFocusableInTouchMode(true);
		// CustomEditText.this.requestFocus();
	}

}
