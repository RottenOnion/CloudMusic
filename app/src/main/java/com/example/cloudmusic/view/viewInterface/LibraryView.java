package com.example.cloudmusic.view.viewInterface;

import android.content.Context;

import com.example.cloudmusic.bean.SongData;

import java.util.List;

/**
 * Created by py on 2017/2/25.
 */

public interface LibraryView {
    void showResult(List<SongData> list);
    void showLoading();
    void showError();
    Context getContext();
}
