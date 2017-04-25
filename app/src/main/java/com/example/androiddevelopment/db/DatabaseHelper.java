package com.example.androiddevelopment.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by asus on 2015/10/20.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String db_name = "msg_db";

    public DatabaseHelper(Context context) {
        super(context, db_name, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String msgSql = "create table favor(favor_id integer not null primary key autoincrement," +
                "username varchar(40) not null," +
                "songname varchar(40) not null," +
                "listname varchar(40)," +
                "addTime varchar(30))";
        db.execSQL(msgSql);

        String sql = "create table msg(id integer  not null primary key autoincrement," +
                "sendMac varchar(40) not null," +
                "receiverMac varchar(40) not null," +
                "content varchar(500) not null," +
                "time varchar(30) not null," +
                "read varchar(2) not null," +
                "isCome varchar(2) not null)";
        db.execSQL(sql);

        String uploadSql = "create table upload(id integer  not null primary key autoincrement," +
                "songName text not null," +
                "songAuthor text,"+
                "songPath text not null)";
        db.execSQL(uploadSql);

        String localMusicSql = "create table localMusic(music_id integer not null primary key autoincrement," +
                "songName text not null," +
                "singer text," +
                "album text," +
                "time text," +
                "url text not null," +
                "musicIcon blob," +
                "isFromServer integer)";   //0  代表来自于本地，1代表从服务器上下载
        db.execSQL(localMusicSql);

        String personLabelsql = "create table personLabel(label_id integer not null primary key autoincrement," +
                "userName text not null," +
                "personLabel text," +
                "isFromServer integer)";   //0  代表来自于本地，1代表从服务器上下载
        db.execSQL(personLabelsql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
