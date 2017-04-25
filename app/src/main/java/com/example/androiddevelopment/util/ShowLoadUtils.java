package com.example.androiddevelopment.util;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by asus on 2016/6/15.
 */
public class ShowLoadUtils {
    private static ProgressDialog progressDialog;

    public static void showProgressBar(Context context, String title, String message) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(context);
            progressDialog.setTitle(title);
            progressDialog.setMessage(message);
            progressDialog.setCancelable(false);
            progressDialog.show();
        } else {
            progressDialog.setTitle(title);
            progressDialog.setMessage(message);
        }
    }

    public static boolean isShowing() {
        return progressDialog != null && progressDialog.isShowing();
    }

    //取消显示加载等待框
    public static void unshowProgressBar() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }
}
