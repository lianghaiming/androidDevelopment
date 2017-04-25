package com.example.androiddevelopment.util;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;

import com.duzun.indexrecyclerview.ItemModel;
import com.duzun.player.R;
import com.duzun.player.bean.LocalMusic;
import com.duzun.player.bean.Music;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * 查找本地歌曲
 * Created by asus on 2015/9/29.
 */
public class LocalMusicUtil {
    private static Uri contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
    private static String sortOrder = MediaStore.Audio.Media.DEFAULT_SORT_ORDER;
    public static final String MUSIC_ONLY_SELECTION = MediaStore.Audio.AudioColumns.IS_MUSIC + "=1"
            + " AND " + MediaStore.Audio.AudioColumns.TITLE + " != ''";
    private static final Uri albumArtUri = Uri
            .parse ("content://media/external/audio/albumart");
    public static ArrayList<ItemModel> musicLists;

    /**
     * 根据歌曲名找到本地音乐
     *
     * @param songName
     * @return
     */
//    public static LocalMusic getLocalMusic(String songName) {
//        for (int i = 0; i < musicLists.size(); i++) {
//            LocalMusic music = musicLists.get(i);
//            if (music.getSongName().equals(songName)) {
//                //如果是从服务器上下载的歌曲，要先解密，然后在进行解码
//                if (music.isFromServer()) {
//                    Utils.log("解密路径 = " + music.getUrl());
//                    File file = decodeMusic(music.getUrl());
//                    music.setUrl(file.getAbsolutePath());
//                }
//                return music;
//            }
//        }
//        return null;
//    }
    public static String getLocalMusicPath (String songName) {
        if (songName.contains ("_-_")) {
            String replace = songName.replace ("_-_", "#");
            String[] split = replace.split ("#");
            songName = split[1];
        }
        for (int i = 0; i < musicLists.size (); i++) {
            LocalMusic music = (LocalMusic) musicLists.get (i);
            String musicName = music.getSongName ();
            musicName = musicName.replace ("[", "");
            musicName = musicName.replace ("]", "");
            musicName = musicName.replace ("/", "");
            musicName = musicName.replace ("'", "");
            //Utils.log("getLocalMusicPath: " + music.getSongName());
            if (musicName.equals (songName)) {
                //如果是从服务器上下载的歌曲，要先解密，然后在进行解码
                if (music.isFromServer ()) {
                    Utils.log ("是从服务器上下载的歌曲，先解密，再解码");
                    String dianbo = decodeMusic ("dianbo", music.getUrl ());
                    Utils.log ("解密完成 " + dianbo);
                    return dianbo;
                }
                return music.getUrl ();
            }
        }
        return "";
    }

    public static final int SCANEROK = 7954;
    public static final int SCANERPARTOK = 7955;

    public static ArrayList<ItemModel> getLocalMusics (Context context, Handler mHandler, HashMap<String, String> lastPaths) {

        musicLists = new ArrayList<> ();
        context.sendBroadcast (new Intent (Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                Uri.parse ("file://" + Environment.getExternalStorageDirectory ().getAbsolutePath ())));

        getMusicFromUri (context, musicLists, lastPaths, contentUri, mHandler);

        // 扫描指定文件的歌曲
        ArrayList<Music> downloadList = FileUtils.getDownloadList ();
        for (int i = 0; i < downloadList.size (); i++) {
            Music music = downloadList.get (i);
            LocalMusic localMusic = new LocalMusic ();
            String songName1 = music.getSongName ();
            String songName = songName1.replace (".duzunmusic", "");

            localMusic.setSongName (songName);
            localMusic.setName (songName);
            localMusic.setAllTime (music.getMusicTime ());
            localMusic.setUrl (music.getPath ());
            localMusic.setAlbumImageName (R.drawable.music_item_icon);
            localMusic.setAuthor ("来自于服务器");
            localMusic.setFromServer (true);

            musicLists.add (localMusic);
        }
        Message msg = new Message ();
        msg.what = SCANEROK;
        msg.obj = musicLists;
        mHandler.sendMessage (msg);
        return musicLists;
    }

