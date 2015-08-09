package com.upac.upacapp;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;

public class EventDetailsClickListener implements OnClickListener {
    private static FragmentActivity context;
    private static int pages;
    private int openedEvent;
    private static String[] titles;
    private static String[] locations;
    private static String[] times;
    private static String[] descriptions;
    private static String[] images;
    private static String[] dates;
    private static String[] ids;
    private static boolean[] hasRaffle;

    public EventDetailsClickListener(final FragmentActivity fa) {
        context = fa;
    }

    public void setOpenedEvent(final int p) {
        openedEvent = p;
    }

    public void setPageAmount(final int p) {
        pages = p;
    }

    public void setInformation(final String[] a, final String[] b, final String[] c, final String[] d, final String[] e, final String[] f,
                               final boolean[] g, final String[] h) {
        titles = a;
        locations = b;
        dates = c;
        times = d;
        descriptions = e;
        images = f;
        hasRaffle = g;
        ids = h;
    }

    @Override
    public void onClick(final View v) {
        final Intent intent = new Intent(context, EventDetailsSlidePagerActivity.class);
        intent.putExtra("AllPages", pages);
        intent.putExtra("Page", openedEvent);
        intent.putExtra("Titles", titles);
        intent.putExtra("Locations", locations);
        intent.putExtra("Dates", dates);
        intent.putExtra("Times", times);
        intent.putExtra("Descriptions", descriptions);
        intent.putExtra("Images", images);
        intent.putExtra("Has_Raffle", hasRaffle);
        intent.putExtra("IDs", ids);
        context.startActivity(intent);
    }
}