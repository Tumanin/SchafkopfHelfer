package com.applicatum.schafkopfhelfer.models;

import com.orm.SugarRecord;
import com.orm.dsl.Ignore;

import java.util.List;

/**
 * Created by Alexx on 02.11.2015.
 */
public class Player extends SugarRecord<Player>{

    String name;
    @Ignore
    int points;
    @Ignore
    public enum State{OUT, PLAY, WAIT, WIN};
    @Ignore
    State state;
    @Ignore
    int color = -1;

    public Player(){

    }

    public Player(String name){
        Player player = new Player();
        player.name = name;
        player.points = 0;
        player.state = State.OUT;
        player.save();
        color = -1;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
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

    public static List<Player> getPlayers(){

        return Player.listAll(Player.class);
    }
}
