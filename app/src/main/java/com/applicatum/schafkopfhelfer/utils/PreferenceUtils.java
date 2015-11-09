package com.applicatum.schafkopfhelfer.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.applicatum.schafkopfhelfer.models.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alexx on 09.11.2015.
 */
public class PreferenceUtils {
    public static final String APP_PREFERENCES = "preferences";

    public static final String PREF_DEFAULTS_SET = "pref_defaults_set";

    public static final String PREF_PLAYER_LIST = "player_list";

    private static PreferenceUtils mInstance = null;
    private final Context mContext;

    private PreferenceUtils(Context context) {
        mContext = context.getApplicationContext();
        setDefaultValues();
    }

    public static PreferenceUtils getInstance(Context context) {

        if(mInstance == null) {
            mInstance = new PreferenceUtils(context);
        }
        return mInstance;
    }

    public SharedPreferences getAppPreferences() {
        return mContext.getSharedPreferences(PreferenceUtils.APP_PREFERENCES, mContext.MODE_PRIVATE);
    }

    public boolean contains(String key) {
        return getAppPreferences().contains(key);
    }

    public void storeString(String key, String value) {
        getAppPreferences().edit().putString(key, value).commit();
    }

    public void storeInt(String key, int value) {
        getAppPreferences().edit().putInt(key, value).commit();
    }

    public void storeLong(String key, long value) {
        getAppPreferences().edit().putLong(key, value).commit();
    }

    public void storeBoolean(String key, boolean value) {
        getAppPreferences().edit().putBoolean(key, value).commit();
    }

    public String getString(String key) {
        return getAppPreferences().getString(key, null);
    }

    public String getString(String key, String defaultValue) {
        return getAppPreferences().getString(key, defaultValue);
    }

    public int getInt(String key) {
        return getAppPreferences().getInt(key, 0);
    }

    public Boolean getBoolean(String key) {
        return getAppPreferences().getBoolean(key, false);
    }


    public void setDefaultValues(){

        if(!getBoolean(PREF_DEFAULTS_SET)) {

            storeBoolean(PREF_DEFAULTS_SET, true);
        }
    }

    public void storePlayer(Player player){
        String name = player.getName();
        String player_list = getString(PREF_PLAYER_LIST, "");
        if(player_list.equals("")){
            storeString(PREF_PLAYER_LIST, name);
        }else{
            String[] players = player_list.split(",");
            boolean found = false;
            for(String playerName : players){
                if(playerName.equals(name)) found = true;
            }
            if (!found) {
                storeString(PREF_PLAYER_LIST, player_list + "," + name);
            }
        }
        /*
        storeString(uniqueid, controller.getName());
        short[] password = controller.getPassword();
        storeInt(uniqueid + "PID1", password[0]);
        storeInt(uniqueid + "PID2", password[1]);
        storeBoolean(uniqueid + "active", controller.isActive());
        storeInt(uniqueid+"vendor", controller.getVendorId());
        */
    }

    public List<Player> getPlayers(){
        List<Player> players = new ArrayList<>();
        String player_list = getString(PREF_PLAYER_LIST, "");
        if(!player_list.equals("")){
            String[] playerNames = player_list.split(",");
            for (String playerName : playerNames) {
                Player player = new Player(playerName);

                /*
                String name = getString(gateway, "name");
                short[] password = new short[2];
                password[0] = (short) getInt(gateway+"PID1");
                password[1] = (short) getInt(gateway+"PID2");
                controller.setUniqueid(uniqueid);
                controller.setName(name);
                controller.setPassword(password);
                controller.setActive(getBoolean(gateway + "active"));
                controller.setVendorId(getInt(gateway+"vendor"));
                controllers.add(controller);
                */
            }
        }
        return players;
    }
}

