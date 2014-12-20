package com.upac.upacapp;

import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.PushService;

import android.app.Application;
import android.content.Context;

import java.util.HashMap;
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
}