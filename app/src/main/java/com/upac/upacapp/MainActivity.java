package com.upac.upacapp;

import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

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
        findPhotos();
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

    public void openFacebook(View v){
        try {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/WSU.UPAC"));
            startActivity(browserIntent);
        }
        catch(Exception e){
            Toast.makeText(this, "No application can handle this request."
                    + " Please install a web browser",  Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    public void openTwitter(View v){
        try {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/UPACWSU"));
            startActivity(browserIntent);
        }
        catch(Exception e){
            Toast.makeText(this, "No application can handle this request."
                    + " Please install a web browser",  Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    public void sendEmail(View v){
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"upac@winona.edu"});
        try {
            startActivity(Intent.createChooser(i, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(MainActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }

	private void findEvents(){
		Session session = AppDelegates.loadFBSession(this);
        Calendar since = Calendar.getInstance();
        since.add(Calendar.DATE, -60);

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
                            DownloadEventsImages dit = new DownloadEventsImages(imageURL, iv[i]);
                            dit.execute();

                            iv[i].setId(i);
                            iv[i].setPadding(25, 0, 25, 0);
                            iv[i].setLayoutParams(imgParams);

                            GradientDrawable gd = new GradientDrawable();
                            gd.setColor(Color.WHITE);
                            gd.setStroke(5, 0x000000);

                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1f);

							tv[i] = new TextView(getApplicationContext());
							tv[i].setText(eventName + "\n" + dayFormat.format(date) + "\n" + timeFormat.format(date) + "\n" + location);
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
						}	// End of for loop

                        nextView = eventsView;
                        getFragmentManager().beginTransaction().replace(R.id.container, events).addToBackStack(null).commit();
					}
					catch(Exception e) {
                        e.printStackTrace();
                    }
		        }	// End of onCompleted
		    }	// End of Callback
		).executeAsync();
	}

    private void findPhotos(){
        Session session = AppDelegates.loadFBSession(this);
        final int PERLINE = 3;

        Bundle params = new Bundle();
        params.putString("fields", "photos");

        new Request(
                session,
                "/WSU.UPAC/albums",
                params,
                HttpMethod.GET,
                new Request.Callback() {
                    public void onCompleted(Response response){
                        String croppedSRC, fullSRC;
                        URL croppedURL, fullURL;

                        galleryView = getLayoutInflater().inflate(R.layout.fragment_gallery, null);
                        LinearLayout ll = (LinearLayout) galleryView.findViewById(R.id.galleryLayout);

                        try{
                            int count = 0;
                            JSONArray arr = response.getGraphObject().getInnerJSONObject().getJSONArray("data").getJSONObject(0).getJSONObject("photos").getJSONArray("data");

                            ImageView[] iv = new ImageView[arr.length()];
                            LinearLayout[] lines = new LinearLayout[arr.length() / PERLINE];

                            for (int x = 0; x < (arr.length() / PERLINE); x++) {
                                lines[x] = new LinearLayout(getApplicationContext());
                                LinearLayout.LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
                                lines[x].setLayoutParams(params);
                                ll.addView(lines[x]);

                                for (int i = 0; i < PERLINE && count < arr.length(); i++) {
                                    JSONObject json_obj = arr.getJSONObject(count);

                                    croppedSRC = json_obj.getString("picture");
                                    fullSRC = json_obj.getString("source");

                                    LinearLayout.LayoutParams imgParams = new LinearLayout.LayoutParams(0, 360, 1f);

                                    iv[count] = new ImageView(getApplicationContext());

                                    croppedURL = new URL(croppedSRC);
                                    fullURL = new URL(fullSRC);
                                    DownloadGalleryImages dgi = new DownloadGalleryImages(croppedURL, iv[count]);
                                    dgi.execute();

                                    iv[count].setId(count);
                                    iv[count].setPadding(2, 2, 2, 2);
                                    iv[count].setLayoutParams(imgParams);

                                    lines[x].addView(iv[count]);

                                    count++;
                                }    // End of for loop
                            }

                            nextView = galleryView;
                            getFragmentManager().beginTransaction().replace(R.id.container, events).addToBackStack(null).commit();
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