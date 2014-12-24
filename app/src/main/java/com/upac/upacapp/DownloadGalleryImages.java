package com.upac.upacapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.net.URL;

public class DownloadGalleryImages extends AsyncTask<ImageView, Void, Bitmap> {
    ImageView imageView;
    URL imageURL;
    Bitmap mIcon_val;

    public DownloadGalleryImages(URL iu, ImageView iv){
        imageURL = iu;
        imageView = iv;
    }

    @Override
    protected Bitmap doInBackground(ImageView... imageViews) {
        return download_Image();
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        imageView.setImageBitmap(result);
    }

    private Bitmap download_Image() {
        try {
            mIcon_val = BitmapFactory.decodeStream(imageURL.openConnection().getInputStream());
        }
        catch(Exception e){
            e.printStackTrace();
        }

        return mIcon_val;
    }
}