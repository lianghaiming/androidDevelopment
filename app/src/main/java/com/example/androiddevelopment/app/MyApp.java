package com.example.androiddevelopment.app;

import com.example.androiddevelopment.greendao.GreenDaoHelper;

/**
 * Created by Administrator on 2017/5/2.
 */
public class MyApp extends BaseApp {

    private static GreenDaoHelper greenDaoHelper;


    @Override
    public void onCreate() {
        super.onCreate();
        initDataBase();

    }

    private void initDataBase() {
        greenDaoHelper = GreenDaoHelper.getInstance(getApplicationContext());
    }

    public static GreenDaoHelper getGreenDaoHelper() {
        return greenDaoHelper;
    }



















}
