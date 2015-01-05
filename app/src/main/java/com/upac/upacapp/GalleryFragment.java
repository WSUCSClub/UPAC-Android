package com.upac.upacapp;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;

public class GalleryFragment extends Fragment {
    public static View galleryView;
    public static final String TAG = "gallery";
    private ViewGroup parent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parent = container;
        return galleryView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            galleryView = getActivity().getLayoutInflater().inflate(R.layout.fragment_gallery, parent, false);
            Session session = AppDelegates.loadFBSession(getActivity());
            final double PER_LINE = 3.0;

            Bundle params = new Bundle();
            params.putString("fields", "photos");

            new Request(
                    session,
                    "/WSU.UPAC/albums",
                    params,
                    HttpMethod.GET,
                    new Request.Callback() {
                        public void onCompleted(Response response) {
                            String croppedSRC;
                            URL croppedURL;

                            LinearLayout ll = (LinearLayout) galleryView.findViewById(R.id.galleryLayout);

                            try {
                                int count = 0;
                                JSONArray arr = response.getGraphObject().getInnerJSONObject().getJSONArray("data").getJSONObject(0).getJSONObject("photos").getJSONArray("data");
                                int lineCount = (int) Math.ceil(arr.length() / PER_LINE);

                                ImageView[] iv = new ImageView[arr.length()];
                                LinearLayout[] lines = new LinearLayout[lineCount];

                                String[] fullSRC = new String[arr.length()];
                                GalleryClickListener[] listeners = new GalleryClickListener[arr.length()];

                                for (int x = 0; x < lineCount; x++) {
                                    lines[x] = new LinearLayout(getActivity());
                                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                                    lines[x].setLayoutParams(params);
                                    ll.addView(lines[x]);

                                    for (int i = 0; i < PER_LINE && count < arr.length(); i++) {
                                        JSONObject json_obj = arr.getJSONObject(count);

                                        croppedSRC = json_obj.getString("picture");
                                        fullSRC[count] = json_obj.getString("source");

                                        LinearLayout.LayoutParams imgParams = new LinearLayout.LayoutParams(0, 360, 1f);

                                        iv[count] = new ImageView(getActivity());

                                        croppedURL = new URL(croppedSRC);
                                        DownloadGalleryImages dgi = new DownloadGalleryImages(croppedURL, iv[count]);
                                        dgi.execute();

                                        listeners[count] = new GalleryClickListener(getActivity());
                                        listeners[count].setOpenedImage(count);

                                        iv[count].setId(count);
                                        iv[count].setPadding(2, 2, 2, 2);
                                        iv[count].setLayoutParams(imgParams);
                                        iv[count].setClickable(true);
                                        iv[count].setOnClickListener(listeners[count]);

                                        lines[x].addView(iv[count]);

                                        count++;
                                    }    // End of for loop
                                }   // End of for loop

                                for (GalleryClickListener g : listeners) {
                                    g.setPageAmount(arr.length());
                                    g.setInformation(fullSRC);
                                }
                            } catch (Exception e) {
                                Toast toast = Toast.makeText(getActivity(), "Something went wrong. Please restart the app.", Toast.LENGTH_SHORT);
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