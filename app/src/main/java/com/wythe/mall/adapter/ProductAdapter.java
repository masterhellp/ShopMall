package com.wythe.mall.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.wythe.mall.R;
import com.wythe.mall.beans.MerchantinfoBean;
import com.wythe.mall.utils.CommonUtils;

import java.util.List;

public class ProductAdapter extends BaseAdapter {
    private Context context;
    private List<MerchantinfoBean> data;


    public ProductAdapter(List<MerchantinfoBean> data, Context context) {
        this.data = data;
        this.context = context;

    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.product_grid_item, null);

            holder.iv = (ImageView) view.findViewById(R.id.grid_item_image1);
            holder.tv_Title = (TextView) view.findViewById(R.id.grid_item_title1);
            holder.tv_Price = (TextView) view.findViewById(R.id.grid_item_price1);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        //设置图片的大小 平分页面
        int width = (CommonUtils.getScreenWidth(context) - CommonUtils.dip2px(context, 5)) / 2;
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width, width);
        holder.iv.setLayoutParams(lp);

        Picasso.with(context)
                .load(data.get(i).getImages())
                .placeholder(R.mipmap.a)
                .error(R.mipmap.a)
                .resize(width, width)
                .centerCrop()
                .into(holder.iv);

        holder.tv_Title.setText(data.get(i).getName());
        holder.tv_Price.setText("￥" + data.get(i).getPrice());
//        }

        return view;
    }

    static class ViewHolder {
        ImageView iv;
        TextView tv_Title;
        TextView tv_Price;
    }

}
