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

import java.util.List;

/**
 * Created by py on 2016/12/20.
 */

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.searchVH>{

    private static OnItemClickListener clickListener;
    private List<SongData> songDatas ;

    public SearchAdapter(List<SongData> songDatas) {
        this.songDatas = songDatas;
    }

    public void update(List<SongData> newSongDatas){
        this.songDatas.clear();
        this.songDatas.addAll(newSongDatas);
    }

    @Override
    public searchVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.song_item,parent,false);
        return new searchVH(view);
    }

    @Override
    public void onBindViewHolder(searchVH holder, int position) {
        SongData song = songDatas.get(position);
        holder.songName.setText(song.getSongname());
        holder.songNumber.setText(String.valueOf(position+1));
        holder.singerName.setText(song.getSingername()+" - " +song.getAlbumname());
    }

    @Override
    public int getItemCount() {
        return songDatas == null? 0 : songDatas.size();
    }

    public static class searchVH extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView songNumber;
        private TextView songName;
        private TextView singerName;
        private ImageButton songBtn;
        private LinearLayout songView;
        public searchVH(View itemView) {
            super(itemView);
            songNumber = (TextView) itemView.findViewById(R.id.song_number);
            songName = (TextView) itemView.findViewById(R.id.song_name);
            singerName = (TextView) itemView.findViewById(R.id.song_singer);
            songView = (LinearLayout) itemView.findViewById(R.id.song_view);
            songBtn = (ImageButton) itemView.findViewById(R.id.song_btn);

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
