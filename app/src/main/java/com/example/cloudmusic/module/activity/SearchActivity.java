package com.example.cloudmusic.module.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;

import com.example.cloudmusic.App;
import com.example.cloudmusic.BaseActivity;
import com.example.cloudmusic.R;
import com.example.cloudmusic.adapter.RecycleViewDivider;
import com.example.cloudmusic.adapter.SearchAdapter;
import com.example.cloudmusic.bean.SongData;
import com.example.cloudmusic.listener.OnItemClickListener;
import com.example.cloudmusic.utils.HttpMethods;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

/**
 * Created by py on 2016/12/19.
 */

public class SearchActivity extends BaseActivity {

    private SearchView searchView;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private SearchAdapter adapter;
    private List<SongData> list;
    private LocalBroadcastManager localBroadcastManager;


    Subscriber<List<SongData>> subscriber = new Subscriber<List<SongData>>() {

        @Override
        public void onStart() {
            super.onStart();
            swipeRefreshLayout.setRefreshing(true);
            Log.d("shit","onStart");
        }

        @Override
        public void onCompleted() {
            swipeRefreshLayout.setRefreshing(false);
        }

        @Override
        public void onError(Throwable e) {
            Log.e("出错",e.toString());
        }

        @Override
        public void onNext(List<SongData> songDatas) {
                Log.d("shit",songDatas.toString());
                list = songDatas;
                adapter.update(songDatas);
                adapter.notifyDataSetChanged();
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        searchView = (SearchView) findViewById(R.id.search);
        recyclerView = (RecyclerView) findViewById(R.id.search_recycler_view);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_layout);
        searchView.setQueryHint("搜索音乐");
        searchView.onActionViewExpanded();
        searchView.setIconifiedByDefault(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(SearchActivity.this,LinearLayoutManager.VERTICAL,false));
        recyclerView.addItemDecoration(new RecycleViewDivider(SearchActivity.this,LinearLayoutManager.HORIZONTAL,2, Color.parseColor("#DCDCDC")));
        List<SongData> emptyList = new ArrayList<>();
        adapter = new SearchAdapter(emptyList);
        recyclerView.setAdapter(adapter);

        adapter.setClickListener(new OnItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                sendToPlay("com.example.cloudmusic.bean.songdata",list,position,App.CLICK_SONG);
            }
        });


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                HttpMethods.getInstance().getSearch(subscriber,query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

    }




}
