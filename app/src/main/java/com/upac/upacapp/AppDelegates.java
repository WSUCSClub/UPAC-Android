package com.upac.upacapp;

import android.app.Activity;

import com.facebook.AccessToken;
import com.facebook.Session;
import com.facebook.SessionState;

import java.util.ArrayList;

public class AppDelegates extends Activity {
    final static Session.StatusCallback ssnStsCllbck = new Session.StatusCallback() {
        public void call(final Session session, SessionState state, Exception exception) {
            // If there is an exception...
            if (exception != null) {
                exception.printStackTrace();
            }
        }
    };

    public static Session loadFBSession(Activity activity) {
        ArrayList<String> permissions = new ArrayList<String>() {{
            add("name");
        }};

        try {
            if (Session.getActiveSession().getState() != null) {
                return Session.openActiveSession(activity, false, ssnStsCllbck);
            } else {
                return null;
            }
        } catch (Exception e) {
            return Session.openActiveSessionWithAccessToken(activity, AccessToken.createFromExistingAccessToken(Secrets.fbAccessToken, null, null, null, permissions), ssnStsCllbck);
        }
    }
}
