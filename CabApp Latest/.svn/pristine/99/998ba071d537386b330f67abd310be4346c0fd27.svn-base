package com.android.cabapp.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.View;

public class OverlayRectangle extends View {

	private float mRadius, mYDistance;

	public OverlayRectangle(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		Bitmap bitmap = Bitmap.createBitmap(canvas.getWidth(),
				canvas.getHeight(), Bitmap.Config.ARGB_8888);
		Canvas temp = new Canvas(bitmap);

		Paint rectanglePaint = new Paint();
		rectanglePaint.setXfermode(new PorterDuffXfermode(Mode.CLEAR));
		rectanglePaint.setColor(getResources().getColor(
				android.R.color.transparent));
		rectanglePaint.setAntiAlias(true);
		temp.drawRect(0, 0, getResources().getDisplayMetrics().widthPixels,
				mRadius + mYDistance, rectanglePaint);

		canvas.drawBitmap(bitmap, 0, 0, new Paint());
		super.onDraw(canvas);
	}

}
