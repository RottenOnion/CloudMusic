package com.example.cloudmusic.utils;

import android.util.Log;

import com.example.cloudmusic.bean.HttpResult;
import com.example.cloudmusic.bean.PageBean;
import com.example.cloudmusic.bean.SearchData;
import com.example.cloudmusic.bean.SongData;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by py on 2016/12/20.
 */

public class HttpMethods {
    public static final String BASE_URL = "http://route.showapi.com/";
    public static final int DEFAULT_TIMEOUT = 5;
    private static HttpMethods INSTANCE;

    private Retrofit retrofit;
    private SearchService searchService;

    private HttpMethods() {
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        httpClientBuilder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);


        retrofit = new Retrofit.Builder()
                .client(httpClientBuilder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .build();

        searchService = retrofit.create(SearchService.class);
    }


    public static HttpMethods getInstance() {

        if (INSTANCE == null)
        {
            INSTANCE = new HttpMethods();
        }
        return INSTANCE;
    }

    public Subscription getSearch(Subscriber<List<SongData>> subscriber, String keyword) {
        Subscription subscription;
        subscription =
                searchService.searh(keyword,"1","29267","b0bc162f7e9448d7a5680b223982b3ce")
                .map(new HttpResultFunc<SearchData>())
                .map(new Func1<SearchData, PageBean>() {
                    @Override
                    public PageBean call(SearchData searchData) {
                        return searchData.getPagebean();
                    }
                })
                .map(new Func1<PageBean, List<SongData>>() {
                    @Override
                    public List<SongData> call(PageBean pageBean) {
                        Log.d("shit",pageBean.getContentlist().toString());
                        return pageBean.getContentlist();
                    }
                })
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
        return subscription;
    }

    private class  HttpResultFunc<T> implements Func1<HttpResult<T>,T> {

        @Override
        public T call(HttpResult<T> httpResult) {
            Log.d("shit1",httpResult.toString());
            if (httpResult.getShowapi_res_code() != 0 ) {
                throw new UnsupportedOperationException();
            }
            return httpResult.getShowapi_res_body();
        }
    }


}
