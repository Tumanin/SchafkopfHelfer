package com.applicatum.schafkopfhelfer.models;

import com.orm.SugarRecord;

import java.util.List;

/**
 * Created by Dominik on 30.11.2015.
 */
public class GameTypes extends SugarRecord {
    String type;
    Game game;
    int value;

    public GameTypes(){

    }

    public GameTypes(Game game, String type, int value){
        //GameTypes gt = new GameTypes();
        this.type = type;
        this.value = value;
        this.game = game;
        this.save();
    }

    public String getType() {
        return type;
    }

    public int getValue() {
        return value;
    }

    public static int getValue(Game game, String type){
        return GameTypes.find(GameTypes.class, "type = ? and game = ?", type, String.valueOf(game.getId())).get(0).getValue();
    }

    public static GameTypes getGameType(Game game, String type){
        return GameTypes.find(GameTypes.class, "type = ? and game = ?", type, String.valueOf(game.getId())).get(0);
    }

    public static List<GameTypes> getGameTypes(Game game){
        return GameTypes.find(GameTypes.class, "game = ?", String.valueOf(game.getId()));
    }

    public void setValue(int value){
        this.value = value;
        this.save();
    }


}
