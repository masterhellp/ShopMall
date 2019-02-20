package com.wythe.mall.view.shopcar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wythe.mall.R;

public class CustomCarGoodsCounterView extends LinearLayout implements View.OnClickListener {
    /**
     * 商品数量
     */
    int mGoodsNumber = 1;

    private TextView tvNumber;

    private UpdateGoodsNumberListener mUpdateGoodsNumberListener;

    public CustomCarGoodsCounterView(Context context) {
        this(context, null);
    }

    public CustomCarGoodsCounterView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomCarGoodsCounterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        View rootView = LayoutInflater.from(getContext()).inflate(R.layout.item_car_number_add_sub_layout, this);
        tvNumber = rootView.findViewById(R.id.tv_number);
        findViewById(R.id.tv_add).setOnClickListener(this);
        findViewById(R.id.tv_sub).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_add:
                addNumber();
                break;
            case R.id.tv_sub:
                subNumber();
                break;
        }
        updateGoodsNumber();
    }


    /**
     * 更新商品数量
     */
    public void updateGoodsNumber() {
        tvNumber.setText(String.valueOf(mGoodsNumber));
        if (mUpdateGoodsNumberListener != null) {
            mUpdateGoodsNumberListener.updateGoodsNumber(mGoodsNumber);
        }
    }

    public void addNumber() {
        ++mGoodsNumber;
    }

    public void subNumber() {
        mGoodsNumber = (mGoodsNumber - 1 < 1) ? 1 : mGoodsNumber - 1;
    }

    /**
     * 获取商品数量
     *
     * @return
     */
    public int getGoodsNumber() {
        return mGoodsNumber;
    }

    public void setGoodsNumber(int goodsNumber) {
        mGoodsNumber = goodsNumber;
        updateGoodsNumber();
    }

    public void setUpdateGoodsNumberListener(UpdateGoodsNumberListener listener) {
        mUpdateGoodsNumberListener = listener;
    }

    /**
     * 更新商品数量监听器
     */
    public static interface UpdateGoodsNumberListener {
        public void updateGoodsNumber(int number);
    }
}