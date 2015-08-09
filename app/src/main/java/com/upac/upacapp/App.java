package com.upac.upacapp;

import android.app.Application;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Parse.enableLocalDatastore(this);
        Parse.initialize(this, Secrets.PARSE_APP_ID, Secrets.PARSE_CLIENT_KEY);

        final Map<String, String> dimensions = new HashMap<>();
        dimensions.put("category", "ThisIsATest");

        ParseAnalytics.trackEventInBackground("read", dimensions);
    }

    public List<ParseObject> getBoardMembers() {
        final ParseQuery<ParseObject> query = ParseQuery.getQuery("Member");
        List<ParseObject> results;

        try {
            results = query.find();
        } catch (final ParseException e) {
            results = null;
            e.printStackTrace();
        }

        return results;
    }

    public List<ParseObject> getRaffles() {
        final ParseQuery<ParseObject> query = ParseQuery.getQuery("Raffle");
        List<ParseObject> results;

        try {
            results = query.find();
        } catch (final ParseException e) {
            results = null;
            e.printStackTrace();
        }

        return results;
    }

    public void addEntry(final String eventId, final String ticketId) {
        final ParseQuery<ParseObject> query = ParseQuery.getQuery("Raffle");

        query.whereEqualTo("eventId", eventId);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(final List<ParseObject> raffle, final ParseException e) {
                if (e == null) {
                    if (!raffle.isEmpty()) {
                        raffle.get(0).add("entries", ticketId);
                        raffle.get(0).saveInBackground();
                    }
                } else {
                    e.printStackTrace();
                }
            }
        });
    }
}