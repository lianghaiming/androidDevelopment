package com.example.androiddevelopment.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.duzun.player.bean.LocalMusic;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by asus on 2015/10/29.
 */
public class LocalMusicDb {

    public static final String TABLENAME = "localMusic";
    public static final String MUSIC_ID = "music_id";
    public static final String SONGNAME = "songName";
    public static final String SINGER = "singer";
    public static final String ALBUM = "album";
    public static final String TIME = "time";
    public static final String URL = "url";
    public static final String MUSICICON = "musicIcon";
    public static final String ISFROMSERVER = "isFromServer";

    private DatabaseHelper database;
    private Context context;
    private static LocalMusicDb musicDbUtil;
    private SQLiteDatabase mDatabase;//数据库实例
    private static final AtomicInteger mAtomicInteger=new AtomicInteger(0);
    private LocalMusicDb(Context context) {
        this.context = context;
        database = new DatabaseHelper(context);
    }

    public static LocalMusicDb getInstance(Context context){

        if(musicDbUtil == null){
            musicDbUtil = new LocalMusicDb(context);
        }
        return musicDbUtil;
    }
    private synchronized SQLiteDatabase openSQLiteDatabase(){
        if(mAtomicInteger.incrementAndGet()==1){
            mDatabase=database.getWritableDatabase();
        }
        return mDatabase;
    }
    /**
     * 插入本地歌曲
     * @param songName
     */
    public void insertLocalMusic(String songName,String singer,String album,String time,
                                 String url,String musicIcon,int isFromServer){

        ContentValues cv = new ContentValues();
        cv.put(SINGER,singer);
        cv.put(SONGNAME,songName);
        cv.put(ALBUM,album);
        cv.put(TIME,time);
        cv.put(URL,url);
        cv.put(MUSICICON,musicIcon);
        cv.put(ISFROMSERVER,isFromServer);

        openSQLiteDatabase().insert(TABLENAME,null,cv);

        closeDatabase();
    }

    /**
     * 查找本地歌曲
     * @return
     */
    public ArrayList<LocalMusic> queryAll(){

        ArrayList<LocalMusic> musicList = new ArrayList<>();

        Cursor cursor = openSQLiteDatabase().query(TABLENAME, null, null,
                null, null, null, null);

        while (cursor.moveToNext()){
            int songNameIndex = cursor.getColumnIndex(SONGNAME);
            int singerIndex = cursor.getColumnIndex(SINGER);
            int albumIndex = cursor.getColumnIndex(ALBUM);
            int timeIndex = cursor.getColumnIndex(TIME);
            int urlIndex = cursor.getColumnIndex(URL);
            int musicIconIndex = cursor.getColumnIndex(MUSICICON);
            int isFromServerIndex = cursor.getColumnIndex(ISFROMSERVER);

            String songName = cursor.getString(songNameIndex);
            String singer = cursor.getString(singerIndex);
            String album = cursor.getString(albumIndex);
            String time = cursor.getString(timeIndex);
            String url = cursor.getString(urlIndex);
            int isFromServer = cursor.getInt(isFromServerIndex);
            byte[] icon = cursor.getBlob(musicIconIndex);

            LocalMusic music = new LocalMusic();
            music.setSongName(songName);
            music.setUrl(url);
            music.setAlbumName(album);
            music.setAuthor(singer);
            music.setAllTime(time);

            Bitmap bitmap = BitmapFactory.decodeByteArray(icon, 0, icon.length);
//            music.setAlbumImage(bitmap);

            if(isFromServer==0){
                music.setFromServer(false);
            }else {
                music.setFromServer(true);
            }

            musicList.add(music);
        }
        cursor.close();
        closeDatabase();
        return musicList;
    }

    /**
     * 删除某条记录
     * @param url   删除的歌曲的路径
     */
    public void deleteMusic(String url){

        openSQLiteDatabase().delete(TABLENAME, URL + " = ? ", new String[]{url});

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
