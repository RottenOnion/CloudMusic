package com.example.cloudmusic.view.viewInterface;

import android.content.Context;
import android.content.Intent;

/**
 * Created by py on 2017/2/26.
 */

public interface PlayView {
    void receiveUiBroadcast(Context context, Intent intent);
    void recevieSeekBarBroadcast(Context context,Intent intent);
}
