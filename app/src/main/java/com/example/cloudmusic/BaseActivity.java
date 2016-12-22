package com.example.cloudmusic;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.cloudmusic.bean.SongData;
import com.example.cloudmusic.listener.OnUpdateViewListener;
import com.example.cloudmusic.module.activity.PlayActivity;

import java.util.ArrayList;
import java.util.List;

import rx.Subscription;

import static com.example.cloudmusic.App.isPlay;
import static com.example.cloudmusic.App.nowSong;

/**
 * Created by py on 2016/12/7.
 */

public class BaseActivity extends AppCompatActivity implements View.OnClickListener{

    private WindowManager windowManager;
    private FrameLayout contentContainer;
    private View floatView;
    private LinearLayout linearLayout;
    private  ImageButton btnPrev,btnNext;
    private TextView songText,singerText;
    private OnUpdateViewListener updateViewListener;

    protected Subscription subscription;
    protected ImageButton btnPlayPause;
    protected LocalBroadcastManager localBroadcastManager;
    protected int playMode = App.LISTLOOP;
    protected int duration;

    protected BaseUIReceiver baseReceiver;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        ViewGroup decorView = (ViewGroup) getWindow().getDecorView();
        contentContainer = (FrameLayout) ((ViewGroup)decorView.getChildAt(0)).getChildAt(1);
        floatView = LayoutInflater.from(getBaseContext()).inflate(R.layout.bottom_activity,null);
        localBroadcastManager = LocalBroadcastManager.getInstance(getBaseContext());
        btnPlayPause = (ImageButton) floatView.findViewById(R.id.btn_play_pause);
        btnPrev = (ImageButton) floatView.findViewById(R.id.btn_prev_song);
        btnNext = (ImageButton) floatView.findViewById(R.id.btn_next_song);
        songText = (TextView) floatView.findViewById(R.id.song_name);
        singerText = (TextView) floatView.findViewById(R.id.singer_name);
        linearLayout = (LinearLayout) floatView.findViewById(R.id.float_activity_view);
        linearLayout.setOnClickListener(this);
        btnNext.setOnClickListener(this);
        btnPlayPause.setOnClickListener(this);
        btnPrev.setOnClickListener(this);


        Log.d("cao","onCreate");

        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        IntentFilter filter = new IntentFilter();
        filter.addAction(App.BROADCAST_SEND_TO_UPDATE);
        baseReceiver = new BaseUIReceiver();
        localBroadcastManager.registerReceiver(baseReceiver,filter);

        if (nowSong != null) {
            Log.d("cao","nowSong == null");
            songText.setText(nowSong.getSongname());
            singerText.setText(nowSong.getSingername());
        }

    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,155);
        layoutParams.gravity = Gravity.BOTTOM;
        contentContainer.addView(floatView,layoutParams);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isPlay) {
            btnPlayPause.setImageResource(R.mipmap.pause_song);
        } else {
            btnPlayPause.setImageResource(R.mipmap.play_song);
        }
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
        //localBroadcastManager.unregisterReceiver(baseReceiver);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0,0);
    }

    protected void unsubscribe () {
        if (subscription != null && !subscription.isUnsubscribed()) {
            Log.e("解绑","大哥我解绑了");
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

                if (isPlay) {
                    btnPlayPause.setImageResource(R.mipmap.play_song);
                    isPlay = false;
                } else {
                    btnPlayPause.setImageResource(R.mipmap.pause_song);
                    isPlay = true;
                }

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
                Intent floatIntent = new Intent(BaseActivity.this, PlayActivity.class);
                floatIntent.putExtra("isPlay",isPlay);
                floatIntent.putExtra("duration",duration);
                floatIntent.putExtra("playMode",playMode);
                floatIntent.putExtra("nowSong",nowSong);
                startActivity(floatIntent);
                break;
        }
    }

    protected void sendToPlay(String listTag, List<SongData> list,int position,int instruction) {
        Intent intent = new Intent();
        intent.setAction(App.BROADCAST_SEND_TO_PLAY_SERVICE);
        intent.putParcelableArrayListExtra(listTag, (ArrayList<? extends Parcelable>) list);
        intent.putExtra("position",position);
        intent.putExtra("instruction",instruction);
        localBroadcastManager.sendBroadcast(intent);
    }

    public void setOnUdateViewListener(OnUpdateViewListener updateViewListener) {
        this.updateViewListener = updateViewListener;
    }

    public class BaseUIReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("cao","BASE收到了广播");

            isPlay = intent.getBooleanExtra("isPlay",false);
            nowSong = intent.getParcelableExtra("nowSong");
            duration = intent.getIntExtra("duration",0);
            songText.setText(nowSong.getSongname());
            singerText.setText(nowSong.getSingername());

            if (isPlay) {
                btnPlayPause.setImageResource(R.mipmap.pause_song);
            } else {
                btnPlayPause.setImageResource(R.mipmap.play_song);
            }

            if (updateViewListener != null) {
                updateViewListener.OnUpdateView(nowSong,duration);
            }
        }
    }


}
