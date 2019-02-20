package com.wythe.mall.adapter.shopcar;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wythe.mall.R;
import com.wythe.mall.beans.Goods;
import com.wythe.mall.beans.ShoppingCarDataBean;
import com.wythe.mall.utils.CommonUtils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class OrderAdapter extends BaseExpandableListAdapter {

    private Context context;
    // 数据源
    private List<ShoppingCarDataBean.DatasBean> data;
    // 总价
    private double total_price;

    public List<ShoppingCarDataBean.DatasBean> getData() {
        return data;
    }

    public void setData(List<ShoppingCarDataBean.DatasBean> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public OrderAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getGroupCount() {
        if (null != data && !data.isEmpty()) {
            return data.size();
        } else {
            return 0;
        }
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if (null != data.get(groupPosition).getGoods() && data.get(groupPosition).getGoods().size() > 0) {
            return data.get(groupPosition).getGoods().size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getGroup(int groupPosition) {
        return null != data ? data.get(groupPosition) : null;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return null != data ? data.get(groupPosition).getGoods().get(childPosition) : null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    static class GroupViewHolder {
        @Bind(R.id.tv_store_name)
        TextView tvStoreName;

        GroupViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }


    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder groupViewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.order_group, null);
            groupViewHolder = new GroupViewHolder(convertView);
            convertView.setTag(groupViewHolder);
        } else {
            groupViewHolder = (GroupViewHolder) convertView.getTag();
        }
        final ShoppingCarDataBean.DatasBean datasBean = data.get(groupPosition);
        //店铺ID
        String store_id = datasBean.getShopKey();
        //店铺名称
        String store_name = datasBean.getShopName();
        if (store_name != null) {
            groupViewHolder.tvStoreName.setText(store_name);
        } else {
            groupViewHolder.tvStoreName.setText("");
        }
        return convertView;
    }

    /********************************************************************************************/
    static class ChildViewHolder {
        @Bind(R.id.iv_photo)
        ImageView ivPhoto;
        @Bind(R.id.tv_name)
        TextView tvName;
        @Bind(R.id.tv_price_value)
        TextView tvPriceValue;
        @Bind(R.id.tv_edit_buy_number)
        TextView tvEditBuyNumber;
        @Bind(R.id.view_last)
        View viewLast;

        ChildViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    /**
     * 子视图
     *
     * @param groupPosition
     * @param childPosition
     * @param isLastChild
     * @param convertView
     * @param parent
     * @return
     */


    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder childViewHolder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.order_child, null);
            childViewHolder = new ChildViewHolder(convertView);
            convertView.setTag(childViewHolder);
        } else {
            childViewHolder = (ChildViewHolder) convertView.getTag();
        }
        final ShoppingCarDataBean.DatasBean datasBean = data.get(groupPosition);

        final Goods goodsBean = datasBean.getGoods().get(childPosition);

        //商品图片
        String goods_image = goodsBean.getGoodImage();
        //商品ID
        final String goods_id = goodsBean.getGoodsId();
        //商品名称
        String goods_name = goodsBean.getGoodsName();
        Log.e("商品名称==!!",goods_name);
        String normal_name = goodsBean.getNormName();
        if(!CommonUtils.isEmpty(normal_name)){
            goods_name = goods_name+"_"+normal_name ;
        }
        if(CommonUtils.isEmpty(goods_name)){
            goods_name = goodsBean.getGoodsName() ;
        }
        //商品价格
        String goods_price = goodsBean.getNormPrice();
        if (CommonUtils.isEmpty(goods_price)) {
            goods_price = goodsBean.getPrice();
        } else {
            Double normalPrice = Double.parseDouble(goods_price);
            if (normalPrice <= 0) {
                goods_price = goodsBean.getPrice();
            }
        }
        //商品数量
        int goods_num = goodsBean.getAmount();
        Glide.with(context)
                .load(goods_image)
                .into(childViewHolder.ivPhoto);

        childViewHolder.tvName.setText(goods_name);
        Double price = Double.parseDouble(goods_price);
        childViewHolder.tvPriceValue.setText("￥" + (price * goods_num));
        childViewHolder.tvEditBuyNumber.setText("数量：" + goods_num);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
