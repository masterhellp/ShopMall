package com.wythe.mall.http.datasource;

import android.content.Context;

import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mob.tools.utils.LocationHelper;
import com.wythe.mall.http.okhttp.OkHttpUtils;
import com.wythe.mall.http.okhttp.callback.StringCallback;
import com.wythe.mall.sqlite.DataBaseListCallBack;
import com.wythe.mall.sqlite.KVDatabase;
import com.wythe.mall.sqlite.LoadListCallBack;
import com.wythe.mall.utils.AppConfig;
import com.wythe.mall.utils.CommonUtils;
import com.wythe.mall.utils.GsonUtils;
import com.wythe.mall.utils.PrintLog;
import com.wythe.mall.utils.ToastUtil;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;


/**
 * Created by ningwang on 15/11/24.
 */
public class MDataSource<T> {

    private final String TAG = "MSource";

    //key值
    private String dataKey;

    //map 请求map
    private Map map;

    //是否使用缓存
    private Boolean useCache;

    //默认缓存有效期 5 分钟
    private int cacheTime = 5;

    //默认分页
    private Boolean pageAble = true;

    //默认一页10条数据
    private int step = 18;

    private Gson mGson;

    private Class mClass;
    //
    private List<T> dataList;
    private Boolean loadingMore = false;
    private Boolean refreshing = false;
    private String lastUpdateTime;

    //首页xml缓存详情 用到
    private String valueDetail;
    private Boolean isIndexPage = false;

    //消息总数
    private int totalCount = 0;
    //返回请求消息
    private String message;
    //返回ret参数
    private int ret;

    private Type type;

    private Context mContext;

    private String tag;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public MDataSource(Context context) {
        super();
        this.mContext = context;
    }


    /**
     * 详情和提交数据 不需要缓存的可以用该构造方法
     *
     * @param map
     * @param context
     */
    public MDataSource(Map map, Context context) {
        super();
        this.map = map;
        model = map.get("model").toString();
        method = map.get("method").toString();
        Log.e("MDataSource",model+"/"+method);
        this.mContext = context;
    }


    /**
     * 首页xml需要缓存的构造方法
     */
    public MDataSource(Map map, Context context, String key, Boolean indexPage) {
        super();
        this.map = map;
        model = map.get("model").toString();
        method = map.get("method").toString();
        this.dataKey = key;
        this.mContext = context;
        this.isIndexPage = indexPage;
        if (isIndexPage) {
            KVDatabase.getStringByKey(mContext, this.dataKey, new DataBaseListCallBack() {
                @Override
                public void callBackByKey(List list, int totalCount, String date, String detailString) {
                    lastUpdateTime = date;
                    valueDetail = detailString;
                }
            });
        }

    }

    /**
     * 列表构造方法
     */
    public MDataSource(String key, Boolean useCache, Boolean pageAble, Map map, Context context, Class aclass, Type type) {
        super();
        this.mGson = new Gson();
        this.dataList = new ArrayList<T>();
        this.dataKey = key;
        this.useCache = useCache;
        this.pageAble = pageAble;
        this.map = map;
        if (null != map.get("model")) {
            model = map.get("model").toString();
        }
        if (null != map.get("method")) {
            method = map.get("method").toString();
        }
        if (null != map.get("step")) {
            this.step = Integer.parseInt(map.get("step").toString());
        }
        this.mContext = context;
        this.type = type;

        this.mClass = aclass;

        // 把page传到key中

        if (this.useCache) {
            KVDatabase.getDataListByKey(mContext, key, 1, mClass, type, new DataBaseListCallBack<T>() {

                @Override
                public void callBackByKey(List<T> list, int count, String date, String detailString) {
                    if (list != null && list.size() > 0) {
                        dataList = list;
                        totalCount = count;
                        lastUpdateTime = date;
                        valueDetail = detailString;
                    }
                }
            });
        }

    }


    // 缓存是否过期
    public Boolean cacheOutOfDate() {
        if (!this.useCache || lastUpdateTime == null || "".equals(lastUpdateTime)) {
            // 如果没有获取到缓存时间，则返回已过期
            return true;
        }
        return (new Date().getTime() - Long.parseLong(lastUpdateTime)) / (1000 * 60) > cacheTime;
    }

    // 是否已经加载完成
    public Boolean hasReachEnd() {
        return (totalCount >= 0) && (totalCount <= this.dataList.size());
    }

    private String model;
    private String method;

    //加载下一页数据
    public void loadMoreDetail(final LoadListCallBack loadListCallBack) {
        loadingMore = true;
        if (this.pageAble) {
            //计算分页
            if (map.get("model") == null) {
                map.put("model", model);
                map.put("method", method);
            }
            map.put("page", (dataList.size() / step + 1) + "");
            map.put("step", step + "");
        }
        //先判断该页数是否有缓存数据 有的话使用缓存
        if (this.useCache) {
            KVDatabase.getDataListByKey(mContext, this.dataKey, (dataList.size() / step + 1), mClass, type, new DataBaseListCallBack<T>() {

                @Override
                public void callBackByKey(List<T> list, int count, String date, String detailString) {
                    if (list != null && list.size() > 0) {
                        PrintLog.printInfor(TAG, "上拉加载更多读取缓存");
                        for (T t : list) {
                            dataList.add(t);
                        }
                        totalCount = count;
                        lastUpdateTime = date;
                        valueDetail = detailString;

                        if (!cacheOutOfDate()) {
                            loadListCallBack.loadList(dataList);
                        } else {
                            okHttpRequestData(loadListCallBack);
                        }
                    } else {
                        okHttpRequestData(loadListCallBack);
                    }
                }
            });
        } else {
            okHttpRequestData(loadListCallBack);
        }

    }

