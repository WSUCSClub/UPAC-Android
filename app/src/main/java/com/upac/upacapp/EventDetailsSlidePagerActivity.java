package com.upac.upacapp;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

public class EventDetailsSlidePagerActivity extends FragmentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        int pages = intent.getIntExtra("AllPages", 0);
        String[] titles = intent.getStringArrayExtra("Titles");
        String[] locations = intent.getStringArrayExtra("Locations");
        String[] dates = intent.getStringArrayExtra("Dates");
        String[] times = intent.getStringArrayExtra("Times");
        String[] descriptions = intent.getStringArrayExtra("Descriptions");
        String[] images = intent.getStringArrayExtra("Images");
        boolean[] hasRaffle = intent.getBooleanArrayExtra("Has_Raffle");
        String[] ids = intent.getStringArrayExtra("IDs");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_slide);
        ViewPager mPager = (ViewPager) findViewById(R.id.pager);

        EventDetailsPageFragment page = new EventDetailsPageFragment();
        page.setInformation(titles, locations, dates, times, descriptions, images, hasRaffle, ids);
        PagerAdapter mPagerAdapter = new EventDetailsPagerAdapter(getSupportFragmentManager(), pages, page);
        mPager.setAdapter(mPagerAdapter);
        mPager.setCurrentItem(intent.getIntExtra("Page", 0));
        mPager.setOffscreenPageLimit(pages);

        View head = getLayoutInflater().inflate(R.layout.head, null);

        ActionBar ab = getActionBar();
        ab.setDisplayShowTitleEnabled(false);
        ab.setDisplayShowHomeEnabled(false);
        ab.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM, ActionBar.DISPLAY_SHOW_CUSTOM);
        ab.setCustomView(head);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}