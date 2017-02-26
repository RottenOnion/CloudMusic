package com.example.cloudmusic.presenter;

import com.example.cloudmusic.bean.SongData;
import com.example.cloudmusic.model.LibraryModel;
import com.example.cloudmusic.model.modelIterface.ILibraryModel;
import com.example.cloudmusic.presenter.preInterface.ILibraryPresenter;
import com.example.cloudmusic.presenter.preInterface.OnLibraryListener;
import com.example.cloudmusic.view.viewInterface.LibraryView;

import java.util.List;

/**
 * Created by py on 2017/2/25.
 */

public class LibraryPresenter implements ILibraryPresenter,OnLibraryListener{

    LibraryView libraryView;
    ILibraryModel libraryModel;

    public LibraryPresenter(LibraryView libraryView) {
        this.libraryView = libraryView;
        libraryModel = new LibraryModel(this,libraryView.getContext());
    }

    @Override
    public void loadSong() {
        libraryView.showLoading();
        libraryModel.loadSong();
    }

    @Override
    public void onSuccess(List<SongData> songDatas) {
        libraryView.showResult(songDatas);
    }

    @Override
    public void onError() {
        libraryView.showError();
    }
}
