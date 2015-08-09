package com.upac.upacapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.IOException;
import java.net.URL;

public class DownloadDetailsImages extends AsyncTask<ImageView, Void, Bitmap> {
    private ImageView imageView;
    private URL imageURL;
    private Bitmap mIcon_val;

    public DownloadDetailsImages(final URL iu, final ImageView iv) {
        imageURL = iu;
        imageView = iv;
    }

    @Override
    protected Bitmap doInBackground(final ImageView... imageViews) {
        return this.download_Image();
    }

    @Override
    protected void onPostExecute(final Bitmap result) {
        imageView.setImageBitmap(result);
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