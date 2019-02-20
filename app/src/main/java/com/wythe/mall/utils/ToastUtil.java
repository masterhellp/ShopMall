package com.wythe.mall.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * 下一个Toast覆盖上一个Toast,防止Toast弹出的时间过长
 */
public class ToastUtil {
    private static Toast mToast;

    public static void makeText(Context mContext, String text) {
        if (mToast != null) {
            mToast.cancel();
        }
        mToast = Toast.makeText(mContext, text, Toast.LENGTH_SHORT);
        mToast.show();
    }
}
