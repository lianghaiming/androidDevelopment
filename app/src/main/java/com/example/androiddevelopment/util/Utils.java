package com.example.androiddevelopment.util;

import android.annotation.TargetApi;
import android.app.Service;
import android.content.Context;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Vibrator;
import android.util.Log;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by asus on 2015/9/17.
 */
public class Utils {
    public static void log(String msg) {
        Log.w("Player", msg);
    }


    public static void Vibrate(final Context activity, long milliseconds) {
        Vibrator vib = (Vibrator) activity.getSystemService(Service.VIBRATOR_SERVICE);
        vib.vibrate(milliseconds);
    }

    //手机号   验证码

    /**
     * 将long型的时间转换成00:00:00格式的
     *
     * @return
     */
    public static String longToStringTime(int duration) {
        duration /= 1000;
        int minPts = duration / 60;
        int secPts = duration % 60;
        int hourPts = minPts / 60;
        String min = "" + minPts;
        String sec = "" + secPts;
        String hour = "" + hourPts;
        if (minPts < 10) {
            min = "0" + minPts;
        }
        if (secPts < 10) {
            sec = "0" + secPts;
        }
        if (hourPts < 10) {
            hour = "0" + hourPts;
        }
        return "" + hour + ":" + min + ":" + sec;
    }

    /**
     * 将00：00：00格式的时间转换成long
     *
     * @param time
     * @return
     */
    public static long StringToLongTime(String time) {
        //Utils.log("Utils----time="+time);
        long totalSec=0;
        try {
            time = time.replaceAll("：", ":");
            String[] my = time.split(":");
            int hour = Integer.parseInt(my[0]);
            int min = Integer.parseInt(my[1]);
            int sec = Integer.parseInt(my[2]);
            totalSec = hour * 3600 + min * 60 + sec;
        }catch (Exception e){
            Utils.log("Utils  StringToLongTime 出错,message="+e.getMessage());
        }
        return totalSec;
    }

    public static String getDate() {
        Calendar c = Calendar.getInstance();

        String year = String.valueOf(c.get(Calendar.YEAR));
        String month = String.valueOf(c.get(Calendar.MONTH) + 1);
        String day = String.valueOf(c.get(Calendar.DAY_OF_MONTH));
        String hour = String.valueOf(c.get(Calendar.HOUR_OF_DAY));
        String mins = String.valueOf(c.get(Calendar.MINUTE));

        StringBuffer sbBuffer = new StringBuffer();
        if (mins.length() == 1) {
            mins = "0" + mins;
        }
        sbBuffer.append(year + "-" + month + "-" + day + " " + hour + ":"
                + mins);

        return sbBuffer.toString();
    }


    public static String getIp(int i) {
        return (i & 0xFF) + "." +
                ((i >> 8) & 0xFF) + "." +
                ((i >> 16) & 0xFF) + "." +
                (i >> 24 & 0xFF);
    }

    private static Map<Integer, Integer> soundMap = new HashMap<Integer, Integer>();

    /**
     * 加载音频池，播放简短音乐
     * * @param current
     */
//    public static void loadVoice(Context context, final int current) {
//
//        AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
//        //最大声音
//        float audioMaxVolumn = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
//        //当前声音音量
//        float audioCurrentVolumn = am.getStreamVolume(AudioManager.STREAM_MUSIC);
//        //声道
//        final float volumnRatio = audioCurrentVolumn / audioMaxVolumn;
//        //初始化音频池
//        SoundPool soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 100);
//        soundMap.put(1, soundPool.load(context, R.raw.a54, 1));
//        soundMap.put(2, soundPool.load(context, R.raw.a43, 1));
//        soundMap.put(3, soundPool.load(context, R.raw.liushui_, 1));
//        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {//设置监听，因为SoundPool加载需要时间
//            @Override
//            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
//                log("开始播放音频");
//                soundPool.play(soundMap.get(current), volumnRatio, volumnRatio, 0, 0, 1);
//            }
//        });
//
//
//    }

    /**
     * 查找wifi
     *
     * @param context
     * @return
     */
    public static boolean researchWifi(Context context) {
        WifiManager manager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = manager.getConnectionInfo();
        String ssid = info.getSSID();
        info.getRssi ();
        if(ssid!=null) {

            Utils.log("当前连接wifi的SSID：" + ssid);
            if (ssid.equals("<unknown ssid>")) {
                return false;
            }
        }
        return true;
    }

    /**
     * 查找wifi
     *
     * @param context
     * @return
     */
    public static String getIpAddress(Context context) {
        WifiManager manager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = manager.getConnectionInfo();
        return getIp(info.getIpAddress());
    }

    /**
     * 获得手机mac地址
     *
     * @param context
     * @return
     */
    public static String getMacAddress(Context context) {
        WifiManager manager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = manager.getConnectionInfo();
        String ssid = info.getSSID();
        Utils.log("当前连接wifi的SSID：" + ssid);
        if (ssid.equals("<unknown ssid>")) {
            return "";
        }
        return info.getMacAddress();
    }

    /**
     * 获取屏幕宽度
     *
     * @param context
     * @return
     */
    public static int getDisplayWidth(Context context) {
        return ((MainActivity) context).getWindowManager().getDefaultDisplay().getWidth();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static long getMusicDuration(String path) {
        long duration = 0;
        MediaFormat mediaFormat;
        MediaExtractor mediaExtractor = new MediaExtractor();
        try {
            Utils.log ("需要解码获取歌曲时长的路径为："+path);
            mediaExtractor.setDataSource(path);
        } catch (Exception e) {
            Utils.log("Utils  设置文件路径出错：" + e.getMessage());
        }
        try {
            mediaFormat = mediaExtractor.getTrackFormat(0);
            duration = mediaFormat.getLong(MediaFormat.KEY_DURATION);
        } catch (Exception e) {
            Utils.log("Utils  读取歌曲文件信息失败：" + e.getMessage());
        }
        return duration;
    }
}
