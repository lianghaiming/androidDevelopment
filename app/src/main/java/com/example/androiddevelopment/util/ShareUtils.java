package com.example.androiddevelopment.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by 122 on 2016/3/25.
 */
public class ShareUtils {
    private static ShareUtils instance;
    private SharedPreferences sp;

    private ShareUtils(Context context, String name){
        sp=context.getSharedPreferences(name,Context.MODE_PRIVATE);
    }
    public static ShareUtils getInstance(Context context,String name){
        instance=new ShareUtils(context,name);
        return instance;
    }
    private SharedPreferences.Editor getEditor(){
        return sp.edit();
    }

    public void savePre(String key,String value){
        SharedPreferences.Editor edit=getEditor();
        edit.putString(key, value);
        edit.commit();
       // edit.clear();
        edit=null;
        sp=null;
    }
    public void deletePre(String key){
        SharedPreferences.Editor edit=getEditor();
        edit.remove(key);
        edit.commit();
        edit.clear();
        sp=null;
    }
    public String getFromShare(String key){
       return  sp.getString(key,"");
    }
    public  boolean getBoolean(String key){
        return sp.getBoolean(key,true);
    }

}
