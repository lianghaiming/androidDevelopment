package com.example.androiddevelopment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import com.example.androiddevelopment.model.LoginRequest;
import com.example.androiddevelopment.model.LoginResponse;
import com.example.androiddevelopment.network.AppNetWork;
import com.example.androiddevelopment.util.LogUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import okhttp3.ResponseBody;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    public static final String BASE_URL = "192.168.1.55/8080";

    private ImageView photo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        photo = (ImageView)findViewById(R.id.image);

        AppNetWork.INSTANCE.getNetApi().getPhoto("image/hello.png")
                .subscribeOn(Schedulers.newThread())
                .map(new Func1<ResponseBody, Bitmap>() {
                    @Override
                    public Bitmap call(ResponseBody responseBody) {
                        Bitmap b = BitmapFactory.decodeStream(responseBody.byteStream());
                        return b;
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
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

//
        AppNetWork.INSTANCE.getNetApi().login(AppNetWork.INSTANCE.getRequestBody(new LoginRequest("楚留香", "123465")))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<LoginResponse>() {
                    @Override
                    public void call(LoginResponse loginResponse) {
                        LogUtil.print("code " + loginResponse.code + " token " + loginResponse.token);
                    }
                });

//        getThread.start();
        }


    private Thread getThread = new Thread(){
        public void run() {
            LogUtil.print("run.........................");
            HttpURLConnection connection = null;
            try {
                URL url = new URL("http://192.168.1.55:8080/Server/RegisterServlet");
                connection = (HttpURLConnection) url.openConnection();
                // 设置请求方法，默认是GET
                connection.setRequestMethod("GET");
                // 设置字符集
                connection.setRequestProperty("Charset", "UTF-8");
                // 设置文件类型
                connection.setRequestProperty("Content-Type", "text/xml; charset=UTF-8");
                // 设置请求参数，可通过Servlet的getHeader()获取
//                connection.setRequestProperty("Cookie", "AppName=" + URLEncoder.encode("你好", "UTF-8"));
                // 设置自定义参数
                connection.setRequestProperty("account", "this is me!");

                connection.setRequestProperty("password", "this is me!");
                LogUtil.print("code " + connection.getResponseCode());

                if(connection.getResponseCode() == 200){
                    LogUtil.print("success");
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                LogUtil.print("error.........................");
                e.printStackTrace();
            } finally {
                if(connection != null){
                    connection.disconnect();
                }
            }
        };
    };


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
