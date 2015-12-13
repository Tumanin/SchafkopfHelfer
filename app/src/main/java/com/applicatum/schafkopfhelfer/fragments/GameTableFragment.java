package com.applicatum.schafkopfhelfer.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ActionMenuView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.applicatum.schafkopfhelfer.MainActivity;
import com.applicatum.schafkopfhelfer.R;
import com.applicatum.schafkopfhelfer.models.Game;
import com.applicatum.schafkopfhelfer.models.Player;
import com.applicatum.schafkopfhelfer.models.Round;
import com.applicatum.schafkopfhelfer.utils.CustomScrollView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class GameTableFragment extends Fragment {

    private static final String TAG = "GameTableFragment";

    View mRootView;
    LinearLayout table;
    LinearLayout tableTitle;
    CustomScrollView scrollTable;
    ViewPager parentPager;
    MainActivity mActivity;
    LayoutInflater inflater;
    Button buttonRemove;
    //Game game;

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
        tableTitle = (LinearLayout) mRootView.findViewById(R.id.tableTitle);
        parentPager = (ViewPager) container;
        scrollTable = (CustomScrollView) mRootView.findViewById(R.id.scrollTable);
        mActivity = (MainActivity) getActivity();
        buttonRemove = (Button) mRootView.findViewById(R.id.buttonRemove);
        this.inflater = inflater;
        scrollTable.setParentPager(parentPager);

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
                                // TODO: update view
                                mActivity.updatePlayers();
                                mActivity.updateAdapter();
                                Log.d(TAG, "Spieler: " + mActivity.players.get(0).getName() + " Punkte: " + mActivity.players.get(0).getPoints());
                                Player player = Player.findMitName(mActivity.players.get(0).getName());
                                Log.d(TAG, "SQL: Spieler: "+player.getName()+" Punkte: "+player.getPoints());
                                updateTable();

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
        Round.removeLastRound();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }
    public void updateTable(){
        Log.d(TAG, "updateTable");
        Game game = Game.lastGame();
        HashMap<Player, ArrayList<String>> tableMap = new HashMap<>();
        HashMap<Player, ArrayList<String>> tableMapUnsorted = game.getRoundsTable();
        if (tableMapUnsorted!=null) {
            List<Player> setPlayers = new ArrayList<>();
            setPlayers.addAll(tableMapUnsorted.keySet());
            for(int i = 0; i<mActivity.players.size(); i++){
                for(int j=0; j<setPlayers.size(); j++){
                    if(mActivity.players.get(i).getName().equals(setPlayers.get(j).getName())){
                        tableMap.put(mActivity.players.get(i), tableMapUnsorted.get(setPlayers.get(j)));
                    }
                }
            }
        }else{
            return;
        }
        tableTitle.removeAllViews();
        table.removeAllViews();
        //List<Player> activePlayers = game.getActivePlayers();
        List<Player> activePlayers = new ArrayList<>();
        activePlayers.addAll(mActivity.players);
        /*
        LinearLayout titleRow = new LinearLayout(mActivity);
        LinearLayout.LayoutParams rowParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        titleRow.setLayoutParams(rowParams);
        titleRow.setOrientation(LinearLayout.HORIZONTAL);
        */

        tableTitle.setWeightSum(activePlayers.size() + 1);
        TextView connorItem = createtableItem();
        connorItem.setText(getResources().getString(R.string.table_item_round));
        tableTitle.addView(connorItem);
        for (Player player : activePlayers){
            TextView playerView = createtableItem();
            playerView.setText(player.getName());
            playerView.setTypeface(null, Typeface.BOLD);
            tableTitle.addView(playerView);
        }
        //table.addView(titleRow);

        roundCount = tableMap.get(activePlayers.get(0)).size();

        for(int i=0; i<roundCount; i++){
            LinearLayout newRow = (LinearLayout) inflater.inflate(R.layout.table_row_layout, null);
            //newRow.setLayoutParams(rowParams);
            newRow.setVisibility(View.VISIBLE);
            newRow.setTag("row"+i+1);
            //newRow.setId(i);
            //newRow.setOrientation(LinearLayout.HORIZONTAL);
            newRow.setWeightSum(activePlayers.size() + 1);
            TextView roundView = createtableItem();
            roundView.setText(String.valueOf(i));
            roundView.setTag(i + 1);
            newRow.addView(roundView);
            for(Player player : activePlayers){
                TextView pointView = createtableItem();
                String points = tableMap.get(player).get(i);
                pointView.setText(points);
                pointView.setTag(points);
                newRow.addView(pointView);
            }
            table.addView(newRow);
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
