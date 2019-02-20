package com.wythe.mall.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.google.gson.reflect.TypeToken;
import com.wythe.mall.R;
import com.wythe.mall.activity.ProductDetailActivity;
import com.wythe.mall.adapter.prodetails.ChatAdapter;
import com.wythe.mall.beans.CommentBean;
import com.wythe.mall.http.datasource.MDataSource;
import com.wythe.mall.sqlite.LoadListCallBack;
import com.wythe.mall.view.NestedScrollWebView;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Request;

/**
 * 商品评价列表
 */
public class TabCFm extends Fragment {
    @Bind(R.id.proDetails_webView)
    NestedScrollWebView webView;
    @Bind(R.id.detial_recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.rl_no_contant)
    RelativeLayout noContent;

    private View view;
    private ProductDetailActivity context;
    private ChatAdapter adapter;
    private MDataSource<CommentBean> dataSource;

    public boolean isCanLoadMore() {
        return canLoadMore;
    }

    public void setCanLoadMore(boolean canLoadMore) {
        this.canLoadMore = canLoadMore;
    }

    // 是否需要加载更多和刷新
    private boolean canLoadMore = false;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = (ProductDetailActivity) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (null == view) {
            view = inflater.inflate(R.layout.fragment_product_info, container, false);
            ButterKnife.bind(this, view);
            initView();
        } else {
            ((ViewGroup) view.getParent()).removeAllViews();
        }

        return view;
    }

    protected void initView() {
        webView.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        LinearLayoutManager manager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(manager);
        adapter = new ChatAdapter(R.layout.chat_list_layout,new ArrayList<CommentBean>());
        adapter.setHeaderFooterEmpty(true,true);
        recyclerView.setAdapter(adapter);
        //获取评论数据
        initData();
        View view = LayoutInflater.from(context).inflate(R.layout.shengming,null);
        adapter.addFooterView(view);
    }

    /**
     * 获取评论数据
     */
    private void initData() {
        HashMap<String, String> map = new HashMap<>();
        map.put("model", "interfaces");
        map.put("method", "getGoodsEvaluate");
        map.put("goodsKey", context.getGoodsId());
        Type mType = new TypeToken<List<CommentBean>>() {
        }.getType();
        dataSource = new MDataSource<>("productDetailComment" + context.getGoodsId(),
                true, false, map, context, CommentBean.class, mType);
        if (dataSource.getDataList().isEmpty()) {
            dataSource.refreshData(loadListCallBack);
        } else {
            //使用缓存数据
            setDataList(dataSource.getTotalCount(), dataSource.getDataList());
            dataSource.refreshData(loadListCallBack);
        }
    }

    private LoadListCallBack<CommentBean> loadListCallBack = new LoadListCallBack<CommentBean>() {
        @Override
        public void loadList(List<CommentBean> list) {
            setDataList(dataSource.getTotalCount(), dataSource.getDataList());
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
    };

    /**
     * 显示列表数据
     *
     * @param totalCount
     * @param list
     */
    private void setDataList(int totalCount, List<CommentBean> list) {
        if (null != list && list.size() > 0) {
            adapter.setNewData(list);
            // 是否可以刷新
            if (list.size() >= totalCount) {
                //已经全部加载完了
                canLoadMore = false;
            } else {
                //有刷新内容 可以刷新
                canLoadMore = true;
            }
        } else {
            //显示暂无内容
            adapter.setEmptyView(R.layout.no_content_view,recyclerView);
        }
    }

    /**
     * 加载更多列表数据
     */
    public void loadMoreDataList() {
        if(null!=dataSource && null!=loadListCallBack){
            dataSource.loadMoreDetail(loadListCallBack);
        }
    }
}
