package com.wythe.mall.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;
import com.wythe.mall.R;
import com.wythe.mall.beans.MerchantListBean;

import java.util.List;

public class MerchantListAdapter extends BaseAdapter {
    private Context context;
    private List<MerchantListBean.ResultsBean.ListBean> data;

    public MerchantListAdapter(Context context, List<MerchantListBean.ResultsBean.ListBean> data){
        this.context=context;
        this.data=data;
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
        if(view==null){
            holder=new ViewHolder();
            view=LayoutInflater.from(context).inflate(R.layout.merchant_item,null);
            holder.shop_name=(TextView) view.findViewById(R.id.shop_Name);
            holder.shop_star=(TextView) view.findViewById(R.id.shop_star);
            holder.merchant_logo=(ImageView) view.findViewById(R.id.merchant_logo);
            holder.merchant_item_layout=(LinearLayout)view.findViewById(R.id.merchant_item_layout);
            view.setTag(holder);
        }else{
            holder= (ViewHolder) view.getTag();
        }

        Log.e("店铺logo===",data.get(i).getLogo());
        Picasso.with(context)
                .load(data.get(i).getLogo())
                .into(holder.merchant_logo);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            ImageView imageView=new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setLayoutParams(layoutParams);

        ViewGroup parentViewGroup = (ViewGroup) holder.merchant_item_layout.getParent();
        if (parentViewGroup != null) {
            parentViewGroup.removeAllViews();
        }
        for (int j = 0; j < data.get(i).getImgList().size(); j++) {
            LoadImage(imageView,data.get(i).getImgList().get(j).getImages());
            holder.merchant_item_layout.addView(imageView);

        }


//                    Glide.with(context).load(list.get(i).getResults().getList().get(i).getUrl()).into(imageView);

//        }
        return view;
    }
    private void LoadImage(ImageView imageView,String url){

        Picasso.with(context)
                .load(url)
                .placeholder(R.mipmap.a)
                .error(R.mipmap.a)
                .resize(120, 120)
                .centerCrop()
                .config(Bitmap.Config.RGB_565)
                .into(imageView);

    }

    static class ViewHolder{
        TextView shop_name;
        TextView shop_star;
        ImageView merchant_logo;//店铺logo
        LinearLayout merchant_item_layout;
    }
}
