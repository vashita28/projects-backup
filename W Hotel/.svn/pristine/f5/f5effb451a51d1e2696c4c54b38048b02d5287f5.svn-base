package uk.co.pocketapp.whotel.customview;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Pramod on 9/5/13.
 */
public class CustomTextView_bookfont extends TextView {

    public CustomTextView_bookfont(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public CustomTextView_bookfont(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomTextView_bookfont(Context context) {
        super(context);
        init();
    }

    public void init() {
        Typeface tf_book = Typeface.createFromAsset(getContext().getAssets(),
                "WSansNew-Book.otf");
        setTypeface(tf_book, 0);
        this.setIncludeFontPadding(false);
    }

}
