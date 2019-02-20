package com.wythe.mall.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.request.target.ImageViewTarget;

public class TransformationUtils extends ImageViewTarget<Bitmap> {

    private ImageView target;
    private Context context;

    private static final String TAG = "TransformationUtils";

    public TransformationUtils(Context context, ImageView target) {
        super(target);
        this.target = target;
        this.context = context;
    }

    @Override
    protected void setResource(Bitmap resource) {
        view.setImageBitmap(resource);
        //获取原图的宽高
        int width = resource.getWidth();
        int height = resource.getHeight();

        Log.e(TAG, "setResource: width :" + width + "  height :" + height);
        //获取imageView的宽
        int imageViewWidth = CommonUtils.getScreenWidth(context);
        Log.e(TAG, "setResource: getScreenWidth :" + width );
        int imageViewHeight = height;
        if (width <= imageViewWidth) {
            //小于屏幕宽度不处理

        } else {
            imageViewWidth = CommonUtils.getScreenWidth(context) - CommonUtils.dip2px(context, 24);
            //计算缩放比例
            float sy = (float) (imageViewWidth * 0.1) / (float) (width * 0.1);
            //计算图片等比例放大后的高
            imageViewHeight = (int) (height * sy);
        }
//        target.setLayoutParams(params);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.width = imageViewWidth;
        lp.height = imageViewHeight;
        target.setLayoutParams(lp);
    }
}
