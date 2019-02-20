package com.wythe.mall.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.wythe.mall.R;
import com.wythe.mall.ui.RedPacketNotUsedFragment;
import com.wythe.mall.ui.RedPacketOutDateFragment;
import com.wythe.mall.ui.RedPacketUsedFragment;
import com.wythe.mall.view.Indicator;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

/**
 * Created by wythe on 2016/7/19.
 */
public class RedPacketActivity extends FragmentActivity implements View.OnClickListener {

    private Indicator indicator;
    private ViewPager viewPager;
    private FragmentPagerAdapter mAdapter;
    private List<Fragment> list;
    private TextView tvNotUsed,tvOutDate,tvUsed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hongbao);
        initView();
    }

    protected void initView() {
        findViewById(R.id.title_left_button).setOnClickListener(this);
        ((TextView)findViewById(R.id.titleText)).setText(R.string.pocket_hongbao);
        indicator = (Indicator) findViewById(R.id.indicator);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        tvNotUsed = (TextView) findViewById(R.id.not_used_tab);
        tvOutDate = (TextView) findViewById(R.id.out_date_tab);
        tvUsed = (TextView) findViewById(R.id.used_tab);
        tvNotUsed.setOnClickListener(this);
        tvOutDate.setOnClickListener(this);
        tvUsed.setOnClickListener(this);

        //List
        list = new ArrayList<Fragment>();
        list.add(new RedPacketNotUsedFragment());
        list.add(new RedPacketOutDateFragment());
        list.add(new RedPacketUsedFragment());
        //Adapter
        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return list.get(position);
            }

            @Override
            public int getCount() {
                return list.size();
            }
        };

        viewPager.setAdapter(mAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                indicator.scroll(position,positionOffset);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void onClick(View v) {
        switch (v.getId()){
            case R.id.title_left_button:{
                finish();
                break;
            }
            case R.id.not_used_tab:{
                viewPager.setCurrentItem(0);
                break;
            }
            case R.id.out_date_tab:{
                viewPager.setCurrentItem(1);
                break;
            }
            case R.id.used_tab:{
                viewPager.setCurrentItem(2);
                break;
            }
        }
    }
}
