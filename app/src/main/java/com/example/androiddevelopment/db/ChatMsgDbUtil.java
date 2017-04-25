package com.example.androiddevelopment.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.duzun.player.bean.ChatMsg;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by asus on 2015/10/20.
 */
public class ChatMsgDbUtil {

    public static final String SENDMAC = "sendMac";
    public static final String RECEIVERMAC = "receiverMac";
    public static final String CONTENT = "content";
    public static final String TIME = "time";
    public static final String READ = "read";
    public static final String ISCOME = "isCome";

    private static DatabaseHelper database;
    private Context context;
    private static ChatMsgDbUtil chatMsgDbUtil;
    private AtomicInteger mAtomicInteger = new AtomicInteger();
    private SQLiteDatabase mDatabase;

    private ChatMsgDbUtil(Context context) {
        this.context = context;
    }

    public static synchronized ChatMsgDbUtil getInstance(Context context) {

        if (chatMsgDbUtil == null) {
            chatMsgDbUtil = new ChatMsgDbUtil(context);
            database = new DatabaseHelper(context);
        }
        return chatMsgDbUtil;
    }

    private synchronized SQLiteDatabase openSQLiteDatabase() {

        if (mAtomicInteger.incrementAndGet() == 1) {
            mDatabase = database.getWritableDatabase();
        }
        return mDatabase;
    }

    /**
     * 插入语句
     *
     * @param chatMsg
     */
    public void insert(ChatMsg chatMsg) {
        ContentValues cv = new ContentValues();
        cv.put(SENDMAC, chatMsg.getSendMac());
        cv.put(RECEIVERMAC, chatMsg.getReceiverMac());
        cv.put(CONTENT, chatMsg.getText());
        cv.put(TIME, chatMsg.getDate());
        boolean msgType = chatMsg.getMsgType();
        String isCome = "";
        if (msgType) {
            isCome = "1";
        } else {
            isCome = "0";
        }
        String read = "";
        boolean isread = chatMsg.isread();
        if (isread) {
            read = "1";
        } else {
            read = "0";
        }
        cv.put(READ, read);
        cv.put(ISCOME, isCome);
        openSQLiteDatabase().insert("msg", null, cv);
        closeDatabase();
    }

    /**
     * 查询出所有未读的消息
     */
    public ArrayList<ChatMsg> queryUnReadMsg(String receiverMac) {
        ArrayList<ChatMsg> msgList = new ArrayList<ChatMsg>();
        if (receiverMac == null) {
            return msgList;
        }

        Cursor query = openSQLiteDatabase().query("msg", new String[]{"id", SENDMAC, CONTENT, TIME},
                "read =? and receiverMac = ?", new String[]{"0", receiverMac}, null, null, null);
        while (query.moveToNext()) {

            int contentIndex = query.getColumnIndex(CONTENT);
            int idIndex = query.getColumnIndex("id");
            int sendMacIndex = query.getColumnIndex(SENDMAC);
            int timeIndex = query.getColumnIndex(TIME);
            String sendMac = query.getString(sendMacIndex);
            String content = query.getString(contentIndex);
            String time = query.getString(timeIndex);
            int id = query.getInt(idIndex);

            ChatMsg msg = new ChatMsg();
            msg.setSendMac(sendMac);
            msg.setDate(time);
            msg.setText(content);
            msg.setIsread(false);
            msg.setMsgType(true);
            msg.setId(id);

            msgList.add(msg);
        }
        query.close();
        closeDatabase();
        return msgList;
    }

    /**
     * 查询历史记录
     */
    public ArrayList<ChatMsg> queryHostory(String sendMac, String index) {
        Cursor query = openSQLiteDatabase().query("msg", new String[]{CONTENT, TIME, READ, ISCOME},
                "sendMac = ? or receiverMac = ?", new String[]{sendMac, sendMac}, null, null, "id DESC", index + ",10");
        ArrayList<ChatMsg> msgList = new ArrayList<ChatMsg>();
        while (query.moveToNext()) {
            int readIndex = query.getColumnIndex(READ);
            int contentIndex = query.getColumnIndex(CONTENT);
            int timeIndex = query.getColumnIndex(TIME);
            int isComeIndex = query.getColumnIndex(ISCOME);
            String content = query.getString(contentIndex);
            String time = query.getString(timeIndex);
            String read = query.getString(readIndex);
            String isCome = query.getString(isComeIndex);

            boolean isRead = false;
            if (read.equals("0")) {
                isRead = false;
            } else {
                isRead = true;
            }

            boolean isComing = false;

            if (isCome.equals("0")) {
                isComing = false;
            } else {
                isComing = true;
            }

            ChatMsg msg = new ChatMsg();
            msg.setSendMac(sendMac);
            msg.setDate(time);
            msg.setText(content);
            msg.setIsread(isRead);
            msg.setMsgType(isComing);

            msgList.add(msg);
        }
        query.close();
        closeDatabase();
        Collections.reverse(msgList);
        return msgList;
    }

    /**
     * 返回记录总数
     *
     * @param sendMac
     * @return
     */
    public long queryCount(String sendMac) {
        String sql = "select " + CONTENT + "," + TIME + "," + READ + "," + ISCOME + " from msg where sendMac = ? or receiverMac = ?";

        String sql1 = "select count(*) from (" + sql + ")";

        Cursor cursor = openSQLiteDatabase().rawQuery(sql1, new String[]{sendMac, sendMac});
        cursor.moveToFirst();
        long count = cursor.getLong(0);
        cursor.close();
       closeDatabase();
        return count;
    }

    public void updateRead(String sendMac) {
        ContentValues cv = new ContentValues();
        cv.put(READ, "1");
        openSQLiteDatabase().update("msg", cv, "sendMac = ?", new String[]{sendMac});
        closeDatabase();
    }

    /**
     * 关闭数据库
     */
    public synchronized void closeDatabase() {
        if (mAtomicInteger.decrementAndGet() == 0) {
            // Closing database
            database.close();
        }
    }
}
