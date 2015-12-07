package com.applicatum.schafkopfhelfer.fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.applicatum.schafkopfhelfer.MainActivity;
import com.applicatum.schafkopfhelfer.R;
import com.applicatum.schafkopfhelfer.StartActivity;
import com.applicatum.schafkopfhelfer.models.Game;


public class StartFragment extends Fragment {

    private View view;
    private StartActivity activity;

    private Button buttonNewGame;
    private Button buttonOldGame;
    private Button buttonStatistic;

    public StartFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_start, container, false);
        activity = (StartActivity) getActivity();

        buttonNewGame = (Button) view.findViewById(R.id.buttonNewGame);
        buttonOldGame = (Button) view.findViewById(R.id.buttonOldGame);
        buttonStatistic = (Button) view.findViewById(R.id.buttonStatistic);

        setListeners();

        return view;
    }

    private void setListeners(){

        buttonNewGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GameSettingsFragment fragment = new GameSettingsFragment();
                activity.startFragment(fragment, true);
            }
        });

        buttonOldGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //GameMainFragment fragment = new GameMainFragment();
                //activity.startFragment(fragment, true);
                Game game = Game.lastGame();
                if (game!=null) {
                    Intent intent = new Intent(activity.getBaseContext(), MainActivity.class);
                    activity.startActivity(intent);
                } else {
                    Toast.makeText(activity, "Es gibt kein altes Spiel", Toast.LENGTH_LONG).show();
                }
            }
        });

        buttonStatistic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlayersFragment fragment = new PlayersFragment();
                fragment.setManagePlayers(true);
                activity.startFragment(fragment, true);
            }
        });
    }

}
