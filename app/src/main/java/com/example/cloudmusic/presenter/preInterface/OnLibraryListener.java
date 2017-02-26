package com.example.cloudmusic.presenter.preInterface;

import com.example.cloudmusic.bean.SongData;

import java.util.List;

/**
 * Created by py on 2017/2/25.
 */

public interface OnLibraryListener {
    void onSuccess(List<SongData> songDatas);
    void onError();
}
