package com.applicatum.schafkopfhelfer.models;

import com.applicatum.schafkopfhelfer.utils.Types;
import com.orm.SugarRecord;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.orm.dsl.Ignore;

/**
 * Created by Alexx on 09.11.2015.
 */
public class Game extends SugarRecord<Game>{

    long date;
    int pot;
    List<Player> activePlayers;
    HashMap<String, Integer> gameTypes;

    public Game(){
        this.date = (new Date().getTime())/1000L;
        pot = 0;

        gameTypes = new HashMap<>();
        gameTypes.put(Types.RAMSCH, 10);
        gameTypes.put(Types.POTT, -1);
        gameTypes.put(Types.PFLICHT, -1);
        gameTypes.put(Types.SAUSPIEL, 10);
        gameTypes.put(Types.FARBSOLO, 50);
        gameTypes.put(Types.WENZ, 50);
        gameTypes.put(Types.GEIER, -1);
        gameTypes.put(Types.BETTEL, -1);
        gameTypes.put(Types.KLOPFEN, 10);
        gameTypes.put(Types.LAUFENDE, 10);
        gameTypes.put(Types.SCHNEIDER, 10);
        gameTypes.put(Types.SCHWARZ, 10);
    }

    public static Game createGame(){
        Game game = new Game();
        game.save();
        return game;
    }

    public void updateGameTypes(HashMap<String, Integer> gameTypes){
        Iterator it = gameTypes.entrySet().iterator();
        while (it.hasNext()){
            Map.Entry<String, Integer> pair = (Map.Entry) it.next();
            this.gameTypes.put(pair.getKey(), pair.getValue());
            it.remove();
        }
    }

    public HashMap<String, Integer> getGameTypes() {
        return gameTypes;
    }

    public List<Player> getActivePlayers() {
        return activePlayers;
    }

    public void updateActivePlayers(List<Player> activePlayers) {
        this.activePlayers = activePlayers;
    }

    public void recordNewRound(String type, List<Player> winners, List<Player> losers, List<Player> jungfrauen, boolean schneider, boolean schwarz, int laufende, int klopf){
        int difference = gameTypes.get(type) + laufende * gameTypes.get(Types.LAUFENDE);
        if(schneider){
            difference += gameTypes.get(Types.SCHNEIDER);
        }
        if(schwarz){
            difference += gameTypes.get(Types.SCHWARZ);
        }
        difference = difference * klopf;

        int NumberOfWinners = winners.size();
        int NumberOfLosers = losers.size();

        int winSum = ((NumberOfWinners+NumberOfLosers)-Math.min(NumberOfWinners,NumberOfLosers))+difference;
        int win = winSum / NumberOfWinners;
        int lose = - winSum / NumberOfLosers;

        if(jungfrauen != null){
            lose += jungfrauen.size() * win / NumberOfLosers;
            for(Player e : jungfrauen){
                winners.remove(e);
            }
        }
        if((gameTypes.get(Types.POTT)==1)&&(type == Types.SAUSPIEL)){
            win += pot/NumberOfWinners;
        }

        Round round = new Round(type, laufende, klopf, schneider, schwarz, Math.min(win, lose), this);

        for(Player p : winners){
            new PlayerRound(p, round, true, win, false);
        }

        for(Player p : losers){
            new PlayerRound(p, round, false, lose, false);
        }

        for(Player p : jungfrauen){
            new PlayerRound(p, round, true, 2*win, true);
        }



    }
}
