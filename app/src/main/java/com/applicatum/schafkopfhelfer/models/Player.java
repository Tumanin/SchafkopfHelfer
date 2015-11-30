package com.applicatum.schafkopfhelfer.models;

import com.orm.SugarRecord;
import com.orm.dsl.Ignore;

import java.util.List;

/**
 * Created by Alexx on 02.11.2015.
 */
public class Player extends SugarRecord<Player>{

    String name;
    boolean visible;
    @Ignore
    int points;
    @Ignore
    int pointsChange;
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
        player.pointsChange = 0;
        player.state = State.OUT;
        player.visible = true;
        player.save();
        color = -1;
    }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

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

    public static void deletePlayer(Player p){
        p.delete();
    }

    public void update(int points, int pointsChange, State state){
        this.points = points;
        this.pointsChange = pointsChange;
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

    public static boolean nameIsUnique(String name){
        List<Player> players = Player.find(Player.class, "name = ?", name);

        if(players.isEmpty()){
            return true;
        } else {
            return false;
        }
    }
}
