package com.upac.upacapp;

import java.util.HashMap;
import java.util.Map;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.PushService;

public class MainActivity extends Activity {
	
	private static int nextFrag;
	private Button navBttn;
			
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Session session = AppDelegates.loadFBSession(this);
		
		System.out.println(new Request(
			    session,
			    "/WSU.UPAC",
			    null,
			    HttpMethod.GET,
			    new Request.Callback() {
			        public void onCompleted(Response response) {
			            /* handle the result */
			        }
			    }
			).executeAsync());
		
		Parse.initialize(this, Secrets.parseAppId, Secrets.parseClientKey);
		// Also in this method, specify a default Activity to handle push notifications
		PushService.setDefaultPushCallback(this, MainActivity.class);
		
		Map<String, String> dimensions = new HashMap<String, String>();
		// What type of news is this?
		dimensions.put("category", "AndroidTest");
		// Is it a weekday or the weekend?
		dimensions.put("dayType", "weekday");
		// Send the dimensions to Parse along with the 'read' event
		 
		ParseAnalytics.trackEvent("read", dimensions);
		
		android.app.ActionBar ab;
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		ab = getActionBar();
		ab.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM, ActionBar.DISPLAY_SHOW_CUSTOM);
		View abBttn = getLayoutInflater().inflate(R.layout.ab_custom_view, null);
		
		navBttn = (Button) abBttn.findViewById(R.id.action_events_button);
		navBttn.setOnClickListener(openPage);
		navBttn = (Button) abBttn.findViewById(R.id.action_gallery_button);
		navBttn.setOnClickListener(openPage);
		navBttn = (Button) abBttn.findViewById(R.id.action_about_button);
		navBttn.setOnClickListener(openPage);
		
		ab.setCustomView(abBttn);
		ab.setDisplayShowTitleEnabled(false);
				
		if (savedInstanceState == null) {
			nextFrag = R.layout.fragment_events;
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}
	
	View.OnClickListener openPage = new View.OnClickListener(){
		public void onClick(View v) {
			switch(v.getId()){ // Case statement to check which button was pressed
			case(R.id.action_events_button):
				nextFrag = R.layout.fragment_events;
								
				getFragmentManager().beginTransaction().replace(R.id.container, new PlaceholderFragment())
											.addToBackStack(null).commit();
				break;
			case(R.id.action_gallery_button):
				nextFrag = R.layout.fragment_gallery;
							
				getFragmentManager().beginTransaction().replace(R.id.container, new PlaceholderFragment())
											.addToBackStack(null).commit();
				break;
			case(R.id.action_about_button):
				nextFrag = R.layout.fragment_about;
			
				getFragmentManager().beginTransaction().replace(R.id.container, new PlaceholderFragment())
											.addToBackStack(null).commit();
				break;
			}
		}
	};

	/*
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment(){
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState){
			View rootView = inflater.inflate(nextFrag, container,
					false);
			return rootView;
		}
	}
}