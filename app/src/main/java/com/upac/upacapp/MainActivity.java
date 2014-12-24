package com.upac.upacapp;

import android.app.ActionBar;
import android.app.Fragment;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.facebook.AppEventsLogger;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends FragmentActivity {

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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        galleryView = getLayoutInflater().inflate(R.layout.fragment_gallery, null);
        aboutView = getLayoutInflater().inflate(R.layout.fragment_about, null);
        Button navBttn;

        App parse = new App();
		
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
        Calendar since = Calendar.getInstance();
        since.add(Calendar.DATE, -60);
        System.out.println("Date: " + since.getTime());

		Bundle params = new Bundle();
		params.putString("fields", "events.since(" + since.getTime() + "){description,cover,location,name,start_time}");
		
		new Request(
			session,
			"/WSU.UPAC",
			params,
			HttpMethod.GET,
			new Request.Callback() {
		        public void onCompleted(Response response){
		        	String location, eventName, description, image;
                    Date date;
                    URL imageURL;

                    eventsView = getLayoutInflater().inflate(R.layout.fragment_events, null);
                    LinearLayout ll = (LinearLayout) eventsView.findViewById(R.id.eventsLayout);

                    SimpleDateFormat inFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
                    SimpleDateFormat dayFormat = new SimpleDateFormat("MMMM dd, yyyy", java.util.Locale.getDefault());
                    SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", java.util.Locale.getDefault());

		        	try{
						JSONArray arr = response.getGraphObject().getInnerJSONObject().getJSONObject("events").getJSONArray("data");
                        LinearLayout[] lines = new LinearLayout[arr.length()];
						TextView[] tv = new TextView[arr.length()];
                        ImageView[] iv = new ImageView[arr.length()];

                        for (int i = 0; i < ( arr.length() ); i++){
							JSONObject json_obj = arr.getJSONObject(i);

                            date = inFormat.parse(json_obj.getString("start_time"));

							location = json_obj.getString("location");
							eventName = json_obj.getString("name");
							description	= json_obj.getString("description");

							try{
								image = json_obj.getJSONObject("cover").getString("source");
							}
							catch(Exception e){
								image = "nothing";
							}

                            LinearLayout.LayoutParams imgParams = new LinearLayout.LayoutParams(550, 290, 1f);

                            iv[i] = new ImageView(getApplicationContext());

                            imageURL = new URL(image);
                            DownloadImageTask dit = new DownloadImageTask(imageURL, iv[i]);
                            dit.execute();

                            iv[i].setId(i);
                            iv[i].setPadding(25, 0, 25, 0);
                            iv[i].setLayoutParams(imgParams);

                            GradientDrawable gd = new GradientDrawable();
                            gd.setColor(Color.WHITE);
                            gd.setStroke(1, Color.BLACK);

                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1f);

							tv[i] = new TextView(getApplicationContext());
							tv[i].setText("Title: " + eventName + "\n" + "Day: " + dayFormat.format(date) + "    " + "Time: " + timeFormat.format(date) + "\n" + "Location: " + location);
                            tv[i].setTextColor(Color.BLACK);
							tv[i].setId(i);
                            tv[i].setPadding(0, 75, 0, 0);
							tv[i].setLayoutParams(params);

                            lines[i] = new LinearLayout(getApplicationContext());
                            lines[i].setBackground(gd);
                            lines[i].setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                            lines[i].addView(iv[i]);
							lines[i].addView(tv[i]);

                            ll.addView(lines[i]);

                            nextView = eventsView;
                            getFragmentManager().beginTransaction().replace(R.id.container, events).addToBackStack(null).commit();
						}	// End of for loop
					}
					catch(Exception e) {
                        e.printStackTrace();
                    }
		        }	// End of onCompleted
		    }	// End of Callback
		).executeAsync();
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