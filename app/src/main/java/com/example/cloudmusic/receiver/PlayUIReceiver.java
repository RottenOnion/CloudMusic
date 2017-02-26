package com.example.cloudmusic.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.cloudmusic.view.viewInterface.PlayView;

/**
 * Created by py on 2017/2/26.
 */

public class PlayUIReceiver extends BroadcastReceiver {

    PlayView playView;

    public PlayUIReceiver(PlayView playView) {
        this.playView = playView;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        playView.receiveUiBroadcast(context,intent);
    }
}