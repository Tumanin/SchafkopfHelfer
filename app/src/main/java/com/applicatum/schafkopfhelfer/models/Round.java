package com.applicatum.schafkopfhelfer.models;

import com.orm.SugarRecord;

import java.util.List;

/**
 * Created by Alexx on 09.11.2015.
 */
public class Round extends SugarRecord<Round>{

    String name;
    List<Player> winners;
    List<Player> losers;

    int laufende;
    int klopfen;
    boolean schneider;
    boolean schwarz;
    int price;
}
