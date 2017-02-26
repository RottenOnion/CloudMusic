package com.example.cloudmusic.model;

import com.example.cloudmusic.bean.SongData;
import com.example.cloudmusic.model.modelIterface.ISearchModel;
import com.example.cloudmusic.presenter.preInterface.OnSearchListener;
import com.example.cloudmusic.utils.HttpMethods;

import java.util.List;

import rx.Subscriber;

/**
 * Created by py on 2017/2/25.
 */

public class SearchModel implements ISearchModel {

    @Override
    public void loadSong(String keyWorld, final OnSearchListener searchListener) {

        HttpMethods.getInstance().getSearch(new Subscriber<List<SongData>>() {

            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                searchListener.onError();
            }

            @Override
            public void onNext(List<SongData> songDatas) {
                searchListener.onSuccess(songDatas);
            }
        }, keyWorld);
    }
}
