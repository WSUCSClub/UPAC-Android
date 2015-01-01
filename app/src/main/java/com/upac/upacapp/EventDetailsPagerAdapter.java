package com.upac.upacapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

class EventDetailsPagerAdapter extends FragmentStatePagerAdapter {
    private int numPages;
    private EventDetailsPageFragment page = new EventDetailsPageFragment();

    public EventDetailsPagerAdapter(FragmentManager fm, int pages) {
        super(fm);
        numPages = pages;
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
