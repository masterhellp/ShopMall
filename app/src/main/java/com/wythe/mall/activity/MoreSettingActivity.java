package com.wythe.mall.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.wythe.mall.R;
import com.wythe.mall.utils.CommonUtils;
import com.wythe.mall.utils.GotoActivity;
import com.wythe.mall.utils.UserManager;
import com.wythe.mall.view.TitleBarView;

import static com.wythe.mall.utils.CommonUtils.isLogin;

/**
 * Created by wythe on 2016/7/17.
 */
public class MoreSettingActivity extends BaseActivity {
    private TitleBarView titleBarView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_setting);
        initView();
    }

    @Override
    protected void initView() {
//        super.initView();
//        super.setNormalTitle(R.string.more_settings);
        titleBarView=(TitleBarView)findViewById(R.id.titleBarView);
        titleBarView.setTitle("更多设置");
//        titleBarView.setEditTitle("注销");
        titleBarView.showEditButton(false);
        titleBarView.showBackButton(true);
        findViewById(R.id.logout_button).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.logout_button:{
//                UserManager.getInstance().setLogin(false);
                SharedPreferences sp = MoreSettingActivity.this.getSharedPreferences("aiShangMall", MoreSettingActivity.this.MODE_PRIVATE);
                SharedPreferences.Editor ed=sp.edit();
                ed.clear();
                ed.commit();
                if(!CommonUtils.isLogin(MoreSettingActivity.this)){
                    GotoActivity.gotoActiviy(this, MainActivity.class, true);
                }

                //TODO 退出登录请求
                break;
            }
        }
    }
}
