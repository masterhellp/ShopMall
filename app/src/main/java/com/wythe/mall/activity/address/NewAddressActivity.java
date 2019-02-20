package com.wythe.mall.activity.address;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.google.gson.reflect.TypeToken;
import com.wythe.mall.R;
import com.wythe.mall.activity.BaseActivity;
import com.wythe.mall.adapter.address.AddressListAdapter;
import com.wythe.mall.adapter.address.NewAddressAdapter;
import com.wythe.mall.beans.NewAddressBean;
import com.wythe.mall.http.datasource.MDataSource;
import com.wythe.mall.http.okhttp.utils.L;
import com.wythe.mall.sqlite.LoadListCallBack;
import com.wythe.mall.utils.StatusBarUtil;
import com.wythe.mall.view.TitleBarView;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Request;

public class NewAddressActivity extends BaseActivity {
    @Bind(R.id.newaddress_listView)
    ListView listView;
    @Bind(R.id.newaddress_titleView)
    TitleBarView titleView;
    private AddressListAdapter adapter;
    private MDataSource<NewAddressBean> dataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_address);
        ButterKnife.bind(this);
        StatusBarUtil.setPaddingSmart(this, titleView);
        //初始化布局文件
        initLayout();
    }

    private void initLayout() {
        //设置标题
        titleView.setTitle("我的地址");
        titleView.showAddAddressButton(true);
        adapter = new AddressListAdapter(this);
        listView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getAddressID();
    }

    private void getAddressID() {
        //提交内容
        HashMap<String, String> map = new HashMap<>();
        map.put("model", "interfaces");
        map.put("method", "getCustomerAddress");
        map.put("customerKey", "51");
        Type mType = new TypeToken<List<NewAddressBean>>() {
        }.getType();
        dataSource = new MDataSource<>("newAddressActivity",
                false, false, map, NewAddressActivity.this, NewAddressBean.class, mType);
        // 刷新数据
        dataSource.refreshData(goodsLoadListCallBack);
    }

    private LoadListCallBack<NewAddressBean> goodsLoadListCallBack = new LoadListCallBack<NewAddressBean>() {
        @Override
        public void loadList(List<NewAddressBean> list) {
            Log.e(TAG, "loadList: =====" + list.size());
            //setGoodsList(list, AppConfig.INDEX_RECOM_GOODS);
            if(null!=list && !list.isEmpty()){
                adapter.setResultList(list);
                adapter.notifyDataSetChanged();
            }
        }

        @Override
        public void systemError(Request request, String errorInfo, Exception e) {
            Log.e(TAG, "loadList: =====" + errorInfo);
            e.printStackTrace();
        }

        @Override
        public void retLoad(String code) {

        }

        @Override
        public void loadString(String string) {

        }
    };

}
