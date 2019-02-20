package com.wythe.mall.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.wythe.mall.R;
import com.wythe.mall.utils.AppManager;
import com.wythe.mall.utils.StatusBarUtil;


/**
 * Created by wythe on 2016/7/6.
 */
public abstract class BaseActivity extends Activity implements View.OnClickListener {
    public static final String TAG = BaseActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Fresco.initialize(getApplicationContext());
        super.onCreate(savedInstanceState);
        StatusBarUtil.immersive(this);
        AppManager.getInstance().addActivity(this);
    }

    protected void initView() {
        findViewById(R.id.title_left_button).setOnClickListener(this);
        findViewById(R.id.title_right_button).setOnClickListener(this);
    }

    protected void initListener() {

    }

    protected void setNormalTitle(int resId) {
        ((TextView) findViewById(R.id.titleText)).setText(resId);
    }

    protected void setNormalTitle(String title) {
        ((TextView) findViewById(R.id.titleText)).setText(title);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_left_button: {
                System.out.print("title_left_button");
                finish();
                break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e(TAG, "onActivityResult: qweqwewq");
        super.onActivityResult(requestCode, resultCode, data);
    }
}
