package com.example.cloudmusic.view;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import com.example.cloudmusic.App;
import com.example.cloudmusic.R;
import com.example.cloudmusic.adapter.RecycleViewDivider;
import com.example.cloudmusic.adapter.SongAdapter;
import com.example.cloudmusic.bean.SongData;
import com.example.cloudmusic.listener.OnItemClickListener;
import com.example.cloudmusic.presenter.RecentPresenter;
import com.example.cloudmusic.presenter.preInterface.IRecentPresenter;
import com.example.cloudmusic.view.viewInterface.RecentView;

import java.util.List;

/**
 * Created by py on 2017/1/1.
 */

public class RecentActivity extends BaseActivity implements RecentView{
    private RecyclerView songRV;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Toolbar toolbar;
    private List<SongData> songList;
    private IRecentPresenter recentPresenter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_library);
        initView();
        recentPresenter = new RecentPresenter(this);
        recentPresenter.loadRecent(this);
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        songRV = (RecyclerView) findViewById(R.id.recycler_view);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_layout);
        toolbar.setTitle("最近播放");
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
        getMenuInflater().inflate(R.menu.menu_delete,menu);
        return true;
    }

    @Override
    public void showResult(List<SongData> songDatas) {
        songList = songDatas;
        songRV.setLayoutManager(new LinearLayoutManager(RecentActivity.this,LinearLayoutManager.VERTICAL,false));
        songRV.addItemDecoration(new RecycleViewDivider(RecentActivity.this,LinearLayoutManager.HORIZONTAL,2, Color.parseColor("#DCDCDC"),130));
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
    public void showError() {
        Toast.makeText(RecentActivity.this, "加载本地歌曲失败", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLoading() {
        swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideLoading() {
        swipeRefreshLayout.setRefreshing(false);
    }
}
