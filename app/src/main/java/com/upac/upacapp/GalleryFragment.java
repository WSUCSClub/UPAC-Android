package com.upac.upacapp;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.Gravity;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
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
            params.putString("fields", "name,photos");

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
                            int allPhotos = 0;

                            try {
                                JSONArray albums = response.getGraphObject().getInnerJSONObject().getJSONArray("data");
                                JSONArray photos = new JSONArray();
                                for (int i = 0; i < albums.length(); i++) {
                                    JSONObject album = albums.getJSONObject(i);
                                    photos.put(album.getJSONObject("photos").getJSONArray("data"));
                                    allPhotos += photos.getJSONArray(i).length();
                                }

                                int lineCount = (int) Math.ceil(allPhotos / PER_LINE);

                                ImageView[] iv = new ImageView[allPhotos];
                                LinearLayout[] lines = new LinearLayout[lineCount];

                                String[] fullSRC = new String[allPhotos];
                                GalleryClickListener[] listeners = new GalleryClickListener[allPhotos];

                                int count = 0;

                                TextView[] albumNames = new TextView[photos.length()];

                                for (int c = 0; c < photos.length(); c++) {
                                    int albumCount = 0;

                                    LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                    textParams.gravity = Gravity.START;
                                    albumNames[c] = new TextView(getActivity());
                                    albumNames[c].setLayoutParams(textParams);
                                    albumNames[c].setText(albums.getJSONObject(c).getString("name"));
                                    albumNames[c].setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                                    albumNames[c].setTypeface(null, Typeface.BOLD);
                                    albumNames[c].setPadding(20, 5, 20, 5);
                                    ll.addView(albumNames[c]);

                                    for (int x = 0; x < photos.getJSONArray(c).length(); x++) {
                                        lines[x] = new LinearLayout(getActivity());
                                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                                        lines[x].setLayoutParams(params);
                                        ll.addView(lines[x]);

                                        for (int i = 0; i < PER_LINE && albumCount < photos.getJSONArray(c).length(); i++) {
                                            JSONObject json_obj = photos.getJSONArray(c).getJSONObject(albumCount);

                                            croppedSRC = json_obj.getString("picture");
                                            fullSRC[count] = json_obj.getString("source");

                                            LinearLayout.LayoutParams imgParams = new LinearLayout.LayoutParams(0, 360, 1f);
                                            imgParams.gravity = Gravity.START;

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
                                            albumCount++;
                                        }    // End of for loop
                                    }   // End of for loop
                                }   // End of for loop

                                for (GalleryClickListener g : listeners) {
                                    g.setPageAmount(allPhotos);
                                    g.setInformation(fullSRC);
                                }
                            } catch (MalformedURLException m) {
                                Toast toast = Toast.makeText(getActivity(), "Malformed URL.", Toast.LENGTH_SHORT);
                                toast.show();
                                m.printStackTrace();
                            } catch (JSONException e) {
                                Toast toast = Toast.makeText(getActivity(), "Could not get gallery photos from Facebook. Please check your internet connection and restart the app.", Toast.LENGTH_LONG);
                                toast.show();
                                e.printStackTrace();
                            } catch (NullPointerException e){
                                Toast toast = Toast.makeText(getActivity(), "Cannot access Facebook photos. Please check your internet connection and restart the app.", Toast.LENGTH_LONG);
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