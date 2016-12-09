package com.example.cloudmusic.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.cloudmusic.R;
import com.example.cloudmusic.bean.LocalSong;
import com.example.cloudmusic.listener.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by py on 2016/12/8.
 */

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.songVH>{
    private List<LocalSong> list = new ArrayList<>();
    private OnItemClickListener clickListener;

    public SongAdapter(List<LocalSong> list) {
        this.list = list;
    }

    @Override
    public songVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.song_item,parent,false);
        return new songVH(view);
    }

    @Override
    public void onBindViewHolder(songVH holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class songVH extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView songNumber;
        private TextView songName;
        private TextView songSinger;
        public songVH(View itemView) {
            super(itemView);
            songNumber = (TextView) itemView.findViewById(R.id.song_number);
            songName = (TextView) itemView.findViewById(R.id.song_name);
            songSinger = (TextView) itemView.findViewById(R.id.song_singer);
        }

        @Override
        public void onClick(View view) {

        }
    }
}
