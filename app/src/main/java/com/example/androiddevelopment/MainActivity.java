package com.example.androiddevelopment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import com.example.androiddevelopment.retrofit.ServiceApi;
import com.example.androiddevelopment.util.MyLogUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    public static final String BASE_URL = "";

    private ImageView photo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        photo = (ImageView)findViewById(R.id.image);

        down();



    }

    private void down() {
        MyLogUtil.print("down");
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://photocdn.sohu.com/")
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ServiceApi serviceApi = retrofit.create(ServiceApi.class);
        serviceApi.downloadPic("20111123/Img326603573.jpg")
                .subscribeOn(Schedulers.newThread())
                .map(new Func1<ResponseBody, Bitmap>() {

                    @Override
                    public Bitmap call(ResponseBody responseBody) {
                        Bitmap bitmap = DownloadImage(responseBody);
                        return bitmap;
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Bitmap>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Bitmap bitmap) {

                        photo.setImageBitmap(bitmap);
                    }
                });


    }

    private Bitmap DownloadImage(ResponseBody body) {

        try {
            Log.d("DownloadImage", "Reading and writing file");
            InputStream in = null;
            FileOutputStream out = null;

            try {
                in = body.byteStream();
                out = new FileOutputStream(getExternalFilesDir(null) + File.separator + "AndroidTutorialPoint.jpg");
                int c;

                while ((c = in.read()) != -1) {
                    out.write(c);
                }
            }
            catch (IOException e) {
                Log.d("DownloadImage",e.toString());
                return null;
            }
            finally {
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
            }

            int width, height;
            Bitmap bMap = BitmapFactory.decodeFile(getExternalFilesDir(null) + File.separator + "AndroidTutorialPoint.jpg");
            width = 2*bMap.getWidth();
            height = 6*bMap.getHeight();
            Bitmap bMap2 = Bitmap.createScaledBitmap(bMap, width, height, false);
//            photo.setImageBitmap(bMap2);

            return bMap;

        } catch (IOException e) {
            Log.d("DownloadImage",e.toString());
            return null;
        }
    }























}
