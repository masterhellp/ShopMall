package com.wythe.mall.sqlite;



import java.util.List;

import okhttp3.Request;

/**
 * Created by ningwang on 15/11/24.
 */
public interface LoadListCallBack<T> {

    // 获取到list
    public void loadList(List<T> list);

    // 获取不到数据 断网
    public void systemError(Request request, String errorInfo, Exception e);


    // 业务错误
    public void retLoad(String code);

    //详情接口 保存String内容
    public void loadString(String string);

    // 暂时不用这个 String转类很好转
    //public void loadClass(T tClass);

}
