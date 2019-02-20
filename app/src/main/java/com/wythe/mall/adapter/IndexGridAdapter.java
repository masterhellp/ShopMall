package com.wythe.mall.adapter;

import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.squareup.picasso.Picasso;
import com.wythe.mall.R;
import com.wythe.mall.beans.IndexGridBean;
import com.wythe.mall.utils.CommonUtils;

import java.util.List;

import androidx.annotation.Nullable;

/**
 * 初始页面九宫格
 */
public class IndexGridAdapter extends BaseQuickAdapter<IndexGridBean, BaseViewHolder> {


    public IndexGridAdapter(int layoutResId, @Nullable List<IndexGridBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, IndexGridBean item) {

        ImageView imageView = helper.getView(R.id.indexGrid_img);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        int width = (CommonUtils.getScreenWidth(mContext) - CommonUtils.dip2px(mContext, 104)) / 5;
        lp.width = width;
        lp.height = width;
        imageView.setLayoutParams(lp);

        Picasso.with(mContext).load(item.getImgUrl())
                .resize(width, width)
                .centerCrop()
                .into(imageView);
        //设置标题
        helper.setText(R.id.indexGrid_name, item.getTitle());

    }
}
