package com.applicatum.schafkopfhelfer.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ActionMenuView;
import android.widget.Button;
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

    private static final String TAG = "GameTableFragment";

    View mRootView;
    LinearLayout table;
    MainActivity mActivity;
    LayoutInflater inflater;
    Button buttonRemove;

    int roundCount;

    public GameTableFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mRootView = inflater.inflate(R.layout.fragment_game_table, container, false);
        table = (LinearLayout) mRootView.findViewById(R.id.table);
        mActivity = (MainActivity) getActivity();
        buttonRemove = (Button) mRootView.findViewById(R.id.buttonRemove);
        this.inflater = inflater;

        roundCount = -1;

        updateTable();

        buttonRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog dialog = new AlertDialog.Builder(mActivity)
                        .setTitle(getResources().getString(R.string.button_remove_last_round))
                        .setMessage(getResources().getString(R.string.text_remove_last_round))
                        .setNegativeButton(getResources().getString(R.string.button_cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton(getResources().getString(R.string.button_ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                removeLastRound();
                                dialog.dismiss();
                            }
                        })
                        .create();
                dialog.show();
            }
        });
        return mRootView;
    }

    private void removeLastRound(){

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }
    public void updateTable(){
        Log.d(TAG, "updateTable");
        roundCount+=1;
        Log.d(TAG, "roundCount: "+roundCount);
        Game game = Game.lastGame();
        //game.
        table.removeAllViews();
        List<Player> activePlayers = game.getActivePlayers();
        LinearLayout titleRow = new LinearLayout(mActivity);
        LinearLayout.LayoutParams rowParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        titleRow.setLayoutParams(rowParams);
        titleRow.setOrientation(LinearLayout.HORIZONTAL);
        titleRow.setWeightSum(activePlayers.size()+1);
        TextView connorItem = createtableItem();
        connorItem.setText(".");
        titleRow.addView(connorItem);
        for (Player player : activePlayers){
            TextView playerView = createtableItem();
            playerView.setText(player.getName());
            playerView.setTypeface(null, Typeface.BOLD);
            titleRow.addView(playerView);
        }
        table.addView(titleRow);

        for(int i=1; i<=roundCount; i++){
            Log.d(TAG, "round: " + i);
            LinearLayout newRow = (LinearLayout) inflater.inflate(R.layout.table_row_layout, null);
            //newRow.setLayoutParams(rowParams);
            newRow.setVisibility(View.VISIBLE);
            newRow.setTag("row"+i);
            //newRow.setId(i);
            //newRow.setOrientation(LinearLayout.HORIZONTAL);
            newRow.setWeightSum(activePlayers.size() + 1);
            TextView roundView = createtableItem();
            roundView.setText(String.valueOf(i));
            roundView.setTag(i);
            newRow.addView(roundView);
            Log.d(TAG, "newRow.addView(roundView)");
            for(Player player : activePlayers){
                TextView pointView = createtableItem();
                int points = player.getPoints();
                pointView.setText(String.valueOf(points));
                pointView.setTag(points);
                newRow.addView(pointView);
                Log.d(TAG, "newRow.addView(pointView): "+points);
            }
            table.addView(newRow);
            Log.d(TAG, "views: "+table);
        }
    }

    private TextView createtableItem(){
        TextView item = new TextView(mActivity);
        item.setMaxEms(4);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
        item.setLayoutParams(params);
        item.setSingleLine(true);
        item.setText("0");
        item.setPadding(2, 2, 2, 2);
        item.setVisibility(View.VISIBLE);
        item.setBackgroundResource(R.drawable.background_cell_border);
        item.setGravity(Gravity.CENTER);
        return item;
    }

}
