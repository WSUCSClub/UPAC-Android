package com.upac.upacapp;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
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
    protected static View galleryView;
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

            DisplayMetrics displayMetrics = getActivity().getResources().getDisplayMetrics();
            final int width = displayMetrics.widthPixels;
            final int PER_LINE = width / 240;

            Bundle params = new Bundle();
            params.putString("fields", "name,photos");

            new Request(
                    session,
                    "/WSU.UPAC/albums",
                    params,
                    HttpMethod.GET,
                    new Request.Callback() {
                        public void onCompleted(Response response) {
                            LinearLayout imagesLayout = (LinearLayout) galleryView.findViewById(R.id.galleryLayout);
                            int allPhotos = 0;

                            try {
                                JSONArray albums = response.getGraphObject().getInnerJSONObject().getJSONArray("data");
                                JSONArray photos = new JSONArray();
                                JSONObject album;

                                for (int i = 0; i < albums.length(); i++) {
                                    album = albums.getJSONObject(i);
                                    photos.put(album.getJSONObject("photos").getJSONArray("data"));
                                    allPhotos += photos.getJSONArray(i).length();
                                }

                                int lineCount = allPhotos / PER_LINE;

                                ImageView galleryImage;
                                LinearLayout line;
                                JSONObject json_obj;

                                String[] fullSRC = new String[allPhotos];
                                GalleryClickListener[] listeners = new GalleryClickListener[allPhotos];

                                int count = 0;

                                int imgHeight = width / PER_LINE;

                                for (int albumNum = 0; albumNum < photos.length(); albumNum++) {
                                    int imageCount = 0;

                                    addAlbumName(albums, imagesLayout, albumNum);

                                    for (int lineNum = 0; lineNum < lineCount; lineNum++) {
                                        line = addLine(imagesLayout);

                                        for (int i = 0; i < PER_LINE && imageCount < photos.getJSONArray(albumNum).length(); i++) {
                                            json_obj = photos.getJSONArray(albumNum).getJSONObject(imageCount);
                                            fullSRC[count] = json_obj.getString("source");
                                            galleryImage = new ImageView(getActivity());

                                            getImage(json_obj, galleryImage);

                                            listeners[count] = new GalleryClickListener(getActivity());
                                            listeners[count].setOpenedImage(count);

                                            setGalleryImageParameters(galleryImage, count, imgHeight, listeners[count]);

                                            line.addView(galleryImage);

                                            count++;
                                            imageCount++;
                                        }    // End of for loop
                                    }   // End of for loop
                                }   // End of for loop

                                for (GalleryClickListener g : listeners) {
                                    g.setPageAmount(allPhotos);
                                    g.setInformation(fullSRC);
                                }
                            } catch (JSONException e) {
                                Toast toast = Toast.makeText(getActivity(), "Could not get gallery photos from Facebook. Please check your internet connection and restart the app.", Toast.LENGTH_LONG);
                                toast.show();
                                e.printStackTrace();
                            } catch (NullPointerException e) {
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

    private void addAlbumName(JSONArray albums, LinearLayout imagesLayout, int object) {
        TextView albumName;
        LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        textParams.gravity = Gravity.START;
        albumName = new TextView(getActivity());
        albumName.setLayoutParams(textParams);
        try {
            albumName.setText(albums.getJSONObject(object).getString("name"));
        } catch (JSONException e) {
            Toast toast = Toast.makeText(getActivity(), "Could not get gallery photos from Facebook. Please check your internet connection and restart the app.", Toast.LENGTH_LONG);
            toast.show();
            e.printStackTrace();
        }
        albumName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        albumName.setTypeface(null, Typeface.BOLD);
        albumName.setPadding(20, 5, 20, 5);
        imagesLayout.addView(albumName);
    }

    private LinearLayout addLine(LinearLayout imagesLayout) {
        LinearLayout line = new LinearLayout(getActivity());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        line.setLayoutParams(params);
        imagesLayout.addView(line);

        return line;
    }

    private void getImage(JSONObject json_obj, ImageView galleryImage) {
        try {
            String croppedSRC = json_obj.getString("picture");
            try {
                URL croppedURL = new URL(croppedSRC);
                DownloadGalleryImages dgi = new DownloadGalleryImages(croppedURL, galleryImage);

                dgi.execute();
            } catch (MalformedURLException m) {
                Toast toast = Toast.makeText(getActivity(), "Malformed URL.", Toast.LENGTH_SHORT);
                toast.show();
                m.printStackTrace();
            }
        } catch (JSONException e) {
            Toast toast = Toast.makeText(getActivity(), "Could not get gallery photos from Facebook. Please check your internet connection and restart the app.", Toast.LENGTH_LONG);
            toast.show();
            e.printStackTrace();
        }
    }

    private void setGalleryImageParameters(ImageView galleryImage, int count, int imgHeight, GalleryClickListener listener) {
        LinearLayout.LayoutParams imgParams = new LinearLayout.LayoutParams(0, imgHeight, 1f);
        imgParams.gravity = Gravity.START;

        galleryImage.setId(count);
        galleryImage.setPadding(2, 2, 2, 2);
        galleryImage.setLayoutParams(imgParams);
        galleryImage.setClickable(true);
        galleryImage.setOnClickListener(listener);
    }
}