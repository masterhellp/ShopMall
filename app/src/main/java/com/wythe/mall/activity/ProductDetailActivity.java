package com.wythe.mall.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.SimpleMultiPurposeListener;
import com.scwang.smartrefresh.layout.util.DensityUtil;
import com.wythe.mall.R;
import com.wythe.mall.activity.orders.FirmOrderActivity;
import com.wythe.mall.adapter.prodetails.SpecAdapter;
import com.wythe.mall.base.BaseApplication;
import com.wythe.mall.beans.Goods;
import com.wythe.mall.beans.ImageBean;
import com.wythe.mall.beans.NormBean;
import com.wythe.mall.beans.SucesseBean;
import com.wythe.mall.http.datasource.MDataSource;
import com.wythe.mall.listeners.MyItemClickListener;
import com.wythe.mall.sqlite.LoadListCallBack;
import com.wythe.mall.ui.ProductInfoFragment;
import com.wythe.mall.ui.TabCFm;
import com.wythe.mall.utils.CommonUtils;
import com.wythe.mall.utils.GsonUtils;
import com.wythe.mall.utils.ScreenUtil;
import com.wythe.mall.utils.SharedPreUtils;
import com.wythe.mall.utils.StatusBarUtil;
import com.wythe.mall.utils.ToastUtil;
import com.wythe.mall.view.ColorFlipPagerTitleView;
import com.wythe.mall.view.ComFragmentAdapter;
import com.wythe.mall.view.MyNestedScrollView;
import com.wythe.mall.view.shopcar.CustomCarGoodsCounterView;
import com.youth.banner.Banner;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.UIUtil;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ButtonBarLayout;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Request;

/**
 * Created by wythe on 2016/7/24.
 */
public class ProductDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "ProductDetailActivity";

    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.view_pager)
    ViewPager viewPager;
    @Bind(R.id.scrollView)
    MyNestedScrollView scrollView;
    @Bind(R.id.buttonBarLayout)
    ButtonBarLayout buttonBarLayout;
    @Bind(R.id.magic_indicator)
    MagicIndicator magicIndicator;
    int toolBarPositionY = 0;
    @Bind(R.id.product_detail_banner)
    Banner banner;
    @Bind(R.id.goods_title)
    TextView goodsTitle;
    @Bind(R.id.product_nowPrice)
    TextView productNowPrice;
    @Bind(R.id.product_oldPrice)
    TextView productOldPrice;
    @Bind(R.id.buy_num)
    TextView buyNum;
    @Bind(R.id.stepper)
    CustomCarGoodsCounterView stepper;
    @Bind(R.id.collapse)
    CollapsingToolbarLayout collapse;
    @Bind(R.id.kefu)
    ImageView kefu;
    @Bind(R.id.shopping_car)
    ImageView shoppingCar;
    @Bind(R.id.now_pay)
    ImageView nowPay;
    @Bind(R.id.shop_name)
    ImageView shopName;
    @Bind(R.id.goods_specListView)
    RecyclerView specListView;
    @Bind(R.id.proDetail_postMoney)
    TextView postMoney;
    @Bind(R.id.proDetail_saleVolume)
    TextView saleVolume;
    @Bind(R.id.toolbar_username)
    TextView toolbarUsername;
    @Bind(R.id.proDetail_specPart)
    LinearLayout specPart;
    @Bind(R.id.proDetail_stocks)
    TextView stocks;
    private int mScrollY = 0;
    private String[] mTitles = new String[]{"商品详情", "商品评价"};
    private List<String> mDataList = Arrays.asList(mTitles);
    //商品规格
    private SpecAdapter specAdapter;
    //当前选中的产品规格
    private String currentSpec;
    //当前选中的产品数量
    private int currentNum = 1;
    //当前产品ID
    private String goodsId;
    //当前产品
    private Goods currentGoods;
    //产品图标
    List<String> urlList = new ArrayList<>();
    NormBean normBean;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                String ReturnMessage = (String) msg.obj;
                Log.e("获取的返回信息:", ReturnMessage);
                SucesseBean bean = new Gson().fromJson(ReturnMessage, SucesseBean.class);
                String code = bean.getCode();
                Log.e("code===", code + "");
                if (code.equals("200")) {
                    ToastUtil.makeText(ProductDetailActivity.this, "加入购物车成功！");
                    finish();
                } else {
                    ToastUtil.makeText(ProductDetailActivity.this, "加入购物车失败！");
                }

            } else if (msg.what == 2) {

            }

        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_detail);
        ButterKnife.bind(this);
        StatusBarUtil.immersive(this);
