package com.applicatum.schafkopfhelfer.models;

import com.orm.SugarRecord;

import java.util.List;

/**
 * Created by Alexx on 09.11.2015.
 */
public class Game extends SugarRecord<Game>{

    List<Round> rounds;
    List<Player> players;
    //List<Player> aussetzer;
    //List<Player> inactive;


}
