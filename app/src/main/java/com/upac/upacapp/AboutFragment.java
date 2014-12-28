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

import com.parse.ParseObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class AboutFragment extends Fragment {
    private static View aboutView;
    private static App parse = new App();
    private static List<ParseObject> memberList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return aboutView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            memberList = parse.getBoardMembers();

            String name, position, email;
            URL imageURL;
            aboutView = getActivity().getLayoutInflater().inflate(R.layout.fragment_about, null);
            LinearLayout ll = (LinearLayout) aboutView.findViewById(R.id.about_sections);
            LinearLayout[] lines = new LinearLayout[memberList.size()];
            ImageView[] iv = new ImageView[memberList.size()];
            TextView[] tv = new TextView[memberList.size()];

            for (int i = 0; i < memberList.size(); i++) {
                try {
                    imageURL = new URL(memberList.get(i).getParseFile("picture").getUrl());
                    name = memberList.get(i).getString("name");
                    position = memberList.get(i).getString("position");
                    email = memberList.get(i).getString("email");

                    EmailBoardMemberClickListener cocl = new EmailBoardMemberClickListener(email, getActivity());

                    LinearLayout.LayoutParams imgParams = new LinearLayout.LayoutParams(290, 290);

                    iv[i] = new ImageView(getActivity());
                    iv[i].setId(i);
                    iv[i].setLayoutParams(imgParams);
                    iv[i].setOnClickListener(cocl);
                    iv[i].setPadding(25, 0, 25, 0);

                    DownloadAboutImages dai = new DownloadAboutImages(imageURL, iv[i]);
                    dai.execute();

                    LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(0, 290, 1f);

                    tv[i] = new TextView(getActivity());
                    tv[i].setText(name + "\n" + position + "\n" + email);
                    tv[i].setTextColor(Color.BLACK);
                    tv[i].setId(i);
                    tv[i].setLayoutParams(textParams);
                    tv[i].setClickable(true);
                    tv[i].setOnClickListener(cocl);
                    tv[i].setPadding(0, 75, 0, 0);

                    GradientDrawable gd = new GradientDrawable();
                    gd.setColor(Color.WHITE);
                    gd.setStroke(5, 0x000000);

                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

                    lines[i] = new LinearLayout(getActivity());
                    lines[i].setLayoutParams(params);
                    lines[i].addView(iv[i]);
                    lines[i].addView(tv[i]);
                    lines[i].setBackground(gd);

                    ll.addView(lines[i]);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement MainActivity.");
        }
    }
}