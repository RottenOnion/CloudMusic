package com.example.cloudmusic.utils;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import com.example.cloudmusic.bean.SongData;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

/**
 * Created by py on 2016/12/8.
 */

public class ScanMusic {
    private List<SongData> songList;
    private static ScanMusic instance;

    public static ScanMusic getInstance() {
        if (instance == null) {
            instance = new ScanMusic();
        }
        return instance;
    }

    public Observable<List<SongData>> getSongList(Context context) {
        songList = new ArrayList<>();
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,null,null,null,MediaStore.Audio.AudioColumns.IS_MUSIC);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                SongData song = new SongData();
                song.setSongname(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)).replace(".mp3","").trim());
                song.setSingername(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)));
                song.setM4a(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)));
                //song.setDuration(cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)));

                if (song.getSongname().contains("-")) {
                    String[] str = song.getSongname().split("-");
                    song.setSingername(str[0]);
                    song.setSongname(str[1]);
                }
                if (song.getSongname().startsWith(" ")) {
                    song.setSongname(song.getSongname().substring(1));
                }
                songList.add(song);
            }
            cursor.close();
        }

        Observable<List<SongData>> observable = Observable.just(songList);
        return observable;
    }

}
