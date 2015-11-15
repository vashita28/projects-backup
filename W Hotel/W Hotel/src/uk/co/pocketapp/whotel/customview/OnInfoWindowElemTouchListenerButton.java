package uk.co.pocketapp.whotel.customview;

import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import com.google.android.gms.maps.model.Marker;

public abstract class OnInfoWindowElemTouchListenerButton implements
		OnTouchListener {
	private final View view;
	private final Drawable bgDrawableNormal;
	private final Drawable bgDrawablePressed;
	private final Handler handler = new Handler();

	private Marker marker;
	private boolean pressed = false;

	private String category;
	private String address;
	private String name;
	private String desc;
	private String page_url;

	public OnInfoWindowElemTouchListenerButton(View view,
			Drawable bgDrawableNormal, Drawable bgDrawablePressed) {
		this.view = view;
		this.bgDrawableNormal = bgDrawableNormal;
		this.bgDrawablePressed = bgDrawablePressed;
	}

	public void setMarker(Marker marker) {
		this.marker = marker;
	}

	public void setMarkerDetails(String category, String address, String name,
			String desc, String page_url) {
		this.category = category;
		this.address = address;
		this.name = name;
		this.desc = desc;
		this.page_url = page_url;
		// Log.d("OnInfoWindowElemTouchListenerButton", "setMarkerDetails"
		// + category + " ," + name + " ," + desc);
	}

	@Override
	public boolean onTouch(View vv, MotionEvent event) {
		if (0 <= event.getX() && event.getX() <= view.getWidth()
				&& 0 <= event.getY() && event.getY() <= view.getHeight()) {
			switch (event.getActionMasked()) {
			case MotionEvent.ACTION_DOWN:
				startPress();
				break;

			// We need to delay releasing of the view a little so it shows the
			// pressed state on the screen
			case MotionEvent.ACTION_UP:
				handler.postDelayed(confirmClickRunnable, 150);
				break;

			case MotionEvent.ACTION_CANCEL:
				endPress();
				break;
			default:
				break;
			}
		} else {
			// If the touch goes outside of the view's area
			// (like when moving finger out of the pressed button)
			// just release the press
			endPress();
		}
		return false;
	}

	private void startPress() {
		if (!pressed) {
			pressed = true;
			handler.removeCallbacks(confirmClickRunnable);
			// try {
			// view.setBackground(bgDrawablePressed);
			// } catch (Exception e) {
			view.setBackgroundDrawable(bgDrawablePressed);
			// }
			if (marker != null)
				marker.showInfoWindow();
		}
	}

	private boolean endPress() {
		if (pressed) {
			this.pressed = false;
			handler.removeCallbacks(confirmClickRunnable);
			// try {
			// view.setBackground(bgDrawableNormal);
			// } catch (Exception e) {
			view.setBackgroundDrawable(bgDrawableNormal);
			// }
			if (marker != null)
				marker.showInfoWindow();
			return true;
		} else
			return false;
	}

	private final Runnable confirmClickRunnable = new Runnable() {
		public void run() {
			if (endPress()) {
				onClickConfirmed(view, marker, category, address, name, desc,
						page_url);
			}
		}
	};

	/**
	 * This is called after a successful click
	 */
	protected abstract void onClickConfirmed(View v, Marker marker,
			String category, String address, String name, String desc,
			String page_url);
}