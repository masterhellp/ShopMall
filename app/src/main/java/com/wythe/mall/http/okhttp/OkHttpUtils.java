package com.wythe.mall.http.okhttp;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.wythe.mall.http.okhttp.builder.GetBuilder;
import com.wythe.mall.http.okhttp.builder.OtherRequestBuilder;
import com.wythe.mall.http.okhttp.builder.PostFileBuilder;
import com.wythe.mall.http.okhttp.builder.PostFormBuilder;
import com.wythe.mall.http.okhttp.builder.PostStringBuilder;
import com.wythe.mall.http.okhttp.builder.SSLSocketClient;
import com.wythe.mall.http.okhttp.callback.Callback;
import com.wythe.mall.http.okhttp.https.HttpsUtils;
import com.wythe.mall.http.okhttp.request.RequestCall;
import com.wythe.mall.utils.PrintLog;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Response;

/**
 * Created by zhy on 15/8/17.
 */
public class OkHttpUtils {
    public static final String TAG = "OkHttpUtils";
    public static final long DEFAULT_MILLISECONDS = 30 * 1000;
    private static OkHttpUtils mInstance;
    private OkHttpClient mOkHttpClient;
    private Handler mDelivery;
    private static String requestTag; // 网络请求tag 用于没请求完关闭该页面 结束请求的标识

    private OkHttpUtils() {
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
        okHttpClientBuilder.sslSocketFactory(SSLSocketClient.getSSLSocketFactory())
                .hostnameVerifier(SSLSocketClient.getHostnameVerifier());

                //cookie enabled 暂时不用cookie
        mDelivery = new Handler(Looper.getMainLooper());


        okHttpClientBuilder.hostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });
        mOkHttpClient = okHttpClientBuilder.cache(null).build();
    }

    private boolean debug = true;
    private String tag;

    public OkHttpUtils debug(String tag) {
        debug = true;
        this.tag = tag;
        return this;
    }


    public OkHttpUtils requestTag(String requestTag) {
        this.requestTag = requestTag;
        return this;
    }

    public static OkHttpUtils getInstance() {
        if (mInstance == null) {
            synchronized (OkHttpUtils.class) {
                if (mInstance == null) {
                    mInstance = new OkHttpUtils();
                }
            }
        }
        return mInstance;
    }

    public Handler getDelivery() {
        return mDelivery;
    }

    public OkHttpClient getOkHttpClient() {
        return mOkHttpClient;
    }


    public static GetBuilder get() {
        return new GetBuilder().tag(requestTag);
    }

    public static PostStringBuilder postString() {
        return new PostStringBuilder().tag(requestTag);
    }

    public static PostFileBuilder postFile() {
        return new PostFileBuilder().tag(requestTag);
    }

    public static PostFormBuilder post() {
        return new PostFormBuilder().tag(requestTag);
    }

    public static OtherRequestBuilder put() {
        return new OtherRequestBuilder(METHOD.PUT);
    }

    public static OtherRequestBuilder head() {
        return new OtherRequestBuilder(METHOD.HEAD);
    }

    public static OtherRequestBuilder delete() {
        return new OtherRequestBuilder(METHOD.DELETE);
    }

    public static OtherRequestBuilder patch() {
        return new OtherRequestBuilder(METHOD.PATCH);
    }


    public void execute(final RequestCall requestCall, Callback callback) {
        if (debug) {
            if (TextUtils.isEmpty(tag)) {
                tag = TAG;
            }
            //Log.d(tag, "{method:" + requestCall.getRequest().method() + ", detail:" + requestCall.getOkHttpRequest().toString() + "}");
        }
        PrintLog.printError(tag, mOkHttpClient.connectTimeoutMillis() + " --- " + mOkHttpClient.readTimeoutMillis() + " ---- " + mOkHttpClient.writeTimeoutMillis());

        if (callback == null)
            callback = Callback.CALLBACK_DEFAULT;
        final Callback finalCallback = callback;

        requestCall.getCall().enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                if (!call.isCanceled()) {
                    sendFailResultCallback(call, e, finalCallback);
                }

            }

            @Override
            public void onResponse(final Call call, final Response response) {

//                PrintLog.printDebug(TAG," response.code() :" + response.code() );
                if (response.code() >= 400 && response.code() <= 599) {
                    try {
                        sendFailResultCallback(call, new RuntimeException(response.body().string()), finalCallback);
                    } catch (IOException e) {
                        sendFailResultCallback(call, new Exception(), finalCallback);
                        e.printStackTrace();
                    }
                    return;
                }

                if (!call.isCanceled()) {
                    try {
                        Object o = finalCallback.parseNetworkResponse(response);
                        sendSuccessResultCallback(o, finalCallback);
                    } catch (Exception e) {
                        sendFailResultCallback(call, e, finalCallback);
                    }
                }

            }
        });
    }


    public void sendFailResultCallback(final Call call, final Exception e, final Callback callback) {

        PrintLog.printDebug(TAG, "---callback----" + callback);
        if (callback == null) return;

        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                if (!call.isCanceled()) {
                    callback.onError(call, e);
                    callback.onAfter();
                }
            }
        });
    }

    public void sendSuccessResultCallback(final Object object, final Callback callback) {
        if (callback == null) return;
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                callback.onResponse(object);
                callback.onAfter();
            }
        });
    }

    public void cancelTag(Object tag) {
        for (Call call : mOkHttpClient.dispatcher().queuedCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
        for (Call call : mOkHttpClient.dispatcher().runningCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
    }


    public void setCertificates(InputStream... certificates) {
        mOkHttpClient = getOkHttpClient().newBuilder()
                .sslSocketFactory(HttpsUtils.getSslSocketFactory(certificates, null, null))
                .build();
    }


    public void setConnectTimeout(int timeout, TimeUnit units) {
        mOkHttpClient = getOkHttpClient().newBuilder()
                .readTimeout(timeout, units)
                .writeTimeout(timeout, units)
                .connectTimeout(timeout, units)
                .build();


    }


    public static class METHOD {
        public static final String HEAD = "HEAD";
        public static final String DELETE = "DELETE";
        public static final String PUT = "PUT";
        public static final String PATCH = "PATCH";


    }
}

