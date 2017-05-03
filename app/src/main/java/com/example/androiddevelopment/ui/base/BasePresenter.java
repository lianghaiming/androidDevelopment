package com.example.androiddevelopment.ui.base;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * Created by Administrator on 2017/5/2.
 */
public class BasePresenter<V> {
    public BaseActivity mContext;

    public BasePresenter(BaseActivity context) {
        mContext = context;
    }

    protected Reference<V> mViewRef;

    public void attachView(V view) {
        mViewRef = new WeakReference<V>(view);
    }

    public boolean isViewAttached() {
        return mViewRef != null && mViewRef.get() != null;
    }

    public void detachView() {
        if(mViewRef != null) {
            mViewRef.clear();
            mViewRef = null;
        }
    }

    public V getView() {
        return mViewRef != null ? mViewRef.get() : null;
    }























}
