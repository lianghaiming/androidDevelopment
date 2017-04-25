package com.example.androiddevelopment.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.duzun.player.utils.Utils;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by asus on 2015/10/29.
 */
public class FavorDbUtil {

    public static final String TABLENAME = "favor";
    public static final String FAVOR_ID = "favor_id";
    //帐号，之前是昵称
    public static final String USERNAME = "username";
    public static final String SONGNAME = "songname";
    public static final String LISTNAME = "listname";
    public static final String ADDTIME = "addTime";

    private static DatabaseHelper database;
    private Context context;
    private static FavorDbUtil favorDbUtil;
    private SQLiteDatabase mDatabase;//数据库实例
    private static final AtomicInteger mAtomicInteger=new AtomicInteger(0);
    private FavorDbUtil(Context context) {
        this.context = context;
    }

    public static synchronized FavorDbUtil getInstance(Context context){

        if(favorDbUtil == null){
            favorDbUtil = new FavorDbUtil(context);
            database=new DatabaseHelper(context);
        }
        return favorDbUtil;
    }
    private synchronized SQLiteDatabase openSQLiteDatabase(){
        if(mAtomicInteger.incrementAndGet()==1){
            mDatabase=database.getWritableDatabase();
        }
        return mDatabase;
    }
    /**
     * 插入喜爱歌曲
     * @param userName
     * @param songName
     */
    public void insertFavor(String userName,String songName){
        ContentValues cv = new ContentValues();
        cv.put(USERNAME,userName);
        cv.put(SONGNAME,songName);
        long id=openSQLiteDatabase().insert(TABLENAME, null, cv);
        if(id!=-1){
//            Utils.log("成功插入收藏歌曲到数据库中");
        }
        closeDatabase();
    }

    /**
     * 查找用户收藏的歌曲
     * @param userName
     * @return
     */
    public ArrayList<String> queryAll(String userName){

        ArrayList<String> nameList = new ArrayList<String>();
//        Utils.log("当前登陆的用户：" + userName);
        Cursor cursor = openSQLiteDatabase().query(TABLENAME, new String[]{SONGNAME}, USERNAME + " = ?",
                new String[]{userName}, null, null, null);
        while ((!cursor.isClosed())&&cursor.moveToNext()){
            int index = cursor.getColumnIndex(SONGNAME);
            String name = cursor.getString(index);
            Utils.log("收藏歌曲名 = "+name);
            nameList.add(name);
        }
        closeDatabase();
        return nameList;
    }

    /**
     * 删除某条记录
     * @param userName
     * @param songName
     */
    public void deleteMusic(String userName,String songName){
        int id=openSQLiteDatabase().delete(TABLENAME,SONGNAME+" = ? and "+USERNAME+" = ?",new String[]{songName,userName});
        if(id!=0)
//            Utils.log("删除收藏记录成功！");
        closeDatabase();
    }

    /**
     * 关闭数据库
     */
    public synchronized void closeDatabase() {
        if(mAtomicInteger.decrementAndGet() == 0) {
            // Closing database
            database.close();
        }
    }
}
