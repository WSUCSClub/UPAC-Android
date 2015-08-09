package com.upac.upacapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.IOException;
import java.net.URL;

public class DownloadAboutImages extends AsyncTask<ImageView, Void, Bitmap> {
    private ImageView imageView;
    private URL imageURL;
    private Bitmap mIcon_val;
    private static final int ROUNDING = 1000;

    public DownloadAboutImages(final URL iu, final ImageView iv) {
        imageURL = iu;
        imageView = iv;
    }

    @Override
    protected Bitmap doInBackground(final ImageView... imageViews) {
        return this.download_Image();
    }

    @Override
    protected void onPostExecute(final Bitmap result) {
        final ImageHelper ih = new ImageHelper();
        Bitmap returnResult = result;

        returnResult = ih.getRoundedAboutImage(returnResult, ROUNDING);

        imageView.setImageBitmap(returnResult);
    }

    private Bitmap download_Image() {
        try {
            mIcon_val = BitmapFactory.decodeStream(imageURL.openConnection().getInputStream());
        } catch (final IOException e) {
            e.printStackTrace();
        }

        return mIcon_val;
    }
}