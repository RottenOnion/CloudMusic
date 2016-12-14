package com.example.cloudmusic.module.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.example.cloudmusic.BaseActivity;
import com.example.cloudmusic.R;
import com.example.cloudmusic.adapter.RecycleViewDivider;
import com.example.cloudmusic.adapter.SongAdapter;
import com.example.cloudmusic.bean.LocalSong;
import com.example.cloudmusic.listener.OnItemClickListener;
import com.example.cloudmusic.utils.ScanMusic;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by py on 2016/12/8.
 */

public class LibraryActivity extends BaseActivity {

    private RecyclerView songRV;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Toolbar toolbar;
    private List<LocalSong> songList;

    Subscriber<List<LocalSong>> subscriber = new Subscriber<List<LocalSong>>() {

        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            swipeRefreshLayout.setRefreshing(false);
            Toast.makeText(LibraryActivity.this, "加载本地歌曲失败", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onNext(List<LocalSong> list) {
            swipeRefreshLayout.setRefreshing(false);
            songList = list;
            songRV.setLayoutManager(new LinearLayoutManager(LibraryActivity.this,LinearLayoutManager.VERTICAL,false));
            songRV.addItemDecoration(new RecycleViewDivider(LibraryActivity.this,LinearLayoutManager.HORIZONTAL,2, Color.parseColor("#DCDCDC"),130));
            SongAdapter adapter = new SongAdapter(songList);
            adapter.setClickListener(new OnItemClickListener() {
                @Override
                public void onClick(View view, int position) {
                    Intent intent = new Intent();
                    intent.setAction("android_CloudMusic_SEND_TO_SERVICE");
                    intent.putParcelableArrayListExtra("com.example.cloudmusic.bean", (ArrayList<? extends Parcelable>) songList);
                    intent.putExtra("position",position);
                    sendBroadcast(intent);
                }
            });
            songRV.setAdapter(adapter);
        }

        @Override
        public void onStart() {
            super.onStart();
            swipeRefreshLayout.setRefreshing(true);
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_music_library);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        songRV = (RecyclerView) findViewById(R.id.recycler_view);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_layout);
        toolbar.setTitle("本地音乐");
        setSupportActionBar(toolbar);



        ScanMusic.getInstance().getSongList(this)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);



    }

}
