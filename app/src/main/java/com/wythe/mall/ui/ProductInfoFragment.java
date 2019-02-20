package com.wythe.mall.ui;

import android.content.Context;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.reflect.TypeToken;
import com.wythe.mall.R;
import com.wythe.mall.activity.ProductDetailActivity;
import com.wythe.mall.adapter.prodetails.DetailsAdapter;
import com.wythe.mall.beans.ImageBean;
import com.wythe.mall.http.datasource.MDataSource;
import com.wythe.mall.sqlite.LoadListCallBack;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.Request;

/**
 * Created by wythe on 2016/7/24.
 */
public class ProductInfoFragment extends Fragment {

    private View view;
    private Context context;
    String goodsId;
    private RecyclerView recyclerView;
    private List<ImageBean> dataList = new ArrayList<>();
    private DetailsAdapter adapter;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        goodsId = ((ProductDetailActivity) context).getGoodsId();

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (null == view) {
            view = inflater.inflate(R.layout.fragment_product_info1, container, false);
        } else {
            ((ViewGroup) view.getParent()).removeAllViews();
        }
        initView();
        initData();
        return view;
    }

    protected void initView() {
        recyclerView = view.findViewById(R.id.productInfo_recyclerView);
        LinearLayoutManager manager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(manager);
        adapter = new DetailsAdapter(R.layout.detail_layout_items, dataList);
        recyclerView.setAdapter(adapter);
        adapter.setHeaderFooterEmpty(true,true);
        View view = LayoutInflater.from(context).inflate(R.layout.shengming,null);
        adapter.addFooterView(view);
    }

    /**
     * 设置商品详情信息
     */
    private void initData() {
        HashMap<String, String> map = new HashMap<>();
        map.put("model", "interfaces");
        map.put("method", "goodsDetailImg");
        map.put("goodsKey", goodsId);
        Type mType = new TypeToken<List<ImageBean>>() {
        }.getType();
        MDataSource<ImageBean> dataSource = new MDataSource<ImageBean>("productDetailsAdList",
                false, false, map, context, ImageBean.class, mType);
        dataSource.refreshData(new LoadListCallBack() {
            @Override
            public void loadList(List list) {
                if (null != list && !list.isEmpty()) {
                    adapter.setNewData(list);
                } else {
                    //暂无详情数据
                    adapter.setEmptyView(R.layout.no_content_view, recyclerView);
                }
            }

            @Override
            public void systemError(Request request, String errorInfo, Exception e) {

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
