package com.example.androiddevelopment.util;

import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;

/**
 * 对上传的文件名和文件大小进行加密
 * Created by asus on 2015/11/10.
 */
public class Md5Util {

    public static long getFileSize(String filePath){
        File file = new File(filePath);
        long size = file.length();
        return size;
    }
    public static String getFileTypes(String filePath){
        File file = new File(filePath);
        String name = file.getName();
        name = name.substring(name.indexOf("."));
        return name;
    }
    private static char HEX_DIGITS[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'A', 'B', 'C', 'D', 'E', 'F'};

    public static String toHexString(byte[] b) {
        StringBuilder sb = new StringBuilder(b.length * 2);
        for (int i = 0; i < b.length; i++) {
            sb.append(HEX_DIGITS[(b[i] & 0xf0) >>> 4]);
            sb.append(HEX_DIGITS[b[i] & 0x0f]);
        }
        return sb.toString().substring(0,4);
    }

    public static String md5sum(String filename) {
        InputStream fis;
        byte[] buffer = new byte[1024];
        int numRead = 0;
        MessageDigest md5;
        try {
            fis = new FileInputStream(filename);
            md5 = MessageDigest.getInstance("MD5");
            while ((numRead = fis.read(buffer)) > 0) {
                md5.update(buffer, 0, numRead);
            }
            fis.close();
            return toHexString(md5.digest());
        } catch (Exception e) {
            System.out.println("error");
            return null;
        }
    }

    public static String getMd5ByFile(String fileName) {
        String value = null;
        FileInputStream in = null;
        try {
            File file = new File(fileName);
             in = new FileInputStream(file);
            MappedByteBuffer byteBuffer = in.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, file.length());
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(byteBuffer);
            BigInteger bi = new BigInteger(1, md5.digest());
            value = bi.toString(16);
        } catch (Exception e) {
            Log.i("TAG", e.toString());
            //e.printStackTrace();
        } finally {
            if(null != in) {
                try {
                    in.close();
                } catch (IOException e) {
                    Log.i("TAG", e.toString());
                    //e.printStackTrace();
                }
            }
        }
        return value.substring(0,4);
    }

    static {
        System.loadLibrary("md5");
    }
    public native String md5Encrypt(String msg);
}
