package com.example.cloudmusic.module.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.cloudmusic.App;
import com.example.cloudmusic.R;u
import com.example.cloudmusic.bean.SongData;

import static com.example.cloudmusic.App.isPlay;

/**
 * Created by py on 2016/12/21.
 */

public class PlayActivity extends AppCompatActivity implements View.OnClickListener{

    private Toolbar toolbar;
    private ImageButton btnPlay,btnPrev,btnNext,btnVoice,btnMode;
    private SeekBar seekBar;
    private TextView currtimeText;
    private TextView durationText;
    private SongData playSong;
    private int playMode;
    private int duration;
    private LocalBroadcastManager localBroadcastManager;
    private PlayUIReceiver receiver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_play);

        initView();

        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        receiver = new PlayUIReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(App.BROADCAST_SEND_TO_UPDATE);
        localBroadcastManager.registerReceiver(receiver,filter);

        Intent intent = getIntent();
        playMode = intent.getIntExtra("playMode",0);
        isPlay = intent.getBooleanExtra("isPlay",false);
        playSong = intent.getParcelableExtra("nowSong");

        toolbar.setTitle(playSong.getSongname());
        toolbar.setSubtitle(playSong.getSingername());
        toolbar.setNavigationIcon(R.mipmap.icon_return);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });



    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_play_mode:

                break;

            case R.id.btn_prev_song:
                Intent prevIntent = new Intent();
                prevIntent.setAction(App.BROADCAST_SEND_TO_PLAY_SERVICE);
                prevIntent.putExtra("instruction",App.PREV_SONG);
                localBroadcastManager.sendBroadcast(prevIntent);
                break;

            case R.id.btn_play_pause:
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
                localBroadcastManager.sendBroadcast(playIntent);
                break;

            case R.id.btn_next_song:
                Intent nextIntent = new Intent();
                nextIntent.setAction(App.BROADCAST_SEND_TO_PLAY_SERVICE);
                nextIntent.putExtra("instruction",App.NEXT_SONG);
                localBroadcastManager.sendBroadcast(nextIntent);
                break;

            case R.id.btn_voice:

                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isPlay) {
            btnPlay.setImageResource(R.mipmap.big_pause);
        } else {
            btnPlay.setImageResource(R.mipmap.big_play);
        }
    }

    public class PlayUIReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            isPlay = intent.getBooleanExtra("isPlay",false);
            playSong = intent.getParcelableExtra("nowSong");
            duration = intent.getIntExtra("duration",0);
            toolbar.setTitle(playSong.getSongname());
            toolbar.setTitle(playSong.getSingername());
            if (isPlay) {
                btnPlay.setImageResource(R.mipmap.big_pause);
            } else {
                btnPlay.setImageResource(R.mipmap.big_play);
            }
        }
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        btnMode = (ImageButton) findViewById(R.id.btn_play_mode);
        btnNext = (ImageButton) findViewById(R.id.btn_next_song);
        btnPrev = (ImageButton) findViewById(R.id.btn_prev_song);
        btnPlay = (ImageButton) findViewById(R.id.btn_play_pause);
        btnVoice = (ImageButton) findViewById(R.id.btn_voice);
        seekBar = (SeekBar) findViewById(R.id.play_bar);
        currtimeText = (TextView) findViewById(R.id.time_now);
        durationText = (TextView) findViewById(R.id.time_sum);

        btnMode.setOnClickListener(this);
        btnPrev.setOnClickListener(this);
        btnPlay.setOnClickListener(this);
        btnNext.setOnClickListener(this);
        btnVoice.setOnClickListener(this);
    }

}
