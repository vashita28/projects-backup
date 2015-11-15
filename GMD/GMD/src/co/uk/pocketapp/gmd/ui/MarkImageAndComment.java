package co.uk.pocketapp.gmd.ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.view.Display;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import co.uk.pocketapp.gmd.R;
import co.uk.pocketapp.gmd.util.AppValues;

public class MarkImageAndComment extends Activity {

	LinearLayout ll_watermark;
	Button btn_save;

	int w, h;

	float x, y;
	Canvas canvas;
	Rect clipBounds_canvas;

	Point point = new Point();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_markimage_and_comment);

		ll_watermark = (LinearLayout) findViewById(R.id.ll_watermark);
		btn_save = (Button) findViewById(R.id.btn_save);

		final Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE))
				.getDefaultDisplay();

		w = display.getWidth();
		h = display.getHeight();

		ll_watermark.addView(new TouchView(this));

		btn_save.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				save();
			}
		});
	}

	// public static Bitmap mark(Bitmap src, String watermark, Point location,
	// Color color, int alpha, int size, boolean underline) {
	// int w = src.getWidth();
	// int h = src.getHeight();
	// Bitmap result = Bitmap.createBitmap(w, h, src.getConfig());
	//
	// Canvas canvas = new Canvas(result);
	// canvas.drawBitmap(src, 0, 0, null);
	//
	// Paint paint = new Paint();
	// paint.setColor(color);
	// paint.setAlpha(alpha);
	// paint.setTextSize(size);
	// paint.setAntiAlias(true);
	// paint.setUnderlineText(underline);
	// canvas.drawText(watermark, location.x, location.y, paint);
	//
	// return result;
	// }

	public static Bitmap mark(Bitmap src, String watermark) {
		int w = src.getWidth();
		int h = src.getHeight();
		Bitmap result = Bitmap.createBitmap(w, h, src.getConfig());
		Canvas canvas = new Canvas(result);
		canvas.drawBitmap(src, 0, 0, null);
		Paint paint = new Paint();
		paint.setColor(Color.RED);
		paint.setTextSize(18);
		paint.setAntiAlias(true);
		paint.setUnderlineText(true);
		canvas.drawText(watermark, 20, 25, paint);

		return result;
	}

	class TouchView extends View {
		Bitmap overlayDefault;
		Bitmap overlay;
		Paint pTouch;
		int X = -100;
		int Y = -100;
		Canvas c2;

		private int mWidth;
		private int mHeight;

		private float MIN_ZOOM = 1f;
		private float MAX_ZOOM = 5f;

		private float scaleFactor = 1.f;
		private ScaleGestureDetector detector;

		// List<Point> points = new ArrayList<Point>();

		@Override
		protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
			mWidth = View.MeasureSpec.getSize(widthMeasureSpec);
			mHeight = View.MeasureSpec.getSize(heightMeasureSpec);

			setMeasuredDimension(mWidth, mHeight);
		}

		public TouchView(Context context) {
			super(context);
			Bitmap bmpDrawable = ((BitmapDrawable) getResources().getDrawable(
					R.drawable.sachin)).getBitmap();

			detector = new ScaleGestureDetector(getContext(),
					new ScaleListener());

			overlayDefault = Bitmap
					.createScaledBitmap(bmpDrawable, w, h, false);
			overlay = overlayDefault.copy(Config.ARGB_8888, true);
			canvas = new Canvas(overlay);

			pTouch = new Paint();
			pTouch.setColor(Color.RED);
			pTouch.setTextSize(18);
			pTouch.setAntiAlias(true);
			pTouch.setUnderlineText(true);
		}

		@Override
		public boolean onTouchEvent(MotionEvent ev) {

			switch (ev.getAction()) {

			case MotionEvent.ACTION_DOWN: {

				// X = (int) ev.getX();
				// Y = (int) ev.getY();

				X = (int) (ev.getX() / scaleFactor + clipBounds_canvas.left);
				Y = (int) (ev.getY() / scaleFactor + clipBounds_canvas.top);

				// Point point = new Point();
				point.x = X;
				point.y = Y;
				// points.add(point);

				invalidate();

				break;
			}

			case MotionEvent.ACTION_MOVE: {

				// X = (int) ev.getX();
				// Y = (int) ev.getY();

				X = (int) (ev.getX() / scaleFactor + clipBounds_canvas.left);
				Y = (int) (ev.getY() / scaleFactor + clipBounds_canvas.top);

				Point point = new Point();
				point.x = X;
				point.y = Y;
				// points.add(point);

				invalidate();
				break;

			}

			case MotionEvent.ACTION_UP:

				break;

			}
			detector.onTouchEvent(ev);
			return true;

		}

		@Override
		public void onDraw(Canvas canvas) {
			super.onDraw(canvas);

			clipBounds_canvas = canvas.getClipBounds();

			canvas.save();
			canvas.scale(scaleFactor, scaleFactor);

			int cx = (mWidth - overlay.getWidth()) / 2;
			int cy = (mHeight - overlay.getHeight()) / 2;

			// draw background
			canvas.drawBitmap(overlay, cx, cy, null);

			Bitmap bmpDrawable = ((BitmapDrawable) getResources().getDrawable(
					R.drawable.pointer)).getBitmap();

			// for (Point point : points) {
			// canvas.drawCircle(point.x, point.y, 40, pTouch);
			canvas.drawBitmap(bmpDrawable, point.x, point.y, null);
			canvas.drawText("DESCRIPTION", point.x + 50, point.y + 50, pTouch);
			// }

			canvas.restore();

		}

		private class ScaleListener extends
				ScaleGestureDetector.SimpleOnScaleGestureListener {
			@Override
			public boolean onScale(ScaleGestureDetector detector) {
				scaleFactor *= detector.getScaleFactor();
				scaleFactor = Math.max(MIN_ZOOM,
						Math.min(scaleFactor, MAX_ZOOM));
				invalidate();
				return true;
			}
		}

	}

	public class Point {

		float x, y;
	}

	private void save() {
		// TODO Auto-generated method stub
		Bitmap toDisk = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		canvas.setBitmap(toDisk);

		/* code... */

		File dir = new File(AppValues.getDirectory() + File.separator
				+ AppValues.APP_NAME);
		dir.mkdir();

		File saveFile = new File(dir, "GMD_watermark.png");
		try {
			saveFile.createNewFile();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
			toDisk.compress(Bitmap.CompressFormat.JPEG, 100,
					new FileOutputStream(saveFile));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/* code... */
	}
}
