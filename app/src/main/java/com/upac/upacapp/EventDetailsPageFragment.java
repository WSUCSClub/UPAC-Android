package com.upac.upacapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class EventDetailsPageFragment extends Fragment {
    public static final String ARG_PAGE = "page";
    public static int mPageNumber;
    private static String[] title, location, time, description;

    public EventDetailsPageFragment() {

    }

    public void setInformation(String[] a, String[] b, String[] c, String[] d){
        title = a;
        location = b;
        time = c;
        description = d;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPageNumber = getArguments().getInt(ARG_PAGE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_event_description, container, false);
        ImageView eventCover = (ImageView) rootView.findViewById(R.id.event_image);
        TextView eventTitle = (TextView) rootView.findViewById(R.id.title);
        TextView eventLocation = (TextView) rootView.findViewById(R.id.location);
        TextView eventTime = (TextView) rootView.findViewById(R.id.time);
        TextView eventDescription = (TextView) rootView.findViewById(R.id.description);

        eventTitle.setText(title[mPageNumber]);
        eventLocation.setText(location[mPageNumber]);
        eventTime.setText(time[mPageNumber]);
        eventDescription.setText(description[mPageNumber]);

        return rootView;
    }

    public EventDetailsPageFragment create(int pageNumber) {
        EventDetailsPageFragment fragment = new EventDetailsPageFragment();
        Bundle args = new Bundle();

        args.putInt(ARG_PAGE, pageNumber);
        fragment.setArguments(args);

        return fragment;
    }
}