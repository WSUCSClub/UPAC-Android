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
import android.widget.LinearLayout.LayoutParams;
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
    private static View galleryView;
    public static final String TAG = "gallery";
    private ViewGroup parent;

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        parent = container;
        return galleryView;
    }

    @Override
    public void onAttach(final Activity activity) {
        super.onAttach(activity);

        try {
            galleryView = this.getActivity().getLayoutInflater().inflate(R.layout.fragment_gallery, parent, false);
            final Session session = AppDelegates.loadFBSession(this.getActivity());

            final DisplayMetrics displayMetrics = this.getActivity().getResources().getDisplayMetrics();
            final int width = displayMetrics.widthPixels;
            final int PER_LINE = width / 240;

            final Bundle params = new Bundle();
            params.putString("fields", "name,photos");

            new Request(
                    session,
                    "/WSU.UPAC/albums",
                    params,
                    HttpMethod.GET,
                    new Request.Callback() {
                        public void onCompleted(final Response response) {
                            final LinearLayout imagesLayout = (LinearLayout) galleryView.findViewById(R.id.galleryLayout);

                            try {
                                final JSONArray albums = response.getGraphObject().getInnerJSONObject().getJSONArray("data");
                                final JSONArray photos = new JSONArray();
                                final int albumCount = albums.length();
                                int allPhotos = 0;

                                for (int i = 0; i < albumCount; i++) {
                                    final JSONObject album = albums.getJSONObject(i);
                                    photos.put(album.getJSONObject("photos").getJSONArray("data"));
                                    allPhotos += photos.getJSONArray(i).length();
                                }

                                final int lineCount = allPhotos / PER_LINE;
                                final String[] fullSRC = new String[allPhotos];
                                final GalleryClickListener[] listeners = new GalleryClickListener[allPhotos];
                                final int imgHeight = width / PER_LINE;
                                final int photoCount = photos.length();

                                int count = 0;

                                for (int albumNum = 0; albumNum < photoCount; albumNum++) {
                                    int imageCount = 0;

                                    addAlbumName(albums, imagesLayout, albumNum);

                                    for (int lineNum = 0; lineNum < lineCount; lineNum++) {
                                        final LinearLayout line = addLine(imagesLayout);

                                        for (int i = 0; i < PER_LINE && imageCount < photos.getJSONArray(albumNum).length(); i++) {
                                            final JSONObject json_obj = photos.getJSONArray(albumNum).getJSONObject(imageCount);
                                            fullSRC[count] = json_obj.getString("source");
                                            final ImageView galleryImage = new ImageView(getActivity());

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
                            } catch (final JSONException e) {
                                final Toast toast = Toast.makeText(getActivity(), "Could not get gallery photos from Facebook. Please check your " +
                                        "internet connection and restart the app.", Toast.LENGTH_LONG);
                                toast.show();
                                e.printStackTrace();
                            } catch (final NullPointerException e) {
                                final Toast toast = Toast.makeText(getActivity(), "Cannot access Facebook photos. Please check your internet connection and restart the app.", Toast.LENGTH_LONG);
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

    private void addAlbumName(final JSONArray albums, final LinearLayout imagesLayout, final int object) {
        final LayoutParams textParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        textParams.gravity = Gravity.START;
        final TextView albumName = new TextView(this.getActivity());
        albumName.setLayoutParams(textParams);
        try {
            albumName.setText(albums.getJSONObject(object).getString("name"));
        } catch (JSONException e) {
            final Toast toast = Toast.makeText(this.getActivity(), "Could not get gallery photos from Facebook. Please check your internet " +
                    "connection and restart the app.", Toast.LENGTH_LONG);
            toast.show();
            e.printStackTrace();
        }
        albumName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        albumName.setTypeface(null, Typeface.BOLD);
        albumName.setPadding(20, 5, 20, 5);
        imagesLayout.addView(albumName);
    }

    private LinearLayout addLine(final LinearLayout imagesLayout) {
        final LinearLayout line = new LinearLayout(this.getActivity());
        final LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        line.setLayoutParams(params);
        imagesLayout.addView(line);

        return line;
    }

    private void getImage(final JSONObject json_obj, final ImageView galleryImage) {
        try {
            final String croppedSRC = json_obj.getString("picture");
            try {
                final URL croppedURL = new URL(croppedSRC);
                final DownloadGalleryImages dgi = new DownloadGalleryImages(croppedURL, galleryImage);

                dgi.execute();
            } catch (final MalformedURLException e) {
                final Toast toast = Toast.makeText(this.getActivity(), "Malformed URL.", Toast.LENGTH_SHORT);
                toast.show();
                e.printStackTrace();
            }
        } catch (final JSONException e) {
            final Toast toast = Toast.makeText(this.getActivity(), "Could not get gallery photos from Facebook. Please check your internet " +
                    "connection and restart the app.", Toast.LENGTH_LONG);
            toast.show();
            e.printStackTrace();
        }
    }

    private void setGalleryImageParameters(final ImageView galleryImage, final int count, final int imgHeight, final GalleryClickListener listener) {
        final LayoutParams imgParams = new LayoutParams(0, imgHeight, 1f);
        imgParams.gravity = Gravity.START;

        galleryImage.setId(count);
        galleryImage.setPadding(2, 2, 2, 2);
        galleryImage.setLayoutParams(imgParams);
        galleryImage.setClickable(true);
        galleryImage.setOnClickListener(listener);
    }
}