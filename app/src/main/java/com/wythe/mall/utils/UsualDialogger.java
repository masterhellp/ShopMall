package com.wythe.mall.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.wythe.mall.R;

import androidx.annotation.NonNull;

/**
 * 通常都会使用的一种交互对话框
 *
 * @Created by Mr.Xu on 2018/4/30.
 */

public class UsualDialogger extends Dialog {
    private final String TITLE;
    private final Bitmap MESSAGE;
    private final String CONFIRMTEXT;
    private final String CANCELTEXT;
    private final onConfirmClickListener ONCONFIRMCLICKLISTENER;
    private final onCancelClickListener ONCANCELCLICKLISTENER;

    public interface onConfirmClickListener {
        void onClick(View view);
    }

    public interface onCancelClickListener {
        void onClick(View view);
    }

    private UsualDialogger(@NonNull Context context, String title, Bitmap message, String confirmText, String cancelText,
                           onConfirmClickListener onConfirmClickListener, onCancelClickListener onCancelClickListener) {
        super(context, R.style.DialogTheme);
        this.TITLE = title;
        this.MESSAGE = message;
        this.CONFIRMTEXT = confirmText;
        this.CANCELTEXT = cancelText;
        this.ONCONFIRMCLICKLISTENER = onConfirmClickListener;
        this.ONCANCELCLICKLISTENER = onCancelClickListener;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_ui);
        setCanceledOnTouchOutside(false);
        initView();
    }

    public static Builder Builder(Context context) {
        return new Builder(context);
    }

    private void initView() {
        Button btnConfirm = findViewById(R.id.btn_confirm);
        Button btnCancel = findViewById(R.id.btn_cancel);
        TextView tvTitle = findViewById(R.id.tv_title);
        ImageView tvMessage = findViewById(R.id.iv_message);

        if (!TITLE.isEmpty()) {
            tvTitle.setText(TITLE);
        }
        if (MESSAGE!=null) {
            tvMessage.setImageBitmap(MESSAGE);
        }
        if (!CONFIRMTEXT.isEmpty()) {
            btnConfirm.setText(CONFIRMTEXT);
        }
        if (!CANCELTEXT.isEmpty()) {
            btnCancel.setText(CANCELTEXT);
        }

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ONCONFIRMCLICKLISTENER == null) {
                    throw new NullPointerException("clicklistener is not null");
                } else {
                    ONCONFIRMCLICKLISTENER.onClick(view);
                }
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ONCANCELCLICKLISTENER == null) {
                    throw new NullPointerException("clicklistener is not null");
                } else {
                    ONCANCELCLICKLISTENER.onClick(view);
                }
            }
        });
    }

    public UsualDialogger shown() {
        show();
        return this;
    }

    public static class Builder {
        private String mTitle;
        private Bitmap mMessage;
        private String mConfirmText;
        private String mCancelText;
        private onConfirmClickListener mOnConfirmClickListener;
        private onCancelClickListener mOnCcancelClickListener;
        private Context mContext;

        private Builder(Context context) {
            this.mContext = context;
        }

        public Builder setTitle(String title) {
            this.mTitle = title;
            return this;
        }

        public Builder setMessage(Bitmap message) {
            this.mMessage = message;
            return this;
        }

        public Builder setOnConfirmClickListener(String confirmText, onConfirmClickListener confirmclickListener) {
            this.mConfirmText = confirmText;
            this.mOnConfirmClickListener = confirmclickListener;
            return this;
        }

        public Builder setOnCancelClickListener(String cancelText, onCancelClickListener onCancelclickListener) {
            this.mCancelText = cancelText;
            this.mOnCcancelClickListener = onCancelclickListener;
            return this;
        }

        public UsualDialogger build() {
            return new UsualDialogger(mContext, mTitle, mMessage, mConfirmText, mCancelText,
                    mOnConfirmClickListener, mOnCcancelClickListener);
        }
    }
}
