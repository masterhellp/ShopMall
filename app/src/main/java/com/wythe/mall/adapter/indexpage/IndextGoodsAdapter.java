package com.wythe.mall.adapter.indexpage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;
import com.wythe.mall.R;
import com.wythe.mall.adapter.IPullToRefreshListAdapter;
import com.wythe.mall.beans.Goods;
import com.wythe.mall.beans.NewGoods;
import com.wythe.mall.utils.AppConfig;
import com.wythe.mall.utils.CommonUtils;


/**
 * 首页推荐和热卖的商品列表
 */
public class IndextGoodsAdapter extends IPullToRefreshListAdapter<Goods> {
    //判断数据类型
    private int type;

    public IndextGoodsAdapter(Context context, int type) {
        super(context);
        this.type = type;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (null == convertView) {
            convertView = inflater.inflate(R.layout.index_recom_items, null);
            viewHolder = new ViewHolder();
            viewHolder.imageView = convertView.findViewById(R.id.index_recom_img);
            viewHolder.titleView = convertView.findViewById(R.id.index_title);
            viewHolder.nowPrice = convertView.findViewById(R.id.index_nowPrice);
            viewHolder.oldPrice = convertView.findViewById(R.id.index_oldPrice);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Goods item = getItem(position);
        //设置图片的大小 平分页面
        int width = (CommonUtils.getScreenWidth(context) - CommonUtils.dip2px(context, 5))/2;
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width, width);
        viewHolder.imageView.setLayoutParams(lp);

        //设置adapter数据
        if (!TextUtils.isEmpty(item.getImages())) {
//            Log.e("图片地址==",item.getImages().toString());
            Picasso.with(context)
                    .load(item.getImages())
                    .placeholder(R.mipmap.a)
                    .error(R.mipmap.a)
                    .resize(width, width)
                    .centerCrop()
                    .config(Bitmap.Config.RGB_565)
                    .into(viewHolder.imageView);
//            Glide.with(context).load(item.getImages()).into(viewHolder.imageView);
        }
        viewHolder.titleView.setText(item.getName());
        if(CommonUtils.isEmpty(item.getPrice())){
            viewHolder.nowPrice.setText("￥100" );
        } else {
            viewHolder.nowPrice.setText("￥" + item.getPrice());
        }
        if (AppConfig.INDEX_HOT_GOODS == type) {
            //热销显示原价
            viewHolder.oldPrice.setText("￥" + item.getMprice());
            // 原价加上横线
            viewHolder.oldPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            viewHolder.oldPrice.setVisibility(View.VISIBLE);
        } else {
            // 推荐不显示原价
            viewHolder.oldPrice.setVisibility(View.GONE);
        }


        return convertView;
    }


    static class ViewHolder {

        ImageView imageView;

        TextView titleView;

        TextView nowPrice;

        TextView oldPrice;

    }
}
