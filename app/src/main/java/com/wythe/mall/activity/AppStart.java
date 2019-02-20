package com.wythe.mall.activity;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;
import com.wythe.mall.R;
import com.wythe.mall.utils.AppConfig;
import com.wythe.mall.utils.CommonUtils;
import com.wythe.mall.utils.PrintLog;
import com.wythe.mall.utils.SharedPreUtils;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 应用启动界面
 *
 * @author FireAnt（http://my.oschina.net/LittleDY）
 * @created 2014年12月22日 上午11:51:56
 */
public class AppStart extends Activity implements View.OnClickListener {

    private static final String TAG = "AppStart";

    @Bind(R.id.splish_time)
    TextView splishTime;
    @Bind(R.id.splashExitButton)
    RelativeLayout splashExitButton;
    @Bind(R.id.splash_imgView)
    ImageView splashImgView;
    // 计时器
    private MyCount count;
    // 广告存储路径
    private String splashUrl;
    // 广告跳转地址
    private String clentUrl;
    // 广告ID
    private String advId;
    //是否是从web页面打开的 如果是 则此参数有值
    private Uri webScameUri;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        setContentView(R.layout.app_start);
        ButterKnife.bind(this);
        //获取scame 如果是从web页面打开的 跳转到相应的页面
        Intent intent = getIntent();
        if (null != intent) {
            webScameUri = intent.getData();
            PrintLog.printDebug(TAG, "跳转链接地址：=====》》》" + webScameUri);
        }
        initSplashView();

    }

    private void initSplashView() {
        // 如果本地有缓存图片
        String localSplash = SharedPreUtils.getPreStringInfo(this, "app_splash");
        if (!CommonUtils.isEmpty(localSplash)) {
            // 设置显示图片
            String tempArray[] = localSplash.split("&_&");
            PrintLog.printDebug(TAG, "localSplash === " + localSplash);
            // 广告ID
            advId = tempArray[0];
            if (tempArray.length > 1) {
                // 广告地址
                splashUrl = tempArray[1];
            }
            if (tempArray.length > 2) {
                // 跳转地址
                clentUrl = tempArray[2];
            }
            PrintLog.printDebug(TAG, "广告跳转地址 ：" + clentUrl);

            PrintLog.printDebug(TAG,"-----imgUrl --:" + splashUrl );
            // 设置URL
            Picasso.with(this)
                    .load(splashUrl)
                    .into(splashImgView);
            splashImgView.setOnClickListener(this);

        } else {
            // 跳转到首页
//            redirectTo();
        }
        //设置点击事件
        splashExitButton.setOnClickListener(this);
        // 设置计时器显示
        splashExitButton.setVisibility(View.VISIBLE);
        count = new MyCount(4 * 1000, 1000);
        // 启动定时器
        count.start();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * 跳转到...
     */
    private void redirectTo() {
        Intent intent = new Intent(this, IndexActivity.class);
        if (null != webScameUri && !CommonUtils.isEmpty(webScameUri.toString())) {
            intent.putExtra("scameUrl", webScameUri.toString());
        }
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.splashExitButton:
                // t停止定时器
                if (null != count) {
                    count.cancel();
                }
                redirectTo();
                break;
            case R.id.splash_imgView:
                // 跳转
//                if (!CommonUtils.isEmpty(clentUrl)) {
//                    // 添加广告点击记录
//                    CommonUtils.addCount(this, null, advId);
//                    count.cancel();
//                    if (clentUrl.toLowerCase().startsWith("http") ||
//                            clentUrl.startsWith("https") || clentUrl.startsWith("www")) {
//                        Intent intent = new Intent();
//                        intent.putExtra("url", clentUrl);
//                        intent.putExtra("page_style", "ad");
//                        intent.setClass(this, CommonWebActivity.class);
//                        redirectTo();
//                        startActivity(intent);
//                    } else if (clentUrl.startsWith("qk")) {
//                        redirectTo();
//                        RouteSkip.skipActivity(this, clentUrl);
//                    }
//                }
                break;
        }
    }

    //倒计时显示的类
    class MyCount extends CountDownTimer {

        public MyCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            PrintLog.printInfor(TAG, "事件结束，关闭广告页面");
            this.cancel();
            redirectTo();
        }

        @Override
        public void onTick(long millisUntilFinished) {
            if (null != splishTime) {
                splishTime.setText((millisUntilFinished / 1000) + "");
            }
        }
    }

    // 屏蔽掉返回按钮
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            PrintLog.printDebug(TAG, "禁止返回按钮");
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
