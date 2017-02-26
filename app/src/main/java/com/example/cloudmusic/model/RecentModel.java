package com.example.cloudmusic.model;

import android.content.Context;

import com.example.cloudmusic.bean.SongData;
import com.example.cloudmusic.model.modelIterface.IRecentModel;
import com.example.cloudmusic.presenter.preInterface.OnRecentListener;
import com.example.cloudmusic.utils.ScanMusic;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by py on 2017/2/25.
 */

public class RecentModel implements IRecentModel {

    OnRecentListener recentListener;

    public RecentModel(OnRecentListener recentListener) {
        this.recentListener = recentListener;
    }

    Subscriber<List<SongData>> subscriber = new Subscriber<List<SongData>>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            recentListener.onError();
        }

        @Override
        public void onNext(List<SongData> songDatas) {
            recentListener.onSuccess(songDatas);
        }

    };

    @Override
    public void loadRecent(Context context) {
        ScanMusic.getInstance().getRecentSongList(context)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }
}
