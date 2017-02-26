package com.example.cloudmusic.model;

import android.content.Context;

import com.example.cloudmusic.bean.SongData;
import com.example.cloudmusic.model.modelIterface.ILibraryModel;
import com.example.cloudmusic.presenter.preInterface.OnLibraryListener;
import com.example.cloudmusic.utils.ScanMusic;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by py on 2017/2/25.
 */

public class LibraryModel implements ILibraryModel{

    private Context context;
    private OnLibraryListener libraryListener;

    public LibraryModel(OnLibraryListener libraryListener, Context context) {
        this.libraryListener = libraryListener;
        this.context = context;
    }

    Subscriber<List<SongData>> subscriber = new Subscriber<List<SongData>>() {

        @Override
        public void onCompleted() {
        }

        @Override
        public void onError(Throwable e) {
            libraryListener.onError();
        }

        @Override
        public void onNext(List<SongData> list) {
            libraryListener.onSuccess(list);
        }
    };
    @Override
    public void loadSong() {
        ScanMusic.getInstance().getLocalSongList(context)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }
}
