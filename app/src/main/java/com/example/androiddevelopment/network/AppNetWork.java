package com.example.androiddevelopment.network;


import com.google.gson.Gson;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2017/5/4.
 */
public enum AppNetWork {
    INSTANCE;

    private String BASE_URL = "http://192.168.1.55:8080/Server/";
    private NetApi mNetApi;

    AppNetWork() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mNetApi = retrofit.create(NetApi.class);

    }


    public RequestBody getRequestBody(Object obj) {
        String route = new Gson().toJson(obj);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), route);
        return body;
    }

    public NetApi getNetApi() {
        return mNetApi;
    }

























}
