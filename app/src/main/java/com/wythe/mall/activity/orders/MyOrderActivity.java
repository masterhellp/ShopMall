package com.wythe.mall.activity.orders;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.wythe.mall.R;
import com.wythe.mall.activity.BaseActivity;
import com.wythe.mall.adapter.shopcar.MyOrderListAdapter;
import com.wythe.mall.beans.OrdersBean;
import com.wythe.mall.http.datasource.MDataSource;
import com.wythe.mall.sqlite.LoadListCallBack;
import com.wythe.mall.utils.CommonUtils;
import com.wythe.mall.utils.SharedPreUtils;
import com.wythe.mall.utils.StatusBarUtil;
import com.wythe.mall.utils.ToastUtil;
import com.wythe.mall.view.TitleBarView;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Request;

/**
 * 我的订单页面
 */
public class MyOrderActivity extends BaseActivity {

    @Bind(R.id.order_titleView)
    TitleBarView titleView;
    @Bind(R.id.order_listView)
    ListView listView;
    @Bind(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @Bind(R.id.order_noContent)
    RelativeLayout noContentView;
    //adapter
    private MyOrderListAdapter adapter;
    //本地未付款的数据
    private List<OrdersBean> localList = new ArrayList<>();
    private List<OrdersBean> completeList = new ArrayList<>();
    //数据源
    private MDataSource<OrdersBean> dataSource;
    //刷新或者加载失败
    private int refreshOrMore = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);
        ButterKnife.bind(this);
        StatusBarUtil.setPaddingSmart(this, titleView);
        initView();
        initData();
    }

    /**
     * 初始化布局文件
     */
    @Override
    protected void initView() {
        //设置标题
        titleView.setTitle("我的订单");
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                //刷新数据
                refreshOrMore = 1;
                if (null != dataSource) {
                    dataSource.refreshData(comCallBack);
                }
            }
        });
        //加载更多
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                //加载更多
                refreshOrMore = 2;
                if (null != dataSource) {
                    dataSource.refreshData(comCallBack);
                }
            }
        });
        refreshLayout.setEnableAutoLoadmore(true);
