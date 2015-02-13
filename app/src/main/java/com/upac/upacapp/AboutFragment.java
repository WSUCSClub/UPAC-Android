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

import com.parse.ParseObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class AboutFragment extends Fragment {
    public static final String TAG = "about";
    private static View aboutView;
    private static App parse = new App();
    private ViewGroup parent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parent = container;
        return aboutView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            List<ParseObject> memberList = parse.getBoardMembers();

            String name, position, email;
            URL imageURL;
            aboutView = getActivity().getLayoutInflater().inflate(R.layout.fragment_about, parent, false);
            LinearLayout ll = (LinearLayout) aboutView.findViewById(R.id.about_sections);

            try {
                LinearLayout[] lines = new LinearLayout[memberList.size()];
                LinearLayout[] infoLayout = new LinearLayout[memberList.size()];
                ImageView[] iv = new ImageView[memberList.size()];
                TextView[] contactName = new TextView[memberList.size()];
                TextView[] contactInfo = new TextView[memberList.size()];

                for (int i = 0; i < memberList.size(); i++) {
                    try {
                        imageURL = new URL(memberList.get(i).getParseFile("picture").getUrl());
                        name = memberList.get(i).getString("name");
                        position = memberList.get(i).getString("position");
                        email = memberList.get(i).getString("email");

                        EmailBoardMemberClickListener cocl = new EmailBoardMemberClickListener(email, getActivity());

                        DisplayMetrics displayMetrics = getActivity().getResources().getDisplayMetrics();
                        int width = displayMetrics.widthPixels;
                        int imgHeight = width / 4;
                        int imgWidth = (int) Math.ceil(width / 2.8);

                        LinearLayout.LayoutParams imgParams = new LinearLayout.LayoutParams(imgWidth, imgHeight, 3f);

                        iv[i] = new ImageView(getActivity());
                        iv[i].setId(i);
                        iv[i].setLayoutParams(imgParams);
                        iv[i].setPadding(25, 0, 25, 0);

                        DownloadAboutImages dai = new DownloadAboutImages(imageURL, iv[i]);
                        dai.execute();

                        LinearLayout.LayoutParams infoParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 7f);

                        infoLayout[i] = new LinearLayout(getActivity());
                        infoLayout[i].setLayoutParams(infoParam);
                        infoLayout[i].setOrientation(LinearLayout.VERTICAL);

                        LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                        contactName[i] = new TextView(getActivity());
                        contactName[i].setText(name);
                        contactName[i].setTextColor(Color.BLACK);
                        contactName[i].setLayoutParams(textParams);
                        contactName[i].setPadding(0, 25, 0, 0);
                        contactName[i].setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);

                        contactInfo[i] = new TextView(getActivity());
                        contactInfo[i].setText(position + "\n" + email);
                        contactInfo[i].setTextColor(Color.BLACK);
                        contactInfo[i].setId(i);
                        contactInfo[i].setLayoutParams(textParams);
                        contactInfo[i].setPadding(0, 0, 0, 25);
                        contactInfo[i].setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);

                        infoLayout[i].addView(contactName[i]);
                        infoLayout[i].addView(contactInfo[i]);

                        GradientDrawable gd = new GradientDrawable();
                        gd.setStroke(1, Color.parseColor("#E1E1E1"));

                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

                        lines[i] = new LinearLayout(getActivity());
                        lines[i].setLayoutParams(params);
                        lines[i].addView(iv[i]);
                        lines[i].addView(infoLayout[i]);
                        lines[i].setBackground(gd);
                        lines[i].setClickable(true);
                        lines[i].setOnClickListener(cocl);

                        ll.addView(lines[i]);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                }
            } catch(NullPointerException e){
                Toast toast = Toast.makeText(getActivity(), "Cannot access Parse files. Please check your internet connection and restart the app.", Toast.LENGTH_LONG);
                toast.show();
                e.printStackTrace();
            }
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement MainActivity.");
        }
    }
}