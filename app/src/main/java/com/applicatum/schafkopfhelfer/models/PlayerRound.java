package com.applicatum.schafkopfhelfer.models;

import com.orm.SugarRecord;

import java.util.Iterator;
import java.util.List;

/**
 * Created by Dominik on 30.11.2015.
 */
public class PlayerRound extends SugarRecord<PlayerRound> {

    Player player;
    Round round;
    boolean gewonnen;
    int globalPoints;
    int gamePoints;
    int changePoints;
    boolean jungfrau;

    public PlayerRound(){

    }

    public PlayerRound(Player player, Round round, boolean gewonnen, int changePoints, boolean jungfrau){
        PlayerRound pr = new PlayerRound();
        pr.player = player;
        player.addPoints(changePoints);
        pr.round = round;
        pr.gewonnen = gewonnen;
        pr.globalPoints = player.getGlobalPoints();
        pr.gamePoints = player.getGamePoints();
        pr.changePoints = changePoints;
        pr.jungfrau = jungfrau;
        pr.save();
    }

    public static List<PlayerRound> getRounds(){
        return PlayerRound.listAll(PlayerRound.class);
    }

    public static void deletePlayerRound(Round r) {
        PlayerRound.deleteAll(PlayerRound.class, "round = ?", String.valueOf(r.getId()));
    }

}
