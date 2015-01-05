package com.upac.upacapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

class GalleryPagerAdapter extends FragmentStatePagerAdapter {
    private static int numPages;
    private static GalleryPageFragment page;

    public GalleryPagerAdapter(FragmentManager fm, int pages, GalleryPageFragment p) {
        super(fm);
        numPages = pages;
        page = p;
    }

    @Override
    public Fragment getItem(int position) {
        return page.create(position);
    }

    @Override
    public int getCount() {
        return numPages;
    }
}
