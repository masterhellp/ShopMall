package com.wythe.mall.adapter.prodetails;

import android.widget.ImageView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.squareup.picasso.Picasso;
import com.wythe.mall.R;
import com.wythe.mall.beans.ImageBean;
import com.wythe.mall.utils.CommonUtils;
import com.wythe.mall.utils.ResizeTransformation;

import java.util.List;

import androidx.annotation.Nullable;

public class DetailsAdapter extends BaseQuickAdapter<ImageBean, BaseViewHolder> {


    public DetailsAdapter(int layoutResId, @Nullable List<ImageBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ImageBean item) {
        ImageView imageView = helper.getView(R.id.proDetail_img);
        if (!CommonUtils.isEmpty(item.getUrl())) {
            Picasso.with(mContext)
                    .load(item.getUrl())
                    .placeholder(R.mipmap.a)
                    .error(R.mipmap.a)
                    .transform(new ResizeTransformation(mContext))
                    .into(imageView);
        } else {
            imageView.setImageResource(R.mipmap.a);
        }
    }
}
