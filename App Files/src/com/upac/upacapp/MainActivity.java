package com.upac.upacapp;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphObject;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.PushService;

public class MainActivity extends Activity {
	
	private static int nextFrag;
	private Button navBttn;
	private View v;
	private ScrollView sv;
	private LinearLayout ll;
	private TextView tv;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
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
		
		v = getLayoutInflater().inflate(R.layout.fragment_events, null);
		
		sv = (ScrollView) v.findViewById(R.id.scrollView1);

	    ll = new LinearLayout(this);
	    ll.setOrientation(LinearLayout.VERTICAL);

	    tv = new TextView(this);
	    
	    Session session = AppDelegates.loadFBSession(this);
	    
		Bundle params = new Bundle();
		params.putString("fields", "events{description,cover,location,name,start_time}");
		
		new Request(
			    session,
			    "/WSU.UPAC",
			    params,
			    HttpMethod.GET,
			    new Request.Callback() {
			        public void onCompleted(Response response){
			        	System.out.println(response);
						try{
							JSONArray arr = response.getGraphObject().getInnerJSONObject().getJSONObject("events").getJSONArray("data");
							
							for (int i = 0; i < ( arr.length() ); i++){
								JSONObject json_obj = arr.getJSONObject(i);
								
								String time			= json_obj.getString("start_time");
								String location		= json_obj.getString("location");
								String eventName	= json_obj.getString("name");
								String description	= json_obj.getString("description");
								String image		= json_obj.getJSONObject("cover").getString("source");
								
								System.out.println("Time: " + time);
								System.out.println("Location: " + location);
								System.out.println("Event Name: " + eventName);
								System.out.println("Description: " + description);
								System.out.println("Image Path: " + image);
																
								tv.setText(time);
								
								ll.addView(tv);
							}
						}
						catch(Exception e){
							System.out.println(e.toString());
						}
			        }
			    }
			).executeAsync();
		
		sv.addView(ll);
				
		if (savedInstanceState == null) {
			nextFrag = R.layout.fragment_events;
			getFragmentManager().beginTransaction()
					.add(R.id.container, new NewFragment()).commit();
		}
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
		public NewFragment(){
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