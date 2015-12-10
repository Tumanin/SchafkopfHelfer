package com.applicatum.schafkopfhelfer.models;

import android.util.Log;

import com.orm.SugarRecord;
import com.orm.query.Condition;
import com.orm.query.Select;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Alexx on 09.11.2015.
 */
public class Round extends SugarRecord{

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
        this.type = type;
        this.laufende = laufende;
        this.klopfen = klopfen;
        this.schneider = schneider;
        this.schwarz = schwarz;
        this.price = price;
        this.date = (new Date().getTime())/1000L;
        this.game = game;
        this.save();
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

    public Game getGame() {
        return game;
    }

    public static void removeLastRound(){
        Log.d("Round", "Remove last Round called");
        Round r = Round.getLastRound();
        Game game = r.getGame();
        PlayerRound.deletePlayerRound(r);
        Log.d("Round", "Delete Round "+r.getId());
        r.delete();
        List<Player> activePlayers = game.getActivePlayers();
        for(Player p : activePlayers){
            p.restorePoints(game);
        }
    }

    public static Round getLastRound(Game game){
        Log.d("Round", "GetLastRound called for game " + game.getId());
        return Select.from(Round.class).where(Condition.prop("game").eq(game.getId())).orderBy("id desc").first();
    }

    public static List<Round> getRounds(Game game){
        return Select.from(Round.class).where(Condition.prop("game").eq(game.getId())).orderBy("id").list();
    }

    public static List<Round> getRoundsDesc(Game game){
        return Select.from(Round.class).where(Condition.prop("game").eq(game.getId())).orderBy("id desc").list();
    }

    public static Round getLastRound(){
        Log.d("Round", "GetLastRound called");
        return Select.from(Round.class).orderBy("id desc").first();
    }







}
