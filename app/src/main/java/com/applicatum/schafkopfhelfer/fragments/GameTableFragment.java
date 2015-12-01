package com.applicatum.schafkopfhelfer.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.applicatum.schafkopfhelfer.MainActivity;
import com.applicatum.schafkopfhelfer.R;
import com.applicatum.schafkopfhelfer.models.Game;
import com.applicatum.schafkopfhelfer.models.Player;

import java.util.List;
import java.util.Random;

public class GameTableFragment extends Fragment {

    private static final String TAG = "GameMainFragment";

    View mRootView;
    TableLayout table;
    MainActivity mActivity;
    LayoutInflater inflater;

    int roundCount;

    public GameTableFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mRootView = inflater.inflate(R.layout.fragment_game_table, container, false);
        table = (TableLayout) mRootView.findViewById(R.id.table);
        mActivity = (MainActivity) getActivity();
        this.inflater = inflater;

        roundCount = -1;

        updateTable();
        return mRootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }
    public void updateTable(){
        Log.d(TAG, "updateTable");
        roundCount+=1;
        Game game = Game.lastGame();
        Random random = new Random();
        //game.
        table.removeAllViews();
        List<Player> activePlayers = game.getActivePlayers();
        TableRow titleRow = new TableRow(mActivity);
        TextView connorItem = (TextView) inflater.inflate(R.layout.item_table_player, null);
        connorItem.setText(".");
        titleRow.addView(connorItem);
        for (Player player : activePlayers){
            TextView playerView = (TextView) inflater.inflate(R.layout.item_table_player, null);
            playerView.setText(player.getName());
            titleRow.addView(playerView);
        }
        table.addView(titleRow);

        for(int i=1; i<=roundCount; i++){
            TableRow newRow = new TableRow(mActivity);

            TextView roundView = (TextView) inflater.inflate(R.layout.item_table_round, null);
            roundView.setText(String.valueOf(i));
            newRow.addView(roundView);

            for(Player player : activePlayers){
                TextView pointView = (TextView) inflater.inflate(R.layout.item_table_points, null);
                int points = player.getPoints();
                pointView.setText(String.valueOf(points));
                newRow.addView(pointView);
            }
            table.addView(newRow);
        }
    }

    /*
    private TextView createtableItem(){
        TextView item = new TextView(mActivity);
        item.setMaxEms(4);
        item.setLayoutParams(new LinearLayout.LayoutParams(mActivity, ));
    }
    */
}
