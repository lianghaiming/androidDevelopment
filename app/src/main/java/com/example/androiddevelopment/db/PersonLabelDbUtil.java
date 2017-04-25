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
public class PersonLabelDbUtil {

    public static final String TABLENAME = "personLabel";
    public static final String LABLE_ID = "label_id";
    //帐号，之前是昵称
    public static final String USERNAME = "username";
    public static final String PERSONLABEL = "personLabel";

    private DatabaseHelper database=null;
    private Context context;
    private SQLiteDatabase mDatabase;//数据库实例
    private static PersonLabelDbUtil personLableDbUtil;
    private static final AtomicInteger mAtomicInteger=new AtomicInteger(0);
    private PersonLabelDbUtil(Context context) {
        this.context = context;
        database = new DatabaseHelper(context);
    }

    public static PersonLabelDbUtil getInstance(Context context){

        if(personLableDbUtil == null){
            personLableDbUtil = new PersonLabelDbUtil(context);
        }
        return personLableDbUtil;
    }
    private synchronized SQLiteDatabase openSQLiteDatabase(){
        if(mAtomicInteger.incrementAndGet()==1){
            mDatabase=database.getReadableDatabase();
        }
        return mDatabase;
    }
    /**
     * 插入喜爱歌曲
     * @param userName
     * @param label
     */
    public void insertFavor(String userName,String label){
        ContentValues cv = new ContentValues();
        cv.put(USERNAME,userName);
        cv.put(PERSONLABEL,label);
        long id=-1;
        try {
            id = openSQLiteDatabase().insert(TABLENAME, null, cv);
        }catch(NullPointerException e){
            Utils.showToast(context,"打开数据库出错！");
        }
        if(id!=-1){
            Utils.log("成功插入收藏歌曲到数据库中");
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
        Utils.log("当前登陆的用户：" + userName);
        Cursor cursor = openSQLiteDatabase().query(TABLENAME, new String[]{PERSONLABEL}, USERNAME + " = ?",
                new String[]{userName}, null, null, null);
        while ((!cursor.isClosed())&&cursor.moveToNext()){
            int index = cursor.getColumnIndex(PERSONLABEL);
            String name = cursor.getString(index);
            Utils.log("收藏标签名 = " + name);
            nameList.add(name);
        }
        cursor.close();
        closeDatabase();
        return nameList;
    }

    /**
     * 删除某条记录
     * @param userName
     * @param labelName
     */
    public void deleteMusic(String userName,String labelName){

        int raw=openSQLiteDatabase().delete(TABLENAME,PERSONLABEL+" = ? and "+USERNAME+" = ?",new String[]{labelName,userName});
        if(raw!=0){
            Utils.log("删除用户:" + userName + "的个人标签：" + labelName + "成功！");
        }
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
