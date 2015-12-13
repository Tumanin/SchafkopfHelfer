package com.applicatum.schafkopfhelfer.models;

import android.util.Log;

import com.orm.SugarRecord;
import com.orm.query.Condition;
import com.orm.query.Select;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dominik on 30.11.2015.
 */
public class GamePlayers extends SugarRecord {

    Player player;
    Game game;
    Integer seat;
    boolean active;


    public GamePlayers(){

    }

    public GamePlayers(Game game, Player player){
        //GamePlayers gp = new GamePlayers();
        this.seat = GamePlayers.getPlayers(game).size()+1;
        this.player = player;
        this.game = game;
        this.active = true;
        this.save();
    }

    public Player getPlayer() {
        return player;
    }

    public Game getGame() {
        return game;
    }

    public Integer getSeat() {
        return seat;
    }

    public void setSeat(Integer seat) {
        this.seat = seat;
        this.save();
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
        this.save();
    }

    public static List<Player> getPlayers(Game game){
        List<GamePlayers> gps = Select.from(GamePlayers.class).where(Condition.prop("game").eq(game.getId())).orderBy("seat").list();
        List<Player> players = new ArrayList<>();
        for(GamePlayers e : gps){
            players.add(e.getPlayer());
        }
        return players;
    }

    public static List<Player> getActivePlayers(Game game){
        List<GamePlayers> gps = Select.from(GamePlayers.class).where(Condition.prop("game").eq(game.getId()), Condition.prop("active").eq(1)).orderBy("seat").list();
        List<Player> players = new ArrayList<>();
        for(GamePlayers e : gps){
            players.add(e.getPlayer());
        }
        return players;
    }

    public static void deleteGamePlayers(Game game){
        List<GamePlayers> gps = GamePlayers.find(GamePlayers.class, "game = ?", String.valueOf(game.getId()));
        Log.d("GamePlayers", "calling updateActivePlayers");
        for(GamePlayers e : gps){
            e.delete();
        }
    }

    public static List<GamePlayers> getAll(){
        List<GamePlayers> gp = GamePlayers.listAll(GamePlayers.class);
        return gp;
    }

    public static GamePlayers getGamePlayer(Game game, Player player){
        return Select.from(GamePlayers.class).where(Condition.prop("game").eq(game.getId()), Condition.prop("player").eq(player.getId())).first();
    }
}
