package com.example.cloudmusic;

import android.support.v7.app.AppCompatActivity;

import rx.Subscription;

/**
 * Created by py on 2016/12/7.
 */

public class BaseActivity extends AppCompatActivity{
    protected Subscription subscription;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unsubscribe();
    }

    protected void unsubscribe () {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }
}
