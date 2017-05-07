package com.example.androiddevelopment.util;


import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * Created by Administrator on 2017/2/16.
 */

public class OkHttpUtil {
    public static void get(String path) {
        OkHttpClient mOKHttpClient = new OkHttpClient();
        final Request request = new Request.Builder().url(path).build();
        Call call = mOKHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {

            }
        });
    }


    public static void post(String path) {
        OkHttpClient mOKHttpClient = new OkHttpClient();
        FormEncodingBuilder builder = new FormEncodingBuilder();
        builder.add("name", "123");
        builder.add("pass", "456");
//        JSONObject json = new JSONObject();
//        try {
//            json.put("content", "你们好");
//            json.put("address", "中国");
//        } catch (Exception e) {
//            LogUtil.Log("JSONObject");
//            e.printStackTrace();
//        }
//
//        builder.add("data", json.toString());
        LogUtil.Log("post");
        final Request request = new Request.Builder().url(path).post(builder.build()).build();
        Call call = mOKHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                LogUtil.Log("onFailure");
            }

            @Override
            public void onResponse(Response response) throws IOException {
                String s = response.body().string();
                LogUtil.Log(s);
            }
        });
    }












}
