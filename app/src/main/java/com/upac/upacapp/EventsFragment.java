package com.upac.upacapp;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class EventsFragment extends Fragment {
    private static View eventsView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return eventsView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            eventsView = getActivity().getLayoutInflater().inflate(R.layout.fragment_events, null);
            Session session = AppDelegates.loadFBSession(getActivity());
            Calendar since = Calendar.getInstance();
            since.add(Calendar.DATE, -60);

            Bundle params = new Bundle();
            params.putString("fields", "events.since(" + since.getTime() + "){description,cover,location,name,start_time}");

            new Request(
                    session,
                    "/WSU.UPAC",
                    params,
                    HttpMethod.GET,
                    new Request.Callback() {
                        public void onCompleted(Response response) {
                            String location, eventName, description, image;
                            Date date;
                            URL imageURL;

                            LinearLayout ll = (LinearLayout) eventsView.findViewById(R.id.eventsLayout);

                            SimpleDateFormat inFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
                            SimpleDateFormat dayFormat = new SimpleDateFormat("MMMM dd, yyyy", java.util.Locale.getDefault());
                            SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", java.util.Locale.getDefault());

                            try {
                                JSONArray arr = response.getGraphObject().getInnerJSONObject().getJSONObject("events").getJSONArray("data");
                                LinearLayout[] lines = new LinearLayout[arr.length()];
                                TextView[] tv = new TextView[arr.length()];
                                ImageView[] iv = new ImageView[arr.length()];

                                for (int i = 0; i < (arr.length()); i++) {
                                    JSONObject json_obj = arr.getJSONObject(i);

                                    date = inFormat.parse(json_obj.getString("start_time"));

                                    location = json_obj.getString("location");
                                    eventName = json_obj.getString("name");
                                    description = json_obj.getString("description");

                                    try {
                                        image = json_obj.getJSONObject("cover").getString("source");
                                    } catch (Exception e) {
                                        image = "nothing";
                                    }

                                    LinearLayout.LayoutParams imgParams = new LinearLayout.LayoutParams(550, 290, 1f);

                                    iv[i] = new ImageView(getActivity());

                                    imageURL = new URL(image);
                                    DownloadEventImages dri = new DownloadEventImages(imageURL, iv[i]);
                                    dri.execute();

                                    iv[i].setId(i);
                                    iv[i].setPadding(25, 0, 25, 0);
                                    iv[i].setLayoutParams(imgParams);

                                    GradientDrawable gd = new GradientDrawable();
                                    gd.setColor(Color.WHITE);
                                    gd.setStroke(5, 0x000000);

                                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1f);

                                    tv[i] = new TextView(getActivity());
                                    tv[i].setText(eventName + "\n" + dayFormat.format(date) + "\n" + timeFormat.format(date) + "\n" + location);
                                    tv[i].setTextColor(Color.BLACK);
                                    tv[i].setId(i);
                                    tv[i].setPadding(0, 75, 0, 0);
                                    tv[i].setLayoutParams(params);

                                    lines[i] = new LinearLayout(getActivity());
                                    lines[i].setBackground(gd);
                                    lines[i].setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                                    lines[i].addView(iv[i]);
                                    lines[i].addView(tv[i]);

                                    ll.addView(lines[i]);
                                }    // End of for loop
                            } catch (Exception e) {
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