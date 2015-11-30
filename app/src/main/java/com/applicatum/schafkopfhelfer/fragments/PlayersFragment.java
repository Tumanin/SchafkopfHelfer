package com.applicatum.schafkopfhelfer.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.applicatum.schafkopfhelfer.MainActivity;
import com.applicatum.schafkopfhelfer.R;
import com.applicatum.schafkopfhelfer.StartActivity;
import com.applicatum.schafkopfhelfer.adapters.PlayersListAdapter;
import com.applicatum.schafkopfhelfer.models.Game;
import com.applicatum.schafkopfhelfer.models.Player;
import com.applicatum.schafkopfhelfer.utils.PlayersList;

import java.util.ArrayList;
import java.util.List;

public class PlayersFragment extends Fragment {

    private View mRootView;
    StartActivity activity;

    List<Player> players;
    List<Player> activePlayers;
    FloatingActionButton fabGo;
    LinearLayout listContainer;
    PlayersListAdapter adapter;
    LayoutInflater inflater;
    View itemView;

    public PlayersFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mRootView = inflater.inflate(R.layout.fragment_players, container, false);
        activity = (StartActivity) getActivity();
        this.inflater = inflater;
        players = new ArrayList<>();
        activePlayers = new ArrayList<>();

        listContainer = (LinearLayout) mRootView.findViewById(R.id.listContainer);
        fabGo = (FloatingActionButton) mRootView.findViewById(R.id.fab);

        fabGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (activePlayers.size() > 3 && activePlayers.size() < 8) {
                    PlayersList.getInstance().setList(activePlayers);

                    Game game = Game.lastGame();
                    game.updateActivePlayers(activePlayers);
                    activePlayers.clear();

                    game = Game.lastGame();

                    Intent intent = new Intent(activity.getBaseContext(), MainActivity.class);
                    activity.startActivity(intent);
                } else {
                    Toast.makeText(activity, "Anzahl der Spieler muss zwischen 4 und 7 sein!", Toast.LENGTH_LONG).show();
                }
            }
        });



        return mRootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        populatePlayers();
    }

    private void populatePlayers(){

        listContainer.removeAllViews();
        List<Player> loadedPlayers = Player.getPlayers();
        if(loadedPlayers!=null){
            players.clear();
            players.addAll(Player.getPlayers());
        }
        adapter = new PlayersListAdapter(activity, players);
        if(adapter.getCount()>0){
            for(int i=0; i < adapter.getCount(); i++){
                itemView = adapter.getView(i, null, null);
                final Player player = (Player)adapter.getItem(i);
                itemView.setTag(player);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!activePlayers.contains(player)){
                            player.setState(Player.State.PLAY);
                            player.setColor(-1);
                            activePlayers.add(player);
                            v.setBackgroundResource(R.drawable.user_item_frame_checked);
                        }else{
                            activePlayers.remove(player);
                            player.setState(Player.State.OUT);
                            v.setBackgroundResource(R.drawable.user_item_frame);
                        }
                    }
                });
                itemView.setClickable(true);
                listContainer.addView(itemView);
            }
        }
        itemView = inflater.inflate(R.layout.player_list_item_add, null);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = inflater.inflate(R.layout.dialog_new_player, null);
                final EditText inputPlayerName = (EditText) view.findViewById(R.id.inputPlayerName);
                AlertDialog dialog = new AlertDialog.Builder(activity)
                        .setTitle(getResources().getString(R.string.title_new_player))
                        .setView(view)
                        .setNegativeButton(getResources().getString(R.string.button_cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton(getResources().getString(R.string.button_ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String playerName = inputPlayerName.getText().toString().replace(",", "").trim();
                                if(playerName.equals("")) {
                                    Toast.makeText(activity, "Der Name darf nicht leer sein!", Toast.LENGTH_LONG).show();
                                } else if(Player.nameIsUnique(playerName)){
                                    Player player = new Player(playerName);
                                    populatePlayers();
                                    dialog.dismiss();
                                } else {
                                    Toast.makeText(activity, "Der Name existiert bereits!", Toast.LENGTH_LONG).show();
                                }
                            }
                        })
                        .create();
                dialog.show();
            }
        });
        itemView.setClickable(true);
        listContainer.addView(itemView);
    }

    private void updatePlayers(){

    }

}
