package com.applicatum.schafkopfhelfer.models;

import com.orm.SugarRecord;
import com.orm.dsl.Ignore;
import com.orm.query.Condition;
import com.orm.query.Select;

import java.util.List;

/**
 * Created by Alexx on 02.11.2015.
 */
public class Player extends SugarRecord<Player>{

    private String name;
    private boolean visible;
    private int globalPoints;
    private int gamePoints;
    private int changePoints;
    public enum State{OUT, PLAY, WAIT, WIN};
    @Ignore
    private State state;
    @Ignore
    private int color = -1;

    public Player(){

    }

    public Player(String name){
        Player player = new Player();
        player.name = name;
        player.globalPoints = 0;
        player.gamePoints = 0;
        player.changePoints = 0;
        player.state = State.OUT;
        player.visible = true;
        player.save();
        color = -1;
    }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public void addPoints(int pointDifference){
        this.changePoints = pointDifference;
        this.globalPoints += pointDifference;
        this.gamePoints += pointDifference;
    }

    public int getChangePoints() {
        return changePoints;
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

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void update(State state){
        this.state = state;
    }

    public void rename(String name){
        this.name = name;
        this.save();
    }

    public void delete(){
        this.visible = false;
        this.name = this.name + "_deleted";
        this.save();
    }

    public static List<Player> getPlayers(){
        return Player.find(Player.class, "visible = ?", "1");
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
