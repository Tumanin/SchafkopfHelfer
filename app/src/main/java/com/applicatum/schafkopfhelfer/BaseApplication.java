package com.applicatum.schafkopfhelfer;

import android.app.Application;

import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;
import com.orm.SugarApp;

/**
 * Created by Alexx on 30.11.2015.
 */
public class BaseApplication extends SugarApp {

    @Override
    public void onCreate() {
        super.onCreate();

        Iconify.with(new FontAwesomeModule());
    }
}
