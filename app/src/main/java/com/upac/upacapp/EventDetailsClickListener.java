package com.upac.upacapp;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;

public class EventDetailsClickListener implements OnClickListener {
    private FragmentActivity context;

    public EventDetailsClickListener(FragmentActivity fa) {
        context = fa;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(context, EventDetailsSlidePagerActivity.class);
        context.startActivity(intent);
    }
}