package com.upac.upacapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

public class Receiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, intent.getStringExtra("param"), Toast.LENGTH_SHORT).show();

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.notify_icon)
                        .setContentTitle("My notification")
                        .setContentText("Hello World!");
    }
}