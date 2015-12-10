package com.applicatum.schafkopfhelfer.models;

import android.util.Log;

import com.applicatum.schafkopfhelfer.utils.Types;
import com.orm.SugarRecord;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.orm.dsl.Ignore;
import com.orm.query.Select;

/**
 * Created by Alexx on 09.11.2015.
 */
public class Game extends SugarRecord{

    private static final String TAG = "GameModel";

    long date;
    int pot;
    @Ignore
    //List<Player> activePlayers;

    public Game(){
        //this.date = (new Date().getTime())/1000L;
        //pot = 0;
        //activePlayers = new ArrayList<>();
    }

    public static Game createGame(){
        Game game = new Game();
        game.date = (new Date().getTime())/1000L;
        game.pot = 0;
        game.save();
        //GameTypes gt = new GameTypes();
        new GameTypes(game, Types.RAMSCH, 10);
        new GameTypes(game, Types.POTT, -1);
        new GameTypes(game, Types.PFLICHT, -1);
        new GameTypes(game, Types.SAUSPIEL, 10);
        new GameTypes(game, Types.FARBSOLO, 50);
        new GameTypes(game, Types.WENZ, 50);
        new GameTypes(game, Types.GEIER, -1);
        new GameTypes(game, Types.BETTEL, -1);
        new GameTypes(game, Types.KLOPFEN, 10);
        new GameTypes(game, Types.LAUFENDE, 10);
        new GameTypes(game, Types.SCHNEIDER, 10);
        new GameTypes(game, Types.SCHWARZ, 10);
        return game;
    }

    public static Game lastGame(){
        return Select.from(Game.class).orderBy("id desc").first();
    }

    public void updateGameTypes(HashMap<String, Integer> gameTypes){
        Iterator it = gameTypes.entrySet().iterator();
        while (it.hasNext()){
            Map.Entry<String, Integer> pair = (Map.Entry) it.next();
            GameTypes.getGameType(this, pair.getKey()).setValue(pair.getValue());
            it.remove();
        }
        this.save();
    }

    public HashMap<String, Integer> getGameTypes() {
        List<GameTypes> gt = GameTypes.getGameTypes(this);
        HashMap<String, Integer> hm = new HashMap<>();
        for(GameTypes e : gt){
            hm.put(e.getType(), e.getValue());
        }
        return hm;
    }

    public List<Player> getActivePlayers() {
        return GamePlayers.getActivePlayers(this);
    }

    public void updateActivePlayers(List<Player> activePlayers) {
        GamePlayers.deleteGamePlayers(this);
        for(Player p : activePlayers){
            System.out.println(p);
            GamePlayers gp = new GamePlayers(this, p);
            gp.save();
        }
    }

    public void recordNewRound(String type, List<Player> winners, List<Player> losers, List<Player> jungfrauen, boolean schneider, boolean schwarz, int laufende, int klopf){
        Log.d(TAG, "Recording new Round of type "+type+" with winners: "+winners.size()+", losers: "+losers.size()+", jungfrauen: "+jungfrauen.size()+", schneider: "+schneider+", schwarz: "+schwarz+", laufende: "+laufende+", geklopft: "+klopf);

        float difference = GameTypes.getValue(this, type) + laufende * GameTypes.getValue(this, Types.LAUFENDE);
        Log.d(TAG, "The price for a game of type "+type+" is "+GameTypes.getValue(this, type));
        if(schneider){
            difference += GameTypes.getValue(this, Types.SCHNEIDER);
        }
        if(schwarz){
            difference += GameTypes.getValue(this, Types.SCHWARZ);
        }

        difference = (float) (difference * Math.pow(2, klopf));

        //Log.d("Game", "Therefore, the difference is "+difference);
        int NumberOfWinners = winners.size();
        int NumberOfLosers = losers.size();

        float winSum = ((NumberOfWinners+NumberOfLosers)-Math.min(NumberOfWinners,NumberOfLosers))*difference;
        Log.d("Game", "The winSum is: "+winSum);
        float win = winSum / NumberOfWinners;
        float lose = - winSum / NumberOfLosers;
        Log.d(TAG, "WIN: "+win);
        Log.d(TAG, "LOSE: "+lose);

        if(jungfrauen != null){
            lose += jungfrauen.size() * win / NumberOfLosers;
            for(Player e : jungfrauen){
                winners.remove(e);
            }
        }
        if((GameTypes.getValue(this, Types.POTT)==1)&&(type == Types.SAUSPIEL)){
            win += pot/NumberOfWinners;
        }

        Round round = new Round(type, laufende, klopf, schneider, schwarz, (int) Math.min(win, lose), this);

        for(Player p : winners){
            Log.d(TAG, "Writing new Player Round for winner. Player: "+p.getId()+" round: "+round.getId()+" win: "+win+"");
            new PlayerRound(p, round, true, (int) win, false);
        }

        for(Player p : losers){
            Log.d(TAG, "Writing new Player Round for loser. Player: "+p.getId()+" round: "+round.getId()+" win: "+lose+"");
            new PlayerRound(p, round, false, (int) lose, false);
        }

        for(Player p : jungfrauen){
            Log.d(TAG, "Writing new Player Round for Jungfrau. Player: "+p.getId()+" round: "+round.getId()+" win: "+2*win+"");
            new PlayerRound(p, round, true, (int) (2*win), true);
        }

        for(Player p : this.getActivePlayers()){
            Log.d(TAG, "Reset ChangePoints of Player: "+p.getId());
            p.resetChangePoints();
        }

        this.save();
    }

    public HashMap<Player, ArrayList<String>> getRoundsTable(){
        HashMap<Player, ArrayList<String>> hm = new HashMap<>();
        List<Round> rounds = Round.getRounds(this);
        for(Player p : this.getActivePlayers()){
            ArrayList<String> values = new ArrayList<>();
            for(Round r : rounds){
                PlayerRound pr = PlayerRound.getPR(r, p);
                if(pr != null){
                    values.add(String.valueOf(pr.getGamePoints()));
                } else {
                    values.add("-");
                }
            }
            hm.put(p, values);
        }
        return hm;
    }

}
