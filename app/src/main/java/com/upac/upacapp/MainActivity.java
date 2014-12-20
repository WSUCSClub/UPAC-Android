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
	private Session currentSession;
    private NewFragment events = new NewFragment();
    private NewFragment gallery = new NewFragment();
    private NewFragment about = new NewFragment();
    private static View nextView;
    private View eventsView;
    private View galleryView;
    private View aboutView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
        galleryView = getLayoutInflater().inflate(R.layout.fragment_gallery, null);
        aboutView = getLayoutInflater().inflate(R.layout.fragment_about, null);
        Button navBttn;

        App parse = new App();
        parse.onCreate(this);

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
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
	
	private void findEvents(){
		Session session = AppDelegates.loadFBSession(this);
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
                    eventsView = getLayoutInflater().inflate(R.layout.fragment_events, null);
                    LinearLayout ll = (LinearLayout) eventsView.findViewById(R.id.eventsLayout);

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

                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

                            params.leftMargin = 50;
                            params.topMargin = i * 50;

							tv[i] = new TextView(getApplicationContext());
							tv[i].setText(description);
							tv[i].setId(i);
							tv[i].setLayoutParams(params);

							ll.addView(tv[i]);

                            nextView = eventsView;
                            getFragmentManager().beginTransaction().replace(R.id.container, events).addToBackStack(null).commit();
						}	// End of for loop
					}
					catch(Exception e) {
                        e.printStackTrace();
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
				nextView = eventsView;

				getFragmentManager().beginTransaction().replace(R.id.container, events)
					.addToBackStack(null).commit();
				break;
			case(R.id.action_gallery_button):
				nextView = galleryView;
							
				getFragmentManager().beginTransaction().replace(R.id.container, gallery)
					.addToBackStack(null).commit();
				break;
			case(R.id.action_about_button):
				nextView = aboutView;
			
				getFragmentManager().beginTransaction().replace(R.id.container, about)
					.addToBackStack(null).commit();
				break;
			}
		}
	};

	public static class NewFragment extends Fragment {
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
			return nextView;
		}
	}
}