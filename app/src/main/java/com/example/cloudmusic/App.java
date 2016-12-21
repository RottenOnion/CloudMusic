package com.example.cloudmusic;

import android.app.Application;
import android.content.Context;

/**
 * Created by py on 2016/12/15.
 */

public class App extends Application {

    private static App app;
    public static final String BROADCAST_SEND_TO_PLAY_SERVICE = "android_CloudMusic_SEND_TO_PLAY_SERVICE";
    public static final int PREV_SONG = 1;
    public static final int PLAY_SONG = 2;
    public static final int NEXT_SONG = 3;
    public static final int CLICK_SONG = 4;
    private  static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static Context getContext() {
        return context;
    }

    public static App getInstance() {
        if (app == null)
            app = new App();
        return app;
    }

}
