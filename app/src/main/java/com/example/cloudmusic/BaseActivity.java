package com.example.cloudmusic;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import rx.Subscription;

/**
 * Created by py on 2016/12/7.
 */

public class BaseActivity extends AppCompatActivity implements View.OnClickListener{

    private WindowManager windowManager;
    private FrameLayout contentContainer;
    private View floatView;
    protected Subscription subscription;
    protected ImageButton btnPlayPause,btnPrev,btnNext;
    private LinearLayout linearLayout;
    private LocalBroadcastManager localBroadcastManager;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewGroup decorView = (ViewGroup) getWindow().getDecorView();
        contentContainer = (FrameLayout) ((ViewGroup)decorView.getChildAt(0)).getChildAt(1);
        floatView = LayoutInflater.from(getBaseContext()).inflate(R.layout.bottom_activity,null);
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        btnPlayPause = (ImageButton) floatView.findViewById(R.id.btn_play_pause);
        btnPrev = (ImageButton) floatView.findViewById(R.id.btn_prev_song);
        btnNext = (ImageButton) floatView.findViewById(R.id.btn_next_song);
        linearLayout = (LinearLayout) floatView.findViewById(R.id.float_activity_view);
        linearLayout.setOnClickListener(this);
        btnNext.setOnClickListener(this);
        btnPlayPause.setOnClickListener(this);
        btnPrev.setOnClickListener(this);

    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,155);
        layoutParams.gravity = Gravity.BOTTOM;
        contentContainer.addView(floatView,layoutParams);
    }

    @Override
    public void startActivity(Intent intent) {
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        super.startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unsubscribe();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0,0);
    }

    protected void unsubscribe () {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_prev_song:
                Intent prevIntent = new Intent();
                prevIntent.setAction(App.BROADCAST_SEND_TO_PLAY_SERVICE);
                prevIntent.putExtra("instruction",App.PREV_SONG);
                localBroadcastManager.sendBroadcast(prevIntent);
                break;
            case R.id.btn_play_pause:
                
                Intent playIntent = new Intent();
                playIntent.setAction(App.BROADCAST_SEND_TO_PLAY_SERVICE);
                playIntent.putExtra("instruction",App.PLAY_SONG);
                localBroadcastManager.sendBroadcast(playIntent);
                break;
            case R.id.btn_next_song:
                Intent nextIntent = new Intent();
                nextIntent.setAction(App.BROADCAST_SEND_TO_PLAY_SERVICE);
                nextIntent.putExtra("instruction",App.NEXT_SONG);
                localBroadcastManager.sendBroadcast(nextIntent);
                break;
            case R.id.float_activity_view:

                break;
        }
    }
}
