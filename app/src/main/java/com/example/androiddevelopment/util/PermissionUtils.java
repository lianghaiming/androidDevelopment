package com.example.androiddevelopment.util;

import com.duzun.jiajunew.config.Config;

/**
 * Created by asus on 2016/6/15.
 */
public class PermissionUtils {
    /**
     * 用户权限判读
     */
    public static String userPri(int pri) {
        String userPriStr = "";
        switch (pri) {
            case Config.PRIO_DENY:
                userPriStr = "无任何权限";
                break;
            case Config.PRIO_READ_ONLY:
                userPriStr = "普通权限";
                break;
            case Config.PRIO_LOCAL_CTRL:
                userPriStr = "本地控制权限";
                break;
            case Config.PRIO_REMOTE_CTRL:
                userPriStr = "云端和本地控制权限";
                break;
            case Config.PRIO_ADMIN:
                userPriStr = "管理员";
                break;
        }
        return userPriStr;
    }
}
