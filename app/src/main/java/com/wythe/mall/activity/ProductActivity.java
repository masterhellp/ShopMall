package com.wythe.mall.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.wythe.mall.R;
import com.wythe.mall.adapter.indexpage.IndextGoodsAdapter;
import com.wythe.mall.beans.Goods;
import com.wythe.mall.http.datasource.MDataSource;
import com.wythe.mall.sqlite.LoadListCallBack;
import com.wythe.mall.utils.AppConfig;
import com.wythe.mall.utils.CommonUtils;
import com.wythe.mall.utils.ToastUtil;
import com.wythe.mall.view.TitleBarView;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Request;

/**
 * Created by wythe on 2016/7/24.
 */
public class ProductActivity extends BaseActivity {

    private static final String TAG = "ProductActivity";
    @Bind(R.id.titleBarView)
    TitleBarView titleBarView;
    @Bind(R.id.sort_search_button_add_time_1)
    TextView tabLeftTextView;
    @Bind(R.id.sort_search_button_composite_arrow)
    View tabLeftImg;
    @Bind(R.id.sort_search_button_composite)
    RelativeLayout tabLeft;
    @Bind(R.id.sort_search_button_sales_1)
    TextView tabSaleTextView;
    @Bind(R.id.sort_search_button_sales)
    RelativeLayout tabSale;
    @Bind(R.id.sort_search_button_price_1)
    TextView tabPriceTextView;
    @Bind(R.id.sort_search_button_price_arrow)
    View tabPriceImg;
    @Bind(R.id.sort_search_button_price)
    RelativeLayout tabPrice;
    @Bind(R.id.pro_Grid)
    GridView proGrid;
    @Bind(R.id.pro_refresh)
    SmartRefreshLayout proRefresh;
    @Bind(R.id.tab_sale_img)
    View tabSaleImg;
    @Bind(R.id.rl_no_contant)
    RelativeLayout noContent;
    // 加載數據的工具欄
    MDataSource<Goods> dataSource;
    @Bind(R.id.sort_search_buttons)
    LinearLayout sortButtons;
    //数据展示列表
    private IndextGoodsAdapter adapter;
    //当前选择的排序类型  0 ：默认是综合排序 sale ：销量 price:价格
    private String orderType = "sale";
    //倒序还是正序
    private String saleOrder = "desc";

    private String priceOrder = "desc";

    private String pageType;

