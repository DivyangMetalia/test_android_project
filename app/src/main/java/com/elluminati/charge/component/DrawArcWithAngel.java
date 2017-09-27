package com.elluminati.charge.component;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;

import com.elluminati.charge.R;

/**
 * Created by elluminati on 20-Sep-17.
 */

public class DrawArcWithAngel {
    private Bitmap bitmapMain;
    private Drawable drawable;
    private Context context;
    private Canvas canvasMain;

    public DrawArcWithAngel(Drawable drawable, Context context) {
        this.drawable = drawable;
        this.context = context;
        if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmapMain = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap
            // will be created of 1x1 pixel
        } else {
            bitmapMain = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable
                    .getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }
        Canvas canvas = new Canvas(bitmapMain);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        canvasMain = new Canvas(bitmapMain);
    }


    public Bitmap getBitmapOverly() {

        return bitmapMain;
    }

    public void addBitmap(int startAngel, int sweepAngel, int color, boolean isAddCap) {

        Bitmap bitmap;
        if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable
                    .getIntrinsicHeight(), Bitmap.Config.ARGB_8888);

        }
        Canvas canvas = new Canvas(bitmap);
        float width = (float) drawable.getIntrinsicWidth();
        float height = (float) drawable.getIntrinsicHeight();

        Paint paint = new Paint();
        paint.setColor(ResourcesCompat.getColor(context.getResources(), color, null));
        paint.setStrokeWidth(context.getResources().getDimensionPixelSize(R.dimen
                .arc_stroke));
        paint.setAntiAlias(true);
        if (isAddCap) {
            paint.setStrokeCap(Paint.Cap.ROUND);
        }

        final RectF oval = new RectF();
        paint.setStyle(Paint.Style.STROKE);
        float padding = width * 0.15f;
        oval.set(padding,
                padding,
                width - padding,
                width - padding);
        canvas.drawArc(oval, startAngel, sweepAngel, false, paint);

        Paint paint2 = new Paint(Paint.FILTER_BITMAP_FLAG);
        canvasMain.drawBitmap(bitmap, 0, 0, paint2);
    }


    public void drawText(String count, int color) {
        Bitmap bitmap;
        if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable
                    .getIntrinsicHeight(), Bitmap.Config.ARGB_8888);

        }
        Canvas canvas = new Canvas(bitmap);
        float width = (float) drawable.getIntrinsicWidth();
        float height = (float) drawable.getIntrinsicHeight();

        Paint paint = new Paint();
        paint.setColor(ResourcesCompat.getColor(context.getResources(), color, null));
        paint.setTextSize(context.getResources().getDimensionPixelSize(R.dimen
                .arc_text));
        paint.setTypeface(Typeface.DEFAULT_BOLD);
        paint.setAntiAlias(true);
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(count, width / 2, height / 2, paint);
        Paint paint2 = new Paint(Paint.FILTER_BITMAP_FLAG);
        canvasMain.drawBitmap(bitmap, 0, 0, paint2);

    }
}
