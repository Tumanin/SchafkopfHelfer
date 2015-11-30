package com.applicatum.schafkopfhelfer.models;

import com.orm.SugarRecord;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import com.orm.dsl.Ignore;

/**
 * Created by Alexx on 09.11.2015.
 */
public class Game extends SugarRecord<Game>{

    long date;
    @Ignore
    List<Player> players;
    HashMap<String, Integer> gameTypes;

    public Game(){
        this.date = (new Date().getTime())/1000L;
    }

    public void createGame(){
        Game game = new Game();
        game.save();
    }


}
