package co.uk.android.lldc.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Gallery;

@SuppressWarnings("deprecation")
public class MyGallery extends Gallery {
	public MyGallery(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public MyGallery(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MyGallery(Context context) {
		super(context);
	}

	// ---------------> to show one image at a time

	@SuppressWarnings("unused")
	private boolean isScrollingLeft(MotionEvent e1, MotionEvent e2) {
		return e2.getX() > e1.getX();
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// int kEvent;
		// if (isScrollingLeft(e1, e2)) { // Check if scrolling left
		// kEvent = KeyEvent.KEYCODE_DPAD_LEFT;
		// } else { // Otherwise scrolling right
		// kEvent = KeyEvent.KEYCODE_DPAD_RIGHT;
		// }
		// onKeyDown(kEvent, null);
		return true;
	}
}
