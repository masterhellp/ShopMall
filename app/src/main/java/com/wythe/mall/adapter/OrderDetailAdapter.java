package com.wythe.mall.adapter;

import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.squareup.picasso.Picasso;
import com.wythe.mall.R;
import com.wythe.mall.beans.Goods;
import com.wythe.mall.beans.OrderDetailBean;
import com.wythe.mall.utils.CommonUtils;

import java.util.List;

import androidx.annotation.Nullable;

/**
 * 订单详情页
 * <p>
 * 订单内容列表
 */

public class OrderDetailAdapter extends BaseQuickAdapter<OrderDetailBean, BaseViewHolder> {
    private String status;
    private String refoundStutas;

    public OrderDetailAdapter(int layoutResId, @Nullable List<OrderDetailBean> data,String status,String refoundStutas) {
        super(layoutResId, data);
        this.status=status;
        this.refoundStutas=refoundStutas;
    }

    @Override
    protected void convert(BaseViewHolder helper, OrderDetailBean item) {
        ImageView imgView = helper.getView(R.id.goods_img);
        if (!CommonUtils.isEmpty(item.getImage())) {
            Picasso.with(mContext)
                    .load(item.getImage())
                    .error(R.mipmap.a)
                    .placeholder(R.mipmap.a)
                    .into(imgView);
        }
        //设置标题
        String name = item.getName() ;
        String normalName = item.getNormName();
        if(CommonUtils.isEmpty(name)){
           name = normalName ;
        } else {
            if(!CommonUtils.isEmpty(normalName)){
                name = name+"-"+normalName;
            }
        }
        if(refoundStutas.equals("无")) {
            if ("1".equals(status)) {

                helper.setText(R.id.edit_button, "待付款,去付款");
                helper.getView(R.id.commit_makeSure).setVisibility(View.GONE);
                helper.getView(R.id.qr_code).setVisibility(View.GONE);

            }
            if ("2".equals(status)) {

                helper.setText(R.id.edit_button, "申请退款");


            }
            if ("3".equals(status)) {

                helper.setText(R.id.edit_button, "申请退款");


            }
            if ("4".equals(status)) {

                helper.setText(R.id.edit_button, "确认收货,去评价");
                helper.getView(R.id.commit_makeSure).setVisibility(View.GONE);
                helper.getView(R.id.commit_makeSure).setEnabled(false);


            }
        }else {
            /**
             *
             * 0无，1退款中等待卖家处理，2退款中卖家已同意退款，退款成功，卖家拒绝退款，买家撤销退款，买家确认收货关闭,卖家完成订单关闭
             * */
            if (refoundStutas.equals("退款中等待卖家处理")) {
                helper.setText(R.id.edit_button, "退款中等待卖家处理");
                helper.getView(R.id.edit_button).setVisibility(View.GONE);
                helper.getView(R.id.qr_code).setVisibility(View.GONE);
                helper.getView(R.id.commit_makeSure).setVisibility(View.GONE);
            }
            if (refoundStutas.equals("退款中卖家已同意退款")) {
                helper.setText(R.id.edit_button, "退款中卖家已同意退款");
                helper.getView(R.id.edit_button).setVisibility(View.GONE);
                helper.getView(R.id.qr_code).setVisibility(View.GONE);
                helper.getView(R.id.commit_makeSure).setVisibility(View.GONE);
            }
            if (refoundStutas.equals("退款成功")) {
                helper.getView(R.id.edit_button).setVisibility(View.GONE);
                helper.getView(R.id.qr_code).setVisibility(View.GONE);
                helper.getView(R.id.commit_makeSure).setVisibility(View.GONE);
                helper.setText(R.id.edit_button, "退款成功");
            }

            if (refoundStutas.equals("卖家继续发货拒绝退款")) {
                helper.getView(R.id.edit_button).setVisibility(View.GONE);
                helper.getView(R.id.qr_code).setVisibility(View.GONE);
                helper.getView(R.id.commit_makeSure).setVisibility(View.GONE);
                helper.setText(R.id.edit_button, "卖家继续发货拒绝退款");
            }
            if (refoundStutas.equals("买家撤销退款")) {
                helper.getView(R.id.edit_button).setVisibility(View.GONE);
                helper.getView(R.id.qr_code).setVisibility(View.GONE);
                helper.getView(R.id.commit_makeSure).setVisibility(View.GONE);
                helper.setText(R.id.edit_button, "买家撤销退款");
            }
            if (refoundStutas.equals("买家确认收货关闭")) {
                helper.getView(R.id.edit_button).setVisibility(View.GONE);
                helper.getView(R.id.qr_code).setVisibility(View.GONE);
                helper.getView(R.id.commit_makeSure).setVisibility(View.GONE);
                helper.setText(R.id.edit_button, "买家确认收货关闭");
            }

        }
        helper.setText(R.id.goods_name, name)
                .setText(R.id.goods_price, "总价：￥" + item.getPrice())
                .setText(R.id.goods_num, "数量："+item.getAmount() + "")
                .addOnClickListener(R.id.edit_button)
                .addOnClickListener(R.id.qr_code)
                .addOnClickListener(R.id.commit_makeSure);

    }
}
