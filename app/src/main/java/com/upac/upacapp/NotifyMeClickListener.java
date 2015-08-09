package com.upac.upacapp;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.Calendar;

public class NotifyMeClickListener implements View.OnClickListener {
    private Button notifyButton;
    private static NotifySQLiteHelper entry;
    private Calendar date;
    private ScheduleClient scheduleClient;
    private Context cont;
    private String entryID;

    public NotifyMeClickListener(final Button b, final NotifySQLiteHelper e, final Calendar c, final Context t, final ScheduleClient s, final String i) {
        notifyButton = b;
        entry = e;
        date = c;
        scheduleClient = s;
        cont = t;
        entryID = i;
    }

    @Override
    public void onClick(final View v) {
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