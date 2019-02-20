package com.wythe.mall.adapter.shopcar;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wythe.mall.R;
import com.wythe.mall.activity.LoginActivity;
import com.wythe.mall.activity.MainActivity;
import com.wythe.mall.activity.orders.FirmOrderActivity;
import com.wythe.mall.base.BaseApplication;
import com.wythe.mall.beans.Customer;
import com.wythe.mall.beans.Goods;
import com.wythe.mall.beans.ShoppingCarDataBean;
import com.wythe.mall.http.datasource.MDataSource;
import com.wythe.mall.sqlite.LoadListCallBack;
import com.wythe.mall.utils.CommonUtils;
import com.wythe.mall.utils.GotoActivity;
import com.wythe.mall.utils.GsonUtils;
import com.wythe.mall.utils.SharedPreUtils;
import com.wythe.mall.utils.ToastUtil;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Request;

/**
 * 购物车的adapter
 * 因为使用的是ExpandableListView，所以继承BaseExpandableListAdapter
 */
public class ShoppingCarAdapter extends BaseExpandableListAdapter {

    private final Context context;
    private final LinearLayout llSelectAll;
    private final ImageView ivSelectAll;
    private final Button btnOrder;
    private final Button btnDelete;
    private final RelativeLayout rlTotalPrice;
    private final TextView tvTotalPrice;
    private List<ShoppingCarDataBean.DatasBean> data;
    private boolean isSelectAll = false;
    private double total_price;

    public ShoppingCarAdapter(Context context, LinearLayout llSelectAll,
                              ImageView ivSelectAll, Button btnOrder, Button btnDelete,
                              RelativeLayout rlTotalPrice, TextView tvTotalPrice) {
        this.context = context;
        this.llSelectAll = llSelectAll;
        this.ivSelectAll = ivSelectAll;
        this.btnOrder = btnOrder;
        this.btnDelete = btnDelete;
        this.rlTotalPrice = rlTotalPrice;
        this.tvTotalPrice = tvTotalPrice;
    }

