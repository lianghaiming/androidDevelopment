package com.example.androiddevelopment.interactor;

import com.example.androiddevelopment.callback.MoodCallback;
import com.example.androiddevelopment.network.RxCenter;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/5/6.
 */
public class MoodInteractor extends BaseInteractor{

    private MoodCallback mCallback;

    public MoodInteractor(MoodCallback callback) {
        mCallback = callback;
    }


    @Override
    public void destory() {
        RxCenter.INSTANCE.removeCompositeSubscription(TaskIds.moodTaskId);
    }

    public void getCacheMood() {
        RxCenter.INSTANCE.getCompositeSubscription(TaskIds.moodTaskId).add(rx.Observable
        .create(new rx.Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                String str = "hello";
                subscriber.onNext(str);
            }
        }).subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                if(mCallback != null) {
                    mCallback.onGetMood(s);
                }

            }
        }));

    }



























}
