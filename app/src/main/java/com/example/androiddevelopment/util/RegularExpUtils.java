package com.example.androiddevelopment.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by huxiaoliu on 2015/11/10.
 * <p>
 * 正则表达式判断工具类
 */
public class RegularExpUtils {


    /**
     * Judge if the string is email address
     *
     * @param mail The email address string
     * @return True if the
     */
    public static boolean checkMail(String mail) {
        boolean flag;
        try {
            String check = "^[\\w-]+(\\.[\\w-]+)*@[\\w-]+(\\.[\\w-]+)+$";
            Pattern regex = Pattern.compile(check);
            Matcher matcher = regex.matcher(mail);
            flag = matcher.matches();
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }

    /**
     * Judge if the string is mobile number
     *
     * @param mobile The mobile number string
     * @return True if the string is mobile number,false otherwise.
     */
    public static boolean checkMobile(String mobile) {
        return mobile.matches("^[1][2,3,4,5,7,8]+\\d{9}");
    }


    /**
     * Judge if the string is 18 ID Card number
     *
     * @param idCard The 18 ID card number string
     * @return True if the string is the 18 IDCard,false otherwise.
     */
    public static boolean checkIdCard(String idCard) {
        boolean flag;
        try {
            String check = "^(\\d{6})(\\d{4})(\\d{2})(\\d{2})(\\d{3})([0-9]|X)$";
            Pattern regex = Pattern.compile(check);
            Matcher matcher = regex.matcher(idCard);
            flag = matcher.matches();
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }


}