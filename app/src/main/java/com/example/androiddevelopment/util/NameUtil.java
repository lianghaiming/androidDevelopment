package com.example.androiddevelopment.util;

import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.Random;

/**
 * Created by xiaofei on 2015/12/19 17:36.
 */
public class NameUtil {

    private static Charset charset = Charset.forName("gb2312");
    protected final static int SCOPE = 0x9faf - 0x4e00 + 1;
    static Random random = new Random();

    public static String getRandName() {

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < 3; i++) {
            sb.append(nextCharacter());
        }
        return sb.toString();
    }

    private static char nextCharacter() {

        CharsetEncoder encoder = charset.newEncoder();
        while (true) {
            char c1 = (char) (random.nextInt(SCOPE) + 0x4e00);
            if (encoder.canEncode(c1)) {
                return c1;
            }
        }
    }

    public static String getNames() {
        String ret = "";
        for (int i = 0; i < 3; i++) {
            String str = null;
            int hightPos, lowPos; // 定义高低位
            Random random = new Random();
            hightPos = (176 + Math.abs(random.nextInt(39))); //获取高位值
            lowPos = (161 + Math.abs(random.nextInt(93))); //获取低位值
            byte[] b = new byte[2];
            b[0] = (new Integer(hightPos).byteValue());
            b[1] = (new Integer(lowPos).byteValue());
            try {
                str = new String(b, "GBk"); //转成中文
            } catch (UnsupportedEncodingException ex) {
                Log.i("TAG", ex.toString());
                //ex.printStackTrace();
            }
            ret += str;
        }
        return ret;
    }
}
