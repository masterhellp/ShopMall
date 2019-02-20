package com.wythe.mall.adapter.prodetails;


import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wythe.mall.R;
import com.wythe.mall.beans.CommentBean;
import com.wythe.mall.utils.CommonUtils;

import java.util.List;

import androidx.annotation.Nullable;

public class ChatAdapter extends BaseQuickAdapter<CommentBean, BaseViewHolder> {

    public ChatAdapter(int layoutResId, @Nullable List<CommentBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CommentBean item) {

        helper.setText(R.id.customer_nameView, "匿名用户")
                .setText(R.id.chat_timeView, item.getTime())
                .setText(R.id.comment_contentText, item.getContent());
        ImageView headImg = helper.getView(R.id.chat_headImg);
        if (!CommonUtils.isEmpty(item.getHeadImg())) {
            Glide.with(mContext)
                    .load(item.getHeadImg())
                    .asBitmap()
                    .placeholder(R.mipmap.a)
                    .into(headImg);
        }
        
    }
}