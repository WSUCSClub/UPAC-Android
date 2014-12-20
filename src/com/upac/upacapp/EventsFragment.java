package com.upac.upacapp;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class EventsFragment extends ListFragment {
	public static final String TAG = "EventsFragment";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, parent, savedInstanceState);

        return view;
    }
	
	public void buildEvents(LinearLayout ll){		
		RelativeLayout rl = (RelativeLayout) getActivity().getLayoutInflater().inflate(R.layout.fragment_events, null);
		
		System.out.println("RL's count: " + rl.getChildCount());
		
		rl.addView(ll);
	}
}