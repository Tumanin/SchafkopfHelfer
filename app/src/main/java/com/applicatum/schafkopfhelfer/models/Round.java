package com.applicatum.schafkopfhelfer.models;

import com.orm.SugarRecord;

import java.util.Date;
import java.util.List;

/**
 * Created by Alexx on 09.11.2015.
 */
public class Round extends SugarRecord<Round>{

    String type;
    int laufende;
    int klopfen;
    boolean schneider;
    boolean schwarz;
    int price;
    long date;
    Game game;

    public Round(){

    }

    public Round(String type, int laufende, int klopfen, boolean schneider, boolean schwarz, int price, Game game){
        Round round = new Round();
        round.type = type;
        round.laufende = laufende;
        round.klopfen = klopfen;
        round.schneider = schneider;
        round.schwarz = schwarz;
        round.price = price;
        round.date = (new Date().getTime())/1000L;
        round.game = game;
        round.save();
    }

    public void setDate(long date) {
        this.date = date;
        this.save();
    }

    public String getType() { return type;}

    public int getPrice() { return price; }

    public int getLaufende() { return laufende; }

    public int getKlopfen() { return klopfen; }

    public boolean getSchneider() { return schneider; }

    public boolean getSchwarz() { return schwarz; }

    public static List<Round> getRounds(){
        return Round.listAll(Round.class);
    }

    public static void deleteRound(Round r){
        PlayerRound.deletePlayerRound(r);
        r.delete();

    }







}
