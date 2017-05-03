package com.example.androiddevelopment.ui.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.androiddevelopment.app.MyApp;

import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/5/2.
 */
public abstract class BaseActivity<V, T extends BasePresenter<V>> extends AppCompatActivity {

    protected T mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApp.addActivity(this);

        //判断是否使用MVP模式
        if((mPresenter = createPresenter()) != null) {
            mPresenter.attachView((V) this);
        }
        setContentView(provideContentViewId());
        ButterKnife.bind(this);
        initView();
        initData();
        initListener();
    }

    public void init() {

    }

    public void initView() {

    }

    public void initData() {

    }

    public void initListener() {

    }

    protected abstract T createPresenter();

    protected abstract int provideContentViewId();

    public void jumpToActivity(Intent intent) {
        startActivity(intent);
    }

    public void jumpToActivity(Class activity) {
        Intent intent = new Intent(this, activity);
        startActivity(intent);
    }

    public void jumpToActivityAndClearTask(Class activity) {
        Intent intent = new Intent(this, activity);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    public void jumpToActivityAndClearTop(Class activity) {
        Intent intent = new Intent(this, activity);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
































}
