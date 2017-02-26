package com.example.cloudmusic.view.viewInterface;

import com.example.cloudmusic.bean.SongData;

import java.util.List;

/**
 * Created by py on 2017/2/25.
 */

public interface ISearchView {
    void showSearchResult(List<SongData> songDatas);
    void showError();
    void showLoading();
    void hideLoading();
}
