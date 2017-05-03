package com.example.androiddevelopment.util;

import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;

/**
 * Created by Administrator on 2017/4/28.
 */
public class MyLogUtil {

    public static void print(String msg) {
//		System.out.println(msg);
        Log.e("tag", msg);
    }

    public static void prin(String msg) {
        System.out.print(msg);
    }

    public static void printHexString(byte[] b) {
        for(int i = 0; i < b.length; i ++) {
            String hex = Integer.toHexString(b[i] & 0xff);
            if(hex.length() == 1) {
                hex = '0' + hex;

            }
            System.out.print(hex.toUpperCase() + " ");
            if((i + 1) % 30 == 0) {
                System.out.println();
            }

        }
        System.out.println();
    }

    public static void printHex(byte b) {
        String hex = Integer.toHexString(b & 0xff);
        if(hex.length() == 1) {
            hex = '0' + hex;
        }
        System.out.print(hex.toUpperCase() + " ");
    }

    public static char[] getChars (byte[] bytes) {
        Charset cs = Charset.forName ("UTF-8");
        ByteBuffer bb = ByteBuffer.allocate (bytes.length);
        bb.put (bytes);
        bb.flip ();
        CharBuffer cb = cs.decode (bb);
        for(int i= 0; i < cb.length(); i ++) {
            System.out.print(cb.get(i) + " ");
        }


        return cb.array();
    }

    public static void d(String msg) {
        Log.e("tag", msg);
    }
}
