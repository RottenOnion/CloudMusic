package com.example.cloudmusic.view;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.cloudmusic.App;
import com.example.cloudmusic.R;
import com.example.cloudmusic.bean.SongData;
import com.example.cloudmusic.listener.OnUpdateViewListener;
import com.example.cloudmusic.receiver.BaseReceiver;
import com.example.cloudmusic.utils.PlayControl;
import com.example.cloudmusic.utils.ScanMusic;
import com.example.cloudmusic.view.viewInterface.BaseView;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.example.cloudmusic.App.isPlay;
import static com.example.cloudmusic.App.nowSong;

/**
 * Created by py on 2016/12/7.
 */

public class BaseActivity extends AppCompatActivity implements BaseView,View.OnClickListener {

    private WindowManager windowManager;
    private FrameLayout contentContainer;
    private View floatView;
    private LinearLayout linearLayout;
    private ImageButton btnPrev, btnNext;
    private TextView songText, singerText;
    private ImageView albumImage;
    private OnUpdateViewListener updateViewListener;
    protected Subscription subscription;
    protected ImageButton btnPlayPause;
    protected LocalBroadcastManager localBroadcastManager;
    protected int playMode = App.LISTLOOP;
    protected int duration;
    protected BaseReceiver baseReceiver;
    private PlayControl playControl;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*
        初始化控件
         */
        initView();

        /*
        广播注册逻辑
         */
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        IntentFilter filter = new IntentFilter();
        filter.addAction(App.BROADCAST_SEND_TO_UPDATE);
        baseReceiver = new BaseReceiver(this);
        localBroadcastManager.registerReceiver(baseReceiver, filter);

        playControl = new PlayControl(localBroadcastManager);

        /*
        初始化正在播放的歌曲与歌手
         */
        if (nowSong != null) {
            songText.setText(nowSong.getSongname());
            singerText.setText(nowSong.getSingername());
        }
        /*
        新增未知功能
         */
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }


    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        /*
        底部放置BaseActivity
         */
        super.onPostCreate(savedInstanceState);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 155);
        layoutParams.gravity = Gravity.BOTTOM;
        contentContainer.addView(floatView, layoutParams);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isPlay) {
            btnPlayPause.setImageResource(R.mipmap.pause_song);
        } else {
            btnPlayPause.setImageResource(R.mipmap.play_song);
        }
        if (nowSong != null && nowSong.getAlbumpic_small() != null) {
            Glide.with(BaseActivity.this)
            .load(nowSong.getAlbumpic_small())
                    .placeholder(R.drawable.album_test)
                    .into(albumImage);
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
        overridePendingTransition(0, 0);
    }

    protected void unsubscribe() {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_prev_song:
                playControl.preSong();
                break;
            case R.id.btn_play_pause:
                playControl.playPause(btnPlayPause);
                break;
            case R.id.btn_next_song:
                playControl.nextSong();
                break;

            case R.id.float_activity_view:
                Intent floatIntent = new Intent(BaseActivity.this, PlayActivity.class);
                floatIntent.putExtra("isPlay", isPlay);
                floatIntent.putExtra("duration", duration);
                floatIntent.putExtra("playMode", playMode);
                floatIntent.putExtra("nowSong", nowSong);
                startActivity(floatIntent);
                break;
        }
    }

    protected void sendToPlay(String listTag, List<SongData> list, int position, int instruction) {
        Intent intent = new Intent();
        intent.setAction(App.BROADCAST_SEND_TO_PLAY_SERVICE);
        intent.putParcelableArrayListExtra(listTag, (ArrayList<? extends Parcelable>) list);
        intent.putExtra("position", position);
        intent.putExtra("instruction", instruction);
        localBroadcastManager.sendBroadcast(intent);
    }

    public void setOnUdateViewListener(OnUpdateViewListener updateViewListener) {
        this.updateViewListener = updateViewListener;
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Base Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }


    private void initView() {
        ViewGroup decorView = (ViewGroup) getWindow().getDecorView();
        contentContainer = (FrameLayout) ((ViewGroup) decorView.getChildAt(0)).getChildAt(1);
        floatView = LayoutInflater.from(getBaseContext()).inflate(R.layout.bottom_activity, null);
        localBroadcastManager = LocalBroadcastManager.getInstance(getBaseContext());
        btnPlayPause = (ImageButton) floatView.findViewById(R.id.btn_play_pause);
        btnPrev = (ImageButton) floatView.findViewById(R.id.btn_prev_song);
        btnNext = (ImageButton) floatView.findViewById(R.id.btn_next_song);
        albumImage = (ImageView) floatView.findViewById(R.id.album_image);
        songText = (TextView) floatView.findViewById(R.id.song_name);
        singerText = (TextView) floatView.findViewById(R.id.singer_name);
        linearLayout = (LinearLayout) floatView.findViewById(R.id.float_activity_view);
        linearLayout.setOnClickListener(this);
        btnNext.setOnClickListener(this);
        btnPlayPause.setOnClickListener(this);
        btnPrev.setOnClickListener(this);
    }

    @Override
    public void showNewMessage(Context context, Intent intent) {
        isPlay = intent.getBooleanExtra("isPlay", false);
        nowSong = intent.getParcelableExtra("nowSong");
        duration = intent.getIntExtra("duration", 0);
        songText.setText(nowSong.getSongname());
        singerText.setText(nowSong.getSingername());
        if (isPlay) {
            btnPlayPause.setImageResource(R.mipmap.pause_song);
        } else {
            btnPlayPause.setImageResource(R.mipmap.play_song);
        }
        if (updateViewListener != null) {
            updateViewListener.OnUpdateView(nowSong, duration);
        }
        //这里是属于model层的东西把
        ScanMusic.getInstance().getAlbumImage(BaseActivity.this, nowSong.getAlbumid())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(String s) {
                        Glide.with(BaseActivity.this)
                                .load(s)
                                .placeholder(R.drawable.album_test)
                                .into(albumImage);
                        //File file = new File(s);

                    }
                });
    }

    @Override
    public void updateImage() {

    }



}
