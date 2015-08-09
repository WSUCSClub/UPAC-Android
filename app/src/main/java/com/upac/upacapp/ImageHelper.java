package com.upac.upacapp;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;

public class ImageHelper {
    private static final int EVENT_HEIGHT = 720;
    private static final int EVENT_WIDTH = 720;
    private static final int ABOUT_HEIGHT = 120;
    private static final int ABOUT_WIDTH = 120;

    public Bitmap getRoundedEventImage(final Bitmap bitmap, final int pixels) {
        final Bitmap output = Bitmap.createBitmap(EVENT_WIDTH, EVENT_HEIGHT, Config.ARGB_8888);
        final Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, EVENT_HEIGHT, EVENT_WIDTH);
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    public Bitmap getRoundedAboutImage(final Bitmap bitmap, final int pixels) {
        final Bitmap output = Bitmap.createBitmap(ABOUT_WIDTH, ABOUT_HEIGHT, Config.ARGB_8888);
        final Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, ABOUT_HEIGHT, ABOUT_WIDTH);
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    public Bitmap getResizedBitmap(final Bitmap bm, final int newHeight, final int newWidth) {
        final int width = bm.getWidth();
        final int height = bm.getHeight();
        final float scaleWidth = ((float) newWidth) / width;
        final float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        final Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        final Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
        return resizedBitmap;
    }
}