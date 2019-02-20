package com.wythe.mall.view;

import android.app.Activity;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.wythe.mall.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by admin on 16/7/13.
 */
public class PhotoDialog extends Dialog implements View.OnClickListener {

    private static final String TAG = "PhotoDialog";
    @Bind(R.id.cancel)
    TextView cancel;
    @Bind(R.id.qualityReason)
    TextView qualityReason;
    @Bind(R.id.queShaoReason)
    TextView queShaoReason;
    @Bind(R.id.notWant)
    TextView notWant;
    @Bind(R.id.wrongGoods)
    TextView wrongGoods;
    @Bind(R.id.bumReason)
    TextView bumReason;
    @Bind(R.id.others)
    TextView others;

    // 设置到
    private Activity context;
    private LayoutInflater mInflater;
    private String reason;

    private DialogListener listener;


    public PhotoDialog(Activity context) {
        super(context);
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        initView();
    }

    public PhotoDialog(Activity context, int themeResId, DialogListener listener) {
        super(context, themeResId);
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.listener = listener;
        initView();
    }

    protected PhotoDialog(Activity context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        initView();
    }


    private void initView() {
        View view = mInflater.inflate(R.layout.dialog_paike, null);
        setContentView(view);
        setCanceledOnTouchOutside(true);
        ButterKnife.bind(this);
        // 设置点击事件  拍照
        cancel.setOnClickListener(this);
        //质量原因
        qualityReason.setOnClickListener(this);
        queShaoReason.setOnClickListener(this);
        notWant.setOnClickListener(this);
        wrongGoods.setOnClickListener(this);
        bumReason.setOnClickListener(this);
        others.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.qualityReason:
                reason = "质量问题";
                break;
            case R.id.queShaoReason:
                reason = "缺少件";

                break;
            case R.id.notWant:
                reason = "不想要了";
                break;
            case R.id.wrongGoods:
                reason = "发错货";
                break;
            case R.id.bumReason:
                reason = "商品与页面描述不符";
                break;
            case R.id.others:
                reason = "其他";
                break;
        }
        dismiss();
        if (null != listener) {
            listener.setMsg(reason);
        }
    }


    public interface DialogListener {
        void setMsg(String msg);
    }

}
