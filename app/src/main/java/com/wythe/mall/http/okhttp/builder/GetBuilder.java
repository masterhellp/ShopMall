package com.wythe.mall.http.okhttp.builder;

import com.wythe.mall.http.okhttp.request.GetRequest;
import com.wythe.mall.http.okhttp.request.RequestCall;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by zhy on 15/12/14.
 */
public class GetBuilder extends OkHttpRequestBuilder
{
    @Override
    public RequestCall build()
    {
        GetRequest getRequest = new GetRequest(url, tag, params, headers);
        getRequest.appendParams();
        return getRequest.build();
    }



    @Override
    public GetBuilder url(String url)
    {
        this.url = url;
        return this;
    }

    @Override
    public GetBuilder tag(Object tag)
    {
        this.tag = tag ;
        return this;
    }

    @Override
    public GetBuilder params(Map<String, String> params)
    {
        this.params = params;
        return this;
    }

    @Override
    public GetBuilder addParams(String key, String val)
    {
        if (this.params == null)
        {
            params = new LinkedHashMap<>();
        }
        params.put(key, val);
        return this;
    }

    @Override
    public GetBuilder headers(Map<String, String> headers)
    {
        this.headers = headers;
        return this;
    }

    @Override
    public GetBuilder addHeader(String key, String val)
    {
        if (this.headers == null)
        {
            headers = new LinkedHashMap<>();
        }
        headers.put(key, val);
        return this;
    }
}
