package com.applicatum.schafkopfhelfer.utils;

import com.applicatum.schafkopfhelfer.models.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alexx on 09.11.2015.
 */
public class PlayersList {

    List<Player> players;

    private static PlayersList playersList;

    private PlayersList(){
        players = new ArrayList<>();
    }

    public static PlayersList getInstance(){
        if(playersList==null){
            playersList = new PlayersList();
        }
        return playersList;
    }

    public void setList(List<Player> newList){
        players.clear();
        players.addAll(newList);
    }

    public List<Player> getList(){
        return players;
    }
}
