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

    public static final String PREF_RAMSCH_PLAY = "pref_ramsch_play";
    public static final String PREF_POTT_PLAY = "pref_pott_play";
    public static final String PREF_SAUSPIEL_PLAY = "pref_sauspiel_play";
    public static final String PREF_SOLO_PLAY = "pref_solo_play";
    public static final String PREF_WENZ_PLAY = "pref_wenz_play";
    public static final String PREF_GEIER_PLAY = "pref_geier_play";
    public static final String PREF_FARBWENZ_PLAY = "pref_farbwenz_play";
    public static final String PREF_FARBGEIER_PLAY = "pref_farbgeier_play";
    public static final String PREF_BETTEL_PLAY = "pref_bettel_play";

    public static final String PREF_RAMSCH_PRICE = "pref_ramsch_price";
    public static final String PREF_POTT_PRICE = "pref_pott_price";
    public static final String PREF_SAUSPIEL_PRICE = "pref_sauspiel_price";
    public static final String PREF_SOLO_PRICE = "pref_solo_price";
    public static final String PREF_WENZ_PRICE = "pref_wenz_price";
    public static final String PREF_GEIER_PRICE = "pref_geier_price";
    public static final String PREF_FARBWENZ_PRICE = "pref_farbwenz_price";
    public static final String PREF_FARBGEIER_PRICE = "pref_farbgeier_price";
    public static final String PREF_BETTEL_PRICE = "pref_bettel_price";

    public static final String PREF_KLOPFEN_PRICE = "pref_klopfen_price";
    public static final String PREF_LAUFENDE_PRICE = "pref_laufende_price";
    public static final String PREF_SCHNEIDER_PRICE = "pref_schneider_price";
    public static final String PREF_SCHWARZ_PRICE = "pref_schwarz_price";

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

            storeBoolean(PREF_RAMSCH_PLAY, true);
            storeBoolean(PREF_POTT_PLAY, false);
            storeBoolean(PREF_SAUSPIEL_PLAY, true);
            storeBoolean(PREF_SOLO_PLAY, true);
            storeBoolean(PREF_WENZ_PLAY, false);
            storeBoolean(PREF_GEIER_PLAY, false);
            storeBoolean(PREF_FARBWENZ_PLAY, false);
            storeBoolean(PREF_FARBGEIER_PLAY, false);
            storeBoolean(PREF_BETTEL_PLAY, false);

            storeInt(PREF_RAMSCH_PRICE, 20);
            storeInt(PREF_POTT_PRICE, 20);
            storeInt(PREF_SAUSPIEL_PRICE, 20);
            storeInt(PREF_SOLO_PRICE, 20);
            storeInt(PREF_WENZ_PRICE, 20);
            storeInt(PREF_GEIER_PRICE, 20);
            storeInt(PREF_FARBWENZ_PRICE, 20);
            storeInt(PREF_FARBGEIER_PRICE, 20);
            storeInt(PREF_BETTEL_PRICE, 20);

            storeInt(PREF_KLOPFEN_PRICE, 20);
            storeInt(PREF_LAUFENDE_PRICE, 20);
            storeInt(PREF_SCHNEIDER_PRICE, 20);
            storeInt(PREF_SCHWARZ_PRICE, 20);
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

