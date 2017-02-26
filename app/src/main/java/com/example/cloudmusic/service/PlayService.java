package com.example.cloudmusic.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

import com.example.cloudmusic.App;
import com.example.cloudmusic.bean.SongData;
import com.example.cloudmusic.model.database.DatabaseHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import static com.example.cloudmusic.App.LISTLOOP;
import static com.example.cloudmusic.App.RANDOM;
import static com.example.cloudmusic.App.SEQUENTIAL;
import static com.example.cloudmusic.App.SINGLE;
import static com.example.cloudmusic.App.currDuration;
import static com.example.cloudmusic.App.duration;
import static com.example.cloudmusic.App.nowSong;

/**
 * Created by py on 2016/12/13.
 */

public class PlayService extends Service {

    private MediaPlayer mediaPlayer;
    private String path;
    private int playMode = App.LISTLOOP;
    private PlayServiceReceiver receiver;
    private List<SongData> songList;
    private int currPosition ;
    private LocalBroadcastManager localBroadcastManager;
    private Timer timer;
    private TimerTask timerTask;
    private DatabaseHelper dbhelper;



    @Override
    public void onCreate() {
        super.onCreate();

        mediaPlayer = new MediaPlayer();
        receiver = new PlayServiceReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(App.BROADCAST_SEND_TO_PLAY_SERVICE);
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        localBroadcastManager.registerReceiver(receiver,filter);
        setMediaListener();

        songList = new ArrayList<>();

        timerTask = new TimerTask() {
            @Override
            public void run() {
                Intent intent = new Intent();
                intent.setAction(App.BROADCAST_SEND_TO_SEEKBAR);
                if (mediaPlayer.isPlaying()) {
                    currDuration = mediaPlayer.getCurrentPosition();
                }
                intent.putExtra("currDuration",currDuration);
                localBroadcastManager.sendBroadcast(intent);
            }
        };

        timer = new Timer();
        timer.schedule(timerTask,0,1000);

        dbhelper = new DatabaseHelper(this,null,App.newVersion);
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        SongData recentSong = new SongData();
        Cursor cursor = db.query("recent",null,null,null,null,null,null);
        if (cursor.moveToLast()) {
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
            recentSong.setLrc_path(cursor.getString(cursor.getColumnIndex("lrc_path")));
        }

        App.nowSong = recentSong;
        Intent intent = new Intent();
        intent.setAction(App.BROADCAST_SEND_TO_UPDATE);
        intent.putExtra("isPlay",false);
        intent.putExtra("playMode",playMode);
        intent.putExtra("nowSong",nowSong);
        localBroadcastManager.sendBroadcast(intent);
        songList.add(nowSong);
        currPosition = 0;
    }

