package com.handpoint.headstart.android.ui;

import com.handpoint.headstart.R;

import android.content.Context;
import android.preference.PreferenceCategory;
import android.util.AttributeSet;
import android.view.View;

/**
 * 
 * Implementation of progress icon for PreferenceCategory class
 *
 */
public class ProgressCategory extends PreferenceCategory {
	private boolean mProgress = false;

    public ProgressCategory(Context context, AttributeSet attrs) {
        super (context, attrs);
        setLayoutResource(R.layout.preference_progress_category);
    }

    @Override
    public void onBindView(View view) {
        super .onBindView(view);
        View textView = view.findViewById(R.id.scanning_text);
        View progressBar = view.findViewById(R.id.scanning_progress);

        int visibility = mProgress ? View.VISIBLE : View.INVISIBLE;
        textView.setVisibility(visibility);
        progressBar.setVisibility(visibility);
    }

    /**
     * Turn on/off the progress indicator and text on the right.
     * @param progressOn whether or not the progress should be displayed
     */
    public void setProgress(boolean progressOn) {
        if (mProgress != progressOn) {
            mProgress = progressOn;
            notifyChanged();
        }
    }

}
