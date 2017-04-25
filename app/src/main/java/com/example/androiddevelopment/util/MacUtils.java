package com.example.androiddevelopment.util;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;

import com.duzun.home.duzunhome.util.MyMD5;

/**
 * Created by asus on 2016/6/13.
 */
public class MacUtils {
    //获取设备mac地址
    public static byte[] getMACString(Context context) {
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        String mac = info.getMacAddress();
        String reMac = "";
        if (!TextUtils.isEmpty(mac)) {
            String[] mac3 = mac.split(":");
            for (String temp : mac3) {
                reMac += temp;
            }
        }
        return MyMD5.hexStringToBytes(reMac);
    }
}
