package com.example.cloudmusic.utils;

import com.example.cloudmusic.bean.HttpResult;
import com.example.cloudmusic.bean.SearchData;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by py on 2016/12/20.
 */

public interface SearchService {
    @GET("213-1")
    Observable<HttpResult<SearchData>> searh(@Query("keyword")String keyword,
                                             @Query("page")String page,
                                             @Query("showapi_appid")String showapi_appid,
                                             @Query("showapi_sign")String showapi_sign);
}
