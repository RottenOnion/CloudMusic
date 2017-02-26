package com.example.cloudmusic.utils;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.ImageButton;

import com.example.cloudmusic.App;
import com.example.cloudmusic.R;

import static com.example.cloudmusic.App.isPlay;

/**
 * Created by py on 2017/2/26.
 */

public class PlayControl {
    private LocalBroadcastManager broadcastManager;

    public PlayControl(LocalBroadcastManager broadcastManager) {
        this.broadcastManager = broadcastManager;
    }

    public void nextSong() {
        Intent nextIntent = new Intent();
        nextIntent.setAction(App.BROADCAST_SEND_TO_PLAY_SERVICE);
        nextIntent.putExtra("instruction",App.NEXT_SONG);
        broadcastManager.sendBroadcast(nextIntent);
    }

    public void preSong() {
        Intent prevIntent = new Intent();
        prevIntent.setAction(App.BROADCAST_SEND_TO_PLAY_SERVICE);
        prevIntent.putExtra("instruction",App.PREV_SONG);
        broadcastManager.sendBroadcast(prevIntent);
    }

    public void playPause(ImageButton btnPlay) {
        if (isPlay) {
            btnPlay.setImageResource(R.mipmap.big_play);
            isPlay = false;
        } else {
            btnPlay.setImageResource(R.mipmap.big_pause);
            isPlay = true;
        }
        Intent playIntent = new Intent();
        playIntent.setAction(App.BROADCAST_SEND_TO_PLAY_SERVICE);
        playIntent.putExtra("instruction",App.PLAY_SONG);
        broadcastManager.sendBroadcast(playIntent);
    }

}
