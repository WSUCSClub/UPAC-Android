package com.upac.upacapp;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ActionBarFragment extends Fragment {
    private static View actionView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        actionView = inflater.inflate(R.layout.ab_custom_view, null);

        return actionView;
    }
}
