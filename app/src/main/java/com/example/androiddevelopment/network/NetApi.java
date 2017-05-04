package com.example.androiddevelopment.network;


import com.example.androiddevelopment.model.LoginResponse;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by Administrator on 2017/5/4.
 */
public interface NetApi {

    @POST("RegisterServlet")
    Observable<LoginResponse> login(@Body RequestBody body);

    @GET()
    Observable<ResponseBody> getPhoto(@Url String url);




















}
