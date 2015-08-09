package com.upac.upacapp;

import android.app.Activity;

import com.facebook.AccessToken;
import com.facebook.Session;
import com.facebook.Session.StatusCallback;
import com.facebook.SessionState;

import java.util.ArrayList;
import java.util.List;

public class AppDelegates extends Activity {
    static final StatusCallback ssnStsCllbck = new StatusCallback() {
        public void call(final Session session, final SessionState state, final Exception exception) {
            // If there is an exception...
            if (exception != null) {
                exception.printStackTrace();
            }
        }
    };

    public static Session loadFBSession(final Activity activity) {
        final List<String> permissions = new ArrayList<>();
        permissions.add("name");

        try {
            return Session.getActiveSession().getState() != null ? Session.openActiveSession(activity, false, ssnStsCllbck) : null;

        } catch (final RuntimeException e) {
            return Session.openActiveSessionWithAccessToken(activity, AccessToken.createFromExistingAccessToken(Secrets.FB_ACCESS_TOKEN.toString(),
                    null, null, null, permissions), ssnStsCllbck);
        }
    }
}
