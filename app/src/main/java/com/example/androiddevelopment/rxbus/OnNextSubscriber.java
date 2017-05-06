package com.example.androiddevelopment.rxbus;

import com.example.androiddevelopment.util.LogUtil;

import rx.Subscriber;

/**
 * Created by Administrator on 2017/5/6.
 */
public abstract class OnNextSubscriber<T> extends Subscriber<T> {
    @Override
    public void onError(Throwable e) {

        LogUtil.print("OnNextSubscriber onError");

    }



    @Override
    public void onCompleted() {
        LogUtil.print("OnNextSubscriber onCompleted");

    }
}
