package com.upac.upacapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class GalleryPagerAdapter extends FragmentStatePagerAdapter {
    private static int numPages;
    private static GalleryPageFragment page;

    public GalleryPagerAdapter(final FragmentManager fm, final int pages, final GalleryPageFragment p) {
        super(fm);
        numPages = pages;
        page = p;
    }

    @Override
    public Fragment getItem(final int position) {
        return page.create(position);
    }

    @Override
    public int getCount() {
        return numPages;
    }
}
