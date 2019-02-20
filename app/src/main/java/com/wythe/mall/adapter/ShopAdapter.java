package com.wythe.mall.adapter;

import android.os.Bundle;

import com.wythe.mall.beans.OneTypeBean;
import com.wythe.mall.ui.TypeFragment;

import java.io.Serializable;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;


/**
 * @author anumbrella
 * @date 2015-8-1
 * <p>
 * fragment的适配器(右侧ViewPager加载选项卡)
 * <p>
 * Fragment对象会一直存留在内存中，所以当有大量的显示页时，就不适合用FragmentPagerAdapter
 * 了，FragmentPagerAdapter 适用于只有少数的page情况，像选项卡
 */
public class ShopAdapter extends FragmentPagerAdapter {

    public ShopAdapter(FragmentManager fragmentManager, List<OneTypeBean> data) {
        super(fragmentManager);
        // TODO Auto-generated constructor stub
        this.data = data;
    }

    private List<OneTypeBean> data;

    @Override
    public Fragment getItem(int index) {
        // TODO Auto-generated method stub
        Fragment fragment = new TypeFragment();
        Bundle bundle = new Bundle();
        // 把选中的index指针传入过去
        bundle.putInt("index", index);
//		bundle.putStringArrayList("bean",data);
        bundle.putSerializable("bean", (Serializable) data);
        // 设定在fragment当中去
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return data.size();
    }

}
