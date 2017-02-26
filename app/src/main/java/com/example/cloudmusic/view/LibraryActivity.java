package com.example.cloudmusic.view;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.cloudmusic.App;
import com.example.cloudmusic.R;
import com.example.cloudmusic.adapter.RecycleViewDivider;
import com.example.cloudmusic.adapter.SongAdapter;
import com.example.cloudmusic.bean.SongData;
import com.example.cloudmusic.listener.OnItemClickListener;
import com.example.cloudmusic.presenter.LibraryPresenter;
import com.example.cloudmusic.presenter.preInterface.ILibraryPresenter;
import com.example.cloudmusic.view.viewInterface.LibraryView;

import java.util.List;

/**
 * Created by py on 2016/12/8.
 */

public class LibraryActivity extends BaseActivity implements LibraryView {

    private RecyclerView songRV;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Toolbar toolbar;
    private List<SongData> songList;
    private ILibraryPresenter libraryPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_music_library);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        initView();
        libraryPresenter = new LibraryPresenter(this);
        libraryPresenter.loadSong();
    }

    private void initView(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        songRV = (RecyclerView) findViewById(R.id.recycler_view);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_layout);
        toolbar.setTitle("本地音乐");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.icon_return);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_search,menu);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        libraryPresenter = null;
    }

    @Override
    public void showResult(List<SongData> list) {
        swipeRefreshLayout.setRefreshing(false);
        songList = list;
        songRV.setLayoutManager(new LinearLayoutManager(LibraryActivity.this,LinearLayoutManager.VERTICAL,false));
        songRV.addItemDecoration(new RecycleViewDivider(LibraryActivity.this,LinearLayoutManager.HORIZONTAL,2, Color.parseColor("#DCDCDC"),130));
        SongAdapter adapter = new SongAdapter(songList);
        adapter.setClickListener(new OnItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                sendToPlay("com.example.cloudmusic.bean.songdata",songList,position, App.CLICK_SONG);
            }
        });
        songRV.setAdapter(adapter);
    }

    @Override
    public void showLoading() {
        swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void showError() {
        swipeRefreshLayout.setRefreshing(false);
        Toast.makeText(LibraryActivity.this, "加载本地歌曲失败", Toast.LENGTH_SHORT).show();
    }

    @Override
    public Context getContext() {
        return this;
    }
}
