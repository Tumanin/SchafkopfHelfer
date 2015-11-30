package com.applicatum.schafkopfhelfer.fragments;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.applicatum.schafkopfhelfer.R;
import com.applicatum.schafkopfhelfer.StartActivity;
import com.applicatum.schafkopfhelfer.utils.PreferenceUtils;

public class GameSettingsFragment extends Fragment {

    FloatingActionButton fabGo;
    View mRootView;
    StartActivity activity;

    RadioGroup ramschGroup;
    RadioButton radioRamsch;
    RadioButton radioPott;

    CheckBox checkSauspiel;
    CheckBox checkSolo;
    CheckBox checkWenz;
    CheckBox checkGeier;
    CheckBox checkFarbwenz;
    CheckBox checkFarbgeier;
    CheckBox checkBettel;

    EditText editRamsch;
    EditText editPott;
    EditText editSauspiel;
    EditText editSolo;
    EditText editWenz;
    EditText editGeier;
    EditText editFarbwenz;
    EditText editFarbgeier;
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

        checkSauspiel = (CheckBox) mRootView.findViewById(R.id.checkSauspiel);
        checkSolo = (CheckBox) mRootView.findViewById(R.id.checkSolo);
        checkWenz = (CheckBox) mRootView.findViewById(R.id.checkWenz);
        checkGeier = (CheckBox) mRootView.findViewById(R.id.checkGeier);
        checkFarbwenz = (CheckBox) mRootView.findViewById(R.id.checkFarbwenz);
        checkFarbgeier = (CheckBox) mRootView.findViewById(R.id.checkFarbgeier);
        checkBettel = (CheckBox) mRootView.findViewById(R.id.checkBettel);

        editRamsch = (EditText) mRootView.findViewById(R.id.editRamsch);
        editPott = (EditText) mRootView.findViewById(R.id.editPott);
        editSauspiel = (EditText) mRootView.findViewById(R.id.editSauspiel);
        editSolo = (EditText) mRootView.findViewById(R.id.editSolo);
        editWenz = (EditText) mRootView.findViewById(R.id.editWenz);
        editGeier = (EditText) mRootView.findViewById(R.id.editGeier);
        editFarbwenz = (EditText) mRootView.findViewById(R.id.editFarbwenz);
        editFarbgeier = (EditText) mRootView.findViewById(R.id.editFarbgeier);
        editBettel = (EditText) mRootView.findViewById(R.id.editBettel);

        editKlopfen = (EditText) mRootView.findViewById(R.id.editKlopfen);
        editLaufende = (EditText) mRootView.findViewById(R.id.editLaufende);
        editSchneider = (EditText) mRootView.findViewById(R.id.editSchneider);
        editSchwarz = (EditText) mRootView.findViewById(R.id.editSchwarz);

        mPreferenceUtils = PreferenceUtils.getInstance(getActivity());

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

    private void setStoredPrices(){
        radioRamsch.setChecked(mPreferenceUtils.getBoolean(PreferenceUtils.PREF_RAMSCH_PLAY));
        editRamsch.setEnabled(mPreferenceUtils.getBoolean(PreferenceUtils.PREF_RAMSCH_PLAY));
        editRamsch.setText(String.valueOf(mPreferenceUtils.getInt(PreferenceUtils.PREF_RAMSCH_PRICE)));


    }

}
