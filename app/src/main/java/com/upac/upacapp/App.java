package com.upac.upacapp;

import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.PushService;

import android.app.Application;
import android.content.Context;

import java.util.HashMap;
import java.util.Map;

public class App extends Application {
    public void onCreate(Context context) {
        super.onCreate();

        try {
            Parse.initialize(context, Secrets.parseAppId, Secrets.parseClientKey);
            // Also in this method, specify a default Activity to handle push notifications
            PushService.setDefaultPushCallback(context, MainActivity.class);

            Map<String, String> dimensions = new HashMap<String, String>();
            // What type of news is this?
            dimensions.put("category", "AndroidTest");
            // Is it a weekday or the weekend?

            ParseAnalytics.trackEvent("read", dimensions);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}