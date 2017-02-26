package com.example.cloudmusic.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.cloudmusic.R;
import com.example.cloudmusic.bean.SongData;
import com.example.cloudmusic.listener.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by py on 2016/12/8.
 */

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.songVH>{
    private List<SongData> list = new ArrayList<>();
    private static OnItemClickListener clickListener;

    public SongAdapter(List<SongData> list) {
        this.list = list;
    }

    @Override
    public songVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.song_item,parent,false);
        return new songVH(view);
    }

    @Override
    public void onBindViewHolder(songVH holder, int position) {
        SongData SongData = list.get(position);
        holder.songName.setText(SongData.getSongname());
        holder.songSinger.setText(SongData.getSingername()+" - "+SongData.getAlbumname());
        holder.songNumber.setText(String.valueOf(position));
    }

    @Override
    public int getItemCount() {
        return list == null? 0 : list.size();
    }

    public static class songVH extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView songNumber;
        private TextView songName;
        private TextView songSinger;
        private ImageButton songBtn;
        private LinearLayout songView;
        public songVH(View itemView) {
            super(itemView);
            songNumber = (TextView) itemView.findViewById(R.id.song_number);
            songName = (TextView) itemView.findViewById(R.id.song_name);
            songSinger = (TextView) itemView.findViewById(R.id.song_singer);
            songBtn = (ImageButton) itemView.findViewById(R.id.song_btn);
            songView = (LinearLayout) itemView.findViewById(R.id.song_view);

            songView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null){
                clickListener.onClick(itemView,getAdapterPosition());
            }
        }
    }

    public void setClickListener(OnItemClickListener clickListener) {
        this.clickListener = clickListener;
    }
}
