package com.wythe.mall.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SharedPreUtils {

    private static final String TAG = "SharedPreUtils";

    private static final String PREFERENCE_FLAG = "aiShangMall";


    public static String getPreStringInfo(Context context, String key) {
        SharedPreferences preferences = context.getSharedPreferences(
                PREFERENCE_FLAG, Context.MODE_PRIVATE);// 文件模式只有在创建的时候才使用
        return preferences.getString(key, "");
    }

    public static int getPreIntInfo(Context context, String key) {
        SharedPreferences preferences = context.getSharedPreferences(
                PREFERENCE_FLAG, Context.MODE_PRIVATE);// 文件模式只有在创建的时候才使用
        return preferences.getInt(key, 0);
    }

    public static int getPreIntInfo(Context context, String key, int value) {
        SharedPreferences preferences = context.getSharedPreferences(
                PREFERENCE_FLAG, Context.MODE_PRIVATE);// 文件模式只有在创建的时候才使用
        return preferences.getInt(key, value);
    }

    public static long getPreLongInfo(Context context, String key) {
        SharedPreferences preferences = context.getSharedPreferences(
                PREFERENCE_FLAG, Context.MODE_PRIVATE);// 文件模式只有在创建的时候才使用
        return preferences.getLong(key, 0);
    }

    public static boolean getPreBooleanInfo(Context context, String key) {
        SharedPreferences preferences = context.getSharedPreferences(
                PREFERENCE_FLAG, Context.MODE_PRIVATE);// 文件模式只有在创建的时候才使用
        return preferences.getBoolean(key, false);
    }

    public static void setStringToPre(Context context, String key, String value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                PREFERENCE_FLAG, Context.MODE_PRIVATE);
        Editor dEditor = sharedPreferences.edit();
        dEditor.putString(key, value);
        dEditor.commit();
    }

    public static void setIntToPre(Context context, String key, int value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                PREFERENCE_FLAG, Context.MODE_PRIVATE);
        Editor dEditor = sharedPreferences.edit();
        dEditor.putInt(key, value);
        dEditor.commit();
    }

    public static void setLongToPre(Context context, String key, long value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                PREFERENCE_FLAG, Context.MODE_PRIVATE);
        Editor dEditor = sharedPreferences.edit();
        dEditor.putLong(key, value);
        dEditor.commit();
    }

    public static void setBooleanToPre(Context context, String key,
                                       boolean value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                PREFERENCE_FLAG, Context.MODE_PRIVATE);
        Editor dEditor = sharedPreferences.edit();
        dEditor.putBoolean(key, value);
        dEditor.commit();
    }

    public static void removePre(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                PREFERENCE_FLAG, Context.MODE_PRIVATE);
        Editor dEditor = sharedPreferences.edit();
        dEditor.remove(key);
        dEditor.commit();
    }

    public static SharedPreferences getSharePreference(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                PREFERENCE_FLAG, Context.MODE_PRIVATE);
        return sharedPreferences;
    }


    public static <T> boolean putListData(Context context, String key, List<T> datalist) {
        SharedPreferences sp = context.getSharedPreferences(
                PREFERENCE_FLAG, Context.MODE_PRIVATE);// 文件模式只有在创建的时候才使用
        if (null == datalist || datalist.size() <= 0)
            return false;
        Editor editor = sp.edit();
        Gson gson = new Gson();
        //转换成json数据，再保存
        String strJson = gson.toJson(datalist);
        editor.putString(key, strJson);
        editor.commit();
        Log.e(TAG, "SharedPreUtils : " + strJson);
        return true;
    }

    /**
     * 获取保存的List
     *
     * @param key key
     * @return 对应的Lis集合
     * 为了避免类型擦除所以这里直接传了type
     */
    public static <T> List<T> getListData(Context context, String key, Type type) {
        List<T> datalist = new ArrayList<>();
        SharedPreferences sp = context.getSharedPreferences(
                PREFERENCE_FLAG, Context.MODE_PRIVATE);// 文件模式只有在创建的时候才使用
        String strJson = sp.getString(key, null);
        Log.e(TAG, "getListData: ----    " + strJson);
        if (null == strJson) {
            return datalist;
        }
        Gson gson = new Gson();
        datalist = gson.fromJson(strJson, type);
        return datalist;
    }

    /**
     * 清空SP的數據
     * @param context
     */
    public static void clearDatas(Context context){
        SharedPreferences sp = context.getSharedPreferences(
                PREFERENCE_FLAG, Context.MODE_PRIVATE);// 文件模式只有在创建的时候才使用
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.commit();
    }
}
