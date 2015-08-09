package com.upac.upacapp;

import android.app.ActionBar;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends FragmentActivity {
    private final EventsFragment events = new EventsFragment();
    private GalleryFragment gallery;
    private AboutFragment about;
    private final ActionBarFragment action = new ActionBarFragment();
    private static View head;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.activity_main);
        head = this.getLayoutInflater().inflate(R.layout.head, null);
        final TextView title = (TextView) head.findViewById(R.id.title);

        final ActionBar ab = this.getActionBar();
        ab.setDisplayShowTitleEnabled(false);
        ab.setDisplayShowHomeEnabled(false);
        ab.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM, ActionBar.DISPLAY_SHOW_CUSTOM);
        ab.setCustomView(head);

        this.getSupportFragmentManager().beginTransaction().replace(R.id.bottom_bar, action).addToBackStack(null).commit();
        this.getSupportFragmentManager().beginTransaction().add(R.id.container, events, EventsFragment.TAG).commit();
        this.getSupportFragmentManager().beginTransaction().show(events).commit();
        title.setText("EVENTS");
    }

    public void openFacebook(final View v) {
        try {
            final Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/WSU.UPAC"));
            this.startActivity(browserIntent);
        } catch (RuntimeException e) {
            Toast.makeText(this, "No application can handle this request."
                    + " Please install a web browser", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    public void openTwitter(final View v) {
        try {
            final Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/UPACWSU"));
            this.startActivity(browserIntent);
        } catch (final RuntimeException e) {
            Toast.makeText(this, "No application can handle this request."
                    + " Please install a web browser", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    public void sendEmail(final View v) {
        final Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL, new String[]{"upac@winona.edu"});
        try {
            this.startActivity(Intent.createChooser(i, "Send mail..."));
        } catch (final ActivityNotFoundException e) {
            Toast.makeText(this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }

    public void openPage(final View v) {
        final TextView title = (TextView) head.findViewById(R.id.title);
        final FragmentTransaction transaction = this.getSupportFragmentManager().beginTransaction();

        switch (v.getId()) { // Case statement to check which button was pressed
            case (R.id.action_events_button):
                if (gallery != null && about != null) {
                    transaction.hide(gallery).hide(about).show(events).commit();
                } else if (gallery != null) {
                    transaction.hide(gallery).show(events).commit();
                } else if (about != null) {
                    transaction.hide(about).show(events).commit();
                }

                title.setText("EVENTS");

                break;
            case (R.id.action_gallery_button):
                if (gallery == null) {
                    gallery = new GalleryFragment();
                    this.getSupportFragmentManager().beginTransaction().add(R.id.container, gallery, GalleryFragment.TAG).commit();
                }

                if (about != null) {
                    transaction.hide(events).hide(about).show(gallery).commit();
                } else {
                    transaction.hide(events).show(gallery).commit();
                }

                title.setText("PHOTOS");

                break;
            case (R.id.action_about_button):
                if (about == null) {
                    about = new AboutFragment();
                    this.getSupportFragmentManager().beginTransaction().add(R.id.container, about, AboutFragment.TAG).commit();
                }

                if (gallery != null) {
                    transaction.hide(gallery).hide(events).show(about).commit();
                } else {
                    transaction.hide(events).show(about).commit();
                }

                title.setText("CONTACT");

                break;
        }
    }

    @Override
    public void onBackPressed() {
        this.finish();
        super.onBackPressed();
    }
}