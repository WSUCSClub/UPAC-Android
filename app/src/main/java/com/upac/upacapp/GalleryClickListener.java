package com.upac.upacapp;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.view.View;

public class GalleryClickListener implements View.OnClickListener {
    private static FragmentActivity context;
    private static int pages;
    private int openedPage;
    private static String[] imageURL;

    public GalleryClickListener(FragmentActivity fa) {
        context = fa;
    }

    public void setOpenedImage(int p) {
        openedPage = p;
    }

    public void setPageAmount(int p) {
        pages = p;
    }

    public void setInformation(String[] img) {
        imageURL = img;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(context, GallerySlidePagerActivity.class);
        intent.putExtra("AllPages", pages);
        intent.putExtra("Page", openedPage);
        intent.putExtra("ImageURLs", imageURL);
        context.startActivity(intent);
    }
}