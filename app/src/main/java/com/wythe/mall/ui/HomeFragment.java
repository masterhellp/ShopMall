package com.wythe.mall.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.RelativeLayout;

import com.google.gson.reflect.TypeToken;
import com.wythe.mall.R;
import com.wythe.mall.activity.ProductActivity;
import com.wythe.mall.activity.ProductDetailActivity;
import com.wythe.mall.activity.SearchActivity;
import com.wythe.mall.adapter.indexpage.IndextGoodsAdapter;
import com.wythe.mall.beans.AdBean;
import com.wythe.mall.beans.Goods;
import com.wythe.mall.http.datasource.MDataSource;
import com.wythe.mall.sqlite.LoadListCallBack;
import com.wythe.mall.utils.AppConfig;
import com.wythe.mall.utils.CommonUtils;
import com.wythe.mall.view.GridViewForScrollView;
import com.youth.banner.Banner;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.fragment.app.Fragment;
import okhttp3.Request;


public class HomeFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "HomeFragment";
    private View view;
    private Banner banner;


    //更多按钮
    private RelativeLayout hotMore, recomMore;

    private GridViewForScrollView hotListView, recomListView;

    private IndextGoodsAdapter hotAdapter, recomAdapter;
    //广告列表
    private List<AdBean> adList = new ArrayList<>();
    //
    public static HomeFragment instance;

    public static HomeFragment getInstance() {
        if (null == instance) {
            instance = new HomeFragment();
        }
        return instance;
    }

    private Context context;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (null == view) {
            view = inflater.inflate(R.layout.fragment_home, container, false);
            initView();
            initDatas();
        } else {
            if (null != view.getParent()) {
                ((ViewGroup) view.getParent()).removeAllViews();
            }
        }
        instance = this;
        return view;
    }

    /**
     * 加载数据
     */
    private void initDatas() {

    }

    private void initView() {
        banner = view.findViewById(R.id.home_banner);
        //设置样式
        banner.setBannerStyle(Banner.CIRCLE_INDICATOR);
        banner.setIndicatorGravity(Banner.CENTER);
        banner.setDelayTime(5000);//设置轮播间隔时间
        banner.setOnBannerClickListener(new Banner.OnBannerClickListener() {//设置点击事件
            @Override
            public void OnBannerClick(View view, int position) {
                Log.e(TAG, "OnBannerClick  position:  " + position);
                //跳转到详情页 banner是从1开始技术的
                AdBean item = adList.get(position - 1);
                if (!CommonUtils.isEmpty(item.getGoodsKey())) {
                    changeToDetaiLActivity(item.getGoodsKey());
                }

            }
        });
        //更多按钮
        hotMore = view.findViewById(R.id.index_hotPart);
        recomMore = view.findViewById(R.id.index_recomPart);
        hotMore.setOnClickListener(this);
        recomMore.setOnClickListener(this);
        // 热销 和 推荐列表数据
        hotListView = view.findViewById(R.id.index_hotListView);
        hotAdapter = new IndextGoodsAdapter(getActivity(), AppConfig.INDEX_HOT_GOODS);
        hotListView.setAdapter(hotAdapter);
        //设置列表的点击事件 点击进入详情页
        hotListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Goods item = (Goods) parent.getItemAtPosition(position);
                Log.e("goodid==", item.getGoodsId());
                changeToDetaiLActivity(item.getGoodsId());
            }
        });
        recomListView = view.findViewById(R.id.index_recomListView);
        recomAdapter = new IndextGoodsAdapter(getActivity(), AppConfig.INDEX_RECOM_GOODS);
        recomListView.setAdapter(recomAdapter);
        recomListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Goods item = (Goods) parent.getItemAtPosition(position);
                Log.e("goodid==", item.getGoodsId());
                changeToDetaiLActivity(item.getGoodsId());
            }
        });
        /**
         * 点击事件
         */
        view.findViewById(R.id.index_searchLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "onClick: 点击跳转");
                Intent intent = new Intent(context, SearchActivity.class);
                startActivity(intent);
            }
        });

    }

    /**
     * 跳转进入商品详情页
     *
     * @param goodKey 商品KEY
     */
    private void changeToDetaiLActivity(String goodKey) {
        if (CommonUtils.isFastClick()) {
            return;
        }
        Intent intent = new Intent(getActivity(), ProductDetailActivity.class);
        intent.putExtra("goodsId", goodKey);
        startActivity(intent);
    }

    //

    /**
     * 加载广告数据
     */
    private void loadAdsData() {
        HashMap<String, String> map = new HashMap<>();
        map.put("model", "interfaces");
        map.put("method", "mainAdv");
        Type mType = new TypeToken<List<AdBean>>() {
        }.getType();
        MDataSource<AdBean> dataSource = new MDataSource<>("indexAdList",
                true, true, map, getActivity(), AdBean.class, mType);
        if (dataSource.getDataList().isEmpty()) {
            dataSource.refreshData(adCallBack);
        } else {
            //使用缓存数据
            setAdsList(dataSource.getDataList());
            dataSource.refreshData(adCallBack);
        }
    }

    /**
     * 设置获取广告的回调
     */
    private LoadListCallBack<AdBean> adCallBack = new LoadListCallBack<AdBean>() {
        @Override
        public void loadList(List<AdBean> list) {
            setAdsList(list);
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
     * 设置列表展示数据
     *
     * @param adsList
     */
    private void setAdsList(List<AdBean> adsList) {
        if (null != adsList && !adsList.isEmpty()) {
            this.adList = adsList;
            //设置展示数据
            List<String> urlList = new ArrayList<>();
            for (int i = 0; i < adsList.size(); i++) {
                AdBean item = adsList.get(i);
                urlList.add(item.getImage());
            }
            banner.setImages(urlList);
        } else {
            // 没有广告数据 隐藏该view
            banner.setVisibility(View.GONE);
        }
    }


    /**
     * 获取热卖数据
     * <p>
     * --20
     */
    private void loadRecomData() {
        HashMap<String, String> map = new HashMap<>();
        map.put("model", "interfaces");
        map.put("method", "goodsList8");
        Type mType = new TypeToken<List<Goods>>() {
        }.getType();
        MDataSource<Goods> dataSource = new MDataSource<>("indexGoodsList8",
                false, false, map, getActivity(), Goods.class, mType);
//        if (dataSource.getDataList().isEmpty()) {
//            dataSource.refreshData(goodsLoadListCallBack);
//        } else {
//            //使用缓存数据
//            setGoodsList(dataSource.getDataList(), AppConfig.INDEX_RECOM_GOODS);
//            // 刷新数据
//            dataSource.refreshData(goodsLoadListCallBack);
//        }
        dataSource.refreshData(goodsLoadListCallBack);
    }

    private LoadListCallBack<Goods> goodsLoadListCallBack = new LoadListCallBack<Goods>() {
        @Override
        public void loadList(List<Goods> list) {
            Log.e(TAG, "loadList: =====" + list.size());
            setGoodsList(list, AppConfig.INDEX_RECOM_GOODS);
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

    //更新推荐和热卖数据
    private void setGoodsList(List<Goods> dataList, int type) {
        if (null != dataList && !dataList.isEmpty()) {
            if (AppConfig.INDEX_RECOM_GOODS == type) {
                recomAdapter.setResultList(dataList);
                recomAdapter.notifyDataSetChanged();
            } else {
                Log.e(TAG, "setGoodsList: 更新列表啊");
                hotAdapter.setResultList(dataList);
                hotAdapter.notifyDataSetChanged();
            }
        } else {
            //暂无推荐数据
        }
    }

    /**
     * 获取推荐数据
     * <p>
     * --26
     */
    private void loadHotData() {
        HashMap<String, String> map = new HashMap<>();
        map.put("model", "interfaces");
        map.put("method", "goodsRecom");
        Type mType = new TypeToken<List<Goods>>() {
        }.getType();
        MDataSource<Goods> dataSource = new MDataSource<>("indexHot",
                false, false, map, getActivity(), Goods.class, mType);
//        if (dataSource.getDataList().isEmpty()) {
//            dataSource.refreshData(hotLoadCallBack);
//        } else {
//            //使用缓存数据
//            setGoodsList(dataSource.getDataList(), AppConfig.INDEX_HOT_GOODS);
//            dataSource.refreshData(hotLoadCallBack);
//        }
        dataSource.refreshData(hotLoadCallBack);

    }

    private LoadListCallBack<Goods> hotLoadCallBack = new LoadListCallBack<Goods>() {
        @Override
        public void loadList(List<Goods> list) {
            Log.e(TAG, "loadList: <<<<<----->" + list.size());
            setGoodsList(list, AppConfig.INDEX_HOT_GOODS);
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
     * @param savedInstanceState
     */

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        banner.isAutoPlay(true);
    }

    @Override
    public void onStop() {
        super.onStop();
        banner.isAutoPlay(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        // 广告 -- 热销 --推荐
        loadAdsData();
        loadHotData();
        loadRecomData();
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(context, ProductActivity.class);
        switch (v.getId()) {
            case R.id.index_hotPart:
                intent.putExtra("pageType", "recom");
                break;
            case R.id.index_recomPart:
                intent.putExtra("pageType", "all");
                break;
            default:
        }

        startActivity(intent);
    }
}
