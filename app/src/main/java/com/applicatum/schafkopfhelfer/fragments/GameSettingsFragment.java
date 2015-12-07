package com.applicatum.schafkopfhelfer.fragments;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.applicatum.schafkopfhelfer.R;
import com.applicatum.schafkopfhelfer.StartActivity;
import com.applicatum.schafkopfhelfer.models.Game;
import com.applicatum.schafkopfhelfer.models.GameTypes;
import com.applicatum.schafkopfhelfer.utils.PreferenceUtils;
import com.applicatum.schafkopfhelfer.utils.Types;

import java.util.HashMap;

public class GameSettingsFragment extends Fragment {

    FloatingActionButton fabGo;
    View mRootView;
    StartActivity activity;
    Game game;

    RadioGroup ramschGroup;
    RadioButton radioRamsch;
    RadioButton radioPott;
    RadioButton radioPflicht;

    CheckBox checkSauspiel;
    CheckBox checkSolo;
    CheckBox checkWenz;
    CheckBox checkGeier;
    CheckBox checkBettel;

    EditText editRamsch;
    EditText editPott;
    EditText editPflicht;
    EditText editSauspiel;
    EditText editSolo;
    EditText editWenz;
    EditText editGeier;
    EditText editBettel;

    EditText editKlopfen;
    EditText editLaufende;
    EditText editSchneider;
    EditText editSchwarz;

    PreferenceUtils mPreferenceUtils;

    public GameSettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mRootView = inflater.inflate(R.layout.fragment_game_settings, container, false);

        ramschGroup = (RadioGroup) mRootView.findViewById(R.id.ramschGroup);
        radioRamsch = (RadioButton) mRootView.findViewById(R.id.radioRamsch);
        radioPott = (RadioButton) mRootView.findViewById(R.id.radioPott);
        radioPflicht = (RadioButton) mRootView.findViewById(R.id.radioPflicht);

        checkSauspiel = (CheckBox) mRootView.findViewById(R.id.checkSauspiel);
        checkSolo = (CheckBox) mRootView.findViewById(R.id.checkSolo);
        checkWenz = (CheckBox) mRootView.findViewById(R.id.checkWenz);
        checkGeier = (CheckBox) mRootView.findViewById(R.id.checkGeier);
        checkBettel = (CheckBox) mRootView.findViewById(R.id.checkBettel);

        editRamsch = (EditText) mRootView.findViewById(R.id.editRamsch);
        editPott = (EditText) mRootView.findViewById(R.id.editPott);
        editPflicht = (EditText) mRootView.findViewById(R.id.editPflicht);
        editSauspiel = (EditText) mRootView.findViewById(R.id.editSauspiel);
        editSolo = (EditText) mRootView.findViewById(R.id.editSolo);
        editWenz = (EditText) mRootView.findViewById(R.id.editWenz);
        editGeier = (EditText) mRootView.findViewById(R.id.editGeier);
        editBettel = (EditText) mRootView.findViewById(R.id.editBettel);

        editKlopfen = (EditText) mRootView.findViewById(R.id.editKlopfen);
        editLaufende = (EditText) mRootView.findViewById(R.id.editLaufende);
        editSchneider = (EditText) mRootView.findViewById(R.id.editSchneider);
        editSchwarz = (EditText) mRootView.findViewById(R.id.editSchwarz);

        mPreferenceUtils = PreferenceUtils.getInstance(getActivity());

        game = Game.lastGame();

        if(game==null){
            game = Game.createGame();
        }
        HashMap<String, Integer> gameTypes = game.getGameTypes();

        radioRamsch.setChecked(gameTypes.get(Types.RAMSCH)!=-1);
        radioPott.setChecked(gameTypes.get(Types.POTT)!=-1);
        radioPflicht.setChecked(gameTypes.get(Types.PFLICHT) != -1);

        editRamsch.setEnabled(gameTypes.get(Types.RAMSCH) != -1);
        editPott.setEnabled(gameTypes.get(Types.POTT) != -1);
        editPflicht.setEnabled(gameTypes.get(Types.PFLICHT) != -1);

