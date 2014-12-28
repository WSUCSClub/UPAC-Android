package com.upac.upacapp;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.AppEventsLogger;
import com.facebook.Session;

public class MainActivity extends FragmentActivity {
    private Session currentSession;
    private EventsFragment events = new EventsFragment();
    private GalleryFragment gallery = new GalleryFragment();
    private AboutFragment about = new AboutFragment();
    private ActionBarFragment action = new ActionBarFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button navBttn;

        ActionBar ab = getActionBar();
        ab.setDisplayShowTitleEnabled(false);
        ab.setDisplayShowHomeEnabled(false);

        getFragmentManager().beginTransaction().replace(R.id.bottom_bar, action).addToBackStack(null).commit();
        getFragmentManager().beginTransaction().replace(R.id.container, events).addToBackStack(null).commit();
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
        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        switch (v.getId()) { // Case statement to check which button was pressed
            case (R.id.action_events_button):
                transaction.replace(R.id.container, events).addToBackStack(null).commit();

                break;
            case (R.id.action_gallery_button):
                transaction.replace(R.id.container, gallery).addToBackStack(null).commit();

                break;
            case (R.id.action_about_button):
                transaction.replace(R.id.container, about).addToBackStack(null).commit();

                break;
        }
    }
}