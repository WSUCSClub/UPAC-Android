package com.upac.upacapp;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.view.View;

public class GalleryClickListener implements View.OnClickListener {
    private static FragmentActivity context;
    private static int pages;
    private int openedPage;
    private static String[] imageURL;

    public GalleryClickListener(final FragmentActivity fa) {
        context = fa;
    }

    public void setOpenedImage(final int p) {
        openedPage = p;
    }

    public void setPageAmount(final int p) {
        pages = p;
    }

    public void setInformation(final String[] img) {
        imageURL = img;
    }

    @Override
    public void onClick(final View v) {
        final Intent intent = new Intent(context, GallerySlidePagerActivity.class);
        intent.putExtra("AllPages", pages);
        intent.putExtra("Page", openedPage);
        intent.putExtra("ImageURLs", imageURL);
        context.startActivity(intent);
    }
}