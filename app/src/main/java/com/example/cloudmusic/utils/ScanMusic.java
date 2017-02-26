package com.example.cloudmusic.utils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import com.example.cloudmusic.App;
import com.example.cloudmusic.bean.SongData;
import com.example.cloudmusic.model.database.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

/**
 * Created by py on 2016/12/8.
 */

public class ScanMusic {
    private List<SongData> songList;
    private static ScanMusic instance;
    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;
    private static final String lrcPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Musiclrc/";

    public static ScanMusic getInstance() {
        if (instance == null) {
            instance = new ScanMusic();
        }
        return instance;
    }

    public Observable<List<SongData>> getLocalSongList(Context context) {
        songList = new ArrayList<>();
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,null,null,null,MediaStore.Audio.AudioColumns.IS_MUSIC);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                SongData song = new SongData();
                song.setSongname(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)).replace(".mp3","").trim());
                song.setSingername(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)).trim());
                song.setM4a(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)));
                song.setAlbumname(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM)));
                song.setAlbumid(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)));
                song.setSongid(cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)));
                //song.setDuration(cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)));

                if (song.getSongname().contains("-")) {
                    String[] str = song.getSongname().split("-");
                    song.setSingername(str[0].trim());
                    song.setSongname(str[1]);
                }
                if (song.getSongname().startsWith(" ")) {
                    song.setSongname(song.getSongname().substring(1));
                }
                song.setLrc_path(lrcPath + song.getSingername() + "-" + song.getSongname() + ".lrc");
                songList.add(song);
            }
            cursor.close();
        }



        Observable<List<SongData>> observable = Observable.just(songList);
        return observable;
    }

    public Observable<List<SongData>> getRecentSongList(Context context) {
        songList = new ArrayList<>();
        dbHelper = new DatabaseHelper(context,null, App.newVersion);
        db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query("recent",null,null,null,null,null,null);
        if (cursor.moveToLast()) {
            do {
                SongData recentSong = new SongData();
                recentSong.setSongid(cursor.getInt(cursor.getColumnIndex("songid")));
                recentSong.setSingerid(cursor.getInt(cursor.getColumnIndex("singerid")));
                recentSong.setM4a(cursor.getString(cursor.getColumnIndex("m4a")));
                recentSong.setSongname(cursor.getString(cursor.getColumnIndex("songname")));
                recentSong.setMedia_mid(cursor.getString(cursor.getColumnIndex("media_mid")));
                recentSong.setAlbumname(cursor.getString(cursor.getColumnIndex("albumname")));
                recentSong.setDownUrl(cursor.getString(cursor.getColumnIndex("downUrl")));
                recentSong.setSingername(cursor.getString(cursor.getColumnIndex("singername")));
                recentSong.setMedia_mid(cursor.getString(cursor.getColumnIndex("strMediaMid")));
                recentSong.setAlbummid(cursor.getString(cursor.getColumnIndex("albummid")));
                recentSong.setSongmid(cursor.getString(cursor.getColumnIndex("songmid")));
                recentSong.setAlbumpic_big(cursor.getString(cursor.getColumnIndex("albumpic_big")));
                recentSong.setAlbumpic_small(cursor.getString(cursor.getColumnIndex("albumpic_small")));
                recentSong.setAlbumid(cursor.getString(cursor.getColumnIndex("albumid")));
                songList.add(recentSong);
            } while (cursor.moveToPrevious());
        }
        cursor.close();
        Observable<List<SongData>> observable = Observable.just(songList);
        return observable;
    }

    public Observable<String> getAlbumImage(Context context,String albumId) {

        String album_art = null;
        if (albumId != null && App.nowSong.getAlbumpic_small() == null) {
            String mUriAlbums = "content://media/external/audio/albums";
            String[] projection = new String[]{"album_art"};
            Cursor cur = context.getContentResolver().query(
                    Uri.parse(mUriAlbums + "/" + albumId),
                    projection, null, null, null);

            if (cur.getCount() > 0 && cur.getColumnCount() > 0) {
                cur.moveToNext();
                album_art = cur.getString(0);
            }
            cur.close();
            cur = null;
        } else if (App.nowSong.getAlbumpic_small() != null) {
            album_art = App.nowSong.getAlbumpic_small();
        }


        Observable<String> observable = Observable.just(album_art);
        return observable;
    }

}
