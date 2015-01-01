package com.upac.upacapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

public class EventDetailsSlidePagerActivity extends FragmentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        int pages = intent.getIntExtra("AllPages", 0);
        String[] titles = intent.getStringArrayExtra("Titles");
        String[] locations = intent.getStringArrayExtra("Locations");
        String[] times = intent.getStringArrayExtra("Times");
        String[] descriptions = intent.getStringArrayExtra("Descriptions");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_slide);
        ViewPager mPager = (ViewPager) findViewById(R.id.pager);

        EventDetailsPageFragment page = new EventDetailsPageFragment();
        page.setInformation(titles, locations, times, descriptions);
        PagerAdapter mPagerAdapter = new EventDetailsPagerAdapter(getSupportFragmentManager(), pages, page);
        mPager.setAdapter(mPagerAdapter);
        mPager.setCurrentItem(intent.getIntExtra("Page", 0));
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}