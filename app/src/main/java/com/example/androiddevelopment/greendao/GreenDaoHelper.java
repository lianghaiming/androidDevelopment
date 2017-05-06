package com.example.androiddevelopment.greendao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by dear33 on 2016/7/4.
 */
public class GreenDaoHelper {
    private static final String GREEN_DB_NAME = "lonely_db";

    private static GreenDaoHelper instance = null;
    private DaoSession daoSession;
    private SQLiteDatabase db;

    public static GreenDaoHelper getInstance(Context context) {
        if (instance == null) {
            synchronized (GreenDaoHelper.class) {
                if (instance == null) {
                    instance = new GreenDaoHelper(context);
                }
            }
        }
        return instance;
    }

    private GreenDaoHelper() {

    }

    private GreenDaoHelper(final Context context) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(context, GREEN_DB_NAME, null);
                db = devOpenHelper.getWritableDatabase();
                DaoMaster daoMaster = new DaoMaster(db);
                daoSession = daoMaster.newSession();
            }
        }).start();
    }

    public DaoSession getSession() {
        return daoSession;
    }
}
