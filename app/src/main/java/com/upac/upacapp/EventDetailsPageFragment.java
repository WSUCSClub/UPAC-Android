package com.upac.upacapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class EventDetailsPageFragment extends Fragment {
    public static final String ARG_PAGE = "page";
    protected static int mPageNumber;
    private static String[] title, location, time, description, images, date, ids;
    private static boolean[] hasRaffle;
    private ScheduleClient scheduleClient;

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
        scheduleClient = new ScheduleClient(getActivity());
        scheduleClient.doBindService();

        View rootView = getActivity().getLayoutInflater().inflate(R.layout.fragment_event_description, container, false);
        ImageView eventCover = (ImageView) rootView.findViewById(R.id.event_image);
        TextView eventTitle = (TextView) rootView.findViewById(R.id.title);
        TextView eventLocation = (TextView) rootView.findViewById(R.id.location);
        TextView eventTime = (TextView) rootView.findViewById(R.id.time);
        TextView eventDescription = (TextView) rootView.findViewById(R.id.description);
        Button interactButton = (Button) rootView.findViewById(R.id.interact_button);

        try {
            URL imageURL = new URL(images[mPageNumber]);
            DownloadDetailsImages ddi = new DownloadDetailsImages(imageURL, eventCover);
            ddi.execute();
        } catch (Exception e) {
            Toast toast = Toast.makeText(getActivity(), "Something went wrong in downloading the image.", Toast.LENGTH_SHORT);
            toast.show();
            e.printStackTrace();
        }

        DisplayMetrics displayMetrics = getActivity().getResources().getDisplayMetrics();
        int height = displayMetrics.heightPixels;
        int imgHeight = (int) (height / 2.3);

        eventCover.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, imgHeight));
        eventTitle.setText(title[mPageNumber]);
        eventLocation.setText(location[mPageNumber]);
        eventTime.setText(date[mPageNumber] + " " + time[mPageNumber]);
        eventDescription.setText(description[mPageNumber]);

        RaffleSQLiteHelper entry = new RaffleSQLiteHelper(getActivity());

        if (hasRaffle[mPageNumber]) {
            interactButton.setVisibility(View.VISIBLE);

            String ticketID = entry.getEntries(ids[mPageNumber]);

            if (ticketID != null) {
                interactButton.setText("Ticket: #" + ticketID);
                interactButton.setClickable(false);
            } else {
                RaffleEntryClickListener enterRaffle = new RaffleEntryClickListener(interactButton, entry, ids[mPageNumber]);

                interactButton.setOnClickListener(enterRaffle);
            }
        } else {
            NotifySQLiteHelper notified = new NotifySQLiteHelper(getActivity());

            String notification = notified.getEntries(ids[mPageNumber]);

            if (notification == null) {
                Calendar cal = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd, yyyy hh:mm a", java.util.Locale.getDefault());

                try {
                    cal.setTime(sdf.parse(date[mPageNumber] + " " + time[mPageNumber]));

                    NotifyMeClickListener notifyMe = new NotifyMeClickListener(interactButton, notified, cal, getActivity(), scheduleClient, ids[mPageNumber]);

                    interactButton.setVisibility(View.VISIBLE);
                    interactButton.setText("Notify Me");
                    interactButton.setOnClickListener(notifyMe);
                } catch (ParseException p) {
                    p.printStackTrace();
                }
            } else {
                interactButton.setVisibility(View.VISIBLE);
                interactButton.setText("To be notified");
                interactButton.setClickable(false);
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

    @Override
    public void onStop() {
        // When our activity is stopped ensure we also stop the connection to the service
        // this stops us leaking our activity into the system *bad*
        if (scheduleClient != null)
            scheduleClient.doUnbindService();
        super.onStop();
    }
}