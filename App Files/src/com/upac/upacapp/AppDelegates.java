package com.upac.upacapp;

import android.app.Activity;

import com.facebook.AccessToken;
import com.facebook.Session;
import com.facebook.SessionState;

public class AppDelegates extends Activity {
	final static Session.StatusCallback ssnStsCllbck = new Session.StatusCallback() {
	    public void call(final Session session, SessionState state, Exception exception) {
	        // If there is an exception...
	        if(exception != null)
	        {
	            // Handle fail case here.
	            return;
	        }

	        // If session is just opened...
	        if(state == SessionState.OPENED)
	        {
	            // Handle success case here.
	            return;
	        }
	    };
	};
	
	public static Session loadFBSession(Activity activity) {
		try{
			if(Session.getActiveSession().getState() != null){
				System.out.println("Already had one");
				return Session.openActiveSession(activity, false, ssnStsCllbck);
			}
			else{
				System.out.println("What the eff");
				return null;
			}
		}
		catch(Exception e){
			System.out.println("This happened");
			return Session.openActiveSessionWithAccessToken(activity, AccessToken.createFromExistingAccessToken(Secrets.fbAccessToken, null, null, null, null), ssnStsCllbck);
		}
	} 
}
