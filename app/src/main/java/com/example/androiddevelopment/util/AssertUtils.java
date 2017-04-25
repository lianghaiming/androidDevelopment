package com.example.androiddevelopment.util;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by asus on 2016/6/23.
 */
public class AssertUtils {

    public static InputStream getFile(Context context, String dir, String name) {
        try {
            return context.getResources().getAssets().open(dir + "/" + name);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String[] getFileNames(Context context, String dir) {
        try {
            return context.getAssets().list(dir);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new String[]{};
    }
}
