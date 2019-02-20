package com.wythe.mall.utils;

import android.util.Log;

/**
 * 创建这个类的初衷是为了测试的时候正常显示Log日志，发布版本的时候能够全部屏蔽掉日志
 *
 * @author ningwang
 */
public class PrintLog {
    private static String TAG = PrintLog.class.getClass().getSimpleName().toString();

    public static void printDebug(String Tag, String Debug) {

        try {
            Log.e(Tag, Debug);
        } catch (Exception e) {
            // TODO: handle exception
            Log.e(TAG, e.getMessage());
        }

    }

    public static void printInfor(String Tag, String Infor) {

        try {
            Log.e(Tag, Infor);
        } catch (Exception e) {
            // TODO: handle exception
            Log.e(TAG, e.getMessage());
        }

    }

    public static void printError(String Tag, String Error) {

        try {
            Log.e(Tag, Error);
        } catch (Exception e) {
            // TODO: handle exception
            Log.e(TAG, e.getMessage());
        }

    }
}
