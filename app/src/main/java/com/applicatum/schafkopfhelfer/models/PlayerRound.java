package com.applicatum.schafkopfhelfer.models;

import android.util.Log;

import com.orm.SugarRecord;
import com.orm.query.Condition;
import com.orm.query.Select;

import java.util.Iterator;
import java.util.List;

/**
 * Created by Dominik on 30.11.2015.
 */
public class PlayerRound extends SugarRecord {

    Player player;
    Round round;
    boolean gewonnen;
    int globalPoints;
    int gamePoints;
    int changePoints;
    boolean jungfrau;

    public PlayerRound(){

    }

    public Round getRound() {
        return round;
    }

    public PlayerRound(Player player, Round round, boolean gewonnen, int changePoints, boolean jungfrau){
        this.player = player;
        this.player.addPoints(changePoints);
        this.round = round;
        this.gewonnen = gewonnen;
        this.globalPoints = player.getGlobalPoints();
        this.gamePoints = player.getPoints();
        this.changePoints = changePoints;
        this.jungfrau = jungfrau;
        this.save();
    }

    public static List<PlayerRound> getRounds(){
        return PlayerRound.listAll(PlayerRound.class);
    }

    public static void deletePlayerRound(Round r) {
        Log.d("PlayerRound", "DeletePlayerRounds for round " + r.getId() + " called");
        PlayerRound.deleteAll(PlayerRound.class, "round = ?", String.valueOf(r.getId()));
    }

    public static PlayerRound getPR(Round r, Player p){
        return Select.from(PlayerRound.class).where(Condition.prop("round").eq(r.getId()), Condition.prop("player").eq(p.getId())).first();
    }

    public int getGamePoints() {
        return gamePoints;
    }

    public int getChangePoints() {
        return changePoints;
    }
}
