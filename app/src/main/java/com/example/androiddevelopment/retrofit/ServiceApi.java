package com.example.androiddevelopment.retrofit;


import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by Administrator on 2017/4/28.
 */
public interface ServiceApi {

    @GET
    Observable<ResponseBody> downloadPic(@Url String url);
}
