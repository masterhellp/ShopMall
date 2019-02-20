package com.wythe.mall.activity.address;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.google.gson.reflect.TypeToken;
import com.wythe.mall.R;
import com.wythe.mall.activity.BaseActivity;
import com.wythe.mall.activity.orders.FirmOrderActivity;
import com.wythe.mall.adapter.address.AddressListAdapter;
import com.wythe.mall.beans.NewAddressBean;
import com.wythe.mall.http.datasource.MDataSource;
import com.wythe.mall.sqlite.LoadListCallBack;
import com.wythe.mall.utils.StatusBarUtil;
import com.wythe.mall.view.TitleBarView;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Request;

/**
 * 选择收货地址
 */
public class AddressListActivity extends BaseActivity {

    @Bind(R.id.address_listView)
    ListView listView;
    @Bind(R.id.address_titleView)
    TitleBarView titleView;
    @Bind(R.id.rl_no_contant)
    RelativeLayout noContent;
    private AddressListAdapter adapter;
    MDataSource<NewAddressBean> dataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_list);
        ButterKnife.bind(this);
        StatusBarUtil.setPaddingSmart(this, titleView);
        //初始化布局文件
        initLayout();

    }


    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    /**
     * 初始化布局文件
     */
    private void initLayout() {
        //设置标题
        titleView.setTitle("选择地址");
        titleView.showAddAddressButton(true);
        adapter = new AddressListAdapter(this);
        listView.setAdapter(adapter);
        //点击选择
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //选择地址 返回上一层页面
                NewAddressBean item = (NewAddressBean) parent.getItemAtPosition(position);
                //选中地址 返回上一层页面
                Intent intent = new Intent(AddressListActivity.this, FirmOrderActivity.class);
                intent.putExtra("address", item);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    private void loadData() {
        //提交内容
        HashMap<String, String> map = new HashMap<>();
        map.put("model", "interfaces");
        map.put("method", "getCustomerAddress");
        map.put("customerKey", "51");
        Type mType = new TypeToken<List<NewAddressBean>>() {
        }.getType();
        dataSource = new MDataSource<>("newAddressActivity",
                false, false, map, this, NewAddressBean.class, mType);
        // 刷新数据
        dataSource.refreshData(goodsLoadListCallBack);
    }

    private LoadListCallBack<NewAddressBean> goodsLoadListCallBack = new LoadListCallBack<NewAddressBean>() {
        @Override
        public void loadList(List<NewAddressBean> list) {
            if (null != list && !list.isEmpty()) {
                adapter.setResultList(list);
                adapter.notifyDataSetChanged();
                noContent.setVisibility(View.GONE);
                listView.setVisibility(View.VISIBLE);
            } else {
                noContent.setVisibility(View.VISIBLE);
                listView.setVisibility(View.GONE);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e(TAG, "requestCode: ---" + requestCode + " ---  :" + resultCode);
        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == AppConfig.REQUESTCODE_ADDRESS && resultCode == RESULT_OK) {
//            Log.e(TAG, "onActivityResult: ---");
//            // 刷新数据
//            if (null != dataSource && null != goodsLoadListCallBack) {
//                dataSource.refreshData(goodsLoadListCallBack);
//            }
//        }
    }

}