    //下拉加载网络请求数据
    private void okHttpRequestData(final LoadListCallBack loadListCallBack) {
        PrintLog.printInfor(TAG, "上拉加载更多网络请求");
        OkHttpUtils.get().params(map).build()
                .execute(new StringCallback() {


                    @Override
                    public void onError(Call call, Exception e) {
                        if (!call.isCanceled()) {
                            loadListCallBack.systemError(call.request(), model + "/" + method + ": ", e);
                        }
                        loadingMore = false;
                    }

                    @Override
                    public void onResponse(String jsonString) {

//                        PrintLog.printError("results:", results);

                        try {
                            //在这里看接口返回的ret码 如果是100 正常继续执行 如果是约定的错误码 返回
                            String results = GsonUtils.getNoteJsonString(jsonString, "results");
                            if (Integer.parseInt(GsonUtils.getNoteJsonString(results, "ret")) == 500) {
                                loadListCallBack.retLoad(GsonUtils.getNoteJsonString(jsonString, "message"));
                                return;
                            }

//                            if (Integer.parseInt(GsonUtils.getNoteJsonString(results, "ret")) == 201) {
//                                RouteSkip.pathCompose(mContext, AppConfig.LOGIN_PAGE, null, true);
//                                return;
//                            }


                            List<T> convertList = GsonUtils.parserJsonToArrayBeans(results, "list", mClass);
                            for (T t : convertList) {
                                dataList.add(t);

                            }
                            String count = GsonUtils.getNoteJsonString(results, "totalCount");
                            count = count.replaceAll("“", "");
                            count = count.replaceAll("”", "");
                            count = count.replaceAll("\"", "");

                            //保存totalcount
                            if (!"".equals(count)) {
                                totalCount = Integer.parseInt(count);
                            } else {
                                totalCount = 0;
                            }

                            lastUpdateTime = new Date().getTime() + "";
                            if (useCache) {
                                //写入缓存
                                if (pageAble) {
                                    KVDatabase.putDataList(mContext, convertList, totalCount, dataKey, ((dataList.size() - 1) / step) + 1);
                                } else {
                                    //删除以前缓存
                                    KVDatabase.deleteListByKey(mContext, dataKey);
                                    KVDatabase.putDataList(mContext, convertList, totalCount, dataKey, 1);
                                }
                            }
                            loadingMore = false;
                            loadListCallBack.loadList(dataList);
                        } catch (Exception e) {
                            loadListCallBack.systemError(null, "OkHttpRequest OnResponse中出错", e);
                            e.printStackTrace();

                        }
                        loadingMore = false;

                    }
                });
    }


