package com.applicatum.schafkopfhelfer.fragments;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.applicatum.schafkopfhelfer.MainActivity;
import com.applicatum.schafkopfhelfer.R;
import com.applicatum.schafkopfhelfer.adapters.UsersDynamicAdapter;
import com.applicatum.schafkopfhelfer.models.Game;
import com.applicatum.schafkopfhelfer.models.GamePlayers;
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
    private HashMap <String, Integer> gameTypes;
    UsersDynamicAdapter usersDynamicAdapter;
    FloatingActionButton fabGo;
    private Button buttonSau;
    private Button buttonRamsch;
    private Button buttonSolo;
    private Spinner soloSpinner;
    private Button buttonSchneider;
    private Button buttonSchwarz;
    private Button buttonLaufende;
    private Button buttonKlopfen;
    private Button buttonClear;
    private Button buttonOk;
    private Button buttonAussetzer;
    private TextView textAussetzer;

    private View gameLayout;
    private View aussetzerLayout;

    //private List<Player> players;

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


        activity = (MainActivity) getActivity();
        game = Game.lastGame();

        if(game==null){
            Log.d(TAG, "no last game found");
        }else{
            gameTypes = game.getGameTypes();
            Log.d(TAG, "game id: "+game.getId());
            List<Player> players = game.getActivePlayers();
            if (players.size()>0) {
                for(Player player : players){
                    Log.d(TAG, "player: "+player.getName());
                }
            } else {
                Log.d(TAG, "no players found");
            }
            List<GamePlayers> gp = GamePlayers.getAll();
            Log.d(TAG, "GamePlayers found: "+gp.size());
            int count = 0;
            for(GamePlayers gPlayers : gp){
                Game localGame = gPlayers.getGame();
                count++;
                Log.d(TAG, "count: "+count);
                if (localGame!=null) {
                    Log.d(TAG, "localGame id: "+localGame.getId());
                    if(gPlayers.getGame().getId() == game.getId()){
                        Log.d(TAG, "GamePlayers of this game with player: "+gPlayers.getPlayer().getName());
                    }
                } else {
                    Log.d(TAG, "localGame has no id");
                }

            }
        }
        setGridView();

        setButtons();

        return view;
    }

    private void enableSetup(){

        if(activity.players.size()<5){
            aussetzer=false;
            aussetzerLayout.setVisibility(View.GONE);
            gameLayout.setVisibility(View.VISIBLE);
        } else{
            winnerCount = 0;
            for(Player player : activity.players){
                if(player.getState()== Player.State.WIN){
                    player.setState(Player.State.PLAY);
                }
            }
            aussetzer=true;
            aussetzerLayout.setVisibility(View.VISIBLE);
            gameLayout.setVisibility(View.GONE);
            fabGo.setVisibility(View.GONE);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


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
        buttonClear = (Button)view.findViewById(R.id.buttonClear);
        buttonOk = (Button)view.findViewById(R.id.buttonOk);
        buttonAussetzer = (Button) view.findViewById(R.id.buttonAussetzer);

        fabGo = (FloatingActionButton) view.findViewById(R.id.fab);

        fabGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enableSetup();
            }
        });
        if (aussetzer || activity.players.size()<5) {
            fabGo.setVisibility(View.GONE);
        } else {
            fabGo.setVisibility(View.VISIBLE);
        }

        buttonSau.setEnabled(false);
        buttonSau.setSelected(false);
        buttonRamsch.setEnabled(false);
        buttonRamsch.setSelected(false);
        buttonSolo.setEnabled(false);
        buttonSolo.setSelected(false);
        soloSpinner.setEnabled(false);

        if(gameTypes.get(Types.RAMSCH)!=-1){
            buttonRamsch.setText(getResources().getString(R.string.ramsch));
        }else if (gameTypes.get(Types.POTT)!=-1){
            buttonRamsch.setText(getResources().getString(R.string.pott));
        }

        List<String> solisArray = new ArrayList<>();
        if(gameTypes.get(Types.FARBSOLO)!=-1){
            solisArray.add(getResources().getString(R.string.farbsolo));
        }
        if(gameTypes.get(Types.WENZ)!=-1){
            solisArray.add(getResources().getString(R.string.wenz));
        }
        if(gameTypes.get(Types.GEIER)!=-1){
            solisArray.add(getResources().getString(R.string.geier));
        }
        if(gameTypes.get(Types.BETTEL)!=-1){
            solisArray.add(getResources().getString(R.string.bettel));
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(activity, R.layout.spinner_item, solisArray);
        //ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
        //        R.array.planets_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        soloSpinner.setAdapter(adapter);

        buttonAussetzer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (usersDynamicAdapter.getCount() - aussetzerCount == 4) {
                    aussetzer = false;
                    aussetzerLayout.setVisibility(View.GONE);
                    gameLayout.setVisibility(View.VISIBLE);
                    if (usersDynamicAdapter.getCount() > 4) {
                        fabGo.setVisibility(View.VISIBLE);
                    } else {
                        fabGo.setVisibility(View.GONE);
                    }
                } else {
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
                    soloSpinner.setEnabled(false);
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
                    soloSpinner.setEnabled(false);
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
                    soloSpinner.setEnabled(true);
                }
            }
        });

        buttonSchneider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(buttonSchneider.isSelected()){
                    buttonSchwarz.setSelected(false);
                }
                buttonSchneider.setSelected(!buttonSchneider.isSelected());
            }
        });

        buttonSchwarz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonSchwarz.setSelected(!buttonSchwarz.isSelected());
                buttonSchneider.setSelected(buttonSchwarz.isSelected());
            }
        });

        buttonLaufende.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (laufende==0) {
                    laufende+=2;
                } else {
                    laufende+=1;
                }
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

        buttonClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                laufende = 0;
                klopfen = 0;
                buttonLaufende.setText(String.valueOf(laufende));
                buttonKlopfen.setText(String.valueOf(klopfen));
            }
        });

        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!buttonSau.isSelected() && !buttonSolo.isSelected() && !buttonRamsch.isSelected()){
                    Toast.makeText(activity, "Wähle Gewinner und Spiel!",
                            Toast.LENGTH_SHORT).show();
                }else if(buttonSau.isSelected() && laufende==2){
                    Toast.makeText(activity, getResources().getString(R.string.text_sau_2_laufende), Toast.LENGTH_LONG).show();

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

                    if(activity.players!=null){
                        String precedingState = activity.players.get(activity.players.size()-1).getState().name();
                        for(Player player : activity.players){
							String newState = Player.State.PLAY.name();
                            if(precedingState.equals(Player.State.WAIT.name())){
                                newState = Player.State.WAIT.name();
                            }
                            precedingState = player.getState().name();
							if(newState.equals(Player.State.WAIT.name())){
                                player.setState(Player.State.WAIT);
                            }else{
                                player.setState(Player.State.PLAY);
                            }
                        }
                    }
                    winnerCount=0;
                    usersDynamicAdapter.set(activity.players);
                    usersDynamicAdapter.notifyDataSetChanged();
                }

            }
        });
    }

    private void setGridView(){

        //activity.players = game.getActivePlayers();

        int waitCount = 0;
        for(Player player : activity.players){
            if(player.getState()== Player.State.WAIT) waitCount+=1;
        }

        if(activity.players.size()-waitCount==4 || activity.players.size()==0){
            aussetzer=false;
            aussetzerLayout.setVisibility(View.GONE);
            gameLayout.setVisibility(View.VISIBLE);
        }

        usersDynamicAdapter = new UsersDynamicAdapter(activity, activity.players, 3);
        gridView.setAdapter(usersDynamicAdapter);
//        add callback to stop edit mode if needed
        gridView.setOnDropListener(new DynamicGridView.OnDropListener() {
            @Override
            public void onActionDrop() {
                gridView.stopEditMode();
                activity.players.clear();
                activity.players.addAll(usersDynamicAdapter.getAllItems());
                activity.updateTable();
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

                Player player = (Player) activity.players.get(position);
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
                                soloSpinner.setEnabled(false);
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
            if ((soloSpinner.getSelectedItem()).equals(getResources().getString(R.string.farbsolo))) {
                type = Types.FARBSOLO;
            } else if ((soloSpinner.getSelectedItem()).equals(getResources().getString(R.string.wenz))){
                type = Types.WENZ;
            } else if ((soloSpinner.getSelectedItem()).equals(getResources().getString(R.string.geier))){
                type = Types.GEIER;
            } else if ((soloSpinner.getSelectedItem()).equals(getResources().getString(R.string.bettel))){
                type = Types.BETTEL;
            }
        } else if(buttonRamsch.isSelected()){
            if (gameTypes.get(Types.RAMSCH)!=-1) {
                type = Types.RAMSCH;
            } else if (gameTypes.get(Types.POTT)!=-1){
                type = Types.POTT;
            }
        }

        List<Player> winners = new ArrayList<>();
        List<Player> losers = new ArrayList<>();
        List<Player> jungfrauen = new ArrayList<>();

        for(Player p : activity.players){
            if(p.getState() == Player.State.WIN){
                winners.add(p);
            } else if(p.getState() == Player.State.PLAY){
                losers.add(p);
            }
        }

        game.recordNewRound(type, winners, losers, jungfrauen, buttonSchneider.isSelected(),
                buttonSchwarz.isSelected(), laufende, klopfen);
    }

    public void updateAdapter(){
        usersDynamicAdapter.set(activity.players);
        usersDynamicAdapter.notifyDataSetChanged();
    }
}
