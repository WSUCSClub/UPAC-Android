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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class AboutFragment extends Fragment {
    public static final String TAG = "about";
    private static View aboutView;
    private static final App parse = new App();
    private ViewGroup parent;

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        parent = container;
        return aboutView;
    }

    @Override
    public void onAttach(final Activity activity) {
        super.onAttach(activity);

        try {
            final List<ParseObject> memberList = parse.getBoardMembers();

            aboutView = this.getActivity().getLayoutInflater().inflate(R.layout.fragment_about, parent, false);
            final LinearLayout ll = (LinearLayout) aboutView.findViewById(R.id.about_sections);

            try {
                final LinearLayout[] lines = new LinearLayout[memberList.size()];
                final LinearLayout[] infoLayout = new LinearLayout[memberList.size()];
                final ImageView[] iv = new ImageView[memberList.size()];
                final TextView[] contactName = new TextView[memberList.size()];
                final TextView[] contactInfo = new TextView[memberList.size()];

                final int memberCount = memberList.size();
                for (int i = 0; i < memberCount; i++) {
                    try {
                        URL imageURL = new URL(memberList.get(i).getParseFile("picture").getUrl());
                        String name = memberList.get(i).getString("name");
                        String position = memberList.get(i).getString("position");
                        String email = memberList.get(i).getString("email");

                        final OnClickListener cocl = new EmailBoardMemberClickListener(email, this.getActivity());

                        final DisplayMetrics displayMetrics = this.getActivity().getResources().getDisplayMetrics();
                        final int width = displayMetrics.widthPixels;
                        final int imgHeight = width / 4;
                        final int imgWidth = (int) Math.ceil(width / 2.8);

                        final LayoutParams imgParams = new LayoutParams(imgWidth, imgHeight, 3f);

                        iv[i] = new ImageView(this.getActivity());
                        iv[i].setId(i);
                        iv[i].setLayoutParams(imgParams);
                        iv[i].setPadding(25, 0, 25, 0);

                        final DownloadAboutImages dai = new DownloadAboutImages(imageURL, iv[i]);
                        dai.execute();

                        final LayoutParams infoParam = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 7f);

                        infoLayout[i] = new LinearLayout(this.getActivity());
                        infoLayout[i].setLayoutParams(infoParam);
                        infoLayout[i].setOrientation(LinearLayout.VERTICAL);

                        final LayoutParams textParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

                        contactName[i] = new TextView(this.getActivity());
                        contactName[i].setText(name);
                        contactName[i].setTextColor(Color.BLACK);
                        contactName[i].setLayoutParams(textParams);
                        contactName[i].setPadding(0, 25, 0, 0);
                        contactName[i].setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);

                        contactInfo[i] = new TextView(this.getActivity());
                        contactInfo[i].setText(position + String.format("%n") + email);
                        contactInfo[i].setTextColor(Color.BLACK);
                        contactInfo[i].setId(i);
                        contactInfo[i].setLayoutParams(textParams);
                        contactInfo[i].setPadding(0, 0, 0, 25);
                        contactInfo[i].setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);

                        infoLayout[i].addView(contactName[i]);
                        infoLayout[i].addView(contactInfo[i]);

                        final GradientDrawable gd = new GradientDrawable();
                        gd.setStroke(1, Color.parseColor("#E1E1E1"));

                        final LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

                        lines[i] = new LinearLayout(this.getActivity());
                        lines[i].setLayoutParams(params);
                        lines[i].addView(iv[i]);
                        lines[i].addView(infoLayout[i]);
                        lines[i].setBackground(gd);
                        lines[i].setClickable(true);
                        lines[i].setOnClickListener(cocl);

                        ll.addView(lines[i]);
                    } catch (final MalformedURLException e) {
                        e.printStackTrace();
                    }
                }
            } catch (final NullPointerException e) {
                final Toast toast = Toast.makeText(this.getActivity(), "Cannot access Parse files. Please check your internet connection and " +
                        "restart the app.", Toast.LENGTH_LONG);
                toast.show();
                e.printStackTrace();
            }
        } catch (final ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement MainActivity.");
        }
    }
}