    /**
     * 自定义设置数据方法；
     * 通过notifyDataSetChanged()刷新数据，可保持当前位置
     *
     * @param data 需要刷新的数据
     */
    public void setData(List<ShoppingCarDataBean.DatasBean> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public int getGroupCount() {
        if (data != null && data.size() > 0) {
            return data.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getGroup(int groupPosition) {
        return data.get(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(final int groupPosition, final boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder groupViewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_shopping_car_group, null);
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
            groupViewHolder.tvStoreName.setText("邻家" + groupPosition + "号店");
        }

        //店铺内的商品都选中的时候，店铺的也要选中
        for (int i = 0; i < datasBean.getGoods().size(); i++) {
            Goods goodsBean = datasBean.getGoods().get(i);
            boolean isSelect = goodsBean.isSelect();
            if (isSelect) {
                datasBean.setIsSelect_shop(true);
            } else {
                datasBean.setIsSelect_shop(false);
                break;
            }
        }

        //因为set之后要重新get，所以这一块代码要放到一起执行
        //店铺是否在购物车中被选中
        final boolean isSelect_shop = datasBean.getIsSelect_shop();
        if (isSelect_shop) {
            groupViewHolder.ivSelect.setImageResource(R.mipmap.select);
        } else {
            groupViewHolder.ivSelect.setImageResource(R.mipmap.unselect);
        }

        //店铺选择框的点击事件
        groupViewHolder.ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datasBean.setIsSelect_shop(!isSelect_shop);

                List<Goods> goods = datasBean.getGoods();
                for (int i = 0; i < goods.size(); i++) {
                    Goods goodsBean = goods.get(i);
                    goodsBean.setSelect(!isSelect_shop);
                }
                notifyDataSetChanged();
            }
        });
        w:
        //当所有的选择框都是选中的时候，全选也要选中
        for (int i = 0; i < data.size(); i++) {
            List<Goods> goods = data.get(i).getGoods();
            for (int y = 0; y < goods.size(); y++) {
                Goods goodsBean = goods.get(y);
                boolean isSelect = goodsBean.isSelect();
                if (isSelect) {
                    isSelectAll = true;
                } else {
                    isSelectAll = false;
                    break w;//根据标记，跳出嵌套循环
                }
            }
        }
        if (isSelectAll) {
            ivSelectAll.setBackgroundResource(R.mipmap.select);
        } else {
            ivSelectAll.setBackgroundResource(R.mipmap.unselect);
        }

        //全选的点击事件
        llSelectAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSelectAll = !isSelectAll;

                if (isSelectAll) {
                    for (int i = 0; i < data.size(); i++) {
                        List<Goods> goods = data.get(i).getGoods();
                        for (int y = 0; y < goods.size(); y++) {
                            Goods goodsBean = goods.get(y);
                            goodsBean.setSelect(true);
                        }
                    }
                } else {
                    for (int i = 0; i < data.size(); i++) {
                        List<Goods> goods = data.get(i).getGoods();
                        for (int y = 0; y < goods.size(); y++) {
                            Goods goodsBean = goods.get(y);
                            goodsBean.setSelect(false);
                        }
                    }
                }
                notifyDataSetChanged();
            }
        });

        //合计的计算
        total_price = 0.0;
        tvTotalPrice.setText("¥0.00");
        for (int i = 0; i < data.size(); i++) {
            List<Goods> goods = data.get(i).getGoods();
            for (int y = 0; y < goods.size(); y++) {
                Goods goodsBean = goods.get(y);
                boolean isSelect = goodsBean.isSelect();
                if (isSelect) {
                    int num = goodsBean.getAmount();
                    String price = goodsBean.getNormPrice();

                    double v1 = Double.parseDouble(price);
                    total_price = total_price + num * v1;
                    //让Double类型完整显示，不用科学计数法显示大写字母E
                    DecimalFormat decimalFormat = new DecimalFormat("0.00");
                    tvTotalPrice.setText("¥" + decimalFormat.format(total_price));
                }
            }
        }

        //去结算的点击事件
        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //创建临时的List，用于存储被选中的商品
                List<Goods> temp = new ArrayList<>();
                for (int i = 0; i < data.size(); i++) {
                    List<Goods> goods = data.get(i).getGoods();
                    for (int y = 0; y < goods.size(); y++) {
                        Goods goodsBean = goods.get(y);
                        boolean isSelect = goodsBean.isSelect();
                        if (isSelect) {
                            //商品所属店铺名称
                            goodsBean.setShopName(data.get(i).getShopName());
                            goodsBean.setShopkey(data.get(i).getShopKey());
                            goodsBean.setShopLogo(data.get(i).getShopImg());
//                            goodsBean.setCartKey(data.get(i).getGoods().get(i).getCartKey());
                            temp.add(goodsBean);
                        }
                    }
                }
                if (temp != null && temp.size() > 0) {//如果有被选中的
                    /**
                     * 实际开发中，如果有被选中的商品，
                     * 则跳转到确认订单页面，完成后续订单流程。
                     */
                    Intent intent = new Intent(context, FirmOrderActivity.class);
                    //将数据传递到确认订单页面 去付款
                    intent.putExtra("selectList", (Serializable) temp);
                    Log.e("商品名称===",temp.get(0).getGoodsName());
                    intent.putExtra("total_price", total_price);
                    // 跳转到订单结算页面 确认订单
                    context.startActivity(intent);
                } else {
                    ToastUtil.makeText(context, "请选择要购买的商品");
                }
            }
        });

        //删除的点击事件
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * 实际开发中，通过回调请求后台接口实现删除操作
                 */
               String userName= SharedPreUtils.getPreStringInfo(context,"userName");

                if(isSelectAll){
                    delALL(userName);
                }

                if (mDeleteListener != null) {
                    mDeleteListener.onDelete();
                }
            }
        });

        return convertView;
    }

    static class GroupViewHolder {
        @Bind(R.id.iv_select)
        ImageView ivSelect;
        @Bind(R.id.tv_store_name)
        TextView tvStoreName;
        @Bind(R.id.ll)
        LinearLayout ll;

        GroupViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    //------------------------------------------------------------------------------------------------
    @Override
    public int getChildrenCount(int groupPosition) {
        if (data.get(groupPosition).getGoods() != null && data.get(groupPosition).getGoods().size() > 0) {
            return data.get(groupPosition).getGoods().size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return data.get(groupPosition).getGoods().get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder childViewHolder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_shopping_car_child, null);
            childViewHolder = new ChildViewHolder(convertView);
            convertView.setTag(childViewHolder);
        } else {
            childViewHolder = (ChildViewHolder) convertView.getTag();
        }
        final ShoppingCarDataBean.DatasBean datasBean = data.get(groupPosition);
        //店铺ID
        String store_id = datasBean.getShopKey();
        //店铺名称
        String store_name = datasBean.getShopName();
        //店铺是否在购物车中被选中
        final boolean isSelect_shop = datasBean.getIsSelect_shop();
        final Goods goodsBean = datasBean.getGoods().get(childPosition);
        //商品图片
        String goods_image = goodsBean.getGoodImage();
        //商品ID
        final String goods_id = goodsBean.getGoodsKey();
        //商品名称
        String goods_name = goodsBean.getGoodsName() + "_" + goodsBean.getNormName();
        //商品价格
        String goods_price = goodsBean.getNormPrice();
        //商品数量
        int goods_num = goodsBean.getAmount();
        //商品是否被选中
        final boolean isSelect = goodsBean.isSelect();

        Glide.with(context)
                .load(goods_image)
                .into(childViewHolder.ivPhoto);
        if (goods_name != null) {
            childViewHolder.tvName.setText(goods_name);
        } else {
            childViewHolder.tvName.setText("");
        }
        if (goods_price != null) {
            childViewHolder.tvPriceValue.setText(goods_price);
        } else {
            childViewHolder.tvPriceValue.setText("");
        }
        if (goods_num > 0) {
            childViewHolder.tvEditBuyNumber.setText(goods_num + "");
        } else {
            childViewHolder.tvEditBuyNumber.setText("");
        }

        //商品是否被选中
        if (isSelect) {
            childViewHolder.ivSelect.setImageResource(R.mipmap.select);
        } else {
            childViewHolder.ivSelect.setImageResource(R.mipmap.unselect);
        }

        //商品选择框的点击事件
        childViewHolder.ivSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goodsBean.setSelect(!isSelect);
                if (!isSelect == false) {
                    datasBean.setIsSelect_shop(false);
                }
                notifyDataSetChanged();
            }
        });

        //加号的点击事件
        childViewHolder.ivEditAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //模拟加号操作
                int num = goodsBean.getAmount();
                num++;
                goodsBean.setAmount(num);
                notifyDataSetChanged();
                /**
                 * 实际开发中，通过回调请求后台接口实现数量的加减
                 */
                if (mChangeCountListener != null) {
                    mChangeCountListener.onChangeCount(goods_id);
                }
            }
        });
        //减号的点击事件
        childViewHolder.ivEditSubtract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //模拟减号操作
                int integer = goodsBean.getAmount();
                if (integer > 1) {
                    integer--;
                    goodsBean.setAmount(integer);
                    /**
                     * 实际开发中，通过回调请求后台接口实现数量的加减
                     */
                    if (mChangeCountListener != null) {
                        mChangeCountListener.onChangeCount(goods_id);
                    }
                } else {
                    ToastUtil.makeText(context, "商品不能再减少了");
                }
                notifyDataSetChanged();
            }
        });

        if (childPosition == data.get(groupPosition).getGoods().size() - 1) {
            childViewHolder.view.setVisibility(View.GONE);
            childViewHolder.viewLast.setVisibility(View.VISIBLE);
        } else {
            childViewHolder.view.setVisibility(View.VISIBLE);
            childViewHolder.viewLast.setVisibility(View.GONE);
        }

        return convertView;
    }

    static class ChildViewHolder {
        @Bind(R.id.iv_select)
        ImageView ivSelect;
        @Bind(R.id.iv_photo)
        ImageView ivPhoto;
        @Bind(R.id.tv_name)
        TextView tvName;
        @Bind(R.id.tv_price_key)
        TextView tvPriceKey;
        @Bind(R.id.tv_price_value)
        TextView tvPriceValue;
        @Bind(R.id.iv_edit_subtract)
        ImageView ivEditSubtract;
        @Bind(R.id.buy_number)
        TextView tvEditBuyNumber;
        @Bind(R.id.iv_edit_add)
        ImageView ivEditAdd;
        @Bind(R.id.view)
        View view;
        @Bind(R.id.view_last)
        View viewLast;

        ChildViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    //-----------------------------------------------------------------------------------------------
    private void addNum(String method,String k) {
        /**
         * 增加购物车中产品的数量
         * 建立请求表单，添加上传服务器的参数
         */
        HashMap<String, String> map = new HashMap<>();
        map.put("model", "interfaces");
        map.put("method", method);
        MDataSource<Customer> dataSource = new MDataSource<>(context);
        dataSource.postData(map, new LoadListCallBack() {
            @Override
            public void loadList(List list) {

            }

            @Override
            public void systemError(Request request, String errorInfo, Exception e) {
                Log.e("systemError: ", errorInfo);
                e.printStackTrace();
            }

            @Override
            public void retLoad(String code) {

            }

            @Override
            public void loadString(String string) {
                Log.e("loadString 登录结果 : " ,string.toString());

            }
        });
    }
    private void delALL(String userid) {
        /**
         * 增加购物车中产品的数量
         * 建立请求表单，添加上传服务器的参数
         */
        HashMap<String, String> map = new HashMap<>();
        map.put("model", "interfaces");
        map.put("method", "emptyShoppingCart");
        map.put("userId",userid);
        MDataSource<Customer> dataSource = new MDataSource<>(context);
        dataSource.postData(map, new LoadListCallBack() {
            @Override
            public void loadList(List list) {

            }

            @Override
            public void systemError(Request request, String errorInfo, Exception e) {
                Log.e("systemError: ", errorInfo);
                e.printStackTrace();
            }

            @Override
            public void retLoad(String code) {

            }

            @Override
            public void loadString(String string) {
                Log.e("loadString 登录结果 : " ,string.toString());

            }
        });
    }
    private void delnum(String method,String k) {
        /**
         * 减少购物车中产品的数量
         * 建立请求表单，添加上传服务器的参数
         */
        HashMap<String, String> map = new HashMap<>();
        map.put("model", "interfaces");
        map.put("method", method);
        MDataSource<Customer> dataSource = new MDataSource<>(context);
        dataSource.postData(map, new LoadListCallBack() {
            @Override
            public void loadList(List list) {

            }

            @Override
            public void systemError(Request request, String errorInfo, Exception e) {
                Log.e("systemError: ", errorInfo);
                e.printStackTrace();
            }

            @Override
            public void retLoad(String code) {

            }

            @Override
            public void loadString(String string) {
                Log.e("loadString 登录结果 : " ,string.toString());

            }
        });
    }



    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    //删除的回调
    public interface OnDeleteListener {
        void onDelete();
    }

    public void setOnDeleteListener(OnDeleteListener listener) {
        mDeleteListener = listener;
    }

    private OnDeleteListener mDeleteListener;

    //修改商品数量的回调
    public interface OnChangeCountListener {
        void onChangeCount(String goods_id);
    }

    public void setOnChangeCountListener(OnChangeCountListener listener) {
        mChangeCountListener = listener;
    }

    private OnChangeCountListener mChangeCountListener;
}