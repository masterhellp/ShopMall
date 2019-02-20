package com.wythe.mall.sqlite;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class CommonSQLiteOpenHelper extends SQLiteOpenHelper {

    public final static String DB_NAME = "qksx.db";
    // 更新时间记录表
    public final static String UPDATE_TIME_TABLE_NAME = "UpdateTimeTable";

    // weibo表表名
    public final static String WEIBO_TABLE_NAME = "WeiboDataTable";

    // key-value 缓存数据库
    public final static String KEY_VALUE_CACHE = "KeyVauleCacheTable";


    public final static int DB_VERSION = 1;
    private static final String TAG = "updateSqliteThread";
    // //////==========================
    private static Thread updateSqliteThread = null;
    private static List<Runnable> runnablesList = new ArrayList<Runnable>();


    private static CommonSQLiteOpenHelper mInstance;

    public CommonSQLiteOpenHelper(Context context, String dbName) {
        super(context, dbName, null, DB_VERSION);
    }

    // 删除数据库
    public static boolean deleteDatabase(Context context) {
        return context.deleteDatabase(DB_NAME);
    }

    public static void addRunnableToupdateSqliteByThread(Runnable runnable) {
        Log.i(TAG, "添加Runnable");
        runnablesList.add(runnable);
        if (null == updateSqliteThread) {
            updateSqliteThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    Log.i(TAG, "updateSqliteThread 开始执行");
                    // TODO Auto-generated method stub
                    while (runnablesList.size() > 0) {
                        Log.i(TAG, "开始执行runnable");
                        runnablesList.get(0).run();
                        Log.i(TAG, "runnable执行完毕，从list移除");
                        runnablesList.remove(0);
                    }
                    Log.i(TAG, "runnablesList执行完毕，销毁updateSqliteThread");
                    updateSqliteThread = null;
                }
            });
            updateSqliteThread.start();// 线程启动
        } else {
            Log.i(TAG, "updateSqliteThread已经启动。");
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 创建更新时间记录表,更新时间粒度精确到指定字段(如某表某字段数据的更新时间)
        StringBuilder SQL = new StringBuilder();
        SQL.append(" CREATE TABLE ");
        SQL.append(UPDATE_TIME_TABLE_NAME);
        SQL.append(" ( ");
        SQL.append(" TableName VARCHAR(100), ");
        SQL.append(" WitchLines VARCHAR(300) DEFAULT '', ");
        SQL.append(" UpdateDateTime TEXT NOT NULL, ");
        SQL.append(" totalCount INTEGER NOT NULL, ");// 查询获取的总数
        SQL.append(" PRIMARY KEY(TableName,WitchLines) ");
        SQL.append(" ) ");
        db.execSQL(SQL.toString());

        // 创建电视点播节目表 推荐页电视点播 推荐页网络视频
        SQL = new StringBuilder();
        SQL.append(" CREATE TABLE ");
        SQL.append(WEIBO_TABLE_NAME);
        SQL.append(" ( ");
        SQL.append(" weiboDataID VARCHAR(100) , ");
        SQL.append(" weiboID VARCHAR(100) , ");
        SQL.append(" belongName VARCHAR(100), ");
        SQL.append(" createdTime VARCHAR(200), ");
        SQL.append(" weiboText VARCHAR(600), ");
        SQL.append(" source VARCHAR(200), ");
        SQL.append(" thumbnailPic VARCHAR(100), ");
        SQL.append(" bmiddlePic VARCHAR(100), ");
        SQL.append(" offcialID VARCHAR(100), ");
        SQL.append(" followersCount VARCHAR(100), ");
        SQL.append(" profileImageURL VARCHAR(100)");
        SQL.append(" ) ");
        db.execSQL(SQL.toString());


        // 创建缓存数据库
        SQL = new StringBuilder();
        SQL.append(" CREATE TABLE ");
        SQL.append(KEY_VALUE_CACHE);
        SQL.append(" ( ");
        SQL.append(" cacheID INTEGER PRIMARY KEY, ");
        SQL.append(" keyString VARCHAR(100) , ");
        SQL.append(" valueString  NOT NULL, ");
        SQL.append(" totalCount VARCHAR(100), ");
        SQL.append(" lastUpdateTime VARCHAR(200), ");
        SQL.append(" pageNum VARCHAR(100) ");
        SQL.append(" ) ");
        db.execSQL(SQL.toString());

    }


    public static CommonSQLiteOpenHelper getInstance(Context context, String dbName) {
        if (mInstance == null) {
            synchronized (CommonSQLiteOpenHelper.class) {
                if (mInstance == null) {
                    mInstance = new CommonSQLiteOpenHelper(context,dbName);
                }
            }
        }
        return mInstance;
    }

    // 数据库版本升级时执行该方法
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }

}
