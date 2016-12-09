package com.example.cloudmusic.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.cloudmusic.R;
import com.example.cloudmusic.bean.Library;
import com.example.cloudmusic.listener.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by py on 2016/12/7.
 */

public class LibraryAdapter extends RecyclerView.Adapter<LibraryAdapter.ViewHolder> {

    private List<Library> libraries = new ArrayList<>();
    private static OnItemClickListener clickListener;

    public LibraryAdapter(List<Library> libraries) {
        this.libraries = libraries;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.library_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Library library = libraries.get(position);
        holder.img.setImageResource(library.getImageSource());
        holder.name.setText(library.getTittle());
    }

    @Override
    public int getItemCount() {
        return libraries == null? 0 :libraries.size();
    }

    public void setClickListener(OnItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView img;
        private TextView name;
        private LinearLayout lib_view;
        public ViewHolder(View itemView) {
            super(itemView);
            img = (ImageView) itemView.findViewById(R.id.library_img);
            name = (TextView) itemView.findViewById(R.id.library_name);
            lib_view = (LinearLayout) itemView.findViewById(R.id.library_view);

            lib_view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null){
                clickListener.onClick(itemView,getAdapterPosition());
            }
        }
    }

}
