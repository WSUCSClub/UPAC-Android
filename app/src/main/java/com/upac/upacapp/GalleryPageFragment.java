package com.upac.upacapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import java.net.URL;

public class GalleryPageFragment extends Fragment {
    public static final String ARG_PAGE = "page";
    protected static int mPageNumber;
    private static String[] imageURLs;

    public GalleryPageFragment() {

    }

    public void setInformation(String[] img) {
        imageURLs = img;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPageNumber = getArguments().getInt(ARG_PAGE);
    }

    @Override
    public View onCreateView(LayoutInflater inf, ViewGroup container, Bundle savedInstanceState) {
        View rootView = getActivity().getLayoutInflater().inflate(R.layout.fragment_full_gallery_photo, container, false);
        ImageView fullImage = (ImageView) rootView.findViewById(R.id.full_image);

        try {
            URL imageURL = new URL(imageURLs[mPageNumber]);
            DownloadDetailsImages ddi = new DownloadDetailsImages(imageURL, fullImage);
            ddi.execute();
        } catch (Exception e) {
            Toast toast = Toast.makeText(getActivity(), "Something went wrong in downloading the image.", Toast.LENGTH_SHORT);
            toast.show();
            e.printStackTrace();
        }

        return rootView;
    }

    public GalleryPageFragment create(int pageNumber) {
        GalleryPageFragment fragment = new GalleryPageFragment();
        Bundle args = new Bundle();

        args.putInt(ARG_PAGE, pageNumber);
        fragment.setArguments(args);

        return fragment;
    }
}