    //当前所属分类
    private String classifyId;
    private int refreshOrMore = 0;
    private LoadListCallBack<Goods> goodsLoadListCallBack = new LoadListCallBack<Goods>() {
        @Override
        public void loadList(List<Goods> list) {
            Log.e(TAG, "loadList: =====" + list.size());
            if (null != list && !list.isEmpty()) {
                //顯示列表數據
                adapter.setResultList(list);
                adapter.notifyDataSetChanged();
                //是否有加载更多
                if (list.size() >= dataSource.getTotalCount()) {
                    //没有加载更多
                    proRefresh.finishLoadmoreWithNoMoreData();
                }
                proRefresh.finishRefresh();
                proRefresh.finishLoadmore();
                noContent.setVisibility(View.GONE);
            } else {
                //显示暂无内容
                noContent.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void systemError(Request request, String errorInfo, Exception e) {
            Log.e(TAG, "loadList: =====" + errorInfo);
            //加载数据失败 了
            if (null != adapter && !adapter.viewList.isEmpty()) {
                if (refreshOrMore == 1) {
                    proRefresh.finishRefresh(false);
                    return;
                } else if (refreshOrMore == 2) {
                    proRefresh.finishLoadmore(false);
                    return;
                }
            } else {
                // 显示错误页面
                ToastUtil.makeText(ProductActivity.this, "请求失败，请稍后重试");
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    @Override
    protected void initView() {
        titleBarView.setTitle("商品列表");
        titleBarView.showEditButton(false);
        titleBarView.showBackButton(true);
        // 标签点击事件
        tabLeft.setOnClickListener(this);
        tabPrice.setOnClickListener(this);
        tabSale.setOnClickListener(this);
        //列表点击事件
        proGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Goods item = (Goods) adapterView.getItemAtPosition(i);
                if (CommonUtils.isFastClick() || CommonUtils.isEmpty(item.getGoodsId())) {
                    return;
                }
                Intent intent = new Intent(ProductActivity.this, ProductDetailActivity.class);
                intent.putExtra("goodsId", item.getGoodsId());
                startActivity(intent);
            }
        });
        //刷新控件 下拉刷新
        proRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                Log.e("setOnRefreshListener==", "刷新完成");
                refreshOrMore = 1;
                if(null!=dataSource){
                    dataSource.refreshData(goodsLoadListCallBack);
                }

            }
        });
        //加载更多
        proRefresh.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                Log.e("setOnLoadmoreListener==", "加载完成");
                refreshOrMore = 2;
                if(null!=dataSource){
                    dataSource.loadMoreDetail(goodsLoadListCallBack);
                }
            }
        });
        // 复用首页的adapter
        adapter = new IndextGoodsAdapter(this, AppConfig.INDEX_RECOM_GOODS);
        proGrid.setAdapter(adapter);
        classifyId = getIntent().getStringExtra("classifyId");
        pageType = getIntent().getStringExtra("pageType");
    }

    /**
     * 加載數據開始
     */
    private void initData() {
        HashMap<String, String> map = new HashMap<>();
        map.put("model", "interfaces");
        if ("recom".equals(pageType)) {
            map.put("method", "goodsRecomAll");
        } else if ("all".equals(pageType)) {
            map.put("method", "goodsList");
        }
        if ("search".equals(pageType)) {
            map.put("method", "searchGoodsByName");
            map.put("goodsName", getIntent().getStringExtra("keyWords"));
            //隐藏tab标签
            sortButtons.setVisibility(View.GONE);
        } else if ("classify".equals(pageType)) {
            map.put("method", "goodsListClassify");
            map.put("classifyId", classifyId);
        } else {
            map.put("sort", orderType);
            if ("sale".equals(orderType)) {
                map.put("order", saleOrder);
            } else {
                map.put("order", priceOrder);
            }
            if (!CommonUtils.isEmpty(classifyId)) {
                // 分类ID
                map.put("classifyId", classifyId);
            }
            //排序类型
            map.put("ordernum", orderType + "");
        }
        Type mType = new TypeToken<List<Goods>>() {
        }.getType();
        dataSource = new MDataSource<>("goodsList" + orderType + classifyId + pageType,
                false, true, map, this, Goods.class, mType);
        dataSource.refreshData(goodsLoadListCallBack);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sort_search_button_sales:
                //销量
                removeTabStyle();
                tabSaleImg.setVisibility(View.VISIBLE);
                tabSaleTextView.setTextColor(Color.RED);
                if ("desc".equals(saleOrder)) {
                    saleOrder = "asc";
                } else {
                    saleOrder = "desc";
                }
                //刷新数据
                initData();
                break;
            case R.id.sort_search_button_price:
                //价格
                removeTabStyle();
                tabPriceImg.setVisibility(View.VISIBLE);
                tabPriceTextView.setTextColor(Color.RED);
                if ("desc".equals(priceOrder)) {
                    priceOrder = "asc";
                } else {
                    priceOrder = "desc";
                }
                //刷新数据
                initData();
                break;
            case R.id.sort_search_button_composite:
                // 综合
                removeTabStyle();
                tabLeftImg.setVisibility(View.VISIBLE);
                tabLeftTextView.setTextColor(Color.RED);
                break;
        }
    }

    private void removeTabStyle() {

        tabLeftImg.setVisibility(View.GONE);
        tabLeftTextView.setTextColor(Color.parseColor("#333333"));

        tabSaleImg.setVisibility(View.GONE);
        tabSaleTextView.setTextColor(Color.parseColor("#333333"));

        tabPriceImg.setVisibility(View.GONE);
        tabPriceTextView.setTextColor(Color.parseColor("#333333"));

    }
}
