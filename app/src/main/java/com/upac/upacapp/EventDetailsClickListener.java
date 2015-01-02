package com.upac.upacapp;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;

public class EventDetailsClickListener implements OnClickListener {
    private static FragmentActivity context;
    private static int openedEvent, pages;
    private static String[] titles, locations, times, descriptions, images;

    public EventDetailsClickListener(FragmentActivity fa) {
        context = fa;
    }

    public void setOpenedEvent(int p){
        openedEvent = p;
    }

    public void setPageAmount(int p){
        pages = p;
    }

    public void setInformation(String[] a, String[] b, String[] c, String[] d, String[] e){
        titles = a;
        locations = b;
        times = c;
        descriptions = d;
        images = e;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(context, EventDetailsSlidePagerActivity.class);
        intent.putExtra("AllPages", pages);
        intent.putExtra("Page", openedEvent);
        intent.putExtra("Titles", titles);
        intent.putExtra("Locations", locations);
        intent.putExtra("Times", times);
        intent.putExtra("Descriptions", descriptions);
        intent.putExtra("Images", images);
        context.startActivity(intent);
    }
}