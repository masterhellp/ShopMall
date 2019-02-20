package com.wythe.mall.utils;

import android.content.Context;
import android.graphics.Bitmap;

import com.squareup.picasso.Transformation;

public class ResizeTransformation implements Transformation {

    int screenWidth;
    double targetWidth;

    public ResizeTransformation(Context context){
        //获得屏幕宽度
        screenWidth = CommonUtils.getScreenWidth(context);
    }

    @Override
    public Bitmap transform(Bitmap bitmap) {
        //如果宽度或高度为0 直接返回bitmap
        if (bitmap.getWidth() == 0 || bitmap.getHeight() == 0) {
            return bitmap;
        }
        targetWidth = bitmap.getWidth() ;
        if(bitmap.getWidth() > screenWidth -100 ){
            //宽度超出屏幕了 需要
            targetWidth  = screenWidth - 100 ;
        }
        //得到图片宽高比,每个参数必须强转成double型
        double ratio = (double) bitmap.getWidth() / (double) bitmap.getHeight();
        Bitmap bitmapResult=null;
        if(bitmap!=null){
            bitmapResult = Bitmap.createScaledBitmap(bitmap, (int) (targetWidth+0.5), (int) ((targetWidth / ratio)+0.5), false);
        }
        if (bitmap != bitmapResult) {
            bitmap.recycle();
        }

        return bitmapResult;
    }
    @Override
    public String key() {
        return "transformation" + screenWidth ;
    }
}