    private static void getMusicFromUri (Context context, ArrayList<ItemModel> mp3Infos, HashMap<String, String> lastPaths,
                                         Uri contentUri, Handler mHandler) {
        //TODO MUSIC_ONLY_SELECTION CHANGE NULL,这个选择条件有错，导致有些手机没办法获取歌曲
        Cursor cursor = context.getContentResolver ().query (contentUri, null,
                null, null, sortOrder);
//        if (cursor == null) {
//
//        } else if (!cursor.moveToFirst ()) {
//
//        } else {
        Utils.log ("LocalMusicUtils  开始查找本地音乐");
        for(cursor.moveToFirst ();! cursor.isAfterLast ();cursor.moveToNext ()){
            int displayNameCol = cursor.getColumnIndexOrThrow (MediaStore.Audio.Media.TITLE);
            int durationCol = cursor.getColumnIndexOrThrow (MediaStore.Audio.Media.DURATION);
            int artistID = cursor.getColumnIndexOrThrow (MediaStore.Audio.Media.ARTIST);
            int album = cursor.getColumnIndexOrThrow (MediaStore.Audio.Media.ALBUM);

            int urlCol = cursor.getColumnIndexOrThrow (MediaStore.Audio.Media.DATA);
//            do {

                int duration = cursor.getInt (durationCol);
                String url = cursor.getString (urlCol);
                Utils.log("LocalMusicUtil  本地歌曲路径为："+url);
                String title = url.substring (url.lastIndexOf ("/") + 1);
                String lastPath = url.substring (0, url.lastIndexOf ("/"));
                lastPaths.put (lastPath, lastPath);
//                if(title.equals(""))
//                    title=url.substring(url.lastIndexOf("/") + 1);
                //Utils.log("需要添加的歌曲名为：" + musicName);
                String artist = cursor.getString (artistID);
                long id = cursor.getLong (cursor
                        .getColumnIndex (MediaStore.Audio.Media._ID));
                long albumId = cursor.getInt (cursor
                        .getColumnIndex (MediaStore.Audio.Media.ALBUM_ID));

                String albumName = cursor.getString (album);

                artist = artist.replace ("<", "");
                artist = artist.replace (">", "");

                File file = new File (url);
                if (url != null && file.exists ()) {

                    LocalMusic music = new LocalMusic ();
//                    if (title.contains("'")||title.contains("[")||title.contains("]")||title.contains("#")) {
//                       continue;
//                    }
//                    Bitmap icon = getArtworkFromFile(context, id, albumId);
                    Object uri = getArtworkFromFile (context, id, albumId);
                    // Utils.log("歌曲路径为："+url+"定义的歌曲题目为："+title);
                    music.setSongName (title);
                    music.setName (title);
                    music.setAlbumImageName (uri);
//                    music.setAlbumImage(icon);
                    music.setAlbumName (albumName);
                    music.setAuthor (artist);
                    music.setAllTime (Utils.longToStringTime (duration));
                    music.setUrl (url);
                    if (!mp3Infos.contains (music)) {   //如果已经有了同名和同时长的歌曲，视为同一首歌
                        mp3Infos.add (music);

                        if (mp3Infos.size () % 10 == 0) {
                            Message msg = new Message ();
                            msg.what = SCANERPARTOK;
                            msg.obj = mp3Infos;
                            mHandler.sendMessage (msg);
                        }
                    }
                }
//            } while (cursor.moveToNext ());

        }
        if (cursor != null) {
            cursor.close ();
        }
    }

