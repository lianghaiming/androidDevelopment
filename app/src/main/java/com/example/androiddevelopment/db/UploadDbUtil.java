package com.example.androiddevelopment.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.androiddevelopment.util.Utils;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by asus on 2015/11/30.
 */
public class UploadDbUtil {

    public static String TABLE_NAME = "upload";
    public static String SONGNAME = "songName";
    public static String SONGPATH = "songPath";
    public static String SONGAUTHOR= "songAuthor";

    private static UploadDbUtil dbUtil;
    private Context context;
    private DatabaseHelper database;
    private SQLiteDatabase mDatabase;//数据库实例
    private static final AtomicInteger mAtomicInteger=new AtomicInteger(0);
    private UploadDbUtil(Context context) {
        this.context = context;
        database = new DatabaseHelper(context);
    }
    private synchronized SQLiteDatabase openSQLiteDatabase(){
        if(mAtomicInteger.incrementAndGet()==1){
            mDatabase=database.getWritableDatabase();
        }
        return mDatabase;
    }
    public static UploadDbUtil getInstance(Context context) {
        if (dbUtil == null) {
            dbUtil = new UploadDbUtil(context);
        }
        return dbUtil;
    }

    public void insertUpload(String songName, String author, String songPath){

        Utils.log("插入数据库songName = "+songName);
        ContentValues cv = new ContentValues();
        cv.put(SONGNAME,songName);
        cv.put(SONGPATH,songPath);
        cv.put(SONGAUTHOR,author);
        openSQLiteDatabase().insert(TABLE_NAME,null,cv);

        closeDatabase();
    }

    public void deleteUpload(String songName){
        Utils.log("删除数据库的songName = " + songName);
        openSQLiteDatabase().delete(TABLE_NAME,"songName = ?",new String[]{songName});
        closeDatabase();
    }

    public boolean searchUpload(String songName){
        Cursor query = openSQLiteDatabase().query(TABLE_NAME, new String[]{"songName"}, "songName = ?",
                new String[]{songName}, null, null, null);

        if(query.moveToNext()){
            query.close();
            return true;
        }
        query.close();
        closeDatabase();
        return false;
    }

//    public ArrayList<LocalMusic> queryAll(){
//        Cursor query = openSQLiteDatabase().query(TABLE_NAME,null,null,
//                null, null, null, null);
//
//        ArrayList<LocalMusic> musicList = new ArrayList<>();
//        while (query.moveToNext()){
//
//            LocalMusic music = new LocalMusic();
//            music.setSongName(query.getString(query.getColumnIndex(SONGNAME)));
//            music.setUrl(query.getString(query.getColumnIndex(SONGPATH)));
//            music.setAuthor(query.getString(query.getColumnIndex(SONGAUTHOR)));
//            music.setUpload(true);
//            music.setProcess(100);
//            musicList.add(music);
//        }
//
//        query.close();
//        closeDatabase();
//        return musicList;
//    }
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
