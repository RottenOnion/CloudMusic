package com.example.cloudmusic.module.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.example.cloudmusic.App;
import com.example.cloudmusic.BaseActivity;
import com.example.cloudmusic.R;
import com.example.cloudmusic.adapter.RecycleViewDivider;
import com.example.cloudmusic.adapter.SongAdapter;
import com.example.cloudmusic.bean.SongData;
import com.example.cloudmusic.listener.OnItemClickListener;
import com.example.cloudmusic.utils.ScanMusic;

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
    private List<SongData> songList;


    Subscriber<List<SongData>> subscriber = new Subscriber<List<SongData>>() {

        @Override
        public void onCompleted() {
        }

        @Override
        public void onError(Throwable e) {
            swipeRefreshLayout.setRefreshing(false);
            Toast.makeText(LibraryActivity.this, "加载本地歌曲失败", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onNext(List<SongData> list) {
            swipeRefreshLayout.setRefreshing(false);
            songList = list;
            songRV.setLayoutManager(new LinearLayoutManager(LibraryActivity.this,LinearLayoutManager.VERTICAL,false));
            songRV.addItemDecoration(new RecycleViewDivider(LibraryActivity.this,LinearLayoutManager.HORIZONTAL,2, Color.parseColor("#DCDCDC"),130));
            SongAdapter adapter = new SongAdapter(songList);
            adapter.setClickListener(new OnItemClickListener() {
                @Override
                public void onClick(View view, int position) {
                    sendToPlay("com.example.cloudmusic.bean.songdata",songList,position,App.CLICK_SONG);
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
