package com.upac.upacapp;

import android.app.Application;
import android.util.Log;

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
        Parse.initialize(this, Secrets.parseAppId, Secrets.parseClientKey);

        Map<String, String> dimensions = new HashMap<String, String>();
        dimensions.put("category", "ThisIsATest");

        ParseAnalytics.trackEvent("read", dimensions);
    }

    public List<ParseObject> getBoardMembers() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Member");
        List<ParseObject> results;

        try {
            results = query.find();
        } catch (ParseException e) {
            results = null;
            e.printStackTrace();
        }

        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> queryList, ParseException e) {
                if (e == null) {
                    Log.d("Name", "Retrieved " + queryList.size() + " members.");
                } else {
                    Log.d("score", "Error: " + e.getMessage());
                }
            }
        });

        return results;
    }

    public List<ParseObject> getRaffles() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Raffle");
        List<ParseObject> results;

        try {
            results = query.find();
        } catch (ParseException e) {
            results = null;
            e.printStackTrace();
        }

        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> queryList, ParseException e) {
                if (e == null) {
                    Log.d("Name", queryList.size() + " events have raffles.");
                } else {
                    Log.d("events", "Error: " + e.getMessage());
                }
            }
        });

        return results;
    }

    public void addEntry(String eventId, String t) {
        final String ticketId = t;
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Raffle");

        query.whereEqualTo("eventId", eventId);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> raffle, ParseException e) {
                if (e == null) {
                    if (raffle.size() != 0) {
                        raffle.get(0).add("entries", ticketId);
                        raffle.get(0).saveInBackground();
                    }
                } else
                    e.printStackTrace();
            }
        });
    }
}