package com.example.androiddevelopment.util;

/**
 * Created by asus on 2016/1/15.
 */
public interface HttpCallBackListener {

    void onFinish(String response);

    void onError(Exception e);
}
