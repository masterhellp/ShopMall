package com.wythe.mall.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.squareup.picasso.Picasso;
import com.wythe.mall.R;
import com.wythe.mall.adapter.ProductAdapter;
import com.wythe.mall.base.BaseApplication;
import com.wythe.mall.beans.Customer;
import com.wythe.mall.beans.MerchantinfoBean;
import com.wythe.mall.beans.ProductBean;
import com.wythe.mall.beans.ShoppingCarDataBean;
import com.wythe.mall.http.datasource.MDataSource;
import com.wythe.mall.sqlite.LoadListCallBack;
import com.wythe.mall.utils.CommonUtils;
import com.wythe.mall.utils.GotoActivity;
import com.wythe.mall.utils.GsonUtils;
import com.wythe.mall.utils.ResizeTransformation;
import com.wythe.mall.utils.SharedPreUtils;
import com.wythe.mall.view.TitleBarView;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Request;

public class MerchantInfoActivity extends BaseActivity implements View.OnClickListener {
    private TitleBarView titleBarView;
    private GridView gv_ShopInfo;
    private SmartRefreshLayout merinfo_refresh;
    private ProductBean bean;
    private TextView concern;//关注
    private ProductAdapter adapter;
    private boolean isconcern = false;//false 未关注
    private String shop_key;
    private TextView shop_Name;
    private ImageView iv_Logo;
    private List<MerchantinfoBean > data;


    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            data=new ArrayList<MerchantinfoBean>();
            if(msg.what==1){
                data= (List<MerchantinfoBean>) msg.obj;
                adapter = new ProductAdapter(data, MerchantInfoActivity.this);
                gv_ShopInfo.setAdapter(adapter);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant_info);
        Intent intent=getIntent();
        shop_key=intent.getStringExtra("shopId");
        Log.e("店铺详情===ID==",shop_key);
        initView();
        initListener();
        postRequest(shop_key);
    }

    /**
     *
     * 关注店铺
     * */
    private void follow(String shopkey,String userid) {
        /**
         * 建立请求表单，添加上传服务器的参数
         */
        HashMap<String, String> map = new HashMap<>();
        map.put("model", "interfaces");
        map.put("method", "appLogin");
        map.put("shopkey",shopkey);
        map.put("userid",userid);
        MDataSource<Customer> dataSource = new MDataSource<>(this);
        dataSource.postData(map, new LoadListCallBack() {
            @Override
            public void loadList(List list) {

            }

            @Override
            public void systemError(Request request, String errorInfo, Exception e) {
                Log.e(TAG, "systemError: " + errorInfo);
                e.printStackTrace();
            }

            @Override
            public void retLoad(String code) {

            }

            @Override
            public void loadString(String string) {
                Log.e(TAG, "loadString 关注店铺 : " + string);
            }
        });
    }


    /**
     *
     * 取消关注店铺
     * */
    private void followFail(String shopkey,String userid) {
        /**
         * 建立请求表单，添加上传服务器的参数
         */
        HashMap<String, String> map = new HashMap<>();
        map.put("model", "interfaces");
        map.put("method", "appLogin");
        map.put("shopkey",shopkey);
        map.put("userid",userid);
        MDataSource<Customer> dataSource = new MDataSource<>(this);
        dataSource.postData(map, new LoadListCallBack() {
            @Override
            public void loadList(List list) {

            }

            @Override
            public void systemError(Request request, String errorInfo, Exception e) {
                Log.e(TAG, "systemError: " + errorInfo);
                e.printStackTrace();
            }

            @Override
            public void retLoad(String code) {

            }

            @Override
            public void loadString(String string) {
                Log.e(TAG, "loadString 关注店铺 : " + string);
            }
        });
    }

    private void postRequest(String shopkey) {
        /**
         * 建立请求表单，添加上传服务器的参数
         */
        HashMap<String, String> map = new HashMap<>();
        map.put("model", "interfaces");
        map.put("method", "shopDetail");
        map.put("shopKey",shopkey);
        Type mType = new TypeToken<List<MerchantinfoBean>>() {
        }.getType();
        MDataSource<MerchantinfoBean> dataSource = new MDataSource<>("merchantinfo",
                false, false, map, MerchantInfoActivity.this, MerchantinfoBean.class, mType);
        if (dataSource.getDataList().isEmpty()) {
            dataSource.refreshData(listCallBack);
        } else {
            //使用缓存数据
            dataSource.refreshData(listCallBack);
        }
    }

    /**
     * 设置获取广告的回调
     */
    private LoadListCallBack<MerchantinfoBean> listCallBack = new LoadListCallBack<MerchantinfoBean>() {
        @Override
        public void loadList(List<MerchantinfoBean> list) {
            Log.e(TAG, "loadList:--- " + list.size());
            shop_Name.setText(list.get(0).getShopName());
            Log.e(TAG, "shop_Name:--- " + shop_Name);
            Log.e(TAG, "logo:--- " + list.get(0).getLogo());
            Picasso.with(MerchantInfoActivity.this)
                    .load(list.get(0).getLogo())
                    .placeholder(R.mipmap.a)
                    .error(R.mipmap.a)
                    .into(iv_Logo);
            Message msg=Message.obtain();
            msg.what=1;
            msg.obj=list;
            handler.sendMessage(msg);


        }

        @Override
        public void systemError(Request request, String errorInfo, Exception e) {
                Log.e(TAG,"errorInfo=="+errorInfo.toString());
        }

        @Override
        public void retLoad(String code) {
            Log.e(TAG,"code=="+code.toString());
        }

        @Override
        public void loadString(String string) {
            Log.e("店铺详情==",string.toString());
        }
    };
    @Override
    protected void initListener() {
        super.initListener();
        concern.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.concern:
                if (!isconcern) {
                    concern.setText("√已关注");
                    concern.setBackgroundColor(Color.parseColor("#F8F8FF"));
                    isconcern = true;
//                    follow(shop_key,"userid");



                } else {
                    concern.setText("+关注");
                    concern.setBackgroundColor(Color.parseColor("#ed6938"));
                    isconcern = false;
//                    followFail(shop_key,"userid");

                }
                break;
        }
    }

    @Override
    protected void initView() {
//        super.initView();
        shop_Name=(TextView) findViewById(R.id.shop_Name);
        iv_Logo=(ImageView) findViewById(R.id.merchant_logo);
        concern = (TextView) findViewById(R.id.concern);
        gv_ShopInfo = (GridView) findViewById(R.id.gv_ShopInfo);
        merinfo_refresh = (SmartRefreshLayout) findViewById(R.id.merinfo_refresh);

        merinfo_refresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                Log.e("setOnRefreshListener==", "刷新完成");
                refreshlayout.finishRefresh();
            }
        });
        //加载更多
        merinfo_refresh.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                Log.e("setOnLoadmoreListener==", "加载完成");
                refreshlayout.finishLoadmore();
            }
        });
//        开始下拉
        merinfo_refresh.setEnableRefresh(true);//启用刷新
        merinfo_refresh.setEnableLoadmore(true);//启用加载
//        关闭下拉
        merinfo_refresh.finishRefresh();
        merinfo_refresh.finishLoadmore();

        titleBarView = (TitleBarView) findViewById(R.id.titleBarView);
        titleBarView.setTitle("店铺详情");
        titleBarView.showEditButton(false);
        titleBarView.showBackButton(true);
        gv_ShopInfo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.e("点击=====",data.get(i).getGoodsId());
                Intent intent =new Intent(MerchantInfoActivity.this,ProductDetailActivity.class);
                intent.putExtra("goodsId",data.get(i).getGoodsId());
                startActivity(intent);

            }
        });
    }
}
