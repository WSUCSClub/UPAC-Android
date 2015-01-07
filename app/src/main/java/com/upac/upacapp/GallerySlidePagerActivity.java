package com.upac.upacapp;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

public class GallerySlidePagerActivity extends FragmentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        int pages = intent.getIntExtra("AllPages", 0);
        String[] URLs = intent.getStringArrayExtra("ImageURLs");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_slide);
        ViewPager mPager = (ViewPager) findViewById(R.id.pager);

        GalleryPageFragment page = new GalleryPageFragment();
        page.setInformation(URLs);
        PagerAdapter mPagerAdapter = new GalleryPagerAdapter(getSupportFragmentManager(), pages, page);
        mPager.setAdapter(mPagerAdapter);
        mPager.setCurrentItem(intent.getIntExtra("Page", 0));
        mPager.setOffscreenPageLimit(6);

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