//        StatusBarUtil.setPaddingSmart(this, toolbar);
        initView();
        //获取商品详情
        initData();
    }

    public String getGoodsId() {
        return goodsId;
    }

    /**
     * 设置view的基本样式
     */
    private void initView() {
        goodsId = getIntent().getStringExtra("goodsId");
        //设置轮播图片
        initBanner();
        // 设置页头
        refreshLayout.setOnMultiPurposeListener(new SimpleMultiPurposeListener() {
            @Override
            public void onHeaderPulling(RefreshHeader header, float percent, int offset, int bottomHeight, int extendHeight) {
                toolbar.setAlpha(1 - Math.min(percent, 1));
            }

            @Override
            public void onHeaderReleasing(RefreshHeader header, float percent, int offset, int bottomHeight, int extendHeight) {
                toolbar.setAlpha(1 - Math.min(percent, 1));
            }
        });
        //关闭刷新事件
        refreshLayout.setEnableRefresh(false);
        refreshLayout.setEnableLoadmore(false);
        //设置加载更多事件
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                // 加载更多数据
                if (null != commentFragment) {
                    commentFragment.loadMoreDataList();
                }
            }
        });


        toolbar.post(new Runnable() {
            @Override
            public void run() {
                dealWithViewPager();
            }
        });
        // 设置滚动监听
        scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            int lastScrollY = 0;
            int h = DensityUtil.dp2px(170);
            int color = ContextCompat.getColor(getApplicationContext(), R.color.red) & 0x00ffffff;

            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                int[] location = new int[2];
                magicIndicator.getLocationOnScreen(location);
                int yPosition = location[1];
                if (yPosition < toolBarPositionY) {
                    scrollView.setNeedScroll(false);
                } else {
                    scrollView.setNeedScroll(true);

                }

                if (lastScrollY < h) {
                    scrollY = Math.min(h, scrollY);
                    mScrollY = scrollY > h ? h : scrollY;
                    buttonBarLayout.setAlpha(1f * mScrollY / h);
                    toolbar.setBackgroundColor(((255 * mScrollY / h) << 24) | color);
                }
                if (scrollY == 0) {
                    ivBack.setImageResource(R.drawable.back_black);
                } else {
                    ivBack.setImageResource(R.drawable.back_white);
                }

                lastScrollY = scrollY;
            }
        });
        // 设置初始状态
        buttonBarLayout.setAlpha(0);
        toolbar.setBackgroundColor(0);
        viewPager.setAdapter(new ComFragmentAdapter(getSupportFragmentManager(), getFragments()));
        viewPager.setOffscreenPageLimit(10);
        // 设置标签样式
        initMagicIndicator();
        // 设置监听事件
        ivBack.setOnClickListener(this);
        kefu.setOnClickListener(this);
        shoppingCar.setOnClickListener(this);
        nowPay.setOnClickListener(this);
        shopName.setOnClickListener(this);
        //设置商品规格
        //设置产品规格
        LinearLayoutManager manager = new LinearLayoutManager(this) {
            @Override
            public RecyclerView.LayoutParams generateDefaultLayoutParams() {
                return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
            }
        };
        manager.setOrientation(RecyclerView.HORIZONTAL);
        specListView.setLayoutManager(manager);
        specAdapter = new SpecAdapter(this, new MyItemClickListener() {
            @Override
            public void onItemClick(View view, int postion) {
                //设置点击事件
                normBean = specAdapter.getDataList().get(postion);
                currentSpec = normBean.getNormKey();
//                Log.e("normID===1",item.getNormId());
                specAdapter.setCurForcused(postion);
                specAdapter.notifyDataSetChanged();
                // 设置当前商品的价格 库存  原件
                stocks.setText("库存：" + normBean.getStock());
                //设置商品数据
                goodsTitle.setText(normBean.getNormName());
                //价格
                productNowPrice.setText("￥" + normBean.getPrice());
                String originalPriceString = normBean.getOriginalPrice();
                if (!CommonUtils.isEmpty(originalPriceString) && Double.parseDouble(originalPriceString) > 0) {
                    productOldPrice.setText("￥" + normBean.getOriginalPrice());
                    productOldPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                }
                //设置当前选中的的商品信息
                currentGoods.setPrice(normBean.getPrice());
                currentGoods.setMprice(normBean.getOriginalPrice());
                currentGoods.setNormName(normBean.getNormName());
                currentGoods.setNormKey(currentSpec);
//                currentGoods.setPostageInitAmount();
            }
        });
        specListView.setAdapter(specAdapter);
    }

    /**
     * 设置商品详情信息
     */
    private void initData() {
        //获取轮播图
        loadImages();
        //获取商品详情
        HashMap<String, String> map = new HashMap<>();
        map.put("model", "interfaces");
        map.put("method", "goodsDetail");
        map.put("goodsKey", goodsId);
        MDataSource<Goods> dataSource = new MDataSource<>(map, this);
        dataSource.getDetail(new LoadListCallBack() {
            @Override
            public void loadList(List list) {

            }

            @Override
            public void systemError(Request request, String errorInfo, Exception e) {

            }

            @Override
            public void retLoad(String code) {

            }

            @Override
            public void loadString(String string) {
                String result = GsonUtils.getNoteJsonString(string, "results");
                Log.e(TAG, "loadString: result :" + result);
                String body = GsonUtils.getNoteJsonString(result, "body");
                Log.e(TAG, "loadString: " + body + " ---- " + body.length());
//                // 将body解析成goods对象
                currentGoods = new Gson().fromJson(body, Goods.class);
                Log.e(TAG, "loadString: " + currentGoods);
                //设置商品数据
                Log.e("shop___Key", currentGoods.getShopkey());
                goodsTitle.setText(currentGoods.getName());
                currentGoods.setPrice(currentGoods.getMprice());
                //价格
                productNowPrice.setText("￥" + currentGoods.getPrice());
                if (!"0".equals(currentGoods.getMprice()) &&
                        !CommonUtils.isEmpty(currentGoods.getMprice())) {
                    productOldPrice.setText("￥" + currentGoods.getMprice());
                    productOldPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                }
                //设置快递费
                String postMoneyString = currentGoods.getPostages();
                if ((!CommonUtils.isEmpty(postMoneyString) && Double.parseDouble(postMoneyString) > 0)
                        || "1".equals(currentGoods.getIsFreePostage())) {
                    //需要邮费
                    postMoney.setText("快递：￥" + currentGoods.getPostages());
                } else {
                    postMoney.setText("快递：免邮");
                }
//
                //设置销量
                saleVolume.setText("销量：" + currentGoods.getSaleVolume());
                // 设置库存
                stocks.setText("库存：" + currentGoods.getStocks());
                //商品规格
                if ("1".equals(currentGoods.getIshas_norms())) {
                    getSpecs();
                }

            }
        });
    }


    /**
     * 获取商品规格
     */
    private void getSpecs() {
        final HashMap<String, String> map = new HashMap<>();
        map.put("model", "interfaces");
        map.put("method", "goodsNorms");
        map.put("goodsKey", goodsId);
        Type mType = new TypeToken<List<NormBean>>() {
        }.getType();
        MDataSource<NormBean> dataSource = new MDataSource<>("productDetailsAdList",
                false, false, map, this, NormBean.class, mType);
        dataSource.refreshData(new LoadListCallBack() {
            @Override
            public void loadList(List list) {
                //设置商品的规格 获取到商品规格的时候展示
                if (!list.isEmpty()) {
                    specAdapter.setDataList(list);
                    specPart.setVisibility(View.VISIBLE);
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

    /**
     * 获取商品轮播图片
     */
    private void loadImages() {
        HashMap<String, String> map = new HashMap<>();
        map.put("model", "interfaces");
        map.put("method", "goodsDetailBarImg");
        map.put("goodsKey", goodsId);
        Type mType = new TypeToken<List<ImageBean>>() {
        }.getType();
        MDataSource<ImageBean> dataSource = new MDataSource<>("productDetailsAdList" + goodsId,
                true, false, map, this, ImageBean.class, mType);
        if (dataSource.getDataList().isEmpty()) {
            dataSource.refreshData(adCallBack);
        } else {
            //先展示缓存数据 然后刷新
//            setAdsList(dataSource.getDataList());
            dataSource.refreshData(adCallBack);
        }
    }

    /**
     * 设置获取广告的回调
     */
    private LoadListCallBack<ImageBean> adCallBack = new LoadListCallBack<ImageBean>() {
        @Override
        public void loadList(List<ImageBean> list) {
            setAdsList(list);
//            for (int i = 0; i < list.size(); i++) {
//                Log.e("loadList==url", list.get(i).getUrl());
//            }

        }

        @Override
        public void systemError(Request request, String errorInfo, Exception e) {

        }

        @Override
        public void retLoad(String code) {

        }

        @Override
        public void loadString(String string) {
            Log.e("轮播json==", string);
        }
    };


    /**
     * 设置列表展示数据
     *
     * @param adsList
     */
    private void setAdsList(List<ImageBean> adsList) {
        if (null != adsList && !adsList.isEmpty()) {
            //设置展示数据
            Log.e("adsList.size==", adsList.size() + "");
            for (int i = 0; i < adsList.size(); i++) {
                ImageBean item = adsList.get(i);
                urlList.add(item.getUrl());
            }
            banner.setImages(urlList);
        } else {
            // 没有广告数据 隐藏该view

        }
    }

    // 设置banner
    private void initBanner() {
        //设置样式
        banner.setBannerStyle(Banner.CIRCLE_INDICATOR);
        banner.setIndicatorGravity(Banner.CENTER);
        banner.setDelayTime(5000);//设置轮播间隔时间
        banner.setOnBannerClickListener(new Banner.OnBannerClickListener() {//设置点击事件
            @Override
            public void OnBannerClick(View view, int position) {
                Toast.makeText(ProductDetailActivity.this, "----你点击了：" +
                        "" + position, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void dealWithViewPager() {
        toolBarPositionY = toolbar.getHeight();
        ViewGroup.LayoutParams params = viewPager.getLayoutParams();
        params.height = ScreenUtil.getScreenHeightPx(getApplicationContext())
                - toolBarPositionY - magicIndicator.getHeight() + 1;
        viewPager.setLayoutParams(params);
    }


    private ProductInfoFragment infoFragment;

    private TabCFm commentFragment;

    private List<Fragment> getFragments() {
        List<Fragment> fragments = new ArrayList<>();
        infoFragment = new ProductInfoFragment();
        commentFragment = new TabCFm();
        fragments.add(infoFragment);
        fragments.add(commentFragment);
        return fragments;
    }

    /**
     * 设置选中标签的样式
     */
    private void initMagicIndicator() {
        CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setScrollPivotX(0.65f);
        commonNavigator.setAdjustMode(true);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return mDataList == null ? 0 : mDataList.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                SimplePagerTitleView simplePagerTitleView = new ColorFlipPagerTitleView(context);
                simplePagerTitleView.setText(mDataList.get(index));
                simplePagerTitleView.setNormalColor(ContextCompat.getColor(ProductDetailActivity.this, R.color.mainBlack));
                simplePagerTitleView.setSelectedColor(ContextCompat.getColor(ProductDetailActivity.this, R.color.mainBlack));
                simplePagerTitleView.setTextSize(16);
                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewPager.setCurrentItem(index, false);
                        //判断是否需要显示加载更多
                        if (index == 1) {
                            refreshLayout.setEnableLoadmore(true);
                            if (null != commentFragment && !commentFragment.isCanLoadMore()) {
                                refreshLayout.finishLoadmoreWithNoMoreData();
                            }
                        } else {
                            refreshLayout.setEnableLoadmore(false);

                        }
                    }
                });
                return simplePagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setMode(LinePagerIndicator.MODE_EXACTLY);
                indicator.setLineHeight(UIUtil.dip2px(context, 2));
                indicator.setLineWidth(UIUtil.dip2px(context, 100));
                indicator.setRoundRadius(UIUtil.dip2px(context, 3));
                indicator.setStartInterpolator(new AccelerateInterpolator());
                indicator.setEndInterpolator(new DecelerateInterpolator(2.0f));
                indicator.setColors(ContextCompat.getColor(ProductDetailActivity.this, R.color.mainRed));
                return indicator;
            }
        });
        magicIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(magicIndicator, viewPager);
    }

    @Override
    public void onClick(View v) {
        //设置商品图片
        if (!urlList.isEmpty() && null != currentGoods) {
            currentGoods.setImages(urlList.get(0));
        }
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.kefu:
                //客服电话 打开QQ
                CommonUtils.openKeFu(this);
                break;
            case R.id.shopping_car:
                currentNum = stepper.getGoodsNumber();
                //加入购物车 判断用户是否登录了
                Log.e(TAG, "onClick: " + currentGoods.getIshas_norms()  );
                if (!CommonUtils.skipLoginPage(this)) {
                    if (CommonUtils.isEmpty(currentSpec) &&
                            "1".equals(currentGoods.getIshas_norms()) && null!=specAdapter &&
                            !specAdapter.getDataList().isEmpty()) {
                        Toast.makeText(this, "请选择商品规格", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    //设置选选择数量
                    currentGoods.setGoods_num(currentNum + "");
                    //设置选中的规格
                    currentGoods.setSelectSpec(currentSpec + "");
                    HashMap<String, String> map = new HashMap<>();
                    map.put("model", "interfaces");
                    map.put("method", "addShoppingCart");
                    //商品ID 商品规格ID,商品购买数量，商品所属店铺KEY，店铺名，商品单价
                    map.put("goodsKey", goodsId);
                    map.put("normKey", currentSpec);
                    map.put("amount", currentNum + "");
                    map.put("shopKey", currentGoods.getShopkey());
                    map.put("shopName", currentGoods.getShopName());
                    Log.e("选中规格价格===", currentGoods.getPrice());
                    map.put("price", currentGoods.getPrice());
//                    Log.e("物品初始邮费===",currentGoods.getPostageInitAmount());
//                    is_free_postage
                    String postMoneyString = currentGoods.getPostages();
                    if ((!CommonUtils.isEmpty(postMoneyString) && Double.parseDouble(postMoneyString) > 0)
                            || "1".equals(currentGoods.getIsFreePostage())) {
                        //需要邮费
//                        postMoney.setText("快递：￥" + currentGoods.getPostages());
                        map.put("postage_init_amount", currentGoods.getPostages());
                    } else {
                        map.put("postage_init_amount", "baoyou");
                    }
                    String is_free_postage = currentGoods.getPostages();
                    if (CommonUtils.isEmpty(is_free_postage) && is_free_postage.equals("0")) {
                        map.put("is_free_postage", is_free_postage);
                    } else {
                        map.put("is_free_postage", is_free_postage);
                    }
//                    map.put("postage_init_amount",currentGoods.getPostageInitAmount());
//                    Log.e("是否包邮===",currentGoods.getIsFreePostage());
//                    map.put("is_free_postage",currentGoods.getIsFreePostage());
                    //用户token
                    String token = BaseApplication.getCustomer().getToken();
                    if (CommonUtils.isEmpty(token)) {
                        token = SharedPreUtils.getPreStringInfo(this, "token");
                    }
                    String customerKey = SharedPreUtils.getPreStringInfo(this, "userName");
                    Log.e("userName=====", customerKey);
                    map.put("customerKey", customerKey);
                    map.put("businessKey", "business_key");
                    MDataSource<Goods> dataSource = new MDataSource<>(this);
                    dataSource.postData(map, new LoadListCallBack() {
                        @Override
                        public void loadList(List list) {

                        }

                        @Override
                        public void systemError(Request request, String errorInfo, Exception e) {

                        }

                        @Override
                        public void retLoad(String code) {

                        }

                        @Override
                        public void loadString(String string) {
                            // 提交数据成功
                            if (!CommonUtils.isEmpty(string) && string.contains("成功")) {
                                ToastUtil.makeText(ProductDetailActivity.this, "添加成功");
                            }
                        }
                    });
                } else {
                    Toast.makeText(this, "请登录后执行该操作", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.now_pay:
                // 现在购买 生成订单
                if (!CommonUtils.skipLoginPage(this)) {
                    //跳转到订单页面 将商品加入到
                    List<Goods> list = new ArrayList<>();
                    if (CommonUtils.isEmpty(currentSpec) && "1".equals(currentGoods.getIshas_norms())
                            && null!=specAdapter &&
                            !specAdapter.getDataList().isEmpty()
                            ) {
                        Toast.makeText(this, "请选择商品规格", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    //设置选中的规格
                    currentGoods.setSelectSpec(currentSpec + "");
                    currentGoods.setGoods_num(stepper.getGoodsNumber() + "");
                    currentGoods.setGoodImage(currentGoods.getLogo());
                    currentGoods.setNormPrice(currentGoods.getPrice());
                    currentGoods.setGoodsKey(goodsId);
                    currentGoods.setAmount(stepper.getGoodsNumber());
                    currentGoods.setGoodsName(normBean.getGoodsName());
                    list.add(currentGoods);
                    Intent intent = new Intent(this, FirmOrderActivity.class);
                    //将数据传递到确认订单页面 去付款
                    intent.putExtra("selectList", (Serializable) list);
                    //计算总价
                    double total_price = currentNum * Double.parseDouble(currentGoods.getPrice());
                    intent.putExtra("total_price", total_price);
                    // 跳转到订单结算页面 确认订单
                    startActivity(intent);
                } else {
                    ToastUtil.makeText(this, "请登录后购买");
                }
                break;
            case R.id.shop_name:
                Log.e("店铺详情==", currentGoods.getShopkey());
                if (null != currentGoods) {
                    Intent intent = new Intent(this, MerchantInfoActivity.class);
                    intent.putExtra("shopId", currentGoods.getShopkey());
                    startActivity(intent);
                }
                break;
            default:
        }
    }

    /**
     * 启动广告的滚动
     */
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
}
