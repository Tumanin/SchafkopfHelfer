package com.applicatum.schafkopfhelfer;

import android.app.Application;

import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;
import com.orm.SugarContext;

/**
 * Created by Alexx on 30.11.2015.
 */
public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        SugarContext.init(this);
        Iconify.with(new FontAwesomeModule());
    }
    @Override
    public void onTerminate() {
        super.onTerminate();
        SugarContext.terminate();
    }
}
