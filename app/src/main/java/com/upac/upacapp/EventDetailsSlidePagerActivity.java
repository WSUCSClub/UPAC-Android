package com.upac.upacapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

public class EventDetailsSlidePagerActivity extends FragmentActivity {
    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private ViewPager mPager;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private PagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        int pages = intent.getIntExtra("AllPages", 0);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_slide);

        // Instantiate a ViewPager and a PagerAdapter.
        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new EventDetailsPagerAdapter(getSupportFragmentManager(), pages);
        mPager.setAdapter(mPagerAdapter);
        mPager.setCurrentItem(intent.getIntExtra("Page", 0));
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}