package com.applicatum.schafkopfhelfer.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

/**
 * Created by Dominik on 28.11.2015.
 */
public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "SchafkopfHelfer.db";
    private static final int DATABASE_VERSION = 1;
    
    public static final String PLAYER_TABLE_NAME = "player";
    public static final String PLAYER_COLUMN_ID = "id";
    public static final String PLAYER_COLUMN_NAME = "name";
    public static final String PLAYER_COLUMN_ACTIVE = "active";

    public static final String ROUND_TABLE_NAME = "round";
    public static final String ROUND_COLUMN_ID = "id";
    public static final String ROUND_COLUMN_DATE = "date";
    public static final String ROUND_COLUMN_TYPE = "type";
    public static final String ROUND_COLUMN_GAME = "game_id";

    public static final String PLAYER_ROUND_TABLE_NAME = "player_round";
    public static final String PLAYER_ROUND_COLUMN_ID = "id";
    public static final String PLAYER_ROUND_COLUMN_PLAYER = "player_id";
    public static final String PLAYER_ROUND_COLUMN_ROUND = "round_id";
    public static final String PLAYER_ROUND_COLUMN_WON = "won";
    public static final String PLAYER_ROUND_COLUMN_POINTS = "points";

    public static final String GAME_TABLE_NAME = "game";
    public static final String GAME_COLUMN_ID = "id";
    public static final String GAME_COLUMN_DATE = "date";



    public DBHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table " + PLAYER_TABLE_NAME + " (" +
                        PLAYER_COLUMN_ID + " integer primary key, " +
                        PLAYER_COLUMN_NAME + " text, " +
                        PLAYER_COLUMN_ACTIVE + " integer);" +
                        "create table " + ROUND_TABLE_NAME + " (" +
                        ROUND_COLUMN_ID + " integer primary key, " +
                        ROUND_COLUMN_DATE + " integer, " +
                        ROUND_COLUMN_TYPE + " text, " +
                        ROUND_COLUMN_GAME + " integer);" +
                        "create table " + PLAYER_ROUND_TABLE_NAME + " (" +
                        PLAYER_ROUND_COLUMN_ID + " integer primary key, " +
                        PLAYER_ROUND_COLUMN_PLAYER + " integer, " +
                        PLAYER_ROUND_COLUMN_ROUND + " integer, " +
                        PLAYER_ROUND_COLUMN_WON + " integer, " +
                        PLAYER_ROUND_COLUMN_POINTS + " integer);" +
                        "create table " + GAME_TABLE_NAME + " (" +
                        GAME_COLUMN_ID + " integer primary key, " +
                        GAME_COLUMN_DATE + " integer);"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(
                "DROP TABLE IF EXISTS " + PLAYER_TABLE_NAME + "; " +
                    "DROP TABLE IF EXISTS " + ROUND_TABLE_NAME + "; " +
                    "DROP TABLE IF EXISTS " + PLAYER_ROUND_TABLE_NAME + "; " +
                    "DROP TABLE IF EXISTS " + GAME_TABLE_NAME + "; "

        );
        onCreate(db);
    }

    public boolean createPlayer(String name)
    {
        if(getPlayerId(name) > -1)
            return false;

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PLAYER_COLUMN_NAME, name);
        contentValues.put(PLAYER_COLUMN_ACTIVE, 0);
        db.insert(PLAYER_TABLE_NAME, null, contentValues);
        return true;
    }

    public int getPlayerId(String name) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("select " + PLAYER_COLUMN_ID + " from " + PLAYER_TABLE_NAME + " where " + PLAYER_COLUMN_NAME + "=" + name + "", null);

        int ret = -1;

        if (cursor != null) {
            cursor.moveToFirst();
            ret = Integer.parseInt(cursor.getString(0));
            cursor.close();
        }
        return ret;
    }

    public boolean updatePlayer (String name, boolean active)
    {
        int act = 0;
        if(active)
            act = 1;

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PLAYER_COLUMN_NAME, name);
        contentValues.put(PLAYER_COLUMN_ACTIVE, act);
        db.update(PLAYER_TABLE_NAME, contentValues, "id = ? ", null );
        return true;
    }

    public Integer deletePlayer (Integer id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(PLAYER_TABLE_NAME,
                "id = ? ",
                new String[] { Integer.toString(id) });
    }

    public ArrayList<Player> getAllPlayers()
    {
        ArrayList<Player> array_list = new ArrayList<Player>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery(
                "SELECT p.name, p.active, pr1.points " +
                        "FROM player p " +
                        "LEFT JOIN player_round pr1 ON (p.id = pr1.player_id) " +
                        "LEFT OUTER JOIN player_round pr2 ON (p.id = pr2.player_id AND " +
                        "    (pr1.date < pr2.date OR pr1.date = pr2.date AND pr1.id < pr2.id)) " +
                        "WHERE pr2.id IS NULL;",
                null );
        res.moveToFirst();

        while(!res.isAfterLast()){
            Player p = new Player(res.getString(0));
            p.setPoints(Integer.parseInt(res.getString(3)));
            array_list.add(p);
            res.moveToNext();
        }
        res.close();
        return array_list;
    }

    public ArrayList<Player> getActivePlayers()
    {
        ArrayList<Player> array_list = new ArrayList<Player>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery(
                "SELECT p.name, p.active, pr1.points " +
                        "FROM player p " +
                        "WHERE p.active='1' " +
                        "LEFT JOIN player_round pr1 ON (p.id = pr1.player_id) " +
                        "LEFT OUTER JOIN player_round pr2 ON (p.id = pr2.player_id AND " +
                        "    (pr1.date < pr2.date OR pr1.date = pr2.date AND pr1.id < pr2.id)) " +
                        "WHERE pr2.id IS NULL;",
                null );
        res.moveToFirst();

        while(!res.isAfterLast()){
            Player p = new Player(res.getString(0));
            p.setPoints(Integer.parseInt(res.getString(3)));
            array_list.add(p);
            res.moveToNext();
        }
        res.close();
        return array_list;
    }


}

