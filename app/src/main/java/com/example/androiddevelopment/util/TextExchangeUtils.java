package com.example.androiddevelopment.util;

import com.duzun.home.duzunhome.util.MyMD5;
import com.duzun.jiajunew.bean.db.Device;
import com.duzun.jiajunew.bean.db.User;

/**
 * Created by Administrator on 2016/5/9.
 * 将收到的设备数据转换为用户可以理解的数据
 */
public class TextExchangeUtils {
    //显示7681设备的数据
    public static String getDeviceData(String data, Device device) {
        String reData = data;
        if (data.length() == 10) {
            reData = "";
            for (int start = 0; start < 10; start += 2) {
                if (start == 0) {
                    if (Integer.valueOf(data.substring(start, start + 2), 16) == 0)
                        reData = reData + "温度: ";
                    else
                        reData = "温度: -" + reData;
                } else if (start == 2) {
                    reData = reData + Integer.valueOf(data.substring(start, start + 2), 16) + ".";
                } else if (start == 4) {
                    reData = reData + Integer.valueOf(data.substring(start, start + 2), 16) + "℃" + "\n";
                } else if (start == 6) {
                    reData = reData + "湿度: " + Integer.valueOf(data.substring(start, start + 2), 16) + ".";
                } else if (start == 8) {
                    reData = reData + Integer.valueOf(data.substring(start, start + 2), 16) + "%";
                }
            }
        } else if (data.length() == 4) {
            reData = "PM2.5: " + Integer.valueOf(data, 16) + "μg/m³";
        } else if (data.length() == 2) {
            if (!data.equals("ff")) {
                reData = data;//开关
                device.setDeviceOpenState(Integer.parseInt(data));
            } else {
                device.setDeviceOpenState(-1);//设置开关的打开关闭状态
            }
//            reData = Integer.valueOf(data, 16) + "";
        } else {
            device.setDeviceOpenState(-1);
        }
        return reData;
    }

    //显示云端返回7688设备列表
    public static String[] Cloud7688List(String count, String data) {
        String[] re = new String[Integer.valueOf(count)];
        byte[] in = MyMD5.hexStringToBytes(data);
        for (int i = 0; i < Integer.valueOf(count); i++) {
            byte[] infoByte = new byte[16];
            System.arraycopy(in, i * 16, infoByte, 0, 16);
            re[i] = new String(infoByte);
        }
        return re;
    }

    //显示云端返回7681设备列表,存为一个Device返回
    public static Device Cloud7681List(String[] dataC) {
        Device device = new Device();
        device.setDevId(dataC[2] + "");//设备id
        for (int i = 0; i < Integer.valueOf(dataC[3]); i++) {
            switch (dataC[4 + i].substring(0, 2)) {
                case "01"://mac地址
                    device.setMac(new String(MyMD5.hexStringToBytes(dataC[4 + i].substring(2, 28))) + "");
                    break;
                case "02"://设备id
                    break;
                case "03"://设备类型
//                    device.setDevType(new String(dataC[4 + i].substring(2, 6)) + "");
                    break;
                case "04"://设备型号
                    device.setDevNum(new String(dataC[4 + i].substring(2, 6)) + "");
                    break;
                case "05"://软件版本
                    device.setSwVer(new String(dataC[4 + i].substring(2, 6)) + "");
                    break;
                case "06"://硬件版本
                    device.setHwVer(new String(dataC[4 + i].substring(2, 6)) + "");
                    break;
                case "07"://状态
//                    device.setDevStatus(new String(dataC[4 + i].substring(5, 6)) + "");
                    break;
                case "08"://数据
//                    switch (device.getDevType()) {
////                        case Config.DEVICE_TEST:
////                            device.setDevData("0");
////                            break;
////                        case Config.DEVICE_SWITCH:
////                            device.setDevData(new String(dataC[4 + i].substring(2, 3)) + "");
////                            break;
////                        case Config.DEVICE_TEP:
////                            device.setDevData(new String(dataC[4 + i].substring(2, 12)) + "");
////                            break;
////                        case Config.DEVICE_PM:
////                            device.setDevData(new String(dataC[4 + i].substring(2, 6)) + "");
////                            break;
//                        default:
//                            device.setDevData("0");
//                            break;
//                    }
                    break;
            }
        }
        return device;
    }

    //显示云端返回用户列表,存为一个user返回
    public static User CloudUserList(String[] dataC) {
        User user = new User();
        user.setMac(dataC[5].substring(0, 12));
//        user.setNickName(new String(MyMD5.hexStringToBytes(dataC[5].substring(12, 76))).trim());
//        user.setAuthority(String.valueOf(MyMD5.ByteToInt(MyMD5.hexStringToBytes(dataC[5].substring(76, 78)))));
//        user.setUserStatus(String.valueOf(MyMD5.ByteToInt(MyMD5.hexStringToBytes(dataC[5].substring(78, 80)))));
        return user;
    }

    //日志查询日期转换成完整的日期
    public static String GetDate(int year, int month, int day) {
        String re = year + "";
        if (month < 10)
            re = re + "0" + month;
        else
            re = re + month;
        if (day < 10)
            re = re + "0" + day;
        else
            re = re + day;
        return re;
    }

    //云端日志查询日期转换成完整的日期
    public static String GetDateC(int year, int month, int day) {
        String re = year + "/";
        if (month < 10)
            re = re + "0" + month + "/";
        else
            re = re + month + "/";
        if (day < 10)
            re = re + "0" + day;
        else
            re = re + day;
        return re;
    }
}
