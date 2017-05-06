package com.example.androiddevelopment.network;


import com.example.androiddevelopment.model.LoginResponse;
import com.example.androiddevelopment.model.UserListResponse;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
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

    @GET("getUserList")
    Observable<UserListResponse> getUserList();


//    @GET("getUserList")
//    Observable<List<User>> getUserList();
//    @GET("getUserList")
//    Observable<String> getUserList();



    @Multipart
    @POST("UploadPhoto")
    Observable<ResponseBody> upLoadPhoto(@Part List<MultipartBody.Part> partList);




















}
