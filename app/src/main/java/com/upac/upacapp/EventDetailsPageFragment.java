package com.upac.upacapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EventDetailsPageFragment extends Fragment {
    public static final String ARG_PAGE = "page";
    public static int mPageNumber;
    private static String[] title, location, time, description, images, date, ids;
    private static boolean[] hasRaffle;

    public EventDetailsPageFragment() {

    }

    public void setInformation(String[] a, String[] b, String[] c, String[] d, String[] e, String[] f, boolean[] g, String[] h) {
        title = a;
        location = b;
        date = c;
        time = d;
        description = e;
        images = f;
        hasRaffle = g;
        ids = h;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPageNumber = getArguments().getInt(ARG_PAGE);
    }

    @Override
    public View onCreateView(LayoutInflater inf, ViewGroup container, Bundle savedInstanceState) {
        View rootView = getActivity().getLayoutInflater().inflate(R.layout.fragment_event_description, container, false);
        ImageView eventCover = (ImageView) rootView.findViewById(R.id.event_image);
        TextView eventTitle = (TextView) rootView.findViewById(R.id.title);
        TextView eventLocation = (TextView) rootView.findViewById(R.id.location);
        TextView eventTime = (TextView) rootView.findViewById(R.id.time);
        TextView eventDescription = (TextView) rootView.findViewById(R.id.description);
        Button interactButton = (Button) rootView.findViewById(R.id.interact_button);

        SimpleDateFormat dayFormat = new SimpleDateFormat("MMMM dd, yyyy", java.util.Locale.getDefault());
        Date eventDate = new Date();

        try{
            eventDate = dayFormat.parse(date[mPageNumber]);
        }
        catch(ParseException p){
            Toast toast = Toast.makeText(getActivity(), "Error parsing dates.", Toast.LENGTH_SHORT);
            toast.show();
            p.printStackTrace();
        }

        try {
            URL imageURL = new URL(images[mPageNumber]);
            DownloadDetailsImages ddi = new DownloadDetailsImages(imageURL, eventCover);
            ddi.execute();
        } catch (Exception e) {
            Toast toast = Toast.makeText(getActivity(), "Something went wrong in downloading the image.", Toast.LENGTH_SHORT);
            toast.show();
            e.printStackTrace();
        }

        eventTitle.setText(title[mPageNumber]);
        eventLocation.setText(location[mPageNumber]);
        eventTime.setText(date[mPageNumber] + " " + time[mPageNumber]);
        eventDescription.setText(description[mPageNumber]);

        if (hasRaffle[mPageNumber]) {
            interactButton.setVisibility(View.VISIBLE);

            RaffleSQLiteHelper entry = new RaffleSQLiteHelper(getActivity());
            String ticketID = entry.getEntries(ids[mPageNumber]);

            if (ticketID != null) {
                interactButton.setText("Ticket: #" + ticketID);
                interactButton.setClickable(false);
            } else {
                RaffleEntryClickListener enterRaffle = new RaffleEntryClickListener(interactButton, entry, ids[mPageNumber]);

                interactButton.setOnClickListener(enterRaffle);
            }
        }
/*        else{
            if(eventDate.compareTo(new Date()) >= 0) {
                interactButton.setVisibility(View.VISIBLE);
                interactButton.setText("Notify Me");
            }
        }*/

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