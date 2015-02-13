package com.upac.upacapp;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class NotifyMeClickListener implements View.OnClickListener {
    private Button notifyButton;
    private static NotifySQLiteHelper entry;
    private Calendar date;
    private ScheduleClient scheduleClient;
    private Context cont;
    private String entryID;

    public NotifyMeClickListener(Button b, NotifySQLiteHelper e, Calendar c, Context t, ScheduleClient s, String i) {
        notifyButton = b;
        entry = e;
        date = c;
        scheduleClient = s;
        cont = t;
        entryID = i;
    }

    @Override
    public void onClick(View v) {
        date.add(Calendar.HOUR, -1);
        scheduleClient.setAlarmForNotification(date);

        date.set(Calendar.HOUR, 0);
        date.set(Calendar.MINUTE, 0);
        scheduleClient.setAlarmForNotification(date);

        entry.addEntry(entryID);
        notifyButton.setClickable(false);
        notifyButton.setText("To be notified");

        Toast.makeText(cont, "You will be notified the day of, and then an hour before, the event.", Toast.LENGTH_LONG).show();
    }
}