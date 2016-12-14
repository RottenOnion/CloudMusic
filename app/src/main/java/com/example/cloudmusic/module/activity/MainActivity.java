package com.example.cloudmusic.module.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;

import com.example.cloudmusic.R;
import com.example.cloudmusic.adapter.LibraryAdapter;
import com.example.cloudmusic.adapter.RecycleViewDivider;
import com.example.cloudmusic.bean.Library;
import com.example.cloudmusic.listener.OnItemClickListener;
import com.example.cloudmusic.module.service.PlayService;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView libraryRv;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Toolbar toolbar;
    private LibraryAdapter adapter;
    private DrawerLayout drawerLayout;


    private List<Library> libraries;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent serviceIntent = new Intent(MainActivity.this, PlayService.class);
        startService(serviceIntent);

        libraryRv = (RecyclerView) findViewById(R.id.main_recycler_view);

        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        toolbar.setTitle("音乐");
        setSupportActionBar(toolbar);

                ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.app_name,R.string.app_name);
        drawerToggle.syncState();
        drawerLayout.addDrawerListener(drawerToggle);

        initLibraries();
        adapter = new LibraryAdapter(libraries);
        libraryRv.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        libraryRv.addItemDecoration(new RecycleViewDivider(this,LinearLayoutManager.HORIZONTAL,2, Color.parseColor("#DCDCDC"),128));
        adapter.setClickListener(new OnItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                //libraryRv点击事件
                if (position == 0) {
                    Intent intent = new Intent(MainActivity.this,LibraryActivity.class);
                    startActivity(intent);
                }
            }
        });
        libraryRv.setAdapter(adapter);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search,menu);
        return true;
    }

    private void initLibraries() {
        libraries = new ArrayList<>();

        Library library1 = new Library();
        library1.setImageSource(R.mipmap.icon_local);
        library1.setTittle("本地音乐");
        library1.setCount(0);
        libraries.add(library1);

        Library library2 = new Library();
        library2.setImageSource(R.mipmap.icon_recent);
        library2.setTittle("最近播放");
        library2.setCount(0);
        libraries.add(library2);

        Library library3 = new Library();
        library3.setImageSource(R.mipmap.icon_download);
        library3.setTittle("下载管理");
        library3.setCount(0);
        libraries.add(library3);

        Library library4 = new Library();
        library4.setImageSource(R.mipmap.icon_singer);
        library4.setTittle("我的歌手");
        library4.setCount(0);
        libraries.add(library4);

    }
}
