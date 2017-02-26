package com.example.cloudmusic.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.cloudmusic.R;
import com.example.cloudmusic.bean.Library;
import com.example.cloudmusic.listener.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by py on 2016/12/7.
 */

public class LibraryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static enum ITEM_TYPE {
        BASE,
        TITLE,
        LIST
    }

    private List<Library> libraries = new ArrayList<>();
    private static OnItemClickListener clickListener;

    public LibraryAdapter(List<Library> libraries) {
        this.libraries = libraries;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE.BASE.ordinal()) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.library_item, parent, false);
            return new BaseViewHolder(view);
        } else if (viewType == ITEM_TYPE.TITLE.ordinal()) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dividing_item,parent,false);
            return new TitleViewHolder(view);
        } else if (viewType == ITEM_TYPE.LIST.ordinal()) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.songlist_item,parent,false);
            return new ListViewHolder(view);
        }
        return null;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof BaseViewHolder) {
            Library library = libraries.get(position);
            ((BaseViewHolder)holder).img.setImageResource(library.getImageSource());
            ((BaseViewHolder)holder).name.setText(library.getTittle());
            if (position == 0) {
                ((BaseViewHolder)holder).count.setText("(305)");
            }
            if (position == 1) {
                ((BaseViewHolder)holder).count.setText("(17)");
            }
        } else if (holder instanceof TitleViewHolder) {

        } else if (holder instanceof ListViewHolder) {
            Library library = libraries.get(position);
            ((ListViewHolder)holder).img.setImageResource(library.getImageSource());
            ((ListViewHolder)holder).listName.setText(library.getTittle());
            //((ListViewHolder)holder).listCount.setText(library.getCount());
        }
    }

    @Override
    public int getItemCount() {
        return libraries == null? 0 :libraries.size();
    }

    public void setClickListener(OnItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public static class BaseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView img;
        private TextView name;
        private TextView count;
        private LinearLayout lib_view;
        public BaseViewHolder(View itemView) {
            super(itemView);
            img = (ImageView) itemView.findViewById(R.id.library_img);
            name = (TextView) itemView.findViewById(R.id.library_name);
            count = (TextView) itemView.findViewById(R.id.library_count);
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

    public static class TitleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private RelativeLayout relativeLayout;
        private ImageButton btn_create;
        public TitleViewHolder(View itemView) {
            super(itemView);
            relativeLayout = (RelativeLayout) itemView.findViewById(R.id.divide_line);
            btn_create = (ImageButton) itemView.findViewById(R.id.btn_create_list);
        }

        @Override
        public void onClick(View view) {

        }
    }

    public static class ListViewHolder extends  RecyclerView.ViewHolder implements  View.OnClickListener {
        private ImageButton btn_list;
        private LinearLayout layout;
        private ImageView img;
        private TextView listName;
        private TextView listCount;
        public ListViewHolder(View itemView) {
            super(itemView);
            btn_list = (ImageButton) itemView.findViewById(R.id.songlist_btn);
            layout = (LinearLayout) itemView.findViewById(R.id.layout_view);
            img = (ImageView) itemView.findViewById(R.id.songlist_image);
            listName = (TextView) itemView.findViewById(R.id.songlist_name);
            listCount = (TextView) itemView.findViewById(R.id.songlist_item);
        }

        @Override
        public void onClick(View view) {

        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position < 4) {
            return ITEM_TYPE.BASE.ordinal();
        } else if (position == 4) {
            return ITEM_TYPE.TITLE.ordinal();
        } else if (position > 4) {
            return ITEM_TYPE.LIST.ordinal();
        }
        return -9999;
    }
}
