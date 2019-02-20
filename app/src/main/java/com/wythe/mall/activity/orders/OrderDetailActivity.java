package com.wythe.mall.activity.orders;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.reflect.TypeToken;
import com.wythe.mall.R;
import com.wythe.mall.activity.BaseActivity;
import com.wythe.mall.activity.EvaluateActivity;
import com.wythe.mall.activity.ReturnGoodsActivity;
import com.wythe.mall.adapter.OrderDetailAdapter;
import com.wythe.mall.beans.Customer;
import com.wythe.mall.beans.OrderDetailBean;
import com.wythe.mall.http.datasource.MDataSource;
import com.wythe.mall.sqlite.LoadListCallBack;
import com.wythe.mall.utils.QRCodeUtil;
import com.wythe.mall.utils.StatusBarUtil;
import com.wythe.mall.utils.ToastUtil;
import com.wythe.mall.utils.UsualDialogger;
import com.wythe.mall.view.TitleBarView;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Request;


public class OrderDetailActivity extends BaseActivity {

    @Bind(R.id.order_titleView)
    TitleBarView titleView;
    @Bind(R.id.orderDetail_listView)
    RecyclerView recyclerView;
    //    @Bind(R.id.refreshLayout)
//    SmartRefreshLayout refreshLayout;
    //设置adapter
    private OrderDetailAdapter adapter;
    String orderId;//订单号
    String ali_OrderId;//支付宝内部订单号
    String status;//订单状态
    String toatolMoney;//订单金额
    private UsualDialogger dialog2 = null;

    public OrderDetailActivity getInstance() {
        return instance;
    }

    public static OrderDetailActivity instance ;
    private String refoundStutas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        ButterKnife.bind(this);
        StatusBarUtil.setPaddingSmart(this, titleView);
        initView();
        initData();
        instance = this ;
    }

    private String content;
    private String logo ,tradeNum,orderDetailKey;

    @Override
    protected void initView() {
        //设置标题
        orderId = getIntent().getStringExtra("orderId");
        toatolMoney = getIntent().getStringExtra("price");
        content = getIntent().getStringExtra("content");
        logo = getIntent().getStringExtra("image");
        tradeNum = getIntent().getStringExtra("aliNo");
        status = getIntent().getStringExtra("status");
        orderDetailKey = getIntent().getStringExtra("orderDetailKey");
        refoundStutas=getIntent().getStringExtra("refundStatus");
        titleView.setTitle("订单详情");
        titleView.showBackButton(true);
        adapter = new OrderDetailAdapter(R.layout.order_detail_items, new ArrayList<OrderDetailBean>(), status,refoundStutas);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        //评价点击按钮
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

                switch (view.getId()) {
                    case R.id.edit_button:

                        //                item.getOrderNumber()
                        if ("1".equals(status)) {
                            return;
                        } else {
                            if (status.equals("2") || status.equals("3")) {
                                //退款页面
                                Intent intent = new Intent(OrderDetailActivity.this, ReturnGoodsActivity.class);
                                intent.putExtra("money", toatolMoney);
                                intent.putExtra("orderId", orderId);
                                intent.putExtra("content", content);
                                intent.putExtra("logo", logo);
                                intent.putExtra("tradeNum", tradeNum);
                                intent.putExtra("orderDetailKey", orderDetailKey);



                                startActivity(intent);
                            } else {
                                //评价页面跳转
//                                commit_MakeSure();

                                OrderDetailBean item = (OrderDetailBean) adapter.getItem(position);
                                Intent intent = new Intent(OrderDetailActivity.this, EvaluateActivity.class);
                                Log.e("OrderDetailActivity", item.getImage());
                                Log.e("OrderDetailActivity", item.getAmount());
                                Log.e("OrderDetailActivity", item.getCreateTime());
                                Log.e("OrderDetailActivity", item.getName());
                                Log.e("OrderDetailActivity", item.getNormName());
                                Log.e("OrderDetailActivity", item.getPrice());
                                intent.putExtra("evaluate_bean", item);
                                startActivity(intent);
                            }
                        }
                        break;
                    case R.id.qr_code:
                        // 跳转到查看二维码页面

                        getQR_Code();
                        break;
                    case R.id.commit_makeSure:
                        commit_MakeSure();
                        finish();
                        break;
                }
            }
        });
    }


    //确认收货
    private void commit_MakeSure(){
        HashMap<String, String> map = new HashMap<>();
        map.put("model", "interfaces");
        map.put("method", "makeSure");
        map.put("orderNumber", orderId);
        Log.e("订单号===",orderId);
        MDataSource<Customer> dataSource = new MDataSource<>(this);
        dataSource.postData(map,new LoadListCallBack() {
            @Override
            public void loadList(List list) {
            }

            @Override
            public void systemError(Request request, String errorInfo, Exception e) {
                ToastUtil.makeText(OrderDetailActivity.this, "加载失败，请稍后重试");
            }

            @Override
            public void retLoad(String code) {

            }

            @Override
            public void loadString(String string) {
                Log.e(TAG, "loadString: "+string );
                ToastUtil.makeText(OrderDetailActivity.this,"确认收货成功");
            }
        });
    }

    //获取二维码信息
    private void getQR_Code() {
        HashMap<String, String> map = new HashMap<>();
        map.put("model", "interfaces");
        map.put("method", "getQR_CODE");
        map.put("orderNumber", orderId);
        Log.e("订单号===", orderId);
        MDataSource<Customer> dataSource = new MDataSource<>(this);
        dataSource.postData(map, new LoadListCallBack() {
            @Override
            public void loadList(List list) {
            }

            @Override
            public void systemError(Request request, String errorInfo, Exception e) {
                ToastUtil.makeText(OrderDetailActivity.this, "加载失败，请稍后重试");
            }

            @Override
            public void retLoad(String code) {

            }

            @Override
            public void loadString(String string) {
                try {
                    Log.e("订单二维码信息", string.toString());
                    Bitmap bitmap = QRCodeUtil.createQRCodeBitmap(string, 480);
                    dialog2 = UsualDialogger.Builder(OrderDetailActivity.this).setTitle("查看二维码")
                            .setMessage(bitmap)
                            .setOnConfirmClickListener("确定", new UsualDialogger.onConfirmClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialog2.dismiss();
                                }
                            })
                            .setOnCancelClickListener("取消", new UsualDialogger.onCancelClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialog2.dismiss();
                                }
                            })
                            .build().shown();
                } catch (Exception e) {
                    Log.e("格式错误", e.getMessage());
                    ToastUtil.makeText(OrderDetailActivity.this, e.getMessage() + "");
                }
            }
        });
    }


    /***
     * 加载数据列表
     */
    private void initData() {
        HashMap<String, String> map = new HashMap<>();
        map.put("model", "interfaces");
        map.put("method", "orderListDetail");
        map.put("orderNumber", orderId);
        Type type = new TypeToken<List<OrderDetailBean>>() {
        }.getType();
        MDataSource<OrderDetailBean> dataSource = new MDataSource<>("orderDetail" + orderId,
                false, false, map, this, OrderDetailBean.class, type);
        dataSource.refreshData(new LoadListCallBack() {
            @Override
            public void loadList(List list) {
                if (null != list && !list.isEmpty()) {
                    adapter.setNewData(list);
                }
            }

            @Override
            public void systemError(Request request, String errorInfo, Exception e) {
                ToastUtil.makeText(OrderDetailActivity.this, "加载失败，请稍后重试");
            }

            @Override
            public void retLoad(String code) {

            }

            @Override
            public void loadString(String string) {

            }
        });
    }
}
