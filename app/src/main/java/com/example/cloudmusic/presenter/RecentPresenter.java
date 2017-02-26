package com.example.cloudmusic.presenter;

import android.content.Context;

import com.example.cloudmusic.bean.SongData;
import com.example.cloudmusic.model.RecentModel;
import com.example.cloudmusic.model.modelIterface.IRecentModel;
import com.example.cloudmusic.presenter.preInterface.IRecentPresenter;
import com.example.cloudmusic.presenter.preInterface.OnRecentListener;
import com.example.cloudmusic.view.viewInterface.RecentView;

import java.util.List;

/**
 * Created by py on 2017/2/25.
 */

public class RecentPresenter implements IRecentPresenter,OnRecentListener {
    RecentView recentView;
    IRecentModel recentModel;

    public RecentPresenter(RecentView recentView) {
        this.recentView = recentView;
        recentModel = new RecentModel(this);
    }

    @Override
    public void loadRecent(Context context) {
        recentView.showLoading();
        recentModel.loadRecent(context);
    }

    @Override
    public void onSuccess(List<SongData> songDatas) {
        recentView.hideLoading();
        recentView.showResult(songDatas);
    }

    @Override
    public void onError() {
        recentView.hideLoading();
        recentView.showError();
    }
}
