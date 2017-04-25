package com.example.androiddevelopment.util;

import android.text.TextUtils;
import android.util.Log;

import com.duzun.player.bean.LrcRow;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 默认的歌词解析器
 *
 * @author Ligang  2014/8/19
 */
public class DefaultLrcParser {
    private static final DefaultLrcParser istance = new DefaultLrcParser();

    public static final DefaultLrcParser getIstance() {
        return istance;
    }

    private DefaultLrcParser() {
    }

    /***
     * 将歌词文件里面的字符串 解析成一个List<LrcRow>
     */
    public ArrayList<LrcRow> getLrcRows(String str) {
        ArrayList<LrcRow> lrcRows = new ArrayList<>();
        if (TextUtils.isEmpty(str)) {
            return lrcRows;
        }
        BufferedReader br = new BufferedReader(new StringReader(str));
        String lrcLine;
        try {
            while ((lrcLine = br.readLine()) != null) {
                List<LrcRow> rows = LrcRow.createRows(lrcLine);
                if (rows != null && rows.size() > 0) {
                    lrcRows.addAll(rows);
                }
            }
            Collections.sort(lrcRows);

            for (int i = 0; i < lrcRows.size() - 1; i++) {
                lrcRows.get(i).setTotalTime(lrcRows.get(i + 1).getTime() - lrcRows.get(i).getTime());
            }

            int size = lrcRows.size() - 1;
            if (size >= 0) {
                lrcRows.get(size).setTotalTime(5000);
            }
        } catch (IOException e) {
            Log.i("TAG", e.toString());
            //e.printStackTrace();
            return lrcRows;
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    Log.i("TAG", e.toString());
                    //e.printStackTrace();
                }
            }
        }

        return lrcRows;
    }

}