        editRamsch.setText(String.valueOf(mPreferenceUtils.getInt(Types.RAMSCH)));
        editPott.setText(String.valueOf(mPreferenceUtils.getInt(Types.POTT)));
        editPflicht.setText(String.valueOf(mPreferenceUtils.getInt(Types.PFLICHT)));

        checkSauspiel.setChecked(gameTypes.get(Types.SAUSPIEL) != -1);
        editSauspiel.setEnabled(gameTypes.get(Types.SAUSPIEL) != -1);
        editSauspiel.setText(String.valueOf(mPreferenceUtils.getInt(Types.SAUSPIEL)));

        checkSolo.setChecked(gameTypes.get(Types.FARBSOLO) != -1);
        editSolo.setEnabled(gameTypes.get(Types.FARBSOLO) != -1);
        editSolo.setText(String.valueOf(mPreferenceUtils.getInt(Types.FARBSOLO)));

        checkWenz.setChecked(gameTypes.get(Types.WENZ) != -1);
        editWenz.setEnabled(gameTypes.get(Types.WENZ) != -1);
        editWenz.setText(String.valueOf(mPreferenceUtils.getInt(Types.WENZ)));

        checkGeier.setChecked(gameTypes.get(Types.GEIER) != -1);
        editGeier.setEnabled(gameTypes.get(Types.GEIER) != -1);
        editGeier.setText(String.valueOf(mPreferenceUtils.getInt(Types.GEIER)));

        checkBettel.setChecked(gameTypes.get(Types.BETTEL) != -1);
        editBettel.setEnabled(gameTypes.get(Types.BETTEL) != -1);
        editBettel.setText(String.valueOf(mPreferenceUtils.getInt(Types.BETTEL)));


        activity = (StartActivity) getActivity();
        fabGo = (FloatingActionButton) mRootView.findViewById(R.id.fab);

        fabGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!(checkSauspiel.isChecked() || checkSolo.isChecked() || checkWenz.isChecked() || checkGeier.isChecked() || checkBettel.isChecked())){
                    Toast.makeText(activity, "Was willst du spielen? Wähle die Spiele!",
                            Toast.LENGTH_LONG).show();
                }else{
                    HashMap<String, Integer> hashMap = new HashMap<String, Integer>();

                    if(radioRamsch.isChecked()){
                        if(editRamsch.getText().toString().equals("")){
                            Toast.makeText(activity, "Ramsch Preis darf nicht leer sein!",
                                    Toast.LENGTH_LONG).show();
                            return;
                        }else{
                            hashMap.put(Types.RAMSCH, Integer.parseInt(editRamsch.getText().toString()));
                            mPreferenceUtils.storeInt(Types.RAMSCH, Integer.parseInt(editRamsch.getText().toString()));
                        }
                    }else{
                        hashMap.put(Types.RAMSCH, -1);
                        mPreferenceUtils.storeInt(Types.RAMSCH, Integer.parseInt(editRamsch.getText().toString()));
                    }

                    if(radioPott.isChecked()){
                        if(editPott.getText().toString().equals("")){
                            Toast.makeText(activity, "Pott Preis darf nicht leer sein!",
                                    Toast.LENGTH_LONG).show();
                            return;
                        }else{
                            hashMap.put(Types.POTT, Integer.parseInt(editPott.getText().toString()));
                            mPreferenceUtils.storeInt(Types.POTT, Integer.parseInt(editPott.getText().toString()));
                        }
                    }else{
                        hashMap.put(Types.POTT, -1);
                        mPreferenceUtils.storeInt(Types.POTT, Integer.parseInt(editPott.getText().toString()));
                    }

                    if(radioPflicht.isChecked()){
                        if(editPflicht.getText().toString().equals("")){
                            Toast.makeText(activity, "Pflicht Preis darf nicht leer sein!",
                                    Toast.LENGTH_LONG).show();
                            return;
                        }else{
                            hashMap.put(Types.PFLICHT, Integer.parseInt(editPflicht.getText().toString()));
                            mPreferenceUtils.storeInt(Types.PFLICHT, Integer.parseInt(editPflicht.getText().toString()));
                        }
                    }else{
                        hashMap.put(Types.PFLICHT, -1);
                        mPreferenceUtils.storeInt(Types.PFLICHT, Integer.parseInt(editPflicht.getText().toString()));
                    }

                    if(checkSauspiel.isChecked()){
                        if(editSauspiel.getText().toString().equals("")){
                            Toast.makeText(activity, "Sauspiel Preis darf nicht leer sein!",
                                    Toast.LENGTH_LONG).show();
                            return;
                        }else{
                            hashMap.put(Types.SAUSPIEL, Integer.parseInt(editSauspiel.getText().toString()));
                            mPreferenceUtils.storeInt(Types.SAUSPIEL, Integer.parseInt(editSauspiel.getText().toString()));
                        }
                    }else{
                        hashMap.put(Types.SAUSPIEL, -1);
                        mPreferenceUtils.storeInt(Types.SAUSPIEL, Integer.parseInt(editSauspiel.getText().toString()));
                    }

                    if(checkSolo.isChecked()){
                        if(editSolo.getText().toString().equals("")){
                            Toast.makeText(activity, "Solo Preis darf nicht leer sein!",
                                    Toast.LENGTH_LONG).show();
                            return;
                        }else{
                            hashMap.put(Types.FARBSOLO, Integer.parseInt(editSolo.getText().toString()));
                            mPreferenceUtils.storeInt(Types.FARBSOLO, Integer.parseInt(editSolo.getText().toString()));
                        }
                    }else{
                        hashMap.put(Types.FARBSOLO, -1);
                        mPreferenceUtils.storeInt(Types.FARBSOLO, Integer.parseInt(editSolo.getText().toString()));
                    }

                    if(checkWenz.isChecked()){
                        if(editWenz.getText().toString().equals("")){
                            Toast.makeText(activity, "Wenz Preis darf nicht leer sein!",
                                    Toast.LENGTH_LONG).show();
                            return;
                        }else{
                            hashMap.put(Types.WENZ, Integer.parseInt(editWenz.getText().toString()));
                            mPreferenceUtils.storeInt(Types.WENZ, Integer.parseInt(editWenz.getText().toString()));
                        }
                    }else{
                        hashMap.put(Types.WENZ, -1);
                        mPreferenceUtils.storeInt(Types.WENZ, Integer.parseInt(editWenz.getText().toString()));
                    }

                    if(checkGeier.isChecked()){
                        if(editGeier.getText().toString().equals("")){
                            Toast.makeText(activity, "Geier Preis darf nicht leer sein!",
                                    Toast.LENGTH_LONG).show();
                            return;
                        }else{
                            hashMap.put(Types.GEIER, Integer.parseInt(editGeier.getText().toString()));
                            mPreferenceUtils.storeInt(Types.GEIER, Integer.parseInt(editGeier.getText().toString()));
                        }
                    }else{
                        hashMap.put(Types.GEIER, -1);
                        mPreferenceUtils.storeInt(Types.GEIER, Integer.parseInt(editGeier.getText().toString()));
                    }

                    if(checkBettel.isChecked()){
                        if(editBettel.getText().toString().equals("")){
                            Toast.makeText(activity, "Bettel Preis darf nicht leer sein!",
                                    Toast.LENGTH_LONG).show();
                            return;
                        }else{
                            hashMap.put(Types.BETTEL, Integer.parseInt(editBettel.getText().toString()));
                            mPreferenceUtils.storeInt(Types.BETTEL, Integer.parseInt(editBettel.getText().toString()));
                        }
                    }else{
                        hashMap.put(Types.BETTEL, -1);
                        mPreferenceUtils.storeInt(Types.BETTEL, Integer.parseInt(editBettel.getText().toString()));
                    }

                    if(editKlopfen.getText().toString().equals("")){
                        Toast.makeText(activity, "Klopfen Preis darf nicht leer sein!",
                                Toast.LENGTH_LONG).show();
                        return;
                    } else {
                        hashMap.put(Types.KLOPFEN, Integer.parseInt(editKlopfen.getText().toString()));
                    }
                    mPreferenceUtils.storeInt(Types.KLOPFEN, Integer.parseInt(editKlopfen.getText().toString()));
                    if(editLaufende.getText().toString().equals("")){
                        Toast.makeText(activity, "Laufende Preis darf nicht leer sein!",
                                Toast.LENGTH_LONG).show();
                        return;
                    }else{
                        hashMap.put(Types.LAUFENDE, Integer.parseInt(editLaufende.getText().toString()));
                        mPreferenceUtils.storeInt(Types.LAUFENDE, Integer.parseInt(editLaufende.getText().toString()));
                    }

                    if(editSchneider.getText().toString().equals("")){
                        Toast.makeText(activity, "Schneider Preis darf nicht leer sein!",
                                Toast.LENGTH_LONG).show();
                        return;
                    }else{
                        hashMap.put(Types.SCHNEIDER, Integer.parseInt(editSchneider.getText().toString()));
                        mPreferenceUtils.storeInt(Types.SCHNEIDER, Integer.parseInt(editSchneider.getText().toString()));
                    }
                    if(editSchwarz.getText().toString().equals("")){
                        Toast.makeText(activity, "Schwarz Preis darf nicht leer sein!",
                                Toast.LENGTH_LONG).show();
                        return;
                    }else{
                        hashMap.put(Types.SCHWARZ, Integer.parseInt(editSchwarz.getText().toString()));
                        mPreferenceUtils.storeInt(Types.SCHWARZ, Integer.parseInt(editSchwarz.getText().toString()));
                    }

                    Game game = Game.createGame();
                    game.updateGameTypes(hashMap);

                    Fragment fragment = new PlayersFragment();
                    activity.startFragment(fragment, true);
                }


            }
        });
        setListeners();

        return mRootView;
    }

    private void setListeners(){

        radioRamsch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    editRamsch.setEnabled(true);
                    radioPott.setChecked(false);
                    editPott.setEnabled(false);
                    radioPflicht.setChecked(false);
                    editPflicht.setEnabled(false);
                }
            }
        });
        radioPott.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    editRamsch.setEnabled(false);
                    radioRamsch.setChecked(false);
                    editPott.setEnabled(true);
                    radioPflicht.setChecked(false);
                    editPflicht.setEnabled(false);
                }
            }
        });
        radioPflicht.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    editRamsch.setEnabled(false);
                    radioPott.setChecked(false);
                    editPott.setEnabled(false);
                    radioRamsch.setChecked(false);
                    editPflicht.setEnabled(true);
                }
            }
        });
        checkSauspiel.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    editSauspiel.setEnabled(true);
                }else{
                    editSauspiel.setEnabled(false);
                }
            }
        });
        checkSolo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    editSolo.setEnabled(true);
                }else{
                    editSolo.setEnabled(false);
                }
            }
        });
        checkWenz.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    editWenz.setEnabled(true);
                }else{
                    editWenz.setEnabled(false);
                }
            }
        });
        checkGeier.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    editGeier.setEnabled(true);
                }else{
                    editGeier.setEnabled(false);
                }
            }
        });
        checkBettel.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    editBettel.setEnabled(true);
                }else{
                    editBettel.setEnabled(false);
                }
            }
        });
    }

    private void setStoredPrices(){
        /*
        radioRamsch.setChecked(mPreferenceUtils.getBoolean(PreferenceUtils.PREF_RAMSCH_PLAY));
        editRamsch.setEnabled(mPreferenceUtils.getBoolean(PreferenceUtils.PREF_RAMSCH_PLAY));
        editRamsch.setText(String.valueOf(mPreferenceUtils.getInt(PreferenceUtils.PREF_RAMSCH_PRICE)));
        */


    }

}
