package com.applicatum.schafkopfhelfer.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.applicatum.schafkopfhelfer.MainActivity;
import com.applicatum.schafkopfhelfer.R;
import com.applicatum.schafkopfhelfer.adapters.UsersDynamicAdapter;
import com.applicatum.schafkopfhelfer.models.Game;
import com.applicatum.schafkopfhelfer.models.Player;
import com.applicatum.schafkopfhelfer.utils.PlayersList;
import com.applicatum.schafkopfhelfer.utils.Types;

import org.askerov.dynamicgrid.DynamicGridView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class GameMainFragment extends Fragment {

    private static final String TAG = "GameMainFragment";

    private DynamicGridView gridView;
    private MainActivity activity;
    private View view;
    private Game game;
    UsersDynamicAdapter usersDynamicAdapter;

    private Button buttonSau;
    private Button buttonRamsch;
    private Button buttonSolo;
    private Spinner soloSpinner;
    private Button buttonSchneider;
    private Button buttonSchwarz;
    private Button buttonLaufende;
    private Button buttonKlopfen;
    private Button buttonRechner;
    private Button buttonOk;
    private Button buttonAussetzer;
    private TextView textAussetzer;

    private View gameLayout;
    private View aussetzerLayout;

    List<Player> players;

    int laufende;
    int klopfen;

    int winnerCount;
    int aussetzerCount;

    boolean schneider;
    boolean schwarz;

    boolean aussetzer;


    public GameMainFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_game_main, container, false);
        gridView = (DynamicGridView) view.findViewById(R.id.usersGrid);

        gameLayout = view.findViewById(R.id.gameLayout);
        aussetzerLayout = view.findViewById(R.id.aussetzerLayout);

        laufende = 0;
        klopfen = 0;
        aussetzer = true;
        winnerCount = 0;
        aussetzerCount = 0;
        schneider = false;
        schwarz = false;

        setButtons();
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        activity = (MainActivity) getActivity();
        game = Game.lastGame();
        setGridView();
    }

    private void setButtons(){
        buttonSau = (Button)view.findViewById(R.id.buttonSau);
        buttonRamsch = (Button)view.findViewById(R.id.buttonRamsch);
        buttonSolo = (Button)view.findViewById(R.id.buttonSolo);
        soloSpinner = (Spinner)view.findViewById(R.id.soloSpinner);
        buttonSchneider = (Button)view.findViewById(R.id.buttonSchneider);
        buttonSchwarz = (Button)view.findViewById(R.id.buttonSchwarz);
        buttonLaufende = (Button)view.findViewById(R.id.buttonLaufende);
        buttonKlopfen = (Button)view.findViewById(R.id.buttonKlopfen);
        buttonRechner = (Button)view.findViewById(R.id.buttonRechner);
        buttonOk = (Button)view.findViewById(R.id.buttonOk);
        buttonAussetzer = (Button) view.findViewById(R.id.buttonAussetzer);

        buttonSau.setEnabled(false);
        buttonSau.setSelected(false);
        buttonRamsch.setEnabled(false);
        buttonRamsch.setSelected(false);
        buttonSolo.setEnabled(false);
        buttonSolo.setSelected(false);

        buttonAussetzer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(usersDynamicAdapter.getCount()-aussetzerCount==4){
                    aussetzer = false;
                    aussetzerLayout.setVisibility(View.GONE);
                    gameLayout.setVisibility(View.VISIBLE);
                }else{
                    Toast.makeText(activity, "Es müssen genau 4 Spieler spielen!",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        buttonSau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!buttonSau.isSelected() && buttonSau.isEnabled()){
                    buttonSau.setSelected(true);
                    buttonRamsch.setSelected(false);
                    buttonSolo.setSelected(false);
                }
            }
        });

        buttonRamsch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!buttonRamsch.isSelected() && buttonRamsch.isEnabled()){
                    buttonRamsch.setSelected(true);
                    buttonSau.setSelected(false);
                    buttonSolo.setSelected(false);
                }
            }
        });

        buttonSolo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!buttonSolo.isSelected() && buttonSolo.isEnabled()){
                    buttonSolo.setSelected(true);
                    buttonSau.setSelected(false);
                    buttonRamsch.setSelected(false);
                }
            }
        });

        buttonSchneider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonSchneider.setSelected(!buttonSchneider.isSelected());
            }
        });

        buttonSchwarz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonSchwarz.setSelected(!buttonSchwarz.isSelected());
            }
        });

        buttonLaufende.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                laufende+=1;
                buttonLaufende.setText(String.valueOf(laufende));
            }
        });
        buttonKlopfen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                klopfen+=1;
                buttonKlopfen.setText(String.valueOf(klopfen));
            }
        });

        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!buttonSau.isSelected() && !buttonSolo.isSelected() && !buttonRamsch.isSelected()){
                    Toast.makeText(activity, "Wähle Gewinner und Spiel!",
                            Toast.LENGTH_SHORT).show();
                }else{
                    recordNewRound();
                    activity.updateTable();
                    laufende = 0;
                    klopfen = 0;
                    buttonLaufende.setText(String.valueOf(laufende));
                    buttonKlopfen.setText(String.valueOf(klopfen));

                    buttonSau.setEnabled(false);
                    buttonSau.setSelected(false);
                    buttonRamsch.setEnabled(false);
                    buttonRamsch.setSelected(false);
                    buttonSolo.setEnabled(false);
                    buttonSolo.setSelected(false);

                    buttonSchneider.setSelected(false);
                    buttonSchwarz.setSelected(false);

                    if(players!=null){
                        for(Player player : players){
                            if(player.getState()== Player.State.WIN){
                                player.setState(Player.State.PLAY);
                            }
                        }
                    }
                    usersDynamicAdapter.notifyDataSetChanged();
                }

            }
        });
    }

    private void setGridView(){

        players = game.getActivePlayers();

        if(players.size()==4){
            aussetzer=false;
            aussetzerLayout.setVisibility(View.GONE);
            gameLayout.setVisibility(View.VISIBLE);
        }

        usersDynamicAdapter = new UsersDynamicAdapter(activity, players, 3);
        gridView.setAdapter(usersDynamicAdapter);
//        add callback to stop edit mode if needed
        gridView.setOnDropListener(new DynamicGridView.OnDropListener() {
            @Override
            public void onActionDrop() {
                gridView.stopEditMode();
            }
        });
        gridView.setOnDragListener(new DynamicGridView.OnDragListener() {
            @Override
            public void onDragStarted(int position) {
                Log.d(TAG, "drag started at position " + position);
            }

            @Override
            public void onDragPositionsChanged(int oldPosition, int newPosition) {
                Log.d(TAG, String.format("drag item position changed from %d to %d", oldPosition, newPosition));
            }
        });
        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                gridView.startEditMode(position);
                return true;
            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Player player = (Player) usersDynamicAdapter.getItem(position);
                if (!aussetzer) {
                    if (player.getState() == Player.State.PLAY) {
                        if (winnerCount < 3) {
                            winnerCount += 1;
                            player.setState(Player.State.WIN);
                            if (winnerCount == 1 || winnerCount == 3) {
                                buttonSau.setEnabled(false);
                                buttonSau.setSelected(false);
                                buttonRamsch.setEnabled(true);
                                buttonSolo.setEnabled(true);
                            } else {
                                buttonSau.setEnabled(true);
                                buttonSau.setSelected(true);
                                buttonRamsch.setEnabled(false);
                                buttonRamsch.setSelected(false);
                                buttonSolo.setEnabled(false);
                                buttonSolo.setSelected(false);
                            }
                        } else {
                            Toast.makeText(activity, "Zu viele Gewinner!",
                                    Toast.LENGTH_SHORT).show();
                        }
                    } else if (player.getState() == Player.State.WIN) {
                        player.setState(Player.State.PLAY);
                        if (winnerCount > 1) {
                            winnerCount -= 1;
                            if (winnerCount == 1 || winnerCount == 3) {
                                buttonSau.setEnabled(false);
                                buttonSau.setSelected(false);
                                buttonRamsch.setEnabled(true);
                                buttonSolo.setEnabled(true);
                            } else {
                                buttonSau.setEnabled(true);
                                buttonSau.setSelected(true);
                                buttonRamsch.setEnabled(false);
                                buttonRamsch.setSelected(false);
                                buttonSolo.setEnabled(false);
                                buttonSolo.setSelected(false);
                            }
                        } else {
                            winnerCount = 0;
                            buttonSau.setEnabled(false);
                            buttonSau.setSelected(false);
                            buttonRamsch.setEnabled(false);
                            buttonRamsch.setSelected(false);
                            buttonSolo.setEnabled(false);
                            buttonSolo.setSelected(false);
                        }
                    }
                } else {
                    if (player.getState() == Player.State.WAIT) {
                        player.setState(Player.State.PLAY);
                        if (aussetzerCount > 0) aussetzerCount -= 1;
                    } else if (player.getState() == Player.State.PLAY) {

                        if (usersDynamicAdapter.getCount() - aussetzerCount > 4) {
                            aussetzerCount += 1;
                            player.setState(Player.State.WAIT);

                        } else {
                            Toast.makeText(activity, "Es müssen genau 4 Spieler spielen!",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                usersDynamicAdapter.notifyDataSetChanged();

            }
        });
    }

    private void recordNewRound(){
        String type = Types.SAUSPIEL;
        if(buttonSolo.isSelected()){
            type = Types.FARBSOLO;
        } else if(buttonRamsch.isSelected()){
            type = Types.RAMSCH;
        }

        List<Player> winners = new ArrayList<>();
        List<Player> losers = new ArrayList<>();
        List<Player> jungfrauen = new ArrayList<>();

        for(Player p : players){
            if(p.getState() == Player.State.WIN){
                winners.add(p);
            } else if(p.getState() == Player.State.PLAY){
                losers.add(p);
            }
        }

        game.recordNewRound(type, winners, losers, jungfrauen, buttonSchneider.isSelected(),
                buttonSchwarz.isSelected(), laufende, klopfen);
    }
}
