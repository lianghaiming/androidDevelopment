package com.example.androiddevelopment.network;

import java.util.HashMap;
import java.util.Map;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by Administrator on 2017/5/6.
 */
public enum RxCenter {
    INSTANCE;
    private Map<Integer, CompositeSubscription> mCompositeSubcriptionMap;

    RxCenter() {
        mCompositeSubcriptionMap = new HashMap<>();
    }

    public void removeCompositeSubscription(int taskId) {
        CompositeSubscription compositeSubscription = mCompositeSubcriptionMap.get(taskId);
        if (compositeSubscription != null) {
            compositeSubscription.unsubscribe();
            mCompositeSubcriptionMap.remove(taskId);
        }
    }

    public CompositeSubscription getCompositeSubscription(int taskId) {
        CompositeSubscription compositeSubscription = mCompositeSubcriptionMap.get(taskId);
        if(compositeSubscription == null) {
            compositeSubscription = new CompositeSubscription();
            mCompositeSubcriptionMap.put(taskId, compositeSubscription);
        }
        return compositeSubscription;
    }


















































}
