package com.applicatum.schafkopfhelfer.models;

import junit.framework.TestCase;

/**
 * Created by Dominik on 29.11.2015.
 */
public class DBHelperTest extends TestCase {

    public void setUp() throws Exception {
        super.setUp();
    }

    public void tearDown() throws Exception {

    }

    public void testCreatePlayer() throws Exception {
        DBHelper db = new DBHelper(null);
        assertTrue(db.createPlayer("hello"));
    }

    public void testGetPlayerId() throws Exception {

    }

    public void testUpdatePlayer() throws Exception {

    }

    public void testDeletePlayer() throws Exception {

    }

    public void testGetAllPlayers() throws Exception {

    }

    public void testGetActivePlayers() throws Exception {

    }
}