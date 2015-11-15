package com.android.cabapp.view;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.EditText;

public class CustomEditText extends EditText {

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

		// if text changes, take care of the button
		this.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

			}

			@Override
			public void afterTextChanged(Editable et) {

				String value = et.toString();
				if (!value.equals(value.toUpperCase())) {
					value = value.toUpperCase();
					CustomEditText.this.setText(value);
					CustomEditText.this.setSelection(et.length());
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
		});
	}

}