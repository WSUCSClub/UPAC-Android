package com.upac.upacapp;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;

public class EventDetailsClickListener implements OnClickListener {
    private FragmentActivity context;
    private int openedEvent;
    private int pages;

    public EventDetailsClickListener(FragmentActivity fa) {
        context = fa;
    }

    public void setOpenedEvent(int p){
        openedEvent = p;
    }

    public void setPageAmount(int p){
        pages = p;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(context, EventDetailsSlidePagerActivity.class);
        intent.putExtra("AllPages", pages);
        intent.putExtra("Page", openedEvent);
        context.startActivity(intent);
    }
}