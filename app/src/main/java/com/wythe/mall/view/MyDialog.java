package com.wythe.mall.view;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioGroup;

import com.wythe.mall.R;

import androidx.annotation.NonNull;

public class MyDialog extends Dialog {

    private Context context;

    //当前选择的搜索类型
    private int currentSelect = 0;

    public int getCurrentSelect() {
        return currentSelect;
    }


    public MyDialog(@NonNull Context context) {
        super(context);
        this.context = context;
        initView();
    }

    public MyDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;
        initView();
    }

    private void initView() {
        View view = LayoutInflater.from(context).inflate(R.layout.search_select_items, null);
        setContentView(view);
        setCanceledOnTouchOutside(true);
        RadioGroup radioGroup = view.findViewById(R.id.radio_selectGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (R.id.goods_item == checkedId) {
                    //选中了商品
                    currentSelect = 0;
                } else {
                    // 选中了店铺
                    currentSelect = 1;
                }
                dismiss();
            }
        });

    }


}
