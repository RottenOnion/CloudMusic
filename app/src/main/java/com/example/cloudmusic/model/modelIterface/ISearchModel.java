package com.example.cloudmusic.model.modelIterface;

import com.example.cloudmusic.presenter.preInterface.OnSearchListener;

/**
 * Created by py on 2017/2/25.
 */

public interface ISearchModel {
    void loadSong(String keyWorld, OnSearchListener searchListener);
}
