package com.wythe.mall.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wythe.mall.R;
import com.wythe.mall.activity.address.AddAddressActivity;
import com.wythe.mall.utils.AppConfig;

import androidx.annotation.Nullable;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 页头设置
 */
public class TitleBarView extends LinearLayout implements View.OnClickListener {

    @Bind(R.id.title_back)
    ImageView titleBack;
    @Bind(R.id.tv_titlebar_center)
    TextView titleView;
    @Bind(R.id.tv_titlebar_right)
    TextView tvTitlebarRight;
    @Bind(R.id.add_addressView)
    ImageView addAddressView;

    private Context context;

    public TextView getTvTitlebarRight() {
        return tvTitlebarRight;
    }

    public void setTvTitlebarRight(TextView tvTitlebarRight) {
        this.tvTitlebarRight = tvTitlebarRight;
    }

    public TitleBarView(Context context) {
        super(context);
        this.context = context;
        initView();
    }

    public TitleBarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView();


    }

    public TitleBarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initView();
    }


    private void initView() {
        View view = LayoutInflater.from(context).inflate(R.layout.index_title_button, this);
        ButterKnife.bind(view);
        //返回按钮
        titleBack.setOnClickListener(this);
        // 添加地址
        addAddressView.setOnClickListener(this);


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back:
                ((Activity) context).onBackPressed();
                break;
            case R.id.add_addressView:
                Intent intent = new Intent(context, AddAddressActivity.class);
                ((Activity) context).startActivityForResult(intent, AppConfig.REQUESTCODE_ADDRESS);
                break;
        }
    }

    /**
     * 设置页面标题
     *
     * @param title
     */
    public void setTitle(String title) {
        titleView.setText(title);
    }

    public void setEditTitle(String title) {
        tvTitlebarRight.setVisibility(View.VISIBLE);
        tvTitlebarRight.setText(title);
    }

    /**
     * 显示添加地址按钮
     */
    public void showAddAddressButton(boolean isShow) {
        if (isShow) {
            addAddressView.setVisibility(View.VISIBLE);
        } else {
            addAddressView.setVisibility(View.GONE);

        }
    }

    /**
     * 显示编辑按钮
     *
     * @param isShow
     */
    public void showEditButton(boolean isShow) {
        if (isShow) {
            tvTitlebarRight.setVisibility(View.VISIBLE);
        } else {
            tvTitlebarRight.setVisibility(View.GONE);
        }
    }

    /**
     * 显示编辑按钮
     *
     * @param isShow
     */
    public void showBackButton(boolean isShow) {
        if (isShow) {
            titleBack.setVisibility(View.VISIBLE);
        } else {
            titleBack.setVisibility(View.GONE);
        }
    }

}
