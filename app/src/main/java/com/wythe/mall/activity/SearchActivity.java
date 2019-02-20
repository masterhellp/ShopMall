package com.wythe.mall.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.reflect.TypeToken;
import com.wythe.mall.R;
import com.wythe.mall.adapter.SearchAdapter;
import com.wythe.mall.beans.SearchBean;
import com.wythe.mall.http.datasource.MDataSource;
import com.wythe.mall.sqlite.LoadListCallBack;
import com.wythe.mall.util.KeyboardUtils;
import com.wythe.mall.utils.CommonUtils;
import com.wythe.mall.view.ColorFlipPagerTitleView;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.buildins.UIUtil;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Request;

public class SearchActivity extends BaseActivity implements View.OnClickListener, TextWatcher {

    @Bind(R.id.search_backImg)
    ImageView searchBackImg;
    @Bind(R.id.search_textView)
    TextView searchText;
    @Bind(R.id.search_button)
    RelativeLayout searchButton;
    @Bind(R.id.search_delInputImg)
    ImageView searchDelInputImg;
    @Bind(R.id.search_inputView)
    AutoCompleteTextView searchInputView;
    @Bind(R.id.magic_indicator)
    MagicIndicator magicIndicator;
    @Bind(R.id.search_historyRecyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.search_inputPart)
    RelativeLayout searchPart;
    private List<String> tabList = new ArrayList<>();
    //搜索关键词
    private SearchAdapter adapter;
    //当前选择的搜索分类 是商品 还是店铺
    private int currentTab = 0;
    // 商品推荐
    private List<SearchBean> goodList = new ArrayList<>();
    // 店铺推荐
    private List<SearchBean> shopList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        initView();
    }

    /**
     * 加载布局文件
     */
    @Override
    protected void initView() {
        //
        searchBackImg.setOnClickListener(this);
        //设置绑定事件
        searchButton.setOnClickListener(this);
        //输入框绑定事件
        searchInputView.addTextChangedListener(this);
        // 删除输入内容
        searchDelInputImg.setOnClickListener(this);
        // 软键盘搜索事件
        searchInputView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    startSearch();
                    return true;
                }
                return false;
            }
        });
        String[] mTitles = new String[]{"商品", "店铺"};
        tabList = Arrays.asList(mTitles);
        //初始化标签内容
        initMagicIndicator();
        //输入框监听
        searchInputView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    //显示标签
                    magicIndicator.setVisibility(View.VISIBLE);
                    searchInputView.requestFocus();
                } else {
                    magicIndicator.setVisibility(View.GONE);
                }
            }
        });
        //
        searchInputView.requestFocus();
        //获取推荐热词
        GridLayoutManager manager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(manager);
        adapter = new SearchAdapter(R.layout.search_recom_items, goodList);
        recyclerView.setAdapter(adapter);
        //设置点击事件
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                //设置热词搜索的跳转
                SearchBean item = (SearchBean) adapter.getItem(position);
                startNewIntentToSearch(item.getKeyword());
            }
        });
        //加载热门数据
        loadHotKeys();
    }


    /**
     * 初始化标签内容
     */
    private void initMagicIndicator() {
        CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setAdjustMode(true);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return null == tabList ? 0 : tabList.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                SimplePagerTitleView simplePagerTitleView = new ColorFlipPagerTitleView(context);
                simplePagerTitleView.setText(tabList.get(index));
                simplePagerTitleView.setNormalColor(ContextCompat.getColor(context, R.color.mainBlack));
                simplePagerTitleView.setSelectedColor(ContextCompat.getColor(context, R.color.red));
                simplePagerTitleView.setTextSize(16);
                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        currentTab = index;
                        magicIndicator.onPageSelected(index);
                        magicIndicator.onPageScrolled(index, 0.0F, 0);
                        //
                        if(index == 0 ){
                            setRecomData(goodList);
                        } else{
                            setRecomData(shopList);
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
                indicator.setColors(ContextCompat.getColor(context, R.color.mainRed));
                return indicator;
            }
        });
        magicIndicator.setNavigator(commonNavigator);
    }

    /**
     * 点击事件
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search_backImg:
                onBackPressed();
                break;
            case R.id.search_button:
                startSearch();
                break;
            case R.id.search_delInputImg:
                searchInputView.getEditableText().clear();
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (!CommonUtils.checkIsNull(this, searchInputView, null)) {
            //输入框内容非空时  显示删除按钮
            searchDelInputImg.setVisibility(View.VISIBLE);
//            searchText.setText("搜索");
        } else {
//            searchText.setText("取消");
            searchDelInputImg.setVisibility(View.GONE);
        }
    }

    /**
     * 开始进行搜索
     */
    private void startSearch() {
        if ("取消".equals(searchText.getText().toString())) {
//                    finish();
            searchInputView.clearFocus();
            magicIndicator.setVisibility(View.GONE);
            searchInputView.getEditableText().clear();
        } else {
            //将关键词加入搜索记录
            CharSequence keyword = searchInputView.getText();
            if (null != keyword && keyword.toString().trim().length() > 0) {
                startNewIntentToSearch(keyword.toString().trim());
            } else {
                Toast.makeText(this, "请输入搜索关键词", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 跳转到搜索页面
     *
     * @param key
     */
    private void startNewIntentToSearch(String key) {
        //隐藏软键盘
        KeyboardUtils.hideKeyboard(searchInputView);
        //执行搜索事件
        Intent intent = new Intent(this, ProductActivity.class);
        intent.putExtra("pageType", "search");
        intent.putExtra("keyWords", key);
        intent.putExtra("searchTab", currentTab);
        startActivity(intent);
    }


    // 获取热词推荐的列表
    private void loadHotKeys() {
        HashMap<String, String> map = new HashMap<>();
        map.put("model", "interfaces");
        map.put("method", "getHotWord");
        Type type = new TypeToken<List<SearchBean>>() {
        }.getType();
        MDataSource<SearchBean> dataSource = new MDataSource<>("getHotWord", false, false, map,
                this, SearchBean.class, type);
        dataSource.refreshData(new LoadListCallBack() {
            @Override
            public void loadList(List list) {
                // 解析数据
                Log.e(TAG, "loadList: " + list.size());
                if (null != list && !list.isEmpty()) {
                    int size = list.size();
                    for (int i = 0; i < size; i++) {
                        SearchBean item = (SearchBean) list.get(i);
                        if ("1".equals(item.getType())) {
                            goodList.add(item);
                        } else {
                            shopList.add(item);
                        }
                    }
                }
                // 默认加载商品的数据
                setRecomData(goodList);
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
     * 设置列表推荐数据
     * @param dataList
     */
    private void setRecomData(List<SearchBean> dataList) {
        if (dataList.isEmpty()) {
            adapter.setEmptyView(R.layout.no_content_view, recyclerView);
        } else {
            adapter.setNewData(dataList);
        }
    }

}
