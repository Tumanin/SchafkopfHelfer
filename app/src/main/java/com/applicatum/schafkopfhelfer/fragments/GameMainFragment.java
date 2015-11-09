package com.applicatum.schafkopfhelfer.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.applicatum.schafkopfhelfer.MainActivity;
import com.applicatum.schafkopfhelfer.R;
import com.applicatum.schafkopfhelfer.adapters.UsersDynamicAdapter;
import com.applicatum.schafkopfhelfer.models.Player;
import com.applicatum.schafkopfhelfer.utils.PlayersList;

import org.askerov.dynamicgrid.DynamicGridView;

import java.util.ArrayList;
import java.util.List;


public class GameMainFragment extends Fragment {

    private static final String TAG = "GameMainFragment";

    private DynamicGridView gridView;
    private MainActivity activity;
    private View view;


    public GameMainFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_game_main, container, false);
        gridView = (DynamicGridView) view.findViewById(R.id.usersGrid);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        activity = (MainActivity) getActivity();
        setGridView();
    }

    private void setGridView(){

        /*
        Player player1 = new Player("User1");
        Player player2 = new Player("User2");
        Player player3 = new Player("User3");
        Player player4 = new Player("User4");
        Player player5 = new Player("User5");
        Player player6 = new Player("User6");
        Player player7 = new Player("User7");
        Player player8 = new Player("User8");
        */

        List<Player> players = new ArrayList<>();
        players.addAll(PlayersList.getInstance().getList());
        /*
        players.add(player1);
        players.add(player2);
        players.add(player3);
        players.add(player4);
        players.add(player5);
        players.add(player6);
        players.add(player7);
        players.add(player8);
        */

        UsersDynamicAdapter usersDynamicAdapter = new UsersDynamicAdapter(activity, players, 3);
        gridView.setAdapter(usersDynamicAdapter);
//        add callback to stop edit mode if needed
        gridView.setOnDropListener(new DynamicGridView.OnDropListener()
        {
            @Override
            public void onActionDrop()
            {
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
                Toast.makeText(activity, "OnClick!",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}
