package com.example.androiddevelopment.model;

import com.example.androiddevelopment.greendao.bean.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/5/6.
 */
public class UserListResponse {

    public int code;

    public List<User> data;

    public UserListResponse() {

    }

    @Override
    public String toString() {
        return "code " + code + data.toString();
    }

    public static class UserData {
        public List<User> user;

        public UserData() {
            user = new ArrayList<>();
        }

        @Override
        public String toString() {
            StringBuffer buffer = new StringBuffer();
            for (User u : user) {
                buffer.append(u.toString());
            }
            return buffer.toString();
        }
    }


}