    private static Object getArtworkFromFile (Context context, long songid,
                                              long albumid) {
        Uri uri = null;
        if (albumid < 0) {
            uri = Uri.parse ("content://media/external/audio/media/"
                    + songid + "/albumart");
        } else {
            uri = ContentUris.withAppendedId (albumArtUri, albumid);
        }
        if (uri == null) {
            return R.drawable.music_item_icon;
        }
        return uri;
    }

//    private static Bitmap getArtworkFromFile(Context context, long songid,
//                                             long albumid) {
//        Bitmap bm = null;
//        if (albumid < 0 && songid < 0) {
//            throw new IllegalArgumentException(
//                    "Must specify an album or a song id");
//        }
//        try {
//            BitmapFactory.Options options = new BitmapFactory.Options();
//            FileDescriptor fd = null;
//            if (albumid < 0) {
//                Uri uri = Uri.parse("content://media/external/audio/media/"
//                        + songid + "/albumart");
//                ParcelFileDescriptor pfd = context.getContentResolver()
//                        .openFileDescriptor(uri, "r");
//                if (pfd != null) {
//                    fd = pfd.getFileDescriptor();
//                }
//            } else {
//                Uri uri = ContentUris.withAppendedId(albumArtUri, albumid);
//                ParcelFileDescriptor pfd = context.getContentResolver()
//                        .openFileDescriptor(uri, "r");
//                if (pfd != null) {
//                    fd = pfd.getFileDescriptor();
//                }
//            }
//            options.inSampleSize = 1;
//            // 只进行大小判断
//            options.inJustDecodeBounds = true;
//            // 调用此方法得到options得到图片大小
//            BitmapFactory.decodeFileDescriptor(fd, null, options);
//            // 所以需要调用computeSampleSize得到图片缩放的比例
//            options.inSampleSize = computeSampleSize(options, 40);
//            // 我们得到了缩放的比例，现在开始正式读入Bitmap数据
//            options.inJustDecodeBounds = false;
//            options.inDither = false;
//            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
//
//            // 根据options参数，减少所需要的内存
//            bm = BitmapFactory.decodeFileDescriptor(fd, null, options);
//        } catch (FileNotFoundException e) {
//            bm = BitmapFactory.decodeResource(context.getResources(),
//                    R.drawable.music_item_icon);
//        }
//        return bm;
//    }

    public static int computeSampleSize (BitmapFactory.Options options, int target) {
        int w = options.outWidth;
        int h = options.outHeight;
        int candidateW = w / target;
        int candidateH = h / target;
        int candidate = Math.max (candidateW, candidateH);
        if (candidate == 0) {
            return 1;
        }
        if (candidate > 1) {
            if ((w > target) && (w / candidate) < target) {
                candidate -= 1;
            }
        }
        if (candidate > 1) {
            if ((h > target) && (h / candidate) < target) {
                candidate -= 1;
            }
        }
        return candidate;
    }

    /**
     * 解密从服务器下载的歌曲
     *
     * @param path 歌曲的路径
     * @return
     */
    public static String decodeMusic (String decodeName, String path) {
        String decodePath = FileUtils.getPhoneDirectory () + "/com/duzun/player/decode";
        File dir = new File (decodePath);
        if (!dir.exists ()) {
            dir.mkdirs ();
        }
//        File outPutFile = new File(dir, decodeName+".mp3");
//        if (!outPutFile.exists()) {
////            boolean delete = outPutFile.delete();
////            Utils.log("删除解密文件结果 = "+delete);
//            try {
//                boolean newFile = outPutFile.createNewFile();
//                Utils.log("创建文件结果 = "+newFile);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
        byte[] buffer = new byte[1024];
        int read = -1;
        FileInputStream fis = null;
        FileOutputStream fos = null;
        String outPath = decodePath + "/" + decodeName;
        File outFile = new File (outPath);
        try {
            fis = new FileInputStream (new File (path));
            fos = new FileOutputStream (outFile);
            int count = 0;
            while ((read = fis.read (buffer, 0, buffer.length)) != -1) {
                // 只对前1M的数据进行解密
                for (int i = 0; i < read; i++) {
                    if (count++ >= 1024 * 1024)
                        break;
                    buffer[i] ^= 0xFB;
                }
                fos.write (buffer, 0, read);
                fos.flush ();
            }
        } catch (Exception e) {
            //e.printStackTrace();
            Log.i ("TAG", e.toString ());
            return "";
        } finally {
            try {
                if (fis != null) {
                    fis.close ();
                }
                if (fos != null) {
                    fos.close ();
                }
            } catch (IOException e) {
                Log.i ("TAG", e.toString ());
                //e.printStackTrace();
            }
        }
        return outPath;
    }
}
