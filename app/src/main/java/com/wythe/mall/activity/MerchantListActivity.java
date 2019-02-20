package com.wythe.mall.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.wythe.mall.R;
import com.wythe.mall.adapter.MerchantListAdapter;
import com.wythe.mall.beans.Goods;
import com.wythe.mall.beans.LoginBean;
import com.wythe.mall.beans.MerchantListBean;
import com.wythe.mall.http.datasource.MDataSource;
import com.wythe.mall.sqlite.LoadListCallBack;
import com.wythe.mall.utils.AppConfig;
import com.wythe.mall.utils.StatusBarUtil;
import com.wythe.mall.utils.ToastUtil;
import com.wythe.mall.utils.UserManager;
import com.wythe.mall.view.TitleBarView;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MerchantListActivity extends BaseActivity  {
    private ListView lv_Merchant;
    private SmartRefreshLayout refreshLayout;
    private List<MerchantListBean> data;
    private List<MerchantListBean> list;
    private TitleBarView titleBarView;
    private MerchantListBean bean;
    private LinearLayout merchant_Layout;
    private TextView title_left_button;//返回
    private TextView titleText;//标题

    private MerchantListAdapter adapter;

    final OkHttpClient client = new OkHttpClient();
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==1){
                String ReturnMessage= (String) msg.obj;
                Log.e("获取的返回信息:",ReturnMessage);
                MerchantListBean bean=new Gson().fromJson(ReturnMessage,MerchantListBean.class);
                String code=bean.getCode();
                Log.e("code===",code+"");
                data=new ArrayList<MerchantListBean>();
                data.add(bean);
                adapter=new MerchantListAdapter(MerchantListActivity.this,bean.getResults().getList());
                lv_Merchant.setAdapter(adapter);
                for (int i = 0; i <bean.getResults().getList().size() ; i++) {
                    Log.e("logo===",bean.getResults().getList().get(i).getLogo());
                };

            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant_list);
        initView();
        postRequest();
    }







    /**
     * 获取商品规格
     */
    private void postRequest(){
        /**
         * 建立请求表单，添加上传服务器的参数
         */
//        RequestBody frombody=new FormBody.Builder()
//                .add("phone",phone)
//                .add("password",password)
////                .add("username",username)
//                .build();


//        Log.e("frombody==",frombody.toString());
        //发起请求
        final Request request=new Request.Builder()
                .url(AppConfig.REQUEST_URL+"interfaces/storeList")
//                .url("http://4k134lmwer.51tcp.cc:63153/interfaces/applogin?userName=admin&password=111111")
//                .post(frombody)
                .build();
        //新建一个线程，用于得到服务器响应的参数
        new Thread(new Runnable() {
            @Override
            public void run() {
                Response response=null;
                try {
                    response=client.newCall(request).execute();
                    if(response.isSuccessful()){
                        //将服务器响应的参数response.body().string())发送到hanlder中，并更新ui
                        mHandler.obtainMessage(1, response.body().string()).sendToTarget();
                    }else{
                        throw new IOException("Unexpected code:" + response);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }
//    private void getMerchantList() {
//        HashMap<String, String> map = new HashMap<>();
//        map.put("model", "interfaces");
//        map.put("method", "storeList");
//        Type mType = new TypeToken<List<MerchantListBean>>() {
//        }.getType();
//        MDataSource<MerchantListBean> dataSource = new MDataSource<>("merchantList",
//                false, false, map, this, MerchantListBean.class, mType);
//        dataSource.refreshData(new LoadListCallBack() {
//            @Override
//            public void loadList(List list) {
//                //设置商品的规格
//                if (!list.isEmpty()) {
////                    specAdapter.setDataList(list);
//                    Message msg=Message.obtain();
//                    msg.what=1;
//                    msg.obj=list;
//                    handler.sendMessage(msg);
//                    Log.e("店铺列表集合=====",list.size()+"");
//
//                }
//            }
//
//            @Override
//            public void systemError(Request request, String errorInfo, Exception e) {
//
//            }
//
//            @Override
//            public void retLoad(String code) {
//
//            }
//
//            @Override
//            public void loadString(String string) {
//
//                Log.e("返回的店铺Json====",string.toString());
//
//
//
//            }
//        });
//    }

    @Override
    protected void initView() {
//        merchant_Layout=(LinearLayout) findViewById(R.id.merchant_Layout);
//        title_left_button=(TextView) findViewById(R.id.title_left_button);
//        title_left_button.setOnClickListener(this);
        titleBarView=(TitleBarView)findViewById(R.id.titleBarView);
        titleBarView.setTitle("店铺列表");
        titleBarView.showEditButton(false);
        titleBarView.showBackButton(true);
        lv_Merchant=(ListView) findViewById(R.id.lv_Merchant);
        refreshLayout=(SmartRefreshLayout) findViewById(R.id.refreshLayout);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                Log.e("MerchantListActivity","正在刷新店铺列表");
                refreshlayout.finishRefresh();
            }
        });
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                Log.e("MerchantListActivity","正在加载店铺列表");
                refreshlayout.finishLoadmore();
            }
        });
//        开始下拉
        refreshLayout.setEnableRefresh(true);//启用刷新
        refreshLayout.setEnableLoadmore(true);//启用加载
//        关闭下拉
        refreshLayout.finishRefresh();
        refreshLayout.finishLoadmore();

        lv_Merchant.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ToastUtil.makeText(MerchantListActivity.this,"你点击了第"+(i+1)+"个");
                Intent intent=new Intent(MerchantListActivity.this,MerchantInfoActivity.class);
                startActivity(intent);
            }
        });

    }
}