//        Type type = new TypeToken<List<OrdersBean>>() {
//        }.getType();
//        localList = SharedPreUtils.getListData(this, "wait_to_fail", type);
        adapter = new MyOrderListAdapter(this, localList);
        listView.setAdapter(adapter);
        //点击列表到订单详情页
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //获取订单编号
                OrdersBean item = (OrdersBean) parent.getItemAtPosition(position);
                Intent intent = new Intent(MyOrderActivity.this, OrderDetailActivity.class);
                Log.e("订单号--Id", item.getOrderNumber());
                Log.e("价格", item.getTotalMoney());
                Log.e("用户名==", SharedPreUtils.getPreStringInfo(MyOrderActivity.this, "userName"));
                Log.e("支付宝内部订单号", item.getTradeNumber());
                Log.e("退款状态",item.getRefundStatus());
                intent.putExtra("orderId", item.getOrderNumber());
                intent.putExtra("refundStatus", item.getRefundStatus());
                intent.putExtra("price", item.getTotalMoney());
                intent.putExtra("aliNo", item.getTradeNumber());
                intent.putExtra("status", item.getStatus());
                intent.putExtra("orderDetailKey", item.getOrderDetailKey());
                //         viewHolder.contentView.setText("购买了" +);
                intent.putExtra("content", item.getName() + "等" + item.getGoodsCount() + "件商品");
                intent.putExtra("image", item.getShopLogo());
                intent.putExtra("tradeNum", item.getTradeNumber());
                Log.e("orderDetailKey==", item.getOrderDetailKey());
                startActivity(intent);

            }
        });
    }

    /**
     * 加载数据
     * 分类两部分：
     * 1. 代付款的订单 15分钟之内未付款的 从本地获取--- 暂不获取
     * 2.已经完成的订单 //从服务端获取
     */
    private void initData() {

        loadDataList();
        //获取未付款的订单数据
//        Log.e(TAG, "initData: " + localList.size());
//
//        for (int i = 0; i < 10; i++) {
//            OrdersBean item = new OrdersBean();
//            item.setImage("http://pic.qingk.cn/image/z/2017/02/f4296aaa87cd4741b1cd1ec9b12ba725_310.jpg");
//            item.setPrice(i + "123" + i);
//            item.setContent(i + "风干牛肉干" + i);
//            item.setStatus("complete");
//            item.setCreateTime("2" + i + "12-12-12 " + i + ":20");
//            item.setShopName(i + "隔壁老王开的店");
//            completeList.add(item);
//        }
//        setDataList(completeList);
//
//        // 获取付款完成的数据
//        HashMap<String, String> map = new HashMap<>();
//        map.put("model", "");
//        map.put("method", "");
//        //---添加参数。。。
//        Type mType = new TypeToken<List<OrdersBean>>() {
//        }.getType();
//        MDataSource<OrdersBean> dataSource = new MDataSource<>("indexHotList",
//                true, true, map, this, OrdersBean.class, mType);
//        if (dataSource.getDataList().isEmpty()) {
//            dataSource.refreshData(comCallBack);
//        } else {
//            //使用缓存数据
//            setDataList(dataSource.getDataList());
//        }
    }

    private void loadDataList() {
        /**
         * 建立请求表单，添加上传服务器的参数
         */
        HashMap<String, String> map = new HashMap<>();
        map.put("model", "interfaces");
        map.put("method", "orderList");
        String customerKey = SharedPreUtils.getPreStringInfo(this, "userName");
        if (CommonUtils.isEmpty(customerKey)) {
            customerKey = "51";
        }
        map.put("customerKey", customerKey);
        Type type = new TypeToken<List<OrdersBean>>() {
        }.getType();
        dataSource = new MDataSource<>("myOrderList" + customerKey,
                false, true, map, this, OrdersBean.class, type);
        dataSource.refreshData(comCallBack);
    }

    /**
     * 获取数据的回调
     */
    private LoadListCallBack<OrdersBean> comCallBack = new LoadListCallBack<OrdersBean>() {
        @Override
        public void loadList(List<OrdersBean> list) {
            setDataList(dataSource.getTotalCount(), list);
        }

        @Override
        public void systemError(Request request, String errorInfo, Exception e) {
            if (null != adapter && !adapter.viewList.isEmpty()) {
                if (refreshOrMore == 1) {
                    refreshLayout.finishRefresh(false);
                } else if (refreshOrMore == 2) {
                    refreshLayout.finishLoadmore(false);
                }
            } else {
                ToastUtil.makeText(MyOrderActivity.this, "加载失败，请稍后重试");
            }

        }

        @Override
        public void retLoad(String code) {

        }

        @Override
        public void loadString(String string) {

        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        loadDataList();
    }

    /**
     * 拼接数据列表
     * orderList?customerKey=
     *
     * @param dataList
     */
    private void setDataList(int totalCount, List<OrdersBean> dataList) {
        if (dataList.isEmpty()) {
            //暂时还没有叮当数据
            noContentView.setVisibility(View.VISIBLE);
            refreshLayout.setVisibility(View.GONE);
        } else {
//            this.completeList = dataList;
//            List<OrdersBean> tempList = new ArrayList<>();
//            tempList.addAll(localList);
//            tempList.addAll(completeList);
            adapter.setResultList(dataList);
            adapter.notifyDataSetChanged();
            noContentView.setVisibility(View.GONE);
            refreshLayout.setVisibility(View.VISIBLE);
            //是否显示加载更多
            if (dataList.size() >= totalCount) {
                refreshLayout.finishLoadmoreWithNoMoreData();
            }
            refreshLayout.finishLoadmore();
            refreshLayout.finishRefresh();
        }

    }


}
