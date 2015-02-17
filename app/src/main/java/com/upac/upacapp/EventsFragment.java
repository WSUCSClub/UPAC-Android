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

public class EventsFragment extends Fragment {
    public static final String TAG = "events";
    private static View eventsView;
    private ViewGroup parent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parent = container;
        return eventsView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            eventsView = getActivity().getLayoutInflater().inflate(R.layout.fragment_events, parent, false);
            Session session = AppDelegates.loadFBSession(getActivity());
            Calendar since = Calendar.getInstance();

            Bundle params = new Bundle();
            params.putString("fields", "events.since(" + since.getTime() + "){description,cover,location,name,start_time}");

            new Request(
                    session,
                    "/WSU.UPAC",
                    params,
                    HttpMethod.GET,
                    new Request.Callback() {
                        public void onCompleted(Response response) {
                            String location, eventName, image;
                            Date date;
                            URL imageURL;

                            LinearLayout ll = (LinearLayout) eventsView.findViewById(R.id.eventsLayout);

                            SimpleDateFormat inFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
                            SimpleDateFormat dayFormat = new SimpleDateFormat("MMMM dd, yyyy", java.util.Locale.getDefault());
                            SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", java.util.Locale.getDefault());

                            try {
                                JSONArray events = response.getGraphObject().getInnerJSONObject().getJSONObject("events").getJSONArray("data");

                                App parse = new App();

                                List<ParseObject> raffles = parse.getRaffles();
                                String[] eventRaffles = new String[raffles.size()];

                                Date today = new Date();

                                for (int x = 0; x < raffles.size(); x++) {
                                    if (raffles.get(x).getDate("endDate").compareTo(today) > 0 && raffles.get(x).getDate("date").compareTo(today) < 0)
                                        eventRaffles[x] = raffles.get(x).getString("eventId");
                                    else
                                        eventRaffles[x] = "-1";
                                }

                                LinearLayout[] lines = new LinearLayout[events.length()];
                                LinearLayout[] infoLayout = new LinearLayout[events.length()];
                                TextView[] eventTitle = new TextView[events.length()];
                                TextView[] eventInfo = new TextView[events.length()];
                                ImageView[] eventImage = new ImageView[events.length()];
                                String[] titles = new String[events.length()];
                                String[] locations = new String[events.length()];
                                String[] dates = new String[events.length()];
                                String[] times = new String[events.length()];
                                String[] descriptions = new String[events.length()];
                                String[] images = new String[events.length()];
                                String[] ids = new String[events.length()];
                                boolean[] hasRaffle = new boolean[events.length()];
                                EventDetailsClickListener[] listeners = new EventDetailsClickListener[events.length()];

                                for (int i = 0; i < (events.length()); i++) {
                                    JSONObject jsonObj = events.getJSONObject(events.length() - i - 1);

                                    date = inFormat.parse(jsonObj.getString("start_time"));
                                    dates[i] = dayFormat.format(date);
                                    times[i] = timeFormat.format(date);

                                    location = jsonObj.getString("location");
                                    locations[i] = jsonObj.getString("location");
                                    eventName = jsonObj.getString("name");
                                    titles[i] = eventName.replaceAll("UPAC Presents: ", "");
                                    ;
                                    descriptions[i] = jsonObj.getString("description");
                                    ids[i] = jsonObj.getString("id");

                                    try {
                                        image = jsonObj.getJSONObject("cover").getString("source");
                                        images[i] = jsonObj.getJSONObject("cover").getString("source");
                                    } catch (Exception e) {
                                        image = "nothing";
                                    }

                                    DisplayMetrics displayMetrics = getActivity().getResources().getDisplayMetrics();
                                    int width = displayMetrics.widthPixels;
                                    int imgHeight = width / 4;
                                    int imgWidth = (int) Math.ceil(width / 2.8);

                                    LinearLayout.LayoutParams imgParams = new LinearLayout.LayoutParams(imgWidth, imgHeight, 3f);

                                    eventImage[i] = new ImageView(getActivity());

                                    imageURL = new URL(image);
                                    DownloadEventImages dei = new DownloadEventImages(imageURL, eventImage[i], imgWidth, imgHeight, displayMetrics.density);
                                    dei.execute();

                                    listeners[i] = new EventDetailsClickListener(getActivity());
                                    listeners[i].setOpenedEvent(i);

                                    eventImage[i].setId(i);
                                    eventImage[i].setPadding(25, 0, 25, 0);
                                    eventImage[i].setLayoutParams(imgParams);

                                    LinearLayout.LayoutParams infoParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 7f);
                                    infoLayout[i] = new LinearLayout(getActivity());
                                    infoLayout[i].setLayoutParams(infoParams);
                                    infoLayout[i].setOrientation(LinearLayout.VERTICAL);

                                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                                    eventTitle[i] = new TextView(getActivity());
                                    eventTitle[i].setText(titles[i]);
                                    eventTitle[i].setTextColor(Color.BLACK);
                                    eventTitle[i].setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                                    eventTitle[i].setPadding(0, 25, 0, 0);
                                    eventTitle[i].setLayoutParams(params);

                                    eventInfo[i] = new TextView(getActivity());
                                    eventInfo[i].setText(dates[i] + "\n" + times[i] + "\n" + location);
                                    eventInfo[i].setTextColor(Color.BLACK);
                                    eventInfo[i].setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                                    eventInfo[i].setPadding(0, 0, 0, 25);
                                    eventInfo[i].setId(i);
                                    eventInfo[i].setLayoutParams(params);

                                    infoLayout[i].addView(eventTitle[i]);
                                    infoLayout[i].addView(eventInfo[i]);

                                    GradientDrawable gd = new GradientDrawable();

                                    hasRaffle[i] = false;

                                    for (String p : eventRaffles) {
                                        if (ids[i].equals(p)) {
                                            hasRaffle[i] = true;
                                        }
                                    }

                                    if (hasRaffle[i])
                                        gd.setColor(Color.parseColor("#ECECFF"));

                                    gd.setStroke(1, Color.parseColor("#E1E1E1"));

                                    lines[i] = new LinearLayout(getActivity());
                                    lines[i].setBackground(gd);
                                    lines[i].setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                                    lines[i].addView(eventImage[i]);
                                    lines[i].addView(infoLayout[i]);
                                    lines[i].setClickable(true);
                                    lines[i].setOnClickListener(listeners[i]);

                                    ll.addView(lines[i]);
                                }    // End of for loop

                                for (EventDetailsClickListener e : listeners) {
                                    e.setPageAmount(events.length());
                                    e.setInformation(titles, locations, dates, times, descriptions, images, hasRaffle, ids);
                                }
                            } catch (MalformedURLException m) {
                                Toast toast = Toast.makeText(getActivity(), "Malformed URL.", Toast.LENGTH_SHORT);
                                toast.show();
                                m.printStackTrace();
                            } catch (ParseException p) {
                                Toast toast = Toast.makeText(getActivity(), "Error parsing dates.", Toast.LENGTH_SHORT);
                                toast.show();
                                p.printStackTrace();
                            } catch (JSONException e) {
                                Toast toast = Toast.makeText(getActivity(), "Could not get Facebook events. Please check your internet connection and restart the app.", Toast.LENGTH_LONG);
                                toast.show();
                                e.printStackTrace();
                            } catch (NullPointerException e) {
                                Toast toast = Toast.makeText(getActivity(), "Cannot access Facebook events. Please check your internet connection and restart the app.", Toast.LENGTH_LONG);
                                toast.show();
                                e.printStackTrace();
                            }
                        }    // End of onCompleted
                    }    // End of Callback
            ).executeAsync();
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement MainActivity.");
        }
    }
}