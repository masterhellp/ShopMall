package com.wythe.mall.sqlite;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.wythe.mall.utils.GsonUtils;
import com.wythe.mall.utils.PrintLog;
import com.wythe.mall.utils.TimeZoneUtil;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by ningwang on 15/11/23.
 */
public class KVDatabase {

    /**
     * 初始化数据库，仅用于在AppDelegate调用一次
     *
     * @param version 当前客户端的版本号
     */
    public static void ininitDatabase(String db, String version) {

    }


    /**
     * 将数组数据存储到数据库
     *
     * @param list       Entity的数组
     * @param totalCount 总数，会比array.count要多
     * @param key        存储所用的Key值
     */
    public static void putDataList(Context context, List list, int totalCount, String key,int pageNum) {


        String listToJSONString = GsonUtils.toJsonString(list);
//        PrintLog.printError("KVDatabase:", listToJSONString);
//        listToJSONString = listToJSONString.replace("[","(");
//        listToJSONString = listToJSONString.replace("]",")");
        //拼装sql 存入到key-value数据库
        String sql = "insert into " + CommonSQLiteOpenHelper.KEY_VALUE_CACHE + " (keyString,valueString,totalCount,lastUpdateTime,pageNum) values ('" +
                key + "','" + listToJSONString + "','" + totalCount + "','" + new Date().getTime() + "','"  + pageNum + "')";

        //PrintLog.printError("insertSQL的列表条数:", list.size() + "条");

        CommonSQLiteOpenHelper util = new CommonSQLiteOpenHelper(
                context, CommonSQLiteOpenHelper.DB_NAME);
        SQLiteDatabase db = util.getReadableDatabase();
        db.execSQL(sql);

        db.close();
        util.close();
    }


    /**
     * 使用key值，获取数据库里的数组
     *
     * @param key Key值
     */
    public static void getDataListByKey(Context context, String key,int pageNum, Class aClass,Type mType,DataBaseListCallBack dataBaseListCallBack) {

        String valueString = "";
        String lastUpdateTime = "";
        int totalCount = 0;
        List dataList;
        CommonSQLiteOpenHelper util = null;
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            util = new CommonSQLiteOpenHelper(
                    context, CommonSQLiteOpenHelper.DB_NAME);
            db = util.getReadableDatabase();

            cursor = db.query(CommonSQLiteOpenHelper.KEY_VALUE_CACHE, new String[]{"keyString",
                    "valueString", "totalCount", "lastUpdateTime"}, "keyString=? and pageNum=?", new String[]{key,pageNum+""}, null, null, null);
            while (cursor.moveToNext()) {
                valueString = cursor.getString(cursor.getColumnIndex("valueString"));
                totalCount = Integer.parseInt(cursor.getString(cursor.getColumnIndex("totalCount")));
                lastUpdateTime = cursor.getString(cursor.getColumnIndex("lastUpdateTime"));
            }
        } catch (Exception e) {
            PrintLog.printError("getDataListByKey", e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
            if (util != null) {
                util.close();
            }


        }

        Gson mGson = new Gson();

        //dataList = mGson.fromJson(valueString, mType);
        dataList = JSON.parseArray(valueString,aClass);
        dataBaseListCallBack.callBackByKey(dataList, totalCount, lastUpdateTime, valueString);
        if (dataList != null) {
            PrintLog.printError("KVDatabase", "数据库保存列表总数：" + dataList.size()
                    + "条 -- totalCount: 总数:" + totalCount + " -- lastUpdateTime:" + TimeZoneUtil.transfLongToString(lastUpdateTime));
        }
    }


    /**
     * 根据Key值，存储字符串
     *
     * @param saveString 要存储的字符串
     * @param key        key值
     */
    public static void putStringByKey(Context context, String key, String saveString) {
        saveString =saveString.replace("'","''");
        String sql = "insert into " + CommonSQLiteOpenHelper.KEY_VALUE_CACHE + " (keyString,valueString,totalCount,lastUpdateTime) values ('" +
                key + "','" + saveString + "','" + 1 + "','" + new Date().getTime() + "')";
        CommonSQLiteOpenHelper util = null;
        SQLiteDatabase db = null;
        try{
            util = new CommonSQLiteOpenHelper(
                    context, CommonSQLiteOpenHelper.DB_NAME);
            db = util.getReadableDatabase();
            db.execSQL(sql);
        }catch (Exception e){
            PrintLog.printError("KVDatabase:","插入sql错误:" + e.getMessage());
        }

        if(db != null){
            db.close();
        }

        if(util != null){
            util.close();
        }

    }


    /**
     * 根据Key值，获取字符串
     *
     * @param key key值
     * @return 要获取的字符串
     */


    public static void getStringByKey(Context context, String key, DataBaseListCallBack dataBaseListCallBack) {
        String getValue = "";
        String lastUpdateTime = "";
        int totalCount = 0;
        CommonSQLiteOpenHelper util = null;
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            util = new CommonSQLiteOpenHelper(
                    context, CommonSQLiteOpenHelper.DB_NAME);
            db = util.getReadableDatabase();

            cursor = db.query(CommonSQLiteOpenHelper.KEY_VALUE_CACHE, new String[]{"keyString",
                    "valueString", "totalCount", "lastUpdateTime"}, "keyString=?", new String[]{key}, null, null, null);
            while (cursor.moveToNext()) {
                getValue = cursor.getString(cursor.getColumnIndex("valueString"));
                totalCount = Integer.parseInt(cursor.getString(cursor.getColumnIndex("totalCount")));
                lastUpdateTime = cursor.getString(cursor.getColumnIndex("lastUpdateTime"));
            }
        } catch (Exception e) {
            PrintLog.printError("getDataListByKey", e.getMessage());
        } finally {
            cursor.close();
            db.close();
            util.close();
        }
        //字符串转一下
        getValue = getValue.replace("''","'");
        dataBaseListCallBack.callBackByKey(null, 1, lastUpdateTime, getValue);
    }


    /**
     * 根据Key值，存储字符串数组
     *
     * @param list 要存储的数组
     * @param key  key值
     */
    public static void putListByKey(String key, List list) {

    }

    /**
     * 根据Key值，获取字符串数组
     *
     * @param key Key值
     * @return 要获取的数组
     */
    public static List getListByKey(String key) {
        List list = new ArrayList();
        return list;
    }


    public static void deleteListByKey(Context context,String key){
        String sql = "DELETE FROM " + CommonSQLiteOpenHelper.KEY_VALUE_CACHE + " WHERE keyString ='" +
                key + "'";
        CommonSQLiteOpenHelper util = null;
        SQLiteDatabase db = null;
        try{
            util = new CommonSQLiteOpenHelper(
                    context, CommonSQLiteOpenHelper.DB_NAME);
            db = util.getReadableDatabase();
            db.execSQL(sql);
        }catch (Exception e){
            PrintLog.printError("KVDatabase:","插入sql错误:" + e.getMessage());
        }

        if(db != null){
            db.close();
        }

        if(util != null){
            util.close();
        }
    }

}
