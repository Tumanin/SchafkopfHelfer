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
        GamePlayers gp = new GamePlayers();
        gp.player = player;
        gp.game = game;
        gp.save();
    }

    public Player getPlayer() {
        return player;
    }

    public Game getGame() {
        return game;
    }

    public static List<Player> returnPlayers(Game game){
        List<GamePlayers> gp = PlayerRound.find(GamePlayers.class, "game = ?", String.valueOf(game.getId()));
        List<Player> players = new ArrayList<>();
        for(GamePlayers e : gp){
            players.add(e.getPlayer());
        }
        return players;
    }


}
