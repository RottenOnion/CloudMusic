package com.example.cloudmusic.utils;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import com.example.cloudmusic.bean.LocalSong;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by py on 2016/12/8.
 */

public class ScanMusic {
    private List<LocalSong> songList;
    private static ScanMusic instance;

    public static ScanMusic getInstance() {
        if (instance == null) {
            instance = new ScanMusic();
        }
        return instance;
    }

    public List<LocalSong> getSongList(Context context) {
        songList = new ArrayList<>();
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,null,null,null,MediaStore.Audio.AudioColumns.IS_MUSIC);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                LocalSong song = new LocalSong();
                song.setSong(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)).replace(".mp3","").replace(" ",""));
                song.setSinger(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)));
                song.setPath(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)));
                song.setDuration(cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)));

                if (song.getSong().contains("-")) {
                    String[] str = song.getSong().split("-");
                    song.setSinger(str[0]);
                    song.setSong(str[1]);
                }



                songList.add(song);
            }
            cursor.close();
        }
        return songList;
    }

}
