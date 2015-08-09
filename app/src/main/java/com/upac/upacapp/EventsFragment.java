package com.upac.upacapp;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.parse.ParseObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class EventsFragment extends Fragment {
    public static final String TAG = "events";
    private static View eventsView;
    private ViewGroup parent;

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        parent = container;
        return eventsView;
    }

    @Override
    public void onAttach(final Activity activity) {
        super.onAttach(activity);

        final DisplayMetrics displayMetrics = this.getActivity().getResources().getDisplayMetrics();
        final int width = displayMetrics.widthPixels;
        final int imgHeight = width / 4;
        final int imgWidth = (int) Math.ceil(width / 2.8);

        try {
            eventsView = this.getActivity().getLayoutInflater().inflate(R.layout.fragment_events, parent, false);
            final Session session = AppDelegates.loadFBSession(this.getActivity());
            final Calendar since = Calendar.getInstance();

            final Bundle params = new Bundle();
            params.putString("fields", "events.since(" + since.getTime() + "){description,cover,location,name,start_time}");

            new Request(
                    session,
                    "/WSU.UPAC",
                    params,
                    HttpMethod.GET,
                    new Request.Callback() {
                        public void onCompleted(final Response response) {
                            String eventName;
                            Date date;
                            URL imageURL;

                            final LinearLayout eventsLayout = (LinearLayout) eventsView.findViewById(R.id.eventsLayout);

                            final SimpleDateFormat inFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
                            final SimpleDateFormat dayFormat = new SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault());
                            final SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());

                            try {
                                final JSONArray events = response.getGraphObject().getInnerJSONObject().getJSONObject("events").getJSONArray("data");
                                final App parse = new App();
                                final List<ParseObject> raffles = parse.getRaffles();
                                final String[] eventRaffles = new String[raffles.size()];
                                final Date today = new Date();
                                final LinearLayout[] infoLayout = new LinearLayout[events.length()];
                                final ImageView[] eventImage = new ImageView[events.length()];
                                final String[] titles = new String[events.length()];
                                final String[] locations = new String[events.length()];
                                final String[] dates = new String[events.length()];
                                final String[] times = new String[events.length()];
                                final String[] descriptions = new String[events.length()];
                                final String[] images = new String[events.length()];
                                final String[] ids = new String[events.length()];
                                final boolean[] hasRaffle = new boolean[events.length()];
                                final EventDetailsClickListener[] listeners = new EventDetailsClickListener[events.length()];
                                final int raffleCount = raffles.size();
                                final int eventCount = events.length();

                                for (int x = 0; x < raffleCount; x++) {
                                    eventRaffles[x] = (raffles.get(x).getDate("endDate").compareTo(today) > 0 && raffles.get(x).getDate("date")
                                            .compareTo(today) < 0) ? raffles.get(x).getString("eventId") : "-1";
                                }

                                for (int i = 0; i < eventCount; i++) {
                                    final JSONObject jsonObj = events.getJSONObject(events.length() - i - 1);

                                    date = inFormat.parse(jsonObj.getString("start_time"));
                                    dates[i] = dayFormat.format(date);
                                    times[i] = timeFormat.format(date);

                                    locations[i] = jsonObj.getString("location");
                                    eventName = jsonObj.getString("name");
                                    titles[i] = eventName.replaceAll("UPAC Presents: ", "");
                                    descriptions[i] = jsonObj.getString("description");
                                    ids[i] = jsonObj.getString("id");
                                    images[i] = jsonObj.getJSONObject("cover").getString("source");

                                    final LayoutParams imgParams = new LayoutParams(imgWidth, imgHeight, 3f);

                                    eventImage[i] = new ImageView(getActivity());

                                    imageURL = new URL(images[i]);
                                    final DownloadEventImages dei = new DownloadEventImages(imageURL, eventImage[i]);
                                    dei.execute();

                                    listeners[i] = new EventDetailsClickListener(getActivity());
                                    listeners[i].setOpenedEvent(i);

                                    eventImage[i].setId(i);
                                    eventImage[i].setPadding(25, 0, 25, 0);
                                    eventImage[i].setLayoutParams(imgParams);

                                    final LayoutParams infoParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 7f);
                                    infoLayout[i] = new LinearLayout(getActivity());
                                    infoLayout[i].setLayoutParams(infoParams);
                                    infoLayout[i].setOrientation(LinearLayout.VERTICAL);

                                    final LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

                                    addEventTitle(infoLayout[i], titles[i], params);
                                    addEventInfo(infoLayout[i], dates[i], times[i], locations[i], params);

                                    hasRaffle[i] = false;

                                    for (final String p : eventRaffles) {
                                        if (ids[i].equals(p)) {
                                            hasRaffle[i] = true;
                                        }
                                    }

                                    final GradientDrawable gd = new GradientDrawable();

                                    if (hasRaffle[i]) {
                                        gd.setColor(Color.parseColor("#ECECFF"));
                                    }

                                    gd.setStroke(1, Color.parseColor("#E1E1E1"));

                                    addLine(gd, eventImage[i], infoLayout[i], listeners[i], eventsLayout);
                                }    // End of for loop

                                for (EventDetailsClickListener e : listeners) {
                                    e.setPageAmount(events.length());
                                    e.setInformation(titles, locations, dates, times, descriptions, images, hasRaffle, ids);
                                }
                            } catch (final MalformedURLException m) {
                                final Toast toast = Toast.makeText(getActivity(), "Malformed URL.", Toast.LENGTH_SHORT);
                                toast.show();
                                m.printStackTrace();
                            } catch (final ParseException p) {
                                final Toast toast = Toast.makeText(getActivity(), "Error parsing dates.", Toast.LENGTH_SHORT);
                                toast.show();
                                p.printStackTrace();
                            } catch (final JSONException e) {
                                final Toast toast = Toast.makeText(getActivity(), "Could not get Facebook events. Please check your internet connection and restart the app.", Toast.LENGTH_LONG);
                                toast.show();
                                e.printStackTrace();
                            } catch (final NullPointerException e) {
                                final Toast toast = Toast.makeText(getActivity(), "Cannot access Facebook events. Please check your internet connection and restart the app.", Toast.LENGTH_LONG);
                                toast.show();
                                e.printStackTrace();
                            }
                        }    // End of onCompleted
                    }    // End of Callback
            ).executeAsync();
        } catch (final ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement MainActivity.");
        }
    }

    private void addEventTitle(final LinearLayout infoLayout, final String title, final LayoutParams params) {
        final TextView eventTitle = new TextView(getActivity());
        eventTitle.setText(title);
        eventTitle.setTextColor(Color.BLACK);
        eventTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        eventTitle.setPadding(0, 25, 0, 0);
        eventTitle.setLayoutParams(params);

        infoLayout.addView(eventTitle);
    }

    private void addEventInfo(final LinearLayout infoLayout, final String date, final String time, final String location, final LayoutParams params) {
        final TextView eventInfo = new TextView(getActivity());
        eventInfo.setText(date + String.format("%n") + time + String.format("%n") + location);
        eventInfo.setTextColor(Color.BLACK);
        eventInfo.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        eventInfo.setPadding(0, 0, 0, 25);
        eventInfo.setLayoutParams(params);

        infoLayout.addView(eventInfo);
    }

    private void addLine(final GradientDrawable gradient, final ImageView eventImage, final LinearLayout infoLayout,
                         final EventDetailsClickListener listener, final LinearLayout eventsLayout) {
        final LinearLayout line = new LinearLayout(getActivity());
        line.setBackground(gradient);
        line.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        line.addView(eventImage);
        line.addView(infoLayout);
        line.setClickable(true);
        line.setOnClickListener(listener);

        eventsLayout.addView(line);
    }
}