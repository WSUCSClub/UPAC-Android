package com.upac.upacapp;

import android.app.ActionBar;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.FragmentActivity;
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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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
        /* The following two lines are poor practice. They were put in place to allow the events images to load.
         * Consider using AsyncTask to more appropriately get the bitmap image from the URL. */
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        galleryView = getLayoutInflater().inflate(R.layout.fragment_gallery, null);
        aboutView = getLayoutInflater().inflate(R.layout.fragment_about, null);
        Button navBttn;

        App parse = new App();

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
                    Bitmap mIcon_val;

                    eventsView = getLayoutInflater().inflate(R.layout.fragment_events, null);
                    LinearLayout ll = (LinearLayout) eventsView.findViewById(R.id.eventsLayout);

                    SimpleDateFormat inFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
                    SimpleDateFormat dayFormat = new SimpleDateFormat("MMMM dd, yyyy", java.util.Locale.getDefault());
                    SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", java.util.Locale.getDefault());

		        	try{
						JSONArray arr = response.getGraphObject().getInnerJSONObject().getJSONObject("events").getJSONArray("data");
						TextView[] tv = new TextView[arr.length()];
                        ImageView[] iv = new ImageView[arr.length()];

                        for (int i = 0; i < ( arr.length() ); i++){
							JSONObject json_obj = arr.getJSONObject(i);

                            date            = inFormat.parse(json_obj.getString("start_time"));

							location		= json_obj.getString("location");
							eventName		= json_obj.getString("name");
							description		= json_obj.getString("description");

							try{
								image		= json_obj.getJSONObject("cover").getString("source");
							}
							catch(Exception e){
								image		= "nothing";
							}

                            imageURL    = new URL(image);

                            mIcon_val   = BitmapFactory.decodeStream(imageURL.openConnection().getInputStream());

                            LinearLayout.LayoutParams imgParams = new LinearLayout.LayoutParams(300, 300);

                            iv[i] = new ImageView(getApplicationContext());
                            iv[i].setImageBitmap(mIcon_val);
                            iv[i].setId(i);
                            iv[i].setLayoutParams(imgParams);

                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1f);

							tv[i] = new TextView(getApplicationContext());
							tv[i].setText("Title: " + eventName + "\n" + "Day: " + dayFormat.format(date) + "    " + "Time: " + timeFormat.format(date) + "\n" + "Location: " + location);
                            tv[i].setTextColor(Color.BLACK);
							tv[i].setId(i);
							tv[i].setLayoutParams(params);

                            ll.addView(iv[i]);
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