    private void setMediaListener() {
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mediaPlayer.reset();

                switch (playMode) {
                    case App.SEQUENTIAL:
                        currPosition++;
                        if (currPosition >= songList.size()) {
                            mediaPlayer.seekTo(0);
                            pauseAndresume();
                        }
                        else
                            play(songList.get(currPosition).getM4a());

                        break;

                    case App.LISTLOOP:
                        currPosition++;
                        if (currPosition >= songList.size()) {
                            currPosition = 0;
                        }
                        play(songList.get(currPosition).getM4a());
                        break;

                    case App.RANDOM:
                        Random random = new Random();
                        currPosition = random.nextInt(songList.size());
                        play(songList.get(currPosition).getM4a());
                        break;

                    case App.SINGLE:
                        play(songList.get(currPosition).getM4a());
                        break;
                    default:break;
                }
            }
        });

        mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                Toast.makeText(PlayService.this, "播放出错了！为您播放下一首", Toast.LENGTH_SHORT).show();
                next();
                return false;
            }
        });


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        stopSelf();
        if (localBroadcastManager != null)
            localBroadcastManager.unregisterReceiver(receiver);
        if(timer != null) {
            timer.cancel();
            timer = null;
        }
        if (dbhelper !=null) {
            dbhelper.close();
        }
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    protected void play(String path) {
        try {
            mediaPlayer.reset();

            mediaPlayer.setDataSource(path);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    Intent intent = new Intent();
                    intent.setAction(App.BROADCAST_SEND_TO_UPDATE);
                    intent.putExtra("isPlay",true);
                    intent.putExtra("playMode",playMode);
                    intent.putExtra("duration",mediaPlayer.getDuration());
                    intent.putExtra("nowSong",songList.get(currPosition));
                    localBroadcastManager.sendBroadcast(intent);
                    mediaPlayer.start();
                    duration = mediaPlayer.getDuration();
                    currDuration = mediaPlayer.getCurrentPosition();

                    //数据库操作
                    SQLiteDatabase db = dbhelper.getWritableDatabase();
                    String arg[] = {String.valueOf(songList.get(currPosition).getSongid())};
                    db.delete("recent","songid = ?",arg);

                    ContentValues values = new ContentValues();
                    values.put("songid",songList.get(currPosition).getSongid());
                    values.put("songname",songList.get(currPosition).getSongname());
                    values.put("singername",songList.get(currPosition).getSingername());
                    values.put("m4a",songList.get(currPosition).getM4a());
                    values.put("media_mid",songList.get(currPosition).getMedia_mid());
                    values.put("singerid",songList.get(currPosition).getSingerid());
                    values.put("albumname",songList.get(currPosition).getAlbumname());
                    values.put("downUrl",songList.get(currPosition).getDownUrl());
                    values.put("strMediaMid",songList.get(currPosition).getStrMediaMid());
                    values.put("albummid",songList.get(currPosition).getAlbummid());
                    values.put("songmid",songList.get(currPosition).getSongmid());
                    values.put("albumpic_big",songList.get(currPosition).getAlbumpic_big());
                    values.put("albumpic_small",songList.get(currPosition).getAlbumpic_small());
                    values.put("albumid",songList.get(currPosition).getAlbumid());
                    values.put("lrc_path",songList.get(currPosition).getLrc_path());
                    db.insert("recent",null,values);
                    values.clear();

                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void pauseAndresume() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {

            mediaPlayer.pause();
        }
        else if (mediaPlayer !=null && !mediaPlayer.isPlaying() && mediaPlayer.getDuration()!=0) {
            mediaPlayer.start();
        }
        else if(mediaPlayer != null && mediaPlayer.getDuration()==0) {
            play(nowSong.getM4a());
        }
    }

    protected void next() {
        mediaPlayer.reset();
        switch (playMode) {
            case App.SEQUENTIAL:
            case App.LISTLOOP:
                currPosition++;
                if (currPosition >= songList.size()) {
                    currPosition = 0;
                }
                play(songList.get(currPosition).getM4a());

                break;

            case App.RANDOM:
                Random random = new Random();
                currPosition = random.nextInt(songList.size());
                play(songList.get(currPosition).getM4a());
                break;

            case App.SINGLE:
                play(songList.get(currPosition).getM4a());
                break;
            default:break;
        }
    }

    protected void prev() {
        mediaPlayer.reset();
        switch (playMode) {
            case SEQUENTIAL:
            case LISTLOOP:
                currPosition--;
                if (currPosition < 0) {
                    currPosition = songList.size() - 1;
                }
                play(songList.get(currPosition).getM4a());

                break;

            case RANDOM:
                Random random = new Random();
                currPosition = random.nextInt(songList.size());
                play(songList.get(currPosition).getM4a());
                break;

            case SINGLE:
                play(songList.get(currPosition).getM4a());
                break;
            default:
                break;
        }
    }

    public class PlayServiceReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            int instruction = intent.getIntExtra("instruction",-1);
            switch (instruction) {
                case App.CLICK_SONG:
                    songList = intent.getParcelableArrayListExtra("com.example.cloudmusic.bean.songdata");
                    currPosition = intent.getIntExtra("position", 0);
                    playMode = intent.getIntExtra("playMode", LISTLOOP);
                    SongData song = songList.get(currPosition);
                    String musicPath = song.getM4a();
                    play(musicPath);
                    break;

                case App.PREV_SONG:
                    if (songList != null) {
                        prev();
                    }
                    break;

                case App.NEXT_SONG:
                    if (songList != null) {
                        next();
                    }
                    break;

                case App.PLAY_SONG:
                    if (songList != null) {
                        pauseAndresume();
                    }
                    break;

                case  App.ADJUST_SEEKBAR:

                    mediaPlayer.seekTo(currDuration);
                    if (!mediaPlayer.isPlaying()) {
                        mediaPlayer.start();
                    }
            }
        }
    }

}
