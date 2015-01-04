package com.upac.upacapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.net.URL;

public class DownloadEventImages extends AsyncTask<ImageView, Void, Bitmap> {
    ImageView imageView;
    URL imageURL;
    Bitmap mIcon_val;
    private static final int ROUNDING = 1000;

    public DownloadEventImages(URL iu, ImageView iv) {
        imageURL = iu;
        imageView = iv;
    }

    @Override
    protected Bitmap doInBackground(ImageView... imageViews) {
        return download_Image();
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        ImageHelper ih = new ImageHelper();

        result = ih.getRoundedEventImage(result, ROUNDING);

        imageView.setImageBitmap(result);
    }

    private Bitmap download_Image() {
        try {
            mIcon_val = BitmapFactory.decodeStream(imageURL.openConnection().getInputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mIcon_val;
    }
}