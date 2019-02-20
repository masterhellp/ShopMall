package com.wythe.mall.view;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wythe.mall.R;
import com.wythe.mall.activity.ProductActivity;
import com.wythe.mall.utils.CommonUtils;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.buildins.UIUtil;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

/**
 * s搜索框
 */
public class SearchView extends LinearLayout implements View.OnClickListener, TextWatcher {

    private static final String TAG = "SearchView";

    private Context context;
    // 搜索按钮
    private RelativeLayout searchButton;

    private TextView searchText;
    //输入框
    private AutoCompleteTextView searchInputView;
    //删除按钮
    private ImageView searchDelInputImg;
    //选择框
    private LinearLayout searchTimePart;

    private TextView nowSelectItem;
    //选择提示箭头
    private ImageView selectIcon;
    // 搜索类型选择框
    private MyDialog myDialog;
    //搜索选项
    private MagicIndicator magicIndicator;
    //标签
    private List<String> tabList = new ArrayList<>();

    public SearchView(Context context) {
        super(context);
        this.context = context;
        initView();
    }

    public SearchView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView();
    }

    public SearchView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initView();
    }

    /**
     * 加载布局文件
     */
    private void initView() {
        View view = LayoutInflater.from(context).inflate(R.layout.include_search_box, this);
        //搜索
        searchButton = view.findViewById(R.id.search_button);
        searchText = view.findViewById(R.id.search_textView);
        //输入框
        searchInputView = view.findViewById(R.id.search_inputView);
        // 删除按钮
        searchDelInputImg = view.findViewById(R.id.search_delInputImg);
        //选择框
        searchTimePart = view.findViewById(R.id.search_timePart);

        nowSelectItem = view.findViewById(R.id.search_timeView);
        selectIcon = view.findViewById(R.id.search_souSuoImg);
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
        searchTimePart.setOnClickListener(this);
        //搜索标签
        magicIndicator = view.findViewById(R.id.magic_indicator);
        String[] mTitles = new String[]{"商品", "店铺"};
        tabList = Arrays.asList(mTitles);
        //初始化标签内容
        initMagicIndicator();
        //输入框监听
        searchInputView.setOnFocusChangeListener(new OnFocusChangeListener() {
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
    }

    /**
     * 初始化标签内容
     */
    private void initMagicIndicator() {
        CommonNavigator commonNavigator = new CommonNavigator(context);
        commonNavigator.setScrollPivotX(0.15f);
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
                        magicIndicator.onPageSelected(index);
                        magicIndicator.onPageScrolled(index, 0.0F, 0);
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


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search_button:
                startSearch();
                break;
            case R.id.search_delInputImg:
                searchInputView.getEditableText().clear();
                break;
            case R.id.search_timePart:
                //显示搜索商家或者店铺
                showSeclectDialog();
                break;
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
                String temp = keyword.toString().trim();
                //执行搜索事件
                Intent intent = new Intent(context, ProductActivity.class);
                intent.putExtra("pageType", "search");
                intent.putExtra("keyWords", temp);
                context.startActivity(intent);
            } else {
                Toast.makeText(context, "请输入搜索关键词", Toast.LENGTH_SHORT).show();
            }
        }


    }

    /**
     * 显示搜索类型
     */
    private void showSeclectDialog() {
        if (null == myDialog) {
            myDialog = new MyDialog(context, R.style.MyDialog);
        }
        //设置下拉箭头
        selectIcon.setImageResource(R.mipmap.ic_quanzi_shouqi);
        myDialog.show();
        myDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                selectIcon.setImageResource(R.mipmap.ic_quanzi_xiala);
                //获取当前选择搜索的类型
                Log.e(TAG, "onDismiss: 当前的搜索类型" + myDialog.getCurrentSelect());
                if (0 == myDialog.getCurrentSelect()) {
                    nowSelectItem.setText("商品");
                } else {
                    nowSelectItem.setText("店铺");
                }

            }
        });


    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

        if (!CommonUtils.checkIsNull(context, searchInputView, null)) {
            //输入框内容非空时  显示删除按钮
            searchDelInputImg.setVisibility(View.VISIBLE);
            searchText.setText("搜索");
        } else {
            searchText.setText("取消");
            searchDelInputImg.setVisibility(View.GONE);
        }
    }
}
