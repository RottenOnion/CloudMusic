package com.example.cloudmusic.module.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

import com.example.cloudmusic.bean.LocalSong;

import java.io.IOException;
import java.util.List;
import java.util.Random;

/**
 * Created by py on 2016/12/13.
 */

public class PlayService extends Service {

    private MediaPlayer mediaPlayer;
    private String path;
    private int playMode = LISTLOOP;
    private PlayServiceReceiver receiver;
    private List<LocalSong> songList;
    private int currPosition;
    private LocalBroadcastManager localBroadcastManager;
    private static final String SEND_TO_SERVICE = "android_CloudMusic_SEND_TO_SERVICE";
    private static final int SEQUENTIAL = 1,LISTLOOP = 2,RANDOM = 3,SINGLE = 4;

    @Override
    public void onCreate() {
        super.onCreate();
        Toast.makeText(PlayService.this, "服务开启了！没毛病", Toast.LENGTH_SHORT).show();
        mediaPlayer = new MediaPlayer();
        receiver = new PlayServiceReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(SEND_TO_SERVICE);
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        localBroadcastManager.registerReceiver(receiver,filter);

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mediaPlayer.reset();

                switch (playMode) {
                    case SEQUENTIAL:
                        currPosition++;
                        if (currPosition >= songList.size()) {
                            mediaPlayer.seekTo(0);
                            pauseAndresume();
                        }
                        else
                            play(songList.get(currPosition).getPath());

                        break;

                    case LISTLOOP:
                        currPosition++;
                        if (currPosition >= songList.size()) {
                            currPosition = 0;
                        }
                        play(songList.get(currPosition).getPath());
                        break;

                    case RANDOM:
                        Random random = new Random();
                        currPosition = random.nextInt(songList.size());
                        play(songList.get(currPosition).getPath());
                        break;

                    case SINGLE:
                        play(songList.get(currPosition).getPath());
                        break;
                    default:break;
                }
            }
        });

        mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                mediaPlayer.stop();
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
        localBroadcastManager.unregisterReceiver(receiver);
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    protected void play(String path) {
        try {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.reset();
            }
            mediaPlayer.setDataSource(path);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    mediaPlayer.start();
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
        else if (mediaPlayer !=null && !mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }
    }

    protected void next() {
        switch (playMode) {
            case SEQUENTIAL:
            case LISTLOOP:
                currPosition++;
                if (currPosition >= songList.size()) {
                    currPosition = 0;
                }
                play(songList.get(currPosition).getPath());

                break;

            case RANDOM:
                Random random = new Random();
                currPosition = random.nextInt(songList.size());
                play(songList.get(currPosition).getPath());
                break;

            case SINGLE:
                play(songList.get(currPosition).getPath());
                break;
            default:break;
        }
    }

    protected void prev() {
        switch (playMode) {
            case SEQUENTIAL:
            case LISTLOOP:
                currPosition--;
                if (currPosition >= songList.size()) {
                    currPosition = 0;
                }
                play(songList.get(currPosition).getPath());

                break;

            case RANDOM:
                Random random = new Random();
                currPosition = random.nextInt(songList.size());
                play(songList.get(currPosition).getPath());
                break;

            case SINGLE:
                play(songList.get(currPosition).getPath());
                break;
            default:
                break;
        }
    }

    public class PlayServiceReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            songList = intent.getParcelableArrayListExtra("com.example.cloudmusic.bean");
            currPosition = intent.getIntExtra("position",0);
            playMode = intent.getIntExtra("playMode",LISTLOOP);
            LocalSong song = songList.get(currPosition);
            String musicPath = song.getPath();
            play(musicPath);
        }
    }

}
