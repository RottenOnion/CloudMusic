package com.example.cloudmusic.model.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by py on 2016/12/31.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String CREATE_RECENT = "create table recent (" +
            "songid integer ," +
            "singerid integer ," +
            "songname text," +
            "media_mid text," +
            "albumname text," +
            "downUrl text primary key unique," +
            "singername text," +
            "strMediaMid text," +
            "albummid text," +
            "songmid text," +
            "albumpic_big text," +
            "albumpic_small text," +
            "albumid text," +
            "m4a text," +
            "lrc_path text)";

    private Context mContext;



    public DatabaseHelper(Context context, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, "cloudmusic.db", factory, version);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_RECENT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("drop table if exists recent");
        onCreate(sqLiteDatabase);
    }
}
