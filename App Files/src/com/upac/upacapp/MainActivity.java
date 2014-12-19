package com.upac.upacapp;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.facebook.AppEventsLogger;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.PushService;

public class MainActivity extends FragmentActivity {
	
	private static int nextFrag;
	private Button navBttn;
	private EventsFragment eventsFrag;
	private Session currentSession;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		restoreFragments(savedInstanceState);
		
		Parse.initialize(this, Secrets.parseAppId, Secrets.parseClientKey);
		// Also in this method, specify a default Activity to handle push notifications
		PushService.setDefaultPushCallback(this, MainActivity.class);
		
		Map<String, String> dimensions = new HashMap<String, String>();
		// What type of news is this?
		dimensions.put("category", "AndroidTest");
		// Is it a weekday or the weekend?
		 
		ParseAnalytics.trackEvent("read", dimensions);
		
		android.app.ActionBar ab;
		
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
	    
		findEvents();
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
	
	private void restoreFragments(Bundle savedInstanceState) {
		FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        if (savedInstanceState != null) {
			eventsFrag = (EventsFragment) manager.getFragment(savedInstanceState, EventsFragment.TAG);
        }

        if (eventsFrag == null) {
			eventsFrag = new EventsFragment();
			transaction.add(R.id.container, eventsFrag, EventsFragment.TAG);
        }

        transaction.commit();
    }
	
	private void findEvents(){
		final Session session = AppDelegates.loadFBSession(this);
		final LinearLayout ll = new LinearLayout(this);
		ll.setOrientation(LinearLayout.VERTICAL);
		ll.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
		final View v = getLayoutInflater().inflate(R.layout.fragment_events, null);
		
		Bundle params = new Bundle();
		params.putString("fields", "events.since(1).limit(5){description,cover,location,name,start_time}");
		
		Request r = new Request(
			session,
			"/WSU.UPAC",
			params,
			HttpMethod.GET,
			new Request.Callback() {
		        public void onCompleted(Response response){
		        	String time, location, eventName, description, image;
		        	
		        	try{
						JSONArray arr = response.getGraphObject().getInnerJSONObject().getJSONObject("events").getJSONArray("data");
						TextView[] tv = new TextView[arr.length()];
						
						for (int i = 0; i < ( arr.length() ); i++){
							JSONObject json_obj = arr.getJSONObject(i);
							
							time			= json_obj.getString("start_time");
							location		= json_obj.getString("location");
							eventName		= json_obj.getString("name");
							description		= json_obj.getString("description");
							
							try{
								image		= json_obj.getJSONObject("cover").getString("source");
							}
							catch(Exception e){
								image		= "nothing";
							}
							
							tv[i] = new TextView(getApplicationContext());
							tv[i].setText(description);
							tv[i].setId(i);
							tv[i].setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
							
							ll.addView(tv[i]);
						}	// End of for loop
					}
					catch(Exception e){
						e.printStackTrace();
					}
		        	
		        	if (response.getRequest().getSession() == session) {
		    			eventsFrag.buildEvents(ll);
		    		}
		        }	// End of onCompleted
		    }	// End of Callback
		);
		
		r.executeAsync();
	}
	
	View.OnClickListener openPage = new View.OnClickListener(){
		public void onClick(View v) {
			switch(v.getId()){ // Case statement to check which button was pressed
			case(R.id.action_events_button):
				nextFrag = R.layout.fragment_events;
								
				getFragmentManager().beginTransaction().replace(R.id.container, new NewFragment())
					.addToBackStack(null).commit();
				break;
			case(R.id.action_gallery_button):
				nextFrag = R.layout.fragment_gallery;
							
				getFragmentManager().beginTransaction().replace(R.id.container, new NewFragment())
					.addToBackStack(null).commit();
				break;
			case(R.id.action_about_button):
				nextFrag = R.layout.fragment_about;
			
				getFragmentManager().beginTransaction().replace(R.id.container, new NewFragment())
					.addToBackStack(null).commit();
				break;
			}
		}
	};

	public static class NewFragment extends Fragment {
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
			View rootView = inflater.inflate(nextFrag, container, false);
			return rootView;
		}
	}
}