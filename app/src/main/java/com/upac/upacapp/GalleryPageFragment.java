package com.upac.upacapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import java.net.MalformedURLException;
import java.net.URL;

public class GalleryPageFragment extends Fragment {
    public static final String ARG_PAGE = "page";
    protected static int mPageNumber;
    private static String[] imageURLs;

    public GalleryPageFragment() {

    }

    public void setInformation(final String[] img) {
        imageURLs = img;
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPageNumber = this.getArguments().getInt(ARG_PAGE);
    }

    @Override
    public View onCreateView(final LayoutInflater inf, final ViewGroup container, final Bundle savedInstanceState) {
        final View rootView = this.getActivity().getLayoutInflater().inflate(R.layout.fragment_full_gallery_photo, container, false);
        final ImageView fullImage = (ImageView) rootView.findViewById(R.id.full_image);

        try {
            final URL imageURL = new URL(imageURLs[mPageNumber]);
            final DownloadDetailsImages ddi = new DownloadDetailsImages(imageURL, fullImage);
            ddi.execute();
        } catch (final MalformedURLException e) {
            final Toast toast = Toast.makeText(this.getActivity(), "Something went wrong in downloading the image.", Toast.LENGTH_SHORT);
            toast.show();
            e.printStackTrace();
        }

        return rootView;
    }

    public GalleryPageFragment create(final int pageNumber) {
        final GalleryPageFragment fragment = new GalleryPageFragment();
        final Bundle args = new Bundle();

        args.putInt(ARG_PAGE, pageNumber);
        fragment.setArguments(args);

        return fragment;
    }
}