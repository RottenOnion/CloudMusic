package com.example.cloudmusic.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.cloudmusic.view.viewInterface.BaseView;

/**
 * Created by py on 2017/2/25.
 */

public class BaseReceiver extends BroadcastReceiver {
    BaseView baseView;

    public BaseReceiver(BaseView baseView) {
        this.baseView = baseView;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        baseView.showNewMessage(context,intent);
    }
}
