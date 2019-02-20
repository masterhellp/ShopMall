package com.wythe.mall.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.net.http.SslError;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.webkit.GeolocationPermissions;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.wythe.mall.utils.CommonUtils;
import com.wythe.mall.utils.PrintLog;

/**
 * Created by ningwang on 16/7/20.
 */
public class BaseWebView extends WebView {
    private static final String TAG = "BaseWebView";
    public static final int INTENT_REQUEST_CODE = 0;
    private ProgressBar progressbar;
    private Context context;
    private AudioManager audioManager;
    private AudioManager.OnAudioFocusChangeListener listener;
    private Boolean isShowProgress = true;
    public String reditUrl = "";

    private DownDPXListener downDPXListener;

    public void setFromMore(boolean fromMore) {
        this.fromMore = fromMore;
    }

    public boolean fromMore = false;

    public String getCurrentUrl() {
        return currentUrl;
    }

    public void setCurrentUrl(String currentUrl) {
        this.currentUrl = currentUrl;
    }

    //当前的url
    private String currentUrl;

    public void setPageType(String pageType) {
        this.pageType = pageType;
    }

    public String pageType;


    public BaseWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        progressbar = new ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal);
        ClipDrawable d = new ClipDrawable(new ColorDrawable(Color.parseColor("#44b549")), Gravity.LEFT, ClipDrawable.HORIZONTAL);
        progressbar.setProgressDrawable(d);
        progressbar.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, CommonUtils.dip2px(context, 2), 0, 0));
        this.addView(progressbar);
        setWebView();
        setWebChromeClient(new WebChromeClient());
    }


    /**
     * 设置webview的基本属性
     */
    @SuppressLint({"SetJavaScriptEnabled", "JavascriptInterface"})
    public void setWebView() {

        CommonUtils.setBasicProperties(context, this);
        // 设置webview的js对象
        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        listener = new AudioManager.OnAudioFocusChangeListener() {
            @Override
            public void onAudioFocusChange(int focusChange) {
                PrintLog.printError(TAG, "onAudioFocusChange: "
                        + focusChange);
            }
        };

        // 设置webview的基本属性
        WebViewClient client = new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                return true;
            }

            @Override
            public void onPageStarted(WebView view, final String url, Bitmap favicon) {
                PrintLog.printError(TAG, "onPageStarted:" + url);
                //特殊处理
                if (url != null) {
                    super.onPageStarted(view, url, favicon);
                }

            }

            @Override
            public void doUpdateVisitedHistory(WebView view, String url, boolean isReload) {
                super.doUpdateVisitedHistory(view, url, isReload);
                currentUrl = url;
                PrintLog.printDebug(TAG, "--当前实际的URL--" + url);
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {

                handler.proceed(); // 接受所有证书
            }

            @Override
            public void onPageFinished(WebView webView, String url) {
                //隐藏加载进度条
                if (null != webView) {
                    webView.getSettings().setBlockNetworkImage(false);
                }
                // baseActiviy里面设置setTitle方法
                super.onPageFinished(webView, url);
            }
        };
        this.setWebViewClient(client);

        this.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                HitTestResult result = ((WebView) v).getHitTestResult();
                if (null == result)
                    return false;
                int type = result.getType();
                if (type == HitTestResult.UNKNOWN_TYPE)
                    return false;
                if (type == HitTestResult.EDIT_TEXT_TYPE) {
                    return false;
                }
                switch (type) {
                    case HitTestResult.PHONE_TYPE:
                        // 处理拨号
                        break;
                    case HitTestResult.EMAIL_TYPE:
                        // 处理Email
                        break;
                    case HitTestResult.GEO_TYPE:
                        // TODO
                        break;
                    case HitTestResult.SRC_ANCHOR_TYPE:
                        // 超链接
                        break;
                    case HitTestResult.SRC_IMAGE_ANCHOR_TYPE:
                    case HitTestResult.IMAGE_TYPE:
                        // 处理长按图片的菜单项
                        String imageUrl = result.getExtra();
                        String imageName = imageUrl.substring(imageUrl.length() - 36, imageUrl.length() - 4);
                        PrintLog.printError(TAG, "imageUrl:" + imageUrl);
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
    }

    public void setDownDPXListener(DownDPXListener downDPXListener) {
        this.downDPXListener = downDPXListener;
    }


    public class WebChromeClient extends android.webkit.WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (isShowProgress) {
                if (newProgress == 100) {
                    progressbar.setVisibility(GONE);
                } else {
                    if (progressbar.getVisibility() == GONE)
                        progressbar.setVisibility(VISIBLE);
                    progressbar.setProgress(newProgress);
                }
            } else {
                progressbar.setVisibility(GONE);
            }
            super.onProgressChanged(view, newProgress);
        }

        @Override
        public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
            callback.invoke(origin, true, false);
            super.onGeolocationPermissionsShowPrompt(origin, callback);

        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            PrintLog.printError(TAG, "title :" + title);
        }
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        LayoutParams lp = (LayoutParams) progressbar.getLayoutParams();
        lp.x = l;
        lp.y = t;
        progressbar.setLayoutParams(lp);
        super.onScrollChanged(l, t, oldl, oldt);
    }


    // 下载doc pdf xls文件的回掉
    public interface DownDPXListener {

        void downComplete();

        void downFail();

    }


}
