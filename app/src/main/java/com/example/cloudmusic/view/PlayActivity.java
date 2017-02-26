package com.example.cloudmusic.view;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.cloudmusic.App;
import com.example.cloudmusic.R;
import com.example.cloudmusic.bean.SongData;
import com.example.cloudmusic.receiver.PlayUIReceiver;
import com.example.cloudmusic.receiver.SeekBarReceiver;
import com.example.cloudmusic.utils.PlayControl;
import com.example.cloudmusic.view.ui.LrcView;
import com.example.cloudmusic.view.viewInterface.PlayView;

import java.text.SimpleDateFormat;

import static com.example.cloudmusic.App.currDuration;
import static com.example.cloudmusic.App.duration;
import static com.example.cloudmusic.App.isPlay;
import static com.example.cloudmusic.App.nowSong;

/**
 * Created by py on 2016/12/21.
 */

public class PlayActivity extends AppCompatActivity implements View.OnClickListener,PlayView{

    private Toolbar toolbar;
    private ImageButton btnPlay,btnPrev,btnNext,btnVoice,btnMode;
    private SeekBar seekBar;
    private TextView currtimeText;
    private TextView durationText;
    private SongData playSong;
    private int playMode;
    private LocalBroadcastManager localBroadcastManager;
    private PlayUIReceiver playUIReceive;
    private SeekBarReceiver seekBarReceiver;
    private SimpleDateFormat simpleDateFormat;
    private LrcView lrcView;
    private PlayControl playControl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        initView();

        localBroadcastManager = LocalBroadcastManager.getInstance(this);

        //设置receiver
        playUIReceive = new PlayUIReceiver(this);
        IntentFilter filter = new IntentFilter();
        filter.addAction(App.BROADCAST_SEND_TO_UPDATE);
        localBroadcastManager.registerReceiver(playUIReceive,filter);
        seekBarReceiver = new SeekBarReceiver(this);
        IntentFilter filter1 = new IntentFilter();
        filter1.addAction(App.BROADCAST_SEND_TO_SEEKBAR);
        localBroadcastManager.registerReceiver(seekBarReceiver,filter1);

        playControl = new PlayControl(localBroadcastManager);

        Intent intent = getIntent();
        playMode = intent.getIntExtra("playMode",0);
        isPlay = intent.getBooleanExtra("isPlay",false);
        playSong = intent.getParcelableExtra("nowSong");

        toolbar.setTitle(playSong.getSongname());
        toolbar.setSubtitle(playSong.getSingername());
        //toolbar.setTitleTextColor(getResources().getColor(R.color.allWhite));
        //toolbar.setSubtitleTextColor(getResources().getColor(R.color.titleGrey));
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
    protected void onResume() {
        super.onResume();
        if (isPlay) {
            btnPlay.setImageResource(R.mipmap.big_pause);
        } else {
            btnPlay.setImageResource(R.mipmap.big_play);
        }
        currtimeText.setText(simpleDateFormat.format(currDuration));
        durationText.setText(simpleDateFormat.format(duration));
        seekBar.setMax(duration);
        seekBar.setProgress(currDuration);
        new Thread(new Runnable() {
            @Override
            public void run() {
                lrcView.setLrcPath(nowSong.getLrc_path());
            }
        }).start();
        lrcView.onProgress(currDuration);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_play_share,menu);
        return true;
    }

    @Override
    public void receiveUiBroadcast(Context context, Intent intent) {
        isPlay = intent.getBooleanExtra("isPlay",false);
        playSong = intent.getParcelableExtra("nowSong");
        duration = intent.getIntExtra("duration",0);
        toolbar.setTitle(playSong.getSongname());
        toolbar.setSubtitle(playSong.getSingername());
        seekBar.setMax(duration);
        if (isPlay) {
            btnPlay.setImageResource(R.mipmap.big_pause);
        } else {
            btnPlay.setImageResource(R.mipmap.big_play);
        }
        durationText.setText(simpleDateFormat.format(duration));

        new Thread(new Runnable() {
            @Override
            public void run() {
                lrcView.setLrcPath(playSong.getLrc_path());
            }
        }).start();
    }

    @Override
    public void recevieSeekBarBroadcast(Context context, Intent intent) {
        currDuration = intent.getIntExtra("currDuration",0);
        seekBar.setProgress(currDuration);
        currtimeText.setText(simpleDateFormat.format(currDuration));

        lrcView.onProgress(currDuration);
        //lrcView.onDrag(seekBar.getProgress());
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
        lrcView = (LrcView) findViewById(R.id.lrc_view);

        btnMode.setOnClickListener(this);
        btnPrev.setOnClickListener(this);
        btnPlay.setOnClickListener(this);
        btnNext.setOnClickListener(this);
        btnVoice.setOnClickListener(this);
        simpleDateFormat = new SimpleDateFormat("mm:ss");
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                //currtimeText.setText(simpleDateFormat.format(seekBar.getProgress()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                lrcView.onDrag(seekBar.getProgress());
                Intent intent = new Intent();
                intent.setAction(App.BROADCAST_SEND_TO_PLAY_SERVICE);
                intent.putExtra("instruction",App.ADJUST_SEEKBAR);
                App.currDuration = seekBar.getProgress();
                localBroadcastManager.sendBroadcast(intent);
                currtimeText.setText(simpleDateFormat.format(currDuration));
                //Log.d("shit",String.valueOf(isPlay));
                isPlay = true;
                if (isPlay) {
                    btnPlay.setImageResource(R.mipmap.big_pause);
                } else {
                    btnPlay.setImageResource(R.mipmap.big_play);
                }

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_play_mode:

                break;

            case R.id.btn_prev_song:
                playControl.preSong();
                break;

            case R.id.btn_play_pause:
                playControl.playPause(btnPlay);
                break;

            case R.id.btn_next_song:
                playControl.nextSong();
                break;

            case R.id.btn_voice:

                break;
        }
    }

}
