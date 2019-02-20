package com.wythe.mall.activity;


import android.os.Bundle;

import com.wythe.mall.R;
import com.wythe.mall.utils.StatusBarUtil;
import com.wythe.mall.view.TitleBarView;

import butterknife.Bind;
import butterknife.ButterKnife;

public class PactActivity extends BaseActivity {

    @Bind(R.id.pact_itleView)
    TitleBarView pactItleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pact);
        ButterKnife.bind(this);
        pactItleView.setTitle("注册协议");
        StatusBarUtil.setPaddingSmart(this, pactItleView);
    }
}
