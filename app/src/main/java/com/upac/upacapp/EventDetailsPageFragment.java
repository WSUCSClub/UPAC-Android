package com.upac.upacapp;

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
        Button raffleButton = (Button) rootView.findViewById(R.id.raffle_button);

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
            raffleButton.setVisibility(View.VISIBLE);

            RaffleSQLiteHelper entry = new RaffleSQLiteHelper(getActivity());
            String ticketID = entry.getEntries(ids[mPageNumber]);

            if (ticketID != null) {
                raffleButton.setText("Ticket number: #" + ticketID);
                raffleButton.setClickable(false);
            }
            else{
                RaffleEntryClickListener enterRaffle = new RaffleEntryClickListener(raffleButton, entry, ids[mPageNumber]);

                raffleButton.setOnClickListener(enterRaffle);
            }
        }

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