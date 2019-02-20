package com.wythe.mall.adapter.shopcar;

import android.app.Activity;
import android.content.Context;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.coorchice.library.SuperTextView;
import com.squareup.picasso.Picasso;
import com.wythe.mall.R;
import com.wythe.mall.adapter.IPullToRefreshListAdapter;
import com.wythe.mall.beans.OrdersBean;
import com.wythe.mall.utils.CommonUtils;
import com.wythe.mall.utils.DateUtil;
import com.wythe.mall.utils.PrintLog;
import com.wythe.mall.utils.SharedPreUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MyOrderListAdapter extends IPullToRefreshListAdapter<OrdersBean> {


    // 未付款的订单
    private List<OrdersBean> wairPayList = new ArrayList<>();

    public MyOrderListAdapter(Context context, List<OrdersBean> localList) {
        super(context);
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.wairPayList = localList;

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (null == convertView) {
            convertView = inflater.inflate(R.layout.my_order_items, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        OrdersBean item = getItem(position);
        Log.e("test", "getView: " + viewHolder.imageView + "----" + item.getImage());
//        //设置数据信息
        Glide.with(context).load(item.getShopLogo()).into(viewHolder.imageView);
        Picasso.with(context)
                .load(item.getShopLogo())
                .error(R.mipmap.a)
                .placeholder(R.mipmap.a)
                .into(viewHolder.imageView);
//        SuperTextView editButton = viewHolder.editView;
//        String status = "订单已完成，去评价";
//        if ("waitPay".equals(item.getStatus())) {
//            status = "订单待支付";
//            editButton.setStrokeColor(Color.RED);
//            editButton.setVisibility(View.VISIBLE);
//            setTimeAndStart(item.getCreateTime(), editButton, item);
//        } else {
//            editButton.setVisibility(View.GONE);
//        }
        viewHolder.shopNameView.setText("订单号：" + item.getOrderNumber());
//        viewHolder.statusView.setText(status);

//        viewHolder.statusView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(context, EvaluateActivity.class);
//                context.startActivity(intent);
//            }
//        });

        viewHolder.timeView.setText(item.getPayTime());
        viewHolder.contentView.setText("购买了" + item.getName() + "等" + item.getGoodsCount() + "件商品");
        viewHolder.moneyView.setText("￥" + item.getTotalMoney());
        Log.e("订单状态=====",item.getStatus());
        if(item.getRefundStatus().equals("无")) {

            if (item.getStatus().equals("1")) {
                viewHolder.refound.setText("待付款");

            }
            if (item.getStatus().equals("2")) {
                viewHolder.refound.setText("已付款");
            }
            if (item.getStatus().equals("3")) {
                viewHolder.refound.setText("已发货");
            }
            if (item.getStatus().equals("4")) {
                viewHolder.refound.setText("交易成功");
            }
        }else {
            /**
             *
             * 0无，1退款中等待卖家处理，2退款中卖家已同意退款，3退款成功，4卖家拒绝退款，5买家撤销退款，6买家确认收货关闭,7卖家完成订单关闭
             * */
            if (item.getRefundStatus().equals("退款中等待卖家处理")) {
                viewHolder.refound.setText("退款中等待卖家处理");
            }
            if (item.getRefundStatus().equals("退款中卖家已同意退款")) {
                viewHolder.refound.setText("退款中卖家已同意退款");
            }
            if (item.getRefundStatus().equals("退款成功")) {
                viewHolder.refound.setText("退款成功");
            }
            if (item.getRefundStatus().equals("卖家继续发货拒绝退款")) {
                viewHolder.refound.setText("卖家继续发货拒绝退款");
            }
            if (item.getRefundStatus().equals("买家撤销退款")) {
                viewHolder.refound.setText("买家撤销退款");
            }
            if (item.getRefundStatus().equals("买家确认收货关闭")) {
                viewHolder.refound.setText("买家确认收货关闭");
            }
        }




        //        if (!"6".equals(item.getStatus()) && !"7".equals(item.getStatus())) {
//            //显示退款按钮
//            viewHolder.refound.setVisibility(View.VISIBLE);
//            viewHolder.refound.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    CommonUtils.openKeFu(context);
//                }
//            });
//        } else {
//            viewHolder.refound.setVisibility(View.GONE);
//        }
//        //设置标签名称
//        if (position > 1) {
//            OrdersBean lastItem = viewList.get(position - 1);
//            if (lastItem.getStatus().equals(item.getStatus())) {
//                //如果当前订单状态与上一个相同则隐藏该标签
//                viewHolder.tabView.setVisibility(View.GONE);
//            } else {
//                if ("waitPay".equals(item.getStatus())) {
//                    viewHolder.tabView.setText("待支付订单");
//                } else {
//                    viewHolder.tabView.setText("已完成订单");
//                }
//                viewHolder.tabView.setVisibility(View.VISIBLE);
//            }
//        } else {
//            if ("waitPay".equals(item.getStatus())) {
//                viewHolder.tabView.setText("待支付订单");
//            } else {
//                viewHolder.tabView.setText("已完成订单");
//            }
//            viewHolder.tabView.setVisibility(View.VISIBLE);
//        }
        return convertView;
    }


    static class ViewHolder {
        @Bind(R.id.order_tabTitle)
        TextView tabView;
        @Bind(R.id.shop_image)
        ImageView imageView;
        @Bind(R.id.shop_Name)
        TextView shopNameView;
        @Bind(R.id.create_time)
        TextView timeView;
        @Bind(R.id.order_money)
        TextView moneyView;
        @Bind(R.id.order_content)
        TextView contentView;
        @Bind(R.id.edit_button)
        SuperTextView editView;
        @Bind(R.id.refount)
        SuperTextView refound;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

    }


    /**
     * 设置定时开始时间 并启动定时器
     *
     * @param time
     */
    public void setTimeAndStart(final String time, final TextView timeView, final OrdersBean item) {
        CountDownTimer timer;
        //将时间转换为long  15分钟时间差
        final long futhureTime = DateUtil.dateStringToLong(time, "yy-MM-dd hh:mm") + 15 * 60 * 1000;
        //当前时间
        long currentTime = System.currentTimeMillis();
        //倒计时与当前时间的时间差
        long totalTime = futhureTime - currentTime;
        PrintLog.printDebug("CountTimeView", "--倒数计时总时间 ：--" + totalTime);
        timer = new CountDownTimer(totalTime, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                //每隔1000ms执行一次该方法 用来更新 时间显示
                if (millisUntilFinished > 0) {
                    longToString(millisUntilFinished, timeView);
                } else {
                    cancel();
                    removeTimeOffItem(item);
                }
            }

            @Override
            public void onFinish() {
                //计时结束时  需要执行的事件 移除掉该未付款的数据
                cancel();
                removeTimeOffItem(item);
            }
        };
        //启动计时器
        timer.start();
    }

    private void removeTimeOffItem(OrdersBean item) {
        //移除该条数据
        viewList.remove(item);
        notifyDataSetChanged();
        //从本地库删除该数据
        wairPayList.remove(item);
        SharedPreUtils.putListData(context, "wait_to_fail", wairPayList);

    }

    /**
     * 将倒计时时间显示在控件上
     * 在主线程中倒计时vView
     *
     * @param tempTime
     */

    public static final long SECOND = 1000;
    public static final long MINUTE = SECOND * 60;
    public static final long HOURS = MINUTE * 60;
    public static final long DAY = HOURS * 24;

    private void longToString(final long tempTime, final TextView timeView) {

        ((Activity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                long day = tempTime / DAY;
                long hour = (tempTime - DAY * day) / HOURS;
                long minute = (tempTime - DAY * day - HOURS * hour) / MINUTE;
                long second = (tempTime - DAY * day - HOURS * hour - MINUTE * minute) / SECOND;
                // 显示分
                String minutes, seconds;
                if (minute <= 0) {
                    minutes = "00";
                } else {
                    minutes = formatNumString(minute);
                }
                // 显示秒
                if (second <= 0) {
                    seconds = "00";
                } else {
                    seconds = formatNumString(second);
                }
                timeView.setText("去付款:" + minutes + "分" + seconds + "秒");
            }
        });
    }

    /**
     * 格式化数字格式
     *
     * @param num num < 10 显示为 01 02
     * @return
     */
    private String formatNumString(long num) {
        String numString = "";
        if (num < 10) {
            numString = "0" + num;
        } else {
            numString = String.valueOf(num);
        }
        return numString;
    }


}
