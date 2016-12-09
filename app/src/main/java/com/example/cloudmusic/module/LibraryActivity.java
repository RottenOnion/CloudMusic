package com.example.cloudmusic.module;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.example.cloudmusic.R;
import com.example.cloudmusic.adapter.RecycleViewDivider;
import com.example.cloudmusic.adapter.SongAdapter;
import com.example.cloudmusic.bean.LocalSong;
import com.example.cloudmusic.utils.ScanMusic;

import java.util.List;

/**
 * Created by py on 2016/12/8.
 */

public class LibraryActivity extends AppCompatActivity {

    private RecyclerView songRV;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Toolbar toolbar;
    private List<LocalSong> songList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_music_library);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        songRV = (RecyclerView) findViewById(R.id.recycler_view);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_layout);
        toolbar.setTitle("本地音乐");
        setSupportActionBar(toolbar);

        songList = ScanMusic.getInstance().getSongList(this);


        songRV.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        songRV.addItemDecoration(new RecycleViewDivider(this,LinearLayoutManager.HORIZONTAL,2, Color.parseColor("#DCDCDC"),130));
        SongAdapter adapter = new SongAdapter(songList);
        songRV.setAdapter(adapter);
    }

}