    //刷新数据 同步获取请求
    public List<T> refreshGetListData() {
        this.refreshing = true;
        if (this.pageAble) {
            //计算分页
            if (map.get("model") == null) {
                map.put("model", model);
                map.put("method", method);
            }
            map.put("page", 1 + "");
            map.put("step", step + "");
        }
        try {
            Response response = OkHttpUtils.get().params(map).build().execute();
            String jsonString = response.body().string();
            dataList.clear();
            String results = GsonUtils.getNoteJsonString(jsonString, "results");


            List<T> convertList = GsonUtils.parserJsonToArrayBeans(results, "list", mClass);
            for (T t : convertList) {
                dataList.add(t);

            }

            String count = GsonUtils.getNoteJsonString(results, "totalCount");
            count = count.replaceAll("“", "");
            count = count.replaceAll("”", "");
            count = count.replaceAll("\"", "");

            //保存totalcount
            if (!"".equals(count)) {
                totalCount = Integer.parseInt(count);
            } else {
                totalCount = 0;
            }

            lastUpdateTime = new Date().getTime() + "";
            if (useCache) {
                //删除以前缓存
                KVDatabase.deleteListByKey(mContext, dataKey);
                //写入缓存
                KVDatabase.putDataList(mContext, dataList, totalCount, dataKey, 1);
            }

            refreshing = false;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return dataList;
    }


    //刷新数据
    public void refreshData(final LoadListCallBack loadListCallBack) {
        this.refreshing = true;
        if (this.pageAble) {
            //计算分页
            if (map.get("model") == null) {
                map.put("model", model);
                map.put("method", method);
            }

            //主站换一换 page是传递过来的
            if ((null == map.get("page") || !"zhuzhanhotspot".equals(model))) {
                map.put("page", 1 + "");
            }
            map.put("step", step + "");
        }
        String okHttpUrl = AppConfig.REQUEST_URL;
//        if (("goods".equals(model) && "listrecmon".equals(method)) ||
//                "shopRecom".equals(model) || "goodsRecomQingk".equals(model)) {
//            okHttpUrl = AppConfig.QKMALL_REQUEST_URL;
//        }

        OkHttpUtils.get().params(map).url(okHttpUrl).build().execute(new StringCallback() {

            @Override
            public void onError(Call call, Exception e) {
                if (!call.isCanceled()) {
                    loadListCallBack.systemError(call.request(), model + "/" + method, e);
                }
                refreshing = false;
            }

            @Override
            public void onResponse(String jsonString) {

                try {

                    dataList.clear();
                    String results = GsonUtils.getNoteJsonString(jsonString, "results");
                    //在这里看接口返回的ret码 如果是100 正常继续执行 如果是约定的错误码 返回
                    if (Integer.parseInt(GsonUtils.getNoteJsonString(results, "ret")) == 500) {
                        loadListCallBack.retLoad(GsonUtils.getNoteJsonString(jsonString, "msg"));
                        return;
                    }
                    List<T> convertList = GsonUtils.parserJsonToArrayBeans(results, "list", mClass);
                    for (T t : convertList) {
                        dataList.add(t);
//                        Log.e("解析JSON==",t.toString());
                    }

                    String count = GsonUtils.getNoteJsonString(results, "totalCount");
                    count = count.replaceAll("“", "");
                    count = count.replaceAll("”", "");
                    count = count.replaceAll("\"", "");

                    //保存totalcount
                    if (!"".equals(count)) {
                        totalCount = Integer.parseInt(count);
                    } else {
                        totalCount = 0;
                    }

                    lastUpdateTime = new Date().getTime() + "";
                    if (useCache) {
                        //删除以前缓存
                        KVDatabase.deleteListByKey(mContext, dataKey);
                        //写入缓存
                        KVDatabase.putDataList(mContext, dataList, totalCount, dataKey, 1);
                    }
                    loadListCallBack.loadList(dataList);
                } catch (Exception e) {
                    loadListCallBack.systemError(null, "OkHttpRequest OnResponse中出错", e);

                }
                refreshing = false;

            }
        });

    }

    //获取详情
    public void getDetail(final LoadListCallBack loadListCallBack) {
        if (map.get("model") == null) {
            map.put("model", model);
            map.put("method", method);
        }


        OkHttpUtils.get().params(map).build().execute(new StringCallback() {

            @Override
            public void onError(Call call, Exception e) {
                //加载出错
                if (!call.isCanceled()) {
                    loadListCallBack.systemError(call.request(), "接口错误", e);
                }
            }

            @Override
            public void onResponse(String dataString) {

                loadListCallBack.loadString(dataString);
            }
        });

    }


    //提交数据
    public void postData(Map postMap, final LoadListCallBack loadListCallBack) {
        // 添加网络tishi
        if (!CommonUtils.isNetOk(mContext)) {
            ToastUtil.makeText(mContext,"网络已断开，请联网后重试");
            return;
        }

        OkHttpUtils.post().params(postMap).build().execute(new StringCallback() {

            @Override
            public void onError(Call call, Exception e) {
                //加载出错
                if (!call.isCanceled()) {
                    loadListCallBack.systemError(call.request(), model + "/" + method, e);
                }

            }

            @Override
            public void onResponse(String dataString) {
//                PrintLog.printError("results:", "dataString:" + dataString);
                if (!"".equals(dataString) && null != dataString) {
                    String results = GsonUtils.getNoteJsonString(dataString, "results");
//                    PrintLog.printError("results:", results);
                    //loadListCallBack.retLoad(GsonUtils.getNoteJsonString(results, "ret"));
                    loadListCallBack.loadString(dataString);
                    //ToaskShow.showToast(mContext, GsonUtils.getNoteJsonString(results, "msg"), Toast.LENGTH_SHORT);
                } else {
                    ToastUtil.makeText(mContext, "请求数据异常，请稍后重试");
                }
            }
        });
    }

    // get set方法
    public String getDataKey() {
        return dataKey;
    }

    public void setDataKey(String dataKey) {
        this.dataKey = dataKey;
    }

    public List<T> getDataList() {
        return dataList;
    }

    public void setDataList(List dataList) {
        this.dataList = dataList;
    }

    public String getValueDetail() {
        return valueDetail;
    }

    public void setValueDetail(String valueDetail) {
        this.valueDetail = valueDetail;
    }

    public Boolean getLoadingMore() {
        return loadingMore;
    }

    public void setLoadingMore(Boolean loadingMore) {
        this.loadingMore = loadingMore;
    }

    public Boolean getRefreshing() {
        return refreshing;
    }

    public void setRefreshing(Boolean refreshing) {
        this.refreshing = refreshing;
    }

    public String getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(String lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getRet() {
        return ret;
    }

    public void setRet(int ret) {
        this.ret = ret;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map getMap() {
        return map;
    }

    public void setMap(Map map) {
        this.map = map;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }


}
