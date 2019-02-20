package com.wythe.mall.activity;


import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.wythe.mall.R;
import com.wythe.mall.ui.CartFragment;
import com.wythe.mall.ui.CategoryFragment;
import com.wythe.mall.ui.HomeFragment;
import com.wythe.mall.ui.PersonalFragment;
import com.wythe.mall.utils.StatusBarUtil;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import butterknife.Bind;
import butterknife.ButterKnife;


public class MainActivity extends FragmentActivity implements View.OnClickListener {

    public static final String TAG = MainActivity.class.getSimpleName();
    @Bind(R.id.frameLayout)
    FrameLayout frameLayout;
    @Bind(R.id.radio_group0)
    LinearLayout radioGroup0;
    @Bind(R.id.radio_group1)
    LinearLayout radioGroup1;
    @Bind(R.id.radio_group2)
    LinearLayout radioGroup2;
    @Bind(R.id.radio_group3)
    LinearLayout radioGroup3;
    @Bind(R.id.radioGroup)
    LinearLayout radioGroup;
    @Bind(R.id.index_homeText)
    TextView indexHomeText;
    @Bind(R.id.index_categoryText)
    TextView indexCategoryText;
    @Bind(R.id.index_cartText)
    TextView indexCartText;
    @Bind(R.id.index_centerText)
    TextView indexCenterText;
    @Bind(R.id.index_homeImg)
    ImageView indexHomeImg;
    @Bind(R.id.index_categoryImg)
    ImageView indexCategoryImg;
    @Bind(R.id.index_cartImg)
    ImageView indexCartImg;
    @Bind(R.id.index_centerImg)
    ImageView indexCenterImg;

    public  Fragment currentFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Fresco.initialize(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        StatusBarUtil.immersive(this);
        initView();
        initListener();
    }


    public void initView() {
        frameLayout = findViewById(R.id.frameLayout);
        //设置样式 默认选中第一个
        onClick(radioGroup0);

    }

    public void initListener() {
        //设置点击事件
        radioGroup0.setOnClickListener(this);
        radioGroup1.setOnClickListener(this);
        radioGroup2.setOnClickListener(this);
        radioGroup3.setOnClickListener(this);
    }

    //
    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (null != currentFragment && currentFragment instanceof HomeFragment) {
//                exit();
//                finish();
                currentFragment = null ;
                HomeFragment.instance = null ;
                CategoryFragment.instance = null ;
                CartFragment.instance = null ;
                PersonalFragment.instance = null ;
                onBackPressed();
            } else {
                onClick(radioGroup0);
                return true;
            }

        }
        return super.onKeyDown(keyCode, event);
    }

    public void exit() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(getApplicationContext(), "再按一次退出程序",
                    Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            finish();
            System.exit(0);
        }
    }


    @Override
    public void onClick(View v) {
        FragmentManager fManager = getSupportFragmentManager();
        FragmentTransaction ft = fManager.beginTransaction();
        //设置默认颜色
        indexHomeText.setTextColor(getResources().getColor(R.color.color_333));
        indexCategoryText.setTextColor(getResources().getColor(R.color.color_333));
        indexCartText.setTextColor(getResources().getColor(R.color.color_333));
        indexCenterText.setTextColor(getResources().getColor(R.color.color_333));
        // 设置默认图
        indexHomeImg.setImageResource(R.mipmap.icon_home_normal);

        indexCategoryImg.setImageResource(R.mipmap.icon_category_normal);

        indexCartImg.setImageResource(R.mipmap.icon_cart_normal);

        indexCenterImg.setImageResource(R.mipmap.icon_center_normal);

        switch (v.getId()) {
            case R.id.radio_group0:
                if (null != HomeFragment.getInstance()) {
                    ft.replace(R.id.frameLayout, HomeFragment.getInstance());
                } else {
                    ft.replace(R.id.frameLayout, new HomeFragment());
                }
                indexHomeText.setTextColor(Color.RED);
                indexHomeImg.setImageResource(R.mipmap.icon_home_selected);
                currentFragment = HomeFragment.getInstance();
                ft.commit();
                break;
            case R.id.radio_group1:
                if (null != CategoryFragment.getInstance()) {
                    ft.replace(R.id.frameLayout, CategoryFragment.getInstance());
                } else {
                    ft.replace(R.id.frameLayout, new CategoryFragment());
                }
                indexCategoryText.setTextColor(Color.RED);
                indexCategoryImg.setImageResource(R.mipmap.icon_category_selected);
                currentFragment = CategoryFragment.getInstance();
                ft.commit();
                break;
            case R.id.radio_group2:
                if (null != CartFragment.getInstance()) {
                    ft.replace(R.id.frameLayout, CartFragment.getInstance());
                } else {
                    ft.replace(R.id.frameLayout, new CartFragment());
                }
                indexCartText.setTextColor(Color.RED);
                indexCartImg.setImageResource(R.mipmap.icon_cart_selected);
                currentFragment = CartFragment.getInstance();
                ft.commit();
                break;
            case R.id.radio_group3:
                if (null != PersonalFragment.getInstance()) {
                    ft.replace(R.id.frameLayout, PersonalFragment.getInstance());
                } else {
                    ft.replace(R.id.frameLayout, new PersonalFragment());
                }
                indexCenterText.setTextColor(Color.RED);
                indexCenterImg.setImageResource(R.mipmap.icon_center_selected);
                currentFragment = PersonalFragment.getInstance();
                ft.commit();
                break;
        }
    }
}
