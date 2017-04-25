package com.example.androiddevelopment.util;

import com.duzun.jiajunew.config.Config;
import com.socks.library.KLog;

/**
 * Created by asus on 2016/7/11.
 */
public class KLogUtils {
    public static void init() {
        KLog.init(Config.isDebug);
    }
}
