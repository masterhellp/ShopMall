package com.wythe.mall.http.okhttp.request;

import okhttp3.CacheControl;
import okhttp3.Request;
import okhttp3.RequestBody;

import java.util.Map;

/**
 * Created by zhy on 15/12/14.
 */
public class GetRequest extends OkHttpRequest
{
    public GetRequest(String url, Object tag, Map<String, String> params, Map<String, String> headers)
    {
        super(url, tag, params, headers);
    }

    @Override
    protected RequestBody buildRequestBody()
    {
        return null;
    }

    @Override
    protected Request buildRequest(Request.Builder builder, RequestBody requestBody)
    {
        builder.addHeader("Referer", "qkAndroid");
        CacheControl FORCE_NETWORK = new CacheControl.Builder().noCache().build();
        return builder.cacheControl(FORCE_NETWORK).get().build();
    }




}
