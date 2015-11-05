package com.applicatum.schafkopfhelfer.models;

/**
 * Created by Alexx on 02.11.2015.
 */
public class User {

    private String name;
    private int points;

    public enum State{PLAY, WAIT, WIN};

    private State state;

    public User(String name){
        this.name = name;
        points = 0;
        state = State.WAIT;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }
}
