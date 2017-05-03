package com.example.androiddevelopment.app;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Administrator on 2017/5/2.
 */
public class BaseApp extends Application {

    public static List<Activity> activities = new LinkedList<>();

    private static Context mContext;
    private static Thread mMainThread;
    private static long mMainThreadId;
    private static Looper mMainLooper;
    private static Handler mHandler;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        mMainThread = Thread.currentThread();
        mMainThreadId = android.os.Process.myTid();
        mHandler = new Handler();
    }

    public static void addActivity(Activity activity) {
        activities.add(activity);
    }

    public static void exit() {
        for(Activity activity : activities) {
            activity.finish();
        }
    }

    public void restart() {
        Intent intent = mContext.getPackageManager().getLaunchIntentForPackage(mContext.getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        mContext.startActivity(intent);
    }

    public static Context getContext() {
        return mContext;
    }

    public static Thread getMainThread() {
        return mMainThread;
    }

    public static long getMainThreadId() {
        return mMainThreadId;
    }

    public static Handler getMainHandler() {
        return mHandler;
    }





























}
