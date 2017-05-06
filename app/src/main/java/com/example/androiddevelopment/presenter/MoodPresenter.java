package com.example.androiddevelopment.presenter;

import com.example.androiddevelopment.callback.IMoodView;
import com.example.androiddevelopment.callback.MoodCallback;
import com.example.androiddevelopment.interactor.MoodInteractor;
import com.example.androiddevelopment.ui.base.BaseActivity;
import com.example.androiddevelopment.ui.base.BasePresenter;

/**
 * Created by Administrator on 2017/5/6.
 */
public class MoodPresenter extends BasePresenter<IMoodView> implements MoodCallback{

    public MoodPresenter(BaseActivity context) {
        super(context);
        mInteractor = new MoodInteractor(this);
    }

    @Override
    public void onGetMood(String data) {
        getView().getMood(data);
        getView().getShowView().setText(data);

    }

    public void getData() {
        ((MoodInteractor)mInteractor).getCacheMood();
    }
















}
