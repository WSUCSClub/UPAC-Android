package com.upac.upacapp;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

public class EventDetailsSlidePagerActivity extends FragmentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        int pages = intent.getIntExtra("AllPages", 0);
        String[] titles = intent.getStringArrayExtra("Titles");
        String[] locations = intent.getStringArrayExtra("Locations");
        String[] times = intent.getStringArrayExtra("Times");
        String[] descriptions = intent.getStringArrayExtra("Descriptions");
        String[] images = intent.getStringArrayExtra("Images");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_slide);
        ViewPager mPager = (ViewPager) findViewById(R.id.pager);

        EventDetailsPageFragment page = new EventDetailsPageFragment();
        page.setInformation(titles, locations, times, descriptions, images);
        PagerAdapter mPagerAdapter = new EventDetailsPagerAdapter(getSupportFragmentManager(), pages, page);
        mPager.setAdapter(mPagerAdapter);
        mPager.setCurrentItem(intent.getIntExtra("Page", 0));

        View head = getLayoutInflater().inflate(R.layout.head, null);

        ActionBar ab = getActionBar();
        ab.setDisplayShowTitleEnabled(false);
        ab.setDisplayShowHomeEnabled(false);
        ab.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM, ActionBar.DISPLAY_SHOW_CUSTOM);
        ab.setCustomView(head);
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}