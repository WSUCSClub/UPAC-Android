package com.upac.upacapp;

import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class MainActivity extends ActionBarActivity {
	
	private static int currFrag;
	private static int nextFrag;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		android.app.ActionBar ab;
		
/*		Button b = (Button) findViewById(R.id.action_events_button);
		b.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				currFrag = nextFrag;
				nextFrag = R.layout.fragment_calendar;
				getSupportFragmentManager().beginTransaction().replace(currFrag, new PlaceholderFragment())
											.addToBackStack(null).commit();
			}
		});*/
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		if (savedInstanceState == null) {
			nextFrag = R.layout.fragment_events;
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		
		ab = this.getActionBar();
		ab.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM, ActionBar.DISPLAY_SHOW_CUSTOM);
		ab.setCustomView(R.layout.ab_custom_view);
		ab.setDisplayShowTitleEnabled(false);
	}

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