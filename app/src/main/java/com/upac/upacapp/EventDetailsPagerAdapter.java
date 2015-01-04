package com.upac.upacapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

class EventDetailsPagerAdapter extends FragmentStatePagerAdapter {
    private static int numPages;
    private static EventDetailsPageFragment page;

    public EventDetailsPagerAdapter(FragmentManager fm, int pages, EventDetailsPageFragment p) {
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