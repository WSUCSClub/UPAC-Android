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
    protected void onCreate(final Bundle savedInstanceState) {
        final Intent intent = this.getIntent();
        final int pages = intent.getIntExtra("AllPages", 0);
        final String[] URLs = intent.getStringArrayExtra("ImageURLs");

        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_screen_slide);
        final ViewPager mPager = (ViewPager) this.findViewById(R.id.pager);

        final GalleryPageFragment page = new GalleryPageFragment();
        page.setInformation(URLs);
        final PagerAdapter mPagerAdapter = new GalleryPagerAdapter(this.getSupportFragmentManager(), pages, page);
        mPager.setAdapter(mPagerAdapter);
        mPager.setCurrentItem(intent.getIntExtra("Page", 0));
        mPager.setOffscreenPageLimit(6);

        final View head = this.getLayoutInflater().inflate(R.layout.head, null);

        final ActionBar ab = this.getActionBar();
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