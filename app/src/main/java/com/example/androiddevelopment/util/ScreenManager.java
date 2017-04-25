package com.example.androiddevelopment.util;

import com.duzun.player.activity.BaseActivity;

import java.util.Stack;

/**
 * Created by asus on 2015/10/28.
 */
public class ScreenManager {

    private static Stack<BaseActivity> activityStack;
    private static ScreenManager instance;

    private ScreenManager() {
        if (activityStack == null) {
            activityStack = new Stack<BaseActivity>();
        }
    }

    public static ScreenManager getScreenManager() {
        if (instance == null) {
            instance = new ScreenManager();
        }
        return instance;
    }

    /**
     * 添加activity
     *
     * @param activity
     */
    public void pushActivity(BaseActivity activity) {
        activityStack.add(activity);
    }

    /**
     * 移除activity
     *
     * @param activity
     */
    public void popActivity(BaseActivity activity) {
        if (activity != null) {
            activity.finish();
            activityStack.remove(activity);
            activity = null;
        }
    }

    public Stack<BaseActivity> getActivityStack() {
        return activityStack;
    }

    public void finishAll() {
        for (BaseActivity activity : activityStack) {
            activity.finish();
        }
    }
}
