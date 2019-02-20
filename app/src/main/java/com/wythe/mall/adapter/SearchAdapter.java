package com.wythe.mall.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wythe.mall.R;
import com.wythe.mall.beans.SearchBean;

import java.util.List;

import androidx.annotation.Nullable;

/**
 * 搜索推荐关键词
 */
public class SearchAdapter extends BaseQuickAdapter<SearchBean, BaseViewHolder> {

    public SearchAdapter(int layoutResId, @Nullable List<SearchBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SearchBean item) {
        helper.setText(R.id.search_recomKey, item.getKeyword());
    }
}
