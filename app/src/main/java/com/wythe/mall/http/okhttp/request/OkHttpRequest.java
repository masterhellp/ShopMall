package com.wythe.mall.http.okhttp.request;


import com.wythe.mall.base.AppContext;
import com.wythe.mall.http.okhttp.callback.Callback;
import com.wythe.mall.http.okhttp.utils.Exceptions;
import com.wythe.mall.utils.AppConfig;
import com.wythe.mall.utils.CommonUtils;
import com.wythe.mall.utils.PrintLog;
import com.wythe.mall.utils.SharedPreUtils;

import java.util.Map;

import okhttp3.Headers;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by zhy on 15/11/6.
 */
public abstract class OkHttpRequest {

    protected String url;
    protected Object tag;
    protected Map<String, String> params;
    protected Map<String, String> headers;
    protected Request.Builder builder = new Request.Builder();

    protected OkHttpRequest(String url, Object tag,
                            Map<String, String> params, Map<String, String> headers) {
        this.url = url;
        this.tag = tag;
        this.params = params;
        this.headers = headers;
        if (params.get("model") != null && params.get("method") != null) {
            this.url = url + params.get("model") + "/" + params.get("method");
        }
//        this.params.put("os", AppConfig.OS);
//        this.params.put("terminal", AppConfig.TERMINAL);
//        this.params.put("v", AppConfig.VERSION);
//        this.params.put("udid", AppContext.getInstance().uid());
        //如果用户已登录 需要添加登录token
        if (CommonUtils.isLogin(AppContext.getInstance())) {
            this.params.put("token", SharedPreUtils.getPreStringInfo(AppContext.getInstance(), "user_token"));
        }
        //打印出请求的字符串
        String printUrl = this.url;
        int i = 0;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (i == 0) {
                printUrl = printUrl + "?" + entry.getKey() + "=" + entry.getValue();
            } else {
                printUrl = printUrl + "&" + entry.getKey() + "=" + entry.getValue();
            }
            i++;
        }
        PrintLog.printError("OkHttpRequest", "printUrl: " + printUrl);
        if (url == null) {
            Exceptions.illegalArgument("url can not be null.");
        }
    }


    public void appendParams() {
        StringBuilder sb = new StringBuilder();
        sb.append(url + "?");
        if (params != null && !params.isEmpty()) {
            for (String key : params.keySet()) {
                //参数中忽略model 和 method
                if (!"model".equals(key) && !"method".equals(key)) {
                    sb.append(key).append("=").append(params.get(key)).append("&");
                }
            }
        }

        sb = sb.deleteCharAt(sb.length() - 1);
        this.url = sb.toString();

//        PrintLog.printError("请求的url路径：", ">>>>>>>>> " + this.url);
    }


    protected abstract RequestBody buildRequestBody();

    protected RequestBody wrapRequestBody(RequestBody requestBody, final Callback callback) {
        return requestBody;
    }

    protected abstract Request buildRequest(Request.Builder builder, RequestBody requestBody);

    public RequestCall build() {
        return new RequestCall(this);
    }


    public Request generateRequest(Callback callback) {
        RequestBody requestBody = wrapRequestBody(buildRequestBody(), callback);
        prepareBuilder();
        return buildRequest(builder, requestBody);
    }


    private void prepareBuilder() {
        builder.url(url).tag(tag);
        builder.addHeader("Referer", "qkAndroid");
        appendHeaders();
    }


    protected void appendHeaders() {
        Headers.Builder headerBuilder = new Headers.Builder();
        if (headers == null || headers.isEmpty()) return;

        for (String key : headers.keySet()) {
            headerBuilder.add(key, headers.get(key));
        }
        builder.headers(headerBuilder.build());
    }

    @Override
    public String toString() {
        return "OkHttpRequest{" +
                "url='" + url + '\'' +
                ", tag=" + tag +
                ", params=" + params +
                ", headers=" + headers +
                '}';
    }
}
