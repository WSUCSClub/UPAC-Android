package com.upac.upacapp;

import android.app.ActionBar;
import android.app.NotificationManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AppEventsLogger;
import com.facebook.Session;

public class MainActivity extends FragmentActivity {
    private Session currentSession;
    private EventsFragment events = new EventsFragment();
    private GalleryFragment gallery;
    private AboutFragment about;
    private ActionBarFragment action = new ActionBarFragment();
    private static View head;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        head = getLayoutInflater().inflate(R.layout.head, null);
        TextView title = (TextView) head.findViewById(R.id.title);

        ActionBar ab = getActionBar();
        ab.setDisplayShowTitleEnabled(false);
        ab.setDisplayShowHomeEnabled(false);
        ab.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM, ActionBar.DISPLAY_SHOW_CUSTOM);
        ab.setCustomView(head);

        getSupportFragmentManager().beginTransaction().replace(R.id.bottom_bar, action).addToBackStack(null).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.container, events, EventsFragment.TAG).commit();
        getSupportFragmentManager().beginTransaction().show(events).commit();
        title.setText("EVENTS");
    }

    @Override
    protected void onPause() {
        super.onPause();

        AppEventsLogger.deactivateApp(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Session.saveSession(currentSession, outState);
    }

    public void openFacebook(View v) {
        try {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/WSU.UPAC"));
            startActivity(browserIntent);
        } catch (Exception e) {
            Toast.makeText(this, "No application can handle this request."
                    + " Please install a web browser", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    public void openTwitter(View v) {
        try {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/UPACWSU"));
            startActivity(browserIntent);
        } catch (Exception e) {
            Toast.makeText(this, "No application can handle this request."
                    + " Please install a web browser", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    public void sendEmail(View v) {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL, new String[]{"upac@winona.edu"});
        try {
            startActivity(Intent.createChooser(i, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }

    public void openPage(View v) {
        TextView title = (TextView) head.findViewById(R.id.title);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        switch (v.getId()) { // Case statement to check which button was pressed
            case (R.id.action_events_button):
                if(gallery != null && about != null)
                    transaction.hide(gallery).hide(about).show(events).commit();
                else if(gallery == null)
                    transaction.hide(about).show(events).commit();
                else
                    transaction.hide(gallery).show(events).commit();

                title.setText("EVENTS");

                break;
            case (R.id.action_gallery_button):
                if (gallery == null) {
                    gallery = new GalleryFragment();
                    getSupportFragmentManager().beginTransaction().add(R.id.container, gallery, GalleryFragment.TAG).commit();
                }

                if (about != null)
                    transaction.hide(events).hide(about).show(gallery).commit();
                else
                    transaction.hide(events).show(gallery).commit();

                title.setText("PHOTOS");

                break;
            case (R.id.action_about_button):
                if (about == null) {
                    about = new AboutFragment();
                    getSupportFragmentManager().beginTransaction().add(R.id.container, about, AboutFragment.TAG).commit();
                }

                if (gallery != null)
                    transaction.hide(gallery).hide(events).show(about).commit();
                else
                    transaction.hide(events).show(about).commit();

                title.setText("CONTACT");

                break;
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}