package com.applicatum.schafkopfhelfer.models;

import android.util.Log;

import com.applicatum.schafkopfhelfer.utils.Types;
import com.orm.SugarRecord;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.orm.dsl.Ignore;
import com.orm.query.Condition;
import com.orm.query.Select;

/**
 * Created by Alexx on 09.11.2015.
 */
public class Game extends SugarRecord{

    private static final String TAG = "GameModel";

    long date;
    int pot;
    //@Ignore
    //List<Player> activePlayers;
    static Game lastGame;
    @Ignore
    ArrayList<Round> rounds;
    @Ignore
    ArrayList<PlayerRound> playerRounds;
    @Ignore
    ArrayList<GamePlayers> gamePlayerses;

    public Game(){
        //this.date = (new Date().getTime())/1000L;
        //pot = 0;
        //activePlayers = new ArrayList<>();
//        rounds = new ArrayList<>();
//        playerRounds = new ArrayList<>();
//        gamePlayerses = new ArrayList<>();
//        lastGame = this;
        Log.d(TAG, "Game() called");
    }

    public static Game createGame(){
        Log.d(TAG, "createGame() called");
        Game game = new Game();
        game.date = (new Date().getTime())/1000L;
        game.pot = 0;
        game.rounds = new ArrayList<>();
        game.playerRounds = new ArrayList<>();
        game.gamePlayerses = new ArrayList<>();
        lastGame = game;
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

    public static Game lastGame(String caller){
        Log.d(TAG, "lastGame start: "+caller);
        if(lastGame != null){
            Log.d(TAG, "lastGame already exists");
            Log.d(TAG, "lastGame rounds size: " + lastGame.getRounds().size());
            Log.d(TAG, "lastGame playerRounds size: "+lastGame.getPlayerRounds().size());
            return lastGame;
        }
        else{
            Log.d(TAG, "lastGame load from SQL");

            Game game = Select.from(Game.class).orderBy("id desc").first();
            List<Round> localRounds = Round.getRounds(game);
            game.addRounds(localRounds);
            //game.getPlayerRounds().clear();
            for(Round round : localRounds){
                List<PlayerRound> localPRs = PlayerRound.getPRforRound(round);
                game.addPRs(localPRs);
            }
            lastGame = game;
            return  game;
        }

    }

    public ArrayList<Round> getRounds(){
        if(rounds==null) rounds = new ArrayList<>();
        return rounds;
    }

    public ArrayList<PlayerRound> getPlayerRounds() {
        if(playerRounds==null) playerRounds = new ArrayList<>();
        return playerRounds;
    }

    public void addRounds(Collection<Round> newRounds){
        Log.d(TAG, "newRounds start");
        if (rounds!=null) {
            rounds.clear();
        } else {

            rounds = new ArrayList<>();
        }
        rounds.addAll(newRounds);
    }

    public void addPRs (Collection<PlayerRound> newPlayerRounds){
        Log.d(TAG, "newPlayerRounds start");
        if (playerRounds==null) {
            playerRounds = new ArrayList<>();
        }

        playerRounds.addAll(newPlayerRounds);
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
        Log.d(TAG, "getActivePlayers start");
        if(gamePlayerses==null) gamePlayerses = new ArrayList<>();
        if(gamePlayerses.size()>0){
            Log.d(TAG, "getActivePlayers already exists");
            List<Player> players = new ArrayList<>();
            for(GamePlayers e : gamePlayerses){
                players.add(e.getPlayer());
            }
            return players;
        }
        Log.d(TAG, "getActivePlayers load from SQL");
        gamePlayerses.addAll(GamePlayers.getGamePlayers(this));
        //lastGame = this;
        return GamePlayers.getActivePlayers(this);
    }

    public void updateActivePlayers(List<Player> activePlayers) {
        Log.d(TAG, "updateActivePlayers start");
        GamePlayers.deleteGamePlayers(this);
        gamePlayerses.clear();
        for(Player p : activePlayers){
            System.out.println(p);
            GamePlayers gp = new GamePlayers(this, p);
            gamePlayerses.add(gp);
            gp.save();
        }
    }

    public void recordNewRound(String type, List<Player> winners, List<Player> losers, List<Player> aussetzer, List<Player> jungfrauen, boolean schneider, boolean schwarz, int laufende, int klopf){
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

        if(jungfrauen.size()>0){
            lose += jungfrauen.size() * win / NumberOfLosers;
            for(Player e : jungfrauen){
                winners.remove(e);
            }
        }
        if((GameTypes.getValue(this, Types.POTT)==1)&&(type.equals(Types.SAUSPIEL))){
            win += pot/NumberOfWinners;
        }

        Round round = new Round(type, laufende, klopf, schneider, schwarz, (int) Math.min(win, lose), this);
        rounds.add(round);

        for(Player p : winners){
            Log.d(TAG, "Writing new Player Round for winner. Player: "+p.getId()+" round: "+round.getId()+" win: "+win+"");
            PlayerRound playerRound = new PlayerRound(p, round, true, (int) win, false);
            playerRounds.add(playerRound);
        }

        for(Player p : losers){
            Log.d(TAG, "Writing new Player Round for loser. Player: "+p.getId()+" round: "+round.getId()+" win: "+lose+"");
            PlayerRound playerRound = new PlayerRound(p, round, false, (int) lose, false);
            playerRounds.add(playerRound);
        }

        for(Player p : jungfrauen){
            Log.d(TAG, "Writing new Player Round for Jungfrau. Player: "+p.getId()+" round: "+round.getId()+" win: "+2*win+"");
            PlayerRound playerRound = new PlayerRound(p, round, true, (int) (2*win), true);
            playerRounds.add(playerRound);
        }

        for(Player p : aussetzer){
            Log.d(TAG, "Reset ChangePoints of Player: "+p.getId());
            p.resetChangePoints();
        }

        this.save();
    }

    private PlayerRound getPRLocal(Round round, Player player){
        Log.d(TAG, "getPRLocal: start. Round: "+round.getId()+"; player: "+player.getName());
        if(playerRounds == null) playerRounds = new ArrayList<>();
        if (playerRounds.size()>0) {
            Log.d(TAG, "getPRLocal: playerRounds already exists");
            for(PlayerRound playerRound : playerRounds){
                if(playerRound.getRound().getId()==round.getId() && playerRound.getPlayer().getId()==player.getId()) return playerRound;
            }
            Log.d(TAG, "getPRLocal: playerRounds not found");
        } else {
            Log.d(TAG, "getPRLocal: playerRounds load from SQL");

            if(this.rounds.size()>0){

                Log.d(TAG, "getRoundsTable: rounds already exists");
            }else{
                Log.d(TAG, "getRoundsTable: rounds load from SQL");

                rounds.addAll(Round.getRounds(this));
            }
            for(Round round1:rounds){
                playerRounds.addAll(PlayerRound.getPRforRound(round1));
            }

            for(PlayerRound playerRound : playerRounds){
                if(playerRound.getRound()==round && playerRound.getPlayer()==player) return playerRound;
            }
        }
        return null;
    }

    public HashMap<Player, ArrayList<String>> getRoundsTable(String caller){
        Log.d(TAG, "getRoundsTable: start: "+caller);
        HashMap<Player, ArrayList<String>> hm = new HashMap<>();
        if(rounds==null) rounds = new ArrayList<>();
        if(rounds.size()>0){
            //localRounds = this.rounds;
            Log.d(TAG, "getRoundsTable: rounds already exists");
        }else{
            Log.d(TAG, "getRoundsTable: rounds load from SQL");

            rounds.addAll(Round.getRounds(this));
        }
        for(Player p : this.getActivePlayers()){
            ArrayList<String> values = new ArrayList<>();
            for(Round r : rounds){
                PlayerRound pr = getPRLocal(r, p);
                if(pr != null){
                    Log.d(TAG, "getRoundsTable: pr points: "+pr.getGamePoints());
                    values.add(String.valueOf(pr.getGamePoints()));
                } else {
                    Log.d(TAG, "getRoundsTable: pr is null");
                    values.add("-");
                }
            }
            hm.put(p, values);
        }
        return hm;
    }

    public HashMap<Player, ArrayList<Integer>> getPlayerCurves(){
        HashMap<Player, ArrayList<Integer>> hm = new HashMap<>();
        List<Round> rounds = Round.getRounds(this);
        for(Player p : this.getActivePlayers()){
            ArrayList<Integer> values = new ArrayList<>();
            for(Round r : rounds){
                PlayerRound pr = PlayerRound.getPR(r, p);
                if(pr != null){
                    values.add(pr.getGamePoints());
                } else {
                    values.add(values.get(values.size()-1)); //getLastValue
                }
            }
            hm.put(p, values);
        }
        return hm;
    }

    public void swapSeats(Player player1, Player player2){
        GamePlayers gp1, gp2;
        gp1 = GamePlayers.getGamePlayer(this, player1);
        gp2 = GamePlayers.getGamePlayer(this, player2);
        int seat = gp1.getSeat();
        gp1.setSeat(gp2.getSeat());
        gp2.setSeat(seat);
    }

    public void deleteRound(Round round){
        for (PlayerRound playerRound : playerRounds){
            if(playerRound.getRound()==round) playerRounds.remove(playerRound);
        }
        rounds.remove(round);
    }

}
