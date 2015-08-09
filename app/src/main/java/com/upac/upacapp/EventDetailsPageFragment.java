package com.upac.upacapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class EventDetailsPageFragment extends Fragment {
    public static final String ARG_PAGE = "page";
    protected static int mPageNumber;
    private static String[] title;
    private static String[] location;
    private static String[] time;
    private static String[] description;
    private static String[] images;
    private static String[] date;
    private static String[] ids;
    private static boolean[] hasRaffle;
    private ScheduleClient scheduleClient;

    public EventDetailsPageFragment() {

    }

    public void setInformation(final String[] a, final String[] b, final String[] c, final String[] d, final String[] e, final String[] f,
                               final boolean[] g, final String[] h) {
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
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPageNumber = this.getArguments().getInt(ARG_PAGE);
    }

    @Override
    public View onCreateView(final LayoutInflater inf, final ViewGroup container, final Bundle savedInstanceState) {
        scheduleClient = new ScheduleClient(this.getActivity());
        scheduleClient.doBindService();

        final View rootView = this.getActivity().getLayoutInflater().inflate(R.layout.fragment_event_description, container, false);
        final ImageView eventCover = (ImageView) rootView.findViewById(R.id.event_image);
        final TextView eventTitle = (TextView) rootView.findViewById(R.id.title);
        final TextView eventLocation = (TextView) rootView.findViewById(R.id.location);
        final TextView eventTime = (TextView) rootView.findViewById(R.id.time);
        final TextView eventDescription = (TextView) rootView.findViewById(R.id.description);
        final Button interactButton = (Button) rootView.findViewById(R.id.interact_button);

        try {
            final URL imageURL = new URL(images[mPageNumber]);
            final DownloadDetailsImages ddi = new DownloadDetailsImages(imageURL, eventCover);
            ddi.execute();
        } catch (final MalformedURLException e) {
            final Toast toast = Toast.makeText(this.getActivity(), "Something went wrong in downloading the image.", Toast.LENGTH_SHORT);
            toast.show();
            e.printStackTrace();
        }

        final DisplayMetrics displayMetrics = this.getActivity().getResources().getDisplayMetrics();
        final int height = displayMetrics.heightPixels;
        final int imgHeight = (int) (height / 2.3);

        eventCover.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, imgHeight));
        eventTitle.setText(title[mPageNumber]);
        eventLocation.setText(location[mPageNumber]);
        eventTime.setText(date[mPageNumber] + ' ' + time[mPageNumber]);
        eventDescription.setText(description[mPageNumber]);

        final RaffleSQLiteHelper entry = new RaffleSQLiteHelper(this.getActivity());

        if (hasRaffle[mPageNumber]) {
            interactButton.setVisibility(View.VISIBLE);

            final String ticketID = entry.getEntries(ids[mPageNumber]);

            if (ticketID != null) {
                interactButton.setText("Ticket: #" + ticketID);
                interactButton.setClickable(false);
            } else {
                final OnClickListener enterRaffle = new RaffleEntryClickListener(interactButton, entry, ids[mPageNumber]);

                interactButton.setOnClickListener(enterRaffle);
            }
        } else {
            final NotifySQLiteHelper notified = new NotifySQLiteHelper(this.getActivity());
            final String notification = notified.getEntries(ids[mPageNumber]);

            if (notification == null) {
                final Calendar cal = Calendar.getInstance();
                final SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd, yyyy hh:mm a", Locale.getDefault());

                try {
                    cal.setTime(sdf.parse(date[mPageNumber] + " " + time[mPageNumber]));

                    final OnClickListener notifyMe = new NotifyMeClickListener(interactButton, notified, cal, this.getActivity(), scheduleClient,
                            ids[mPageNumber]);

                    interactButton.setVisibility(View.VISIBLE);
                    interactButton.setText("Notify Me");
                    interactButton.setOnClickListener(notifyMe);
                } catch (final ParseException p) {
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

    public EventDetailsPageFragment create(final int pageNumber) {
        final EventDetailsPageFragment fragment = new EventDetailsPageFragment();
        final Bundle args = new Bundle();

        args.putInt(ARG_PAGE, pageNumber);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onStop() {
        // When our activity is stopped ensure we also stop the connection to the service
        // this stops us leaking our activity into the system *bad*
        if (scheduleClient != null) {
            scheduleClient.doUnbindService();
        }
        super.onStop();
    }
}