package com.applicatum.schafkopfhelfer.fragments;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.applicatum.schafkopfhelfer.R;
import com.applicatum.schafkopfhelfer.StartActivity;

public class GameSettingsFragment extends Fragment {

    FloatingActionButton fabGo;
    View mRootView;
    StartActivity activity;

    public GameSettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mRootView = inflater.inflate(R.layout.fragment_game_settings, container, false);

        activity = (StartActivity) getActivity();
        fabGo = (FloatingActionButton) mRootView.findViewById(R.id.fab);

        fabGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new PlayersFragment();
                activity.startFragment(fragment, true);
            }
        });

        return mRootView;
    }

}
