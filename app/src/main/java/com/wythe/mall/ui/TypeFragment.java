package com.wythe.mall.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.wythe.mall.R;
import com.wythe.mall.activity.ProductActivity;
import com.wythe.mall.adapter.GridViewAdapter;
import com.wythe.mall.adapter.Type;
import com.wythe.mall.beans.OneTypeBean;
import com.wythe.mall.beans.TwoClassFyBean;
import com.wythe.mall.http.datasource.MDataSource;
import com.wythe.mall.sqlite.LoadListCallBack;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import okhttp3.Request;

/**
 * @author anumbrella
 * @date 2015-8-1
 * <p>
 * 用于右侧栏显示详细信息
 * <p>
 * (滑动的ViewPager,每一个滑动的页面都是一个fragment)
 */
public class TypeFragment extends Fragment implements View.OnClickListener {

    private ArrayList<Type> listType;
    // widget网格view
    private GridView gridView;
    // 网格适配器
    private GridViewAdapter adapter;
    private SmartRefreshLayout refreshLayout;

    // 右侧栏目中的子选项
    private Type type;

    // 子选项名称
    private String typeName;

    private List<OneTypeBean> data;
    private List<TwoClassFyBean> list;
    private List<TwoClassFyBean> list_T;
    private int num;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                list = new ArrayList<>();

                list = (List<TwoClassFyBean>) msg.obj;

                Log.e("loadList: =====", "loadList: =====" + list.size());
                adapter = new GridViewAdapter(getActivity(), list);
                gridView.setAdapter(adapter);
            }
        }
    };

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putSerializable("list", (Serializable) list);
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        data = (List<OneTypeBean>) getArguments().getSerializable("bean");

        num = getArguments().getInt("index");
        Log.e("onCreateView==",data.get(num).getClassifyKey());
        Log.e("onResume===num",num+"");
        if (savedInstanceState != null) {
            list_T = new ArrayList<TwoClassFyBean>();
            if (list_T == list) {
                for (int i = 0; i < list.size(); i++) {
                    Log.e("onCreateView===", list.get(i).getClassifyName());
                }
            } else {
                getClassTwoFy(data.get(num).getClassifyKey());
            }

        } else {
            getClassTwoFy(data.get(num).getClassifyKey());
        }

        View view = inflater.inflate(R.layout.shopslist_layout, null);

        gridView = (GridView) view.findViewById(R.id.GridViewList);
        refreshLayout = (SmartRefreshLayout) view.findViewById(R.id.smart_Refresh);
//		layout_Quality=(LinearLayout) view.findViewById(R.id.layout_Quality);
//		layout_Flavor=(LinearLayout) view.findViewById(R.id.layout_Flavor);
//		layout_Work=(LinearLayout) view.findViewById(R.id.layout_Work);
//		llZhuangchang = (LinearLayout) view.findViewById(R.id.shop_zhuanchang);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                Log.e("setOnRefreshListener==", "刷新完成");
                refreshlayout.finishRefresh();
            }
        });
        //加载更多
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                Log.e("setOnLoadmoreListener==", "加载完成");
                refreshlayout.finishLoadmore();
            }
        });
//        开始下拉
        refreshLayout.setEnableRefresh(true);//启用刷新
        refreshLayout.setEnableLoadmore(true);//启用加载
//        关闭下拉
        refreshLayout.finishRefresh();
        refreshLayout.finishLoadmore();
//		topImage = (ImageView) view.findViewById(R.id.topImage);


        Log.e("key==", num + "");
        for (int i = 0; i < data.size(); i++) {
            Log.e("key==", data.get(i).getClassifyKey());
            Log.e("name==", data.get(i).getClassifyName());

        }
//		if (index == 0){
//			llZhuangchang.setVisibility(View.VISIBLE);
//			llZhuangchang.setOnClickListener(this);
//		}
        bindLinstener();
        typeName = data.get(num).getClassifyName();
//		icon = DataModel.iconList[index];

        ((TextView) view.findViewById(R.id.TypeName)).setText(typeName);

        // 为listType装载数据
        getClassTwoFy(data.get(num).getClassifyKey());
        gridView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TwoClassFyBean item = (TwoClassFyBean) parent.getItemAtPosition(position);
                Intent intent = new Intent(getContext(), ProductActivity.class);
                intent.putExtra("pageType", "classify");
                intent.putExtra("classifyId", item.getClassifyKey());
                Log.e("你点击的是==",item.getQualification());
                startActivity(intent);
                Toast.makeText(getActivity(), "你点击的是" + typeName, Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }


    private void bindLinstener() {
//		layout_Quality.setOnClickListener(this);
//		layout_Flavor.setOnClickListener(this);
//		layout_Work.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("onResume==",data.get(num).getClassifyKey());
        Log.e("onResume==num",num+"");
        getClassTwoFy(data.get(num).getClassifyKey());

    }

    private void getClassTwoFy(String key) {
        HashMap<String, String> map = new HashMap<>();
        map.put("model", "interfaces");
        map.put("method", "classifyTwo");
        map.put("parentKey", key);
        java.lang.reflect.Type mType = new TypeToken<List<TwoClassFyBean>>() {
        }.getType();
        MDataSource<TwoClassFyBean> dataSource = new MDataSource<>("typefragment",
                false, false, map, getActivity(), TwoClassFyBean.class, mType);
        //接口提交  返回上一个页面
        if (dataSource.getDataList().isEmpty()) {
            dataSource.refreshData(goodsLoadListCallBack);
        } else {
            //使用缓存数据
            // setGoodsList(dataSource.getDataList(), AppConfig.INDEX_RECOM_GOODS);
            // 刷新数据
            dataSource.refreshData(goodsLoadListCallBack);
        }
    }

    private LoadListCallBack<TwoClassFyBean> goodsLoadListCallBack = new LoadListCallBack<TwoClassFyBean>() {
        @Override
        public void loadList(List<TwoClassFyBean> list) {
            Message message = Message.obtain();
            message.what = 1;
            message.obj = list;
            handler.sendMessage(message);

        }

        @Override
        public void systemError(Request request, String errorInfo, Exception e) {
            Log.e("loadList: =====", "loadList: =====" + errorInfo);
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
    public void onClick(View v) {
        switch (v.getId()) {
//			case R.id.layout_Quality:
//				GotoActivity.gotoActiviy(getActivity(), ProductActivity.class);
//				break;
//			case R.id.layout_Flavor:
//				GotoActivity.gotoActiviy(getActivity(), ProductActivity.class);
//				break;
//			case R.id.layout_Work:
//				GotoActivity.gotoActiviy(getActivity(), ProductActivity.class);
//				break;
        }
    }

}
