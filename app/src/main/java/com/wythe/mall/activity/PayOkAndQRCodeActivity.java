package com.wythe.mall.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.wythe.mall.R;
import com.wythe.mall.activity.orders.MyOrderActivity;
import com.wythe.mall.utils.QRCodeUtil;
import com.wythe.mall.utils.UsualDialogger;

import butterknife.ButterKnife;

public class PayOkAndQRCodeActivity extends BaseActivity {
    //    @Bind(R.id.qrcode)
//    TextView qrcode;
//    @Bind(R.id.payok_titleBarView)
//    TitleBarView titleBarView;
    private String qr_Message;
    private String goodslist;
    private UsualDialogger dialog2 = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_ok_and_qrcode);
        ButterKnife.bind(this);
//        StatusBarUtil.setPaddingSmart(this, titleBarView);
        initView();
        initData();
    }

    private void initData() {
//        addPay(payjson,goodslist);
    }

    @Override
    protected void initView() {
//        titleBarView.setTitle("二维码展示");
        Intent intent = getIntent();
        qr_Message = intent.getStringExtra("qrmessage");
        Log.e(TAG, "initView: " + qr_Message);
        Bitmap bitmap = QRCodeUtil.createQRCodeBitmap(qr_Message, 480);
        dialog2 = UsualDialogger.Builder(this).setTitle("支付成功")
                .setMessage(bitmap)
                .setOnConfirmClickListener("返回首页", new UsualDialogger.onConfirmClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent1 = new Intent(PayOkAndQRCodeActivity.this, MainActivity.class);
                        startActivity(intent1);
                        dialog2.dismiss();
                        finish();
                    }
                })
                .setOnCancelClickListener("查看订单", new UsualDialogger.onCancelClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent1 = new Intent(PayOkAndQRCodeActivity.this, MyOrderActivity.class);
                        startActivity(intent1);
                        dialog2.dismiss();
                        finish();
                    }
                })
                .build().shown();
    }


}
