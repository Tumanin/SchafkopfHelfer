package com.applicatum.schafkopfhelfer.models;

import com.orm.SugarRecord;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dominik on 30.11.2015.
 */
public class GamePlayers extends SugarRecord<GamePlayers> {

    Player player;
    Game game;


    public GamePlayers(){

    }

    public GamePlayers(Game game, Player player){
        //GamePlayers gp = new GamePlayers();
        this.player = player;
        this.game = game;
        this.save();
    }

    public Player getPlayer() {
        return player;
    }

    public Game getGame() {
        return game;
    }

    public static List<Player> returnPlayers(Game game){
        List<GamePlayers> gps = GamePlayers.find(GamePlayers.class, "game = ?", String.valueOf(game.getId()));
        List<Player> players = new ArrayList<>();
        for(GamePlayers e : gps){
            players.add(e.getPlayer());
        }
        return players;
    }

    public static void deleteGamePlayers(Game game){
        List<GamePlayers> gps = GamePlayers.find(GamePlayers.class, "game = ?", String.valueOf(game.getId()));
        for(GamePlayers e : gps){
            e.delete();
        }
    }

    public static List<GamePlayers> getAll(){
        List<GamePlayers> gp = GamePlayers.listAll(GamePlayers.class);
        return gp;
    }

}
