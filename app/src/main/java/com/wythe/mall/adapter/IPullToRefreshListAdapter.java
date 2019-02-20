package com.wythe.mall.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 类描述:
 *
 * @author ChenHaidong
 * @Date 日期 ：  2013-9-9 时间 ： 下午4:02:47
 */
public abstract class IPullToRefreshListAdapter<T> extends BaseAdapter {

    public Context context;

    public LayoutInflater inflater;

    public String type;

    public List<T> list;

    public int currentSelectedId;

    public boolean isShowNoContent() {
        return showNoContent;
    }

    public void setShowNoContent(boolean showNoContent) {
        this.showNoContent = showNoContent;
    }

    public boolean showNoContent;

    public Boolean isListPage = false;

    public Boolean getListPage() {
        return isListPage;
    }

    public void setListPage(Boolean listPage) {
        isListPage = listPage;
    }


    public int getNoContentViewHeight() {
        return noContentViewHeight;
    }

    public void setNoContentViewHeight(int noContentViewHeight) {
        this.noContentViewHeight = noContentViewHeight;
    }

    public int noContentViewHeight;


    public List<T> viewList = new ArrayList<T>();
    public List<String> keyList = new ArrayList<String>();

    public String getListPosition() {
        return listPosition;
    }

    public void setListPosition(String listPosition) {
        this.listPosition = listPosition;
    }

    public String listPosition;


    public List<String> getDeleteList() {
        return deleteList;
    }

    public void setDeleteList(List<String> deleteList) {
        this.deleteList = deleteList;
    }

    //需要显示删除按钮的项
    public List<String> deleteList = new ArrayList<>();


    public List<String> getKeyList() {
        return keyList;
    }

    public void setKeyList(List<String> keyList) {
        this.keyList = keyList;
    }

    public boolean isHaveMore() {
        return isHaveMore;
    }

    public void setIsHaveMore(boolean isHaveMore) {
        this.isHaveMore = isHaveMore;
    }

    public boolean isHaveMore = false;

    public IPullToRefreshListAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    public IPullToRefreshListAdapter(Context c, String type, List<T> list) {
        this.context = c;
        this.type = type;
        this.list = list;
        this.inflater = LayoutInflater.from(c);
    }

    public void setSelection(int currentSelectedId) {
        if (currentSelectedId < 0) {
            return;
        }
        this.currentSelectedId = currentSelectedId;
        this.notifyDataSetChanged();
    }

    public void setResultList(List<T> resultList) {
        // TODO Auto-generated method stub
        this.viewList = resultList;
    }

    public void addAllResultList(List<T> list) {
        // TODO Auto-generated method stub
        this.viewList.addAll(list);
    }

    @Override
    public int getCount() {
        try {
            return this.viewList.size();
        } catch (Exception e) {
            return 0;
        }
    }

    @Override
    public T getItem(int position) {
        return this.viewList.get(position);
    }
}
