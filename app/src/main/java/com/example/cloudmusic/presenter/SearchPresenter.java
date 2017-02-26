package com.example.cloudmusic.presenter;

import com.example.cloudmusic.bean.SongData;
import com.example.cloudmusic.model.SearchModel;
import com.example.cloudmusic.model.modelIterface.ISearchModel;
import com.example.cloudmusic.presenter.preInterface.ISearchPresenter;
import com.example.cloudmusic.presenter.preInterface.OnSearchListener;
import com.example.cloudmusic.view.viewInterface.ISearchView;

import java.util.List;

/**
 * Created by py on 2017/2/25.
 */

public class SearchPresenter implements ISearchPresenter,OnSearchListener{
    ISearchView searchView;
    ISearchModel searchModel;

    public SearchPresenter(ISearchView searchView) {
        this.searchView = searchView;
        searchModel = new SearchModel();
    }

    @Override
    public void onSuccess(List<SongData> songDatas) {
        searchView.hideLoading();
        searchView.showSearchResult(songDatas);
    }

    @Override
    public void onError() {
        searchView.hideLoading();
        searchView.showError();
    }

    @Override
    public void searchSong(String keyWord) {
        searchView.showLoading();
        searchModel.loadSong(keyWord,this);
    }
}
