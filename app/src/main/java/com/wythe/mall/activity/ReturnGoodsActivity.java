package com.wythe.mall.activity;


import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.coorchice.library.SuperTextView;
import com.squareup.picasso.Picasso;
import com.wythe.mall.R;
import com.wythe.mall.activity.orders.OrderDetailActivity;
import com.wythe.mall.beans.OrdersBean;
import com.wythe.mall.http.datasource.MDataSource;
import com.wythe.mall.sqlite.LoadListCallBack;
import com.wythe.mall.utils.CommonUtils;
import com.wythe.mall.utils.SharedPreUtils;
import com.wythe.mall.utils.ToastUtil;
import com.wythe.mall.view.PhotoDialog;
import com.wythe.mall.view.TitleBarView;

import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Request;

public class ReturnGoodsActivity extends BaseActivity {

    @Bind(R.id.return_titleBar)
    TitleBarView returnTitleBar;
    @Bind(R.id.goods_img)
    ImageView goodsImg;
    @Bind(R.id.goods_name)
    TextView goodsName;
    @Bind(R.id.goods_price)
    TextView goodsPrice;
    @Bind(R.id.goods_num)
    TextView goodsNum;
    @Bind(R.id.return_contentPart)
    RelativeLayout returnContentPart;
    @Bind(R.id.return_reasonTitle)
    TextView returnReasonTitle;
    @Bind(R.id.reason_menu)
    ImageView reasonMenu;
    @Bind(R.id.reason_part)
    RelativeLayout reasonPart;
    @Bind(R.id.login_password)
    EditText loginPassword;
    @Bind(R.id.submit_button)
    SuperTextView submitButton;
    @Bind(R.id.choose_reason)
    TextView chooseReason;
    @Bind(R.id.refund_type0)
    RelativeLayout refundType0;
    @Bind(R.id.refund_type1)
    RelativeLayout refundType1;
    @Bind(R.id.type1Img)
    ImageView type1Img;
    @Bind(R.id.type0Img)
    ImageView type0Img;

    private PhotoDialog photoDialog;

    private String reason;

    private String orderId;

    private String refundType = "1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return_goods);
        ButterKnife.bind(this);
        initView();
    }

    String money;

    //初始化布局文件
    @Override
    protected void initView() {
        //设置标题
        returnTitleBar.setTitle("申请退货");
        reasonMenu.setOnClickListener(this);
        //获取商品信息
//        intent.putExtra("money", toatolMoney);
//        intent.putExtra("orderId", orderId);
//        intent.putExtra("content", content);
//        intent.putExtra("logo", logo);
//        startActivity(intent);
        money = getIntent().getStringExtra("money");
        goodsPrice.setText(money);
        goodsName.setText(getIntent().getStringExtra("content"));
        String logo = getIntent().getStringExtra("logo");
        orderId = getIntent().getStringExtra("orderId");
        if (!CommonUtils.isEmpty(logo)) {
            Picasso.with(this)
                    .load(logo)
                    .placeholder(R.mipmap.a)
                    .error(R.mipmap.a)
                    .into(goodsImg);
        }
        //退货类型
        refundType0.setOnClickListener(this);
        refundType1.setOnClickListener(this);

        // 提交按钮
        submitButton.setOnClickListener(this);

    }


    /**
     * 设置点击事件
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.reason_menu:
                if (null == photoDialog) {
                    photoDialog = new PhotoDialog(this, R.style.Dialog_Fullscreen, new PhotoDialog.DialogListener() {
                        @Override
                        public void setMsg(String msg) {
                            if (!CommonUtils.isEmpty(msg)) {
                                chooseReason.setText(msg);
                                reason = msg;
                            }
                        }
                    });
                }
                photoDialog.show();
                break;
            case R.id.submit_button:
                submitData();
                break;
            case R.id.refund_type0:
                refundType = "0";
                type0Img.setImageResource(R.mipmap.ic_address_checked);
                type1Img.setImageResource(R.mipmap.ic_address_unchecked);
                break;
            case R.id.refund_type1:
                refundType = "1";
                type1Img.setImageResource(R.mipmap.ic_address_checked);
                type0Img.setImageResource(R.mipmap.ic_address_unchecked);
                break;
        }
    }

    /**
     * 提交退款数据
     */
    public void submitData() {
        HashMap<String, String> map = new HashMap<>();
        map.put("model", "interfaces");
        map.put("method", "refund");
        map.put("tradeNumber", getIntent().getStringExtra("tradeNum"));
        map.put("orderNumber", orderId);
        map.put("orderDetailKey", getIntent().getStringExtra("orderDetailKey"));
        map.put("refundType", refundType);
        map.put("reason", reason);
        map.put("customerKey", SharedPreUtils.getPreStringInfo(this, "userName"));
        map.put("money", money);
        MDataSource<OrdersBean> dataSource = new MDataSource<>(this);
        dataSource.postData(map, new LoadListCallBack() {
            @Override
            public void loadList(List list) {

            }

            @Override
            public void systemError(Request request, String errorInfo, Exception e) {

            }

            @Override
            public void retLoad(String code) {

            }

            @Override
            public void loadString(String string) {
                Log.e(TAG, "loadString:--- "+ string);
                if(string.contains("成功")){
                    ToastUtil.makeText(ReturnGoodsActivity.this,"申请成功");
                    finish();
                    if(null!=OrderDetailActivity.instance){
                        OrderDetailActivity.instance.finish();
                    }

                }

            }
        });


    }
}
