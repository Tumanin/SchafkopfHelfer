package com.applicatum.schafkopfhelfer.models;

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
        return GamePlayers.returnPlayers(this);
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
        int difference = GameTypes.getValue(this, type) + laufende * GameTypes.getValue(this, Types.LAUFENDE);
        if(schneider){
            difference += GameTypes.getValue(this, Types.SCHNEIDER);
        }
        if(schwarz){
            difference += GameTypes.getValue(this, Types.SCHWARZ);
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
        if((GameTypes.getValue(this, Types.POTT)==1)&&(type == Types.SAUSPIEL)){
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
        this.save();
    }
}
