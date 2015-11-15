package com.android.cabapp.view;

import com.android.cabapp.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PorterDuffXfermode;
import android.graphics.PorterDuff.Mode;
import android.util.AttributeSet;
import android.view.View;

public class OverlayCircle extends View {

	private float mCentreX, mCentreY, mRadius, mXDistance, mYDistance;
	
	

	public OverlayCircle(Context context, AttributeSet attrs) {
		super(context, attrs);
		/*mRadius = getResources().getDisplayMetrics().heightPixels / 8;
		mYDistance = mRadius / 4;
		mXDistance = mRadius / 3;
		mCentreX = getResources().getDisplayMetrics().widthPixels - mXDistance;
		mCentreY = mXDistance + mRadius;
*/
	}
	
	
	public  void setRadius(float radius)
	{
		mRadius=radius;
	}
	
	public  void setXDistance(float xDistance)
	{
		mXDistance=xDistance;
	}
	
	public  void setYDistance(float yDistance)
	{
		mYDistance=yDistance;
	}
	
	
	
	@Override
	protected void onDraw(Canvas canvas) {
		Bitmap bitmap = Bitmap.createBitmap(canvas.getWidth(),
				canvas.getHeight(), Bitmap.Config.ARGB_8888);
		Canvas temp = new Canvas(bitmap);
	   
		Paint rectanglePaint = new Paint();
		Paint transparentPaint = new Paint();
		rectanglePaint.setColor(getResources().getColor(R.color.overlay_bg));
		// rectanglePaint.setColor(0xcc000000);
		temp.drawRect(0, 0, getResources().getDisplayMetrics().widthPixels,
				mRadius + mYDistance, rectanglePaint);
		
		
		
/*
		mCentreX =  mXDistance;
		mCentreY = mXDistance + mRadius;*/
		transparentPaint.setXfermode(new PorterDuffXfermode(Mode.CLEAR));
		transparentPaint.setColor(getResources().getColor(
				android.R.color.transparent));
		transparentPaint.setAntiAlias(true);
		
		

		temp.drawCircle(mXDistance, mYDistance, mRadius, transparentPaint);
		canvas.drawBitmap(bitmap, 0, 0, new Paint());
		super.onDraw(canvas);
	}

}
