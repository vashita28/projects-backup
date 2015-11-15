package co.uk.android.lldc.custom;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by manpreet on 3/17/15.
 */
public class CustomImageView extends ImageView {



    private int angleInDegree=6;

    public CustomImageView(Context ctx, AttributeSet attrs) {
        super(ctx, attrs);
    }



    @Override
    protected void onDraw(Canvas canvas) {

        Drawable drawable = getDrawable();

        if (drawable == null) {
            return;
        }

        if (getWidth() == 0 || getHeight() == 0) {
            return;
        }
        Bitmap b = ((BitmapDrawable) drawable).getBitmap();
        Bitmap bitmap = b.copy(Bitmap.Config.ARGB_8888, true);


        int w = getWidth(), h = getHeight();

        Bitmap bitmapWithTriangle = getBitmapWithTriangle(bitmap, w, h);
        canvas.drawBitmap(bitmapWithTriangle, 0, 0, null);

    }

    private Bitmap getBitmapWithTriangle(Bitmap bitmap, int w, int h) {


        double ratio=h/w;
         Bitmap finalBitmap = Bitmap.createScaledBitmap(bitmap, getResources().getDisplayMetrics().widthPixels,(int) (getResources().getDisplayMetrics().widthPixels*ratio),
                false);

        h= finalBitmap.getHeight();
        w= finalBitmap.getWidth();

        Canvas canvas = new Canvas(finalBitmap);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        final Rect rect = new Rect(0, 0, finalBitmap.getWidth(),
                finalBitmap.getHeight());

        Point point1_draw = new Point(0, h);
        Point point2_draw = new Point( w,h-(int)(Math.tan(Math.toRadians(angleInDegree))*w));
        Point point3_draw = new Point(w, h);

        Path path = new Path();

        path.moveTo(point1_draw.x,point1_draw.y);

        path.lineTo(point2_draw.x,point2_draw.y);

        path.lineTo(point3_draw.x,point3_draw.y);
      //  path.lineTo(point1_draw.x,point1_draw.y);

        path.close();

        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(Color.parseColor("#FFFFFF"));
        canvas.drawPath(path, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(finalBitmap, rect, rect, paint);


        return finalBitmap;




    }

    public int getAngleInDegree() {
        return angleInDegree;
    }

    public void setAngleInDegree(int angleInDegree) {
        this.angleInDegree = angleInDegree;
    }


}
