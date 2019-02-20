package com.wythe.mall.sqlite;

import java.util.List;

/**
 * Created by ningwang on 15/11/24.
 */
public interface DataBaseListCallBack<T> {

    public void callBackByKey(List<T> list, int totalCount, String date, String detailString);

}
