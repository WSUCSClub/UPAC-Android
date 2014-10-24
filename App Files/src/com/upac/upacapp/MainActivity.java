package com.upac.upacapp;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class MainActivity extends Activity {
	
	private static int nextFrag;
	private Button navBttn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		android.app.ActionBar ab;
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		ab = this.getActionBar();
		ab.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM, ActionBar.DISPLAY_SHOW_CUSTOM);
		View abBttn = getLayoutInflater().inflate(R.layout.ab_custom_view, null);
		
		navBttn = (Button) abBttn.findViewById(R.id.action_events_button);
		navBttn.setOnClickListener(openPage);
		navBttn = (Button) abBttn.findViewById(R.id.action_media_button);
		navBttn.setOnClickListener(openPage);
		navBttn = (Button) abBttn.findViewById(R.id.action_raffle_button);
		navBttn.setOnClickListener(openPage);
		navBttn = (Button) abBttn.findViewById(R.id.action_settings);
		navBttn.setOnClickListener(openPage);
		
		ab.setCustomView(abBttn);
		ab.setDisplayShowTitleEnabled(false);
		
		Log.i("Message: ", "CHECK 1");
		
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
			case(R.id.action_media_button):
				nextFrag = R.layout.fragment_media;
							
				getFragmentManager().beginTransaction().replace(R.id.container, new PlaceholderFragment())
											.addToBackStack(null).commit();
				break;
			case(R.id.action_raffle_button):
				nextFrag = R.layout.fragment_raffle;
			
				getFragmentManager().beginTransaction().replace(R.id.container, new PlaceholderFragment())
											.addToBackStack(null).commit();
				break;
			case(R.id.action_settings):
				nextFrag = R.layout.fragment_settings;
				
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