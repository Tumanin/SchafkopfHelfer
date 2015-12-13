package com.applicatum.schafkopfhelfer.models;


import android.util.Log;

import com.orm.SugarRecord;
import com.orm.query.Condition;
import com.orm.query.Select;

import java.util.List;

/**
 * Created by Alexx on 02.11.2015.
 */
public class Player extends SugarRecord {

    private String name;
    private boolean visible;
    private int globalPoints;
    private int gamePoints;
    private int changePoints;
    public enum State{OUT, PLAY, WAIT, WIN};
    private State state;
    private int color = -1;

    public Player(){

    }

    public Player(String name){
        this.name = name;
        this.globalPoints = 0;
        this.gamePoints = 0;
        this.changePoints = 0;
        this.state = State.OUT;
        this.visible = true;
        this.color = -1;
        this.save();
        Log.d("Player", "Created new Player with id "+this.getId());
    }

    public String getName() { return name; }

    public void setName(String name) {
        this.name = name;
        this.save();
    }

    public void addPoints(int pointDifference){
        //Log.d("Player", "Adding points to player: " + this.getId() + " points global: " + this.globalPoints + " gamePoints: " + this.gamePoints + " pointDifference: " + pointDifference);
        this.changePoints = pointDifference;
        this.globalPoints += pointDifference;
        this.gamePoints += pointDifference;
        this.save();
    }

    public int getChangePoints() {
        Log.d("Player", "ChangePoints for player " + this.getId() + ": " + this.changePoints);
        return this.changePoints;
    }

    public int getGlobalPoints() {
        //Select query = Select.from(PlayerRound.class)
        //        .where(Condition.prop("player").eq(this.getId())).orderBy("id desc");
        //PlayerRound LastRound = (PlayerRound) query.first();
        return globalPoints;
    }

    public int getPoints() {
        return gamePoints;
    }

    public void restorePoints(Game game) {
        Log.d("Player", "RestorePoints called for player "+this.getId()+" and game "+game.getId());
        PlayerRound lastPR = this.getLastPR(game);

        if(lastPR != null){
            Log.d("Player", "LastPR is " + lastPR.getId() + ", last Round is "+lastPR.getRound().getId());
            gamePoints = lastPR.getGamePoints();
            changePoints = lastPR.getChangePoints();
        } else {
            gamePoints = 0;
            changePoints = 0;
        }
        Log.d("Player", "New gamePoints: " + gamePoints);
        Log.d("Player", "New changePoints: " + changePoints);
        this.save();
    }

    public PlayerRound getLastPR(Game game){
        List<Round> rounds = Round.getRoundsDesc(game);
        PlayerRound pr = null;
        for(Round r : rounds){
            pr = Select.from(PlayerRound.class).where(Condition.prop("round").eq(r.getId()),Condition.prop("player").eq(this.getId())).first();
            if(pr != null){
                break;
            }
        }
        return pr;
    }

    public void resetPoints(Game game){
        this.gamePoints = 0;
        this.changePoints = 0;
        this.save();
    }

    public void resetChangePoints(){
        this.changePoints = 0;
        this.save();
        Log.d("Player", "New changePoints is: " + this.getChangePoints());
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
        Log.d("Player", "Set state of player "+this.getId()+" to "+state);
        this.save();
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
        Log.d("Player", "Set color of player "+this.getId()+" to "+color);
        this.save();
    }

    public void update(State state){
        this.state = state;
    }

    public void rename(String name){
        this.name = name;
        this.save();
    }

    public void deletePlayer(){
        this.visible = false;
        this.name = this.name + "_deleted";
        this.save();
    }

    public static Player findMitName(String name){
        return Player.find(Player.class, "name = ?", name).get(0);
    }

    public static List<Player> getPlayers(){
        return Player.find(Player.class, "visible = ?", "1");
    }

    public static Player getPlayerById(int id){
        return Player.findById(Player.class, id);
    }

    public static boolean nameIsUnique(String name) {
        List<Player> players = Player.find(Player.class, "name = ?", name);

        if (players.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }
}
