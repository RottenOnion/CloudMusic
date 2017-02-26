package com.example.cloudmusic.view;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.cloudmusic.App;
import com.example.cloudmusic.R;
import com.example.cloudmusic.adapter.RecycleViewDivider;
import com.example.cloudmusic.adapter.SearchAdapter;
import com.example.cloudmusic.bean.SongData;
import com.example.cloudmusic.listener.OnItemClickListener;
import com.example.cloudmusic.presenter.SearchPresenter;
import com.example.cloudmusic.presenter.preInterface.ISearchPresenter;
import com.example.cloudmusic.view.viewInterface.ISearchView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by py on 2016/12/19.
 */

public class SearchActivity extends BaseActivity implements ISearchView {

    private SearchView searchView;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private SearchAdapter adapter;
    private List<SongData> list;
    private Toolbar toolbar;
    private LocalBroadcastManager localBroadcastManager;
    private ISearchPresenter searchPresenter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initView();

    }

    private void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.search_recycler_view);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_layout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.icon_return);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(SearchActivity.this, LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new RecycleViewDivider(SearchActivity.this, LinearLayoutManager.HORIZONTAL, 2, Color.parseColor("#DCDCDC")));
        List<SongData> emptyList = new ArrayList<>();
        adapter = new SearchAdapter(emptyList);
        recyclerView.setAdapter(adapter);
        searchPresenter = new SearchPresenter(this);
        adapter.setClickListener(new OnItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                sendToPlay("com.example.cloudmusic.bean.songdata", list, position, App.CLICK_SONG);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /*
        初始化搜索栏
         */
        getMenuInflater().inflate(R.menu.menu_activity_search, menu);
        final MenuItem item = menu.findItem(R.id.action_search);
        searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setQueryHint("搜索音乐");
        searchView.onActionViewExpanded();
        searchView.setIconified(false);
        //searchView.setIconifiedByDefault(false);
        searchView.setSubmitButtonEnabled(true);

        /*
        搜索按键点击监听
         */
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchPresenter.searchSong(query);
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return true;
    }

    @Override
    public void showSearchResult(List<SongData> songDatas) {
        list = songDatas;
        adapter.update(songDatas);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void showError() {
        Toast.makeText(this, "查找歌曲时发生未知错误", Toast.LENGTH_SHORT).show();
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
