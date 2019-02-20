package com.wythe.mall.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.wythe.mall.R;
import com.wythe.mall.beans.OrderDetailBean;
import com.wythe.mall.beans.SucesseBean;
import com.wythe.mall.utils.AppConfig;
import com.wythe.mall.utils.CommonUtils;
import com.wythe.mall.utils.KeyBoardManager;
import com.wythe.mall.utils.SharedPreUtils;
import com.wythe.mall.utils.StatusBarUtil;
import com.wythe.mall.utils.ToastUtil;
import com.wythe.mall.view.TitleBarView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class EvaluateActivity extends BaseActivity implements View.OnClickListener, TextWatcher {
    private EditText et_comment_content;
    private TextView tv_submit;
    private ImageView iv_comment_star_1, iv_comment_star_2,
            iv_comment_star_3, iv_comment_star_4, iv_comment_star_5;

    private List<ImageView> starList;
    private int currentStarCount;
    private InputMethodManager manager;
    final OkHttpClient client = new OkHttpClient();
    @Bind(R.id.evaluate_titleView)
    TitleBarView titleView;
    //晒单图片最多选择四张
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                String ReturnMessage = (String) msg.obj;
                Log.e("获取的返回信息:", ReturnMessage);
                SucesseBean bean = new Gson().fromJson(ReturnMessage, SucesseBean.class);
                String code = bean.getCode();
                Log.e("code===", code + "");
                if (code.equals("200")) {
                    ToastUtil.makeText(EvaluateActivity.this, "提交成功");
                    finish();
                }

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluate);
        ButterKnife.bind(this);
        StatusBarUtil.setPaddingSmart(this, titleView);
        initView();
        initListener();

    }

    @Override
    public void initView() {
        titleView.setTitle("商品评价");
        starList = new ArrayList<>();
        currentStarCount = 5;//默认5星
        manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        tv_submit = findViewById(R.id.tv_submit);
        et_comment_content = findViewById(R.id.et_comment_content);
        starList.add(iv_comment_star_1 = findViewById(R.id.iv_comment_star_1));
        starList.add(iv_comment_star_2 = findViewById(R.id.iv_comment_star_2));
        starList.add(iv_comment_star_3 = findViewById(R.id.iv_comment_star_3));
        starList.add(iv_comment_star_4 = findViewById(R.id.iv_comment_star_4));
        starList.add(iv_comment_star_5 = findViewById(R.id.iv_comment_star_5));
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (KeyBoardManager.isShouldHideInput(v, ev)) {
                if (manager != null) {
                    manager.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

    @Override
    public void initListener() {
        tv_submit.setOnClickListener(this);
        iv_comment_star_1.setOnClickListener(this);
        iv_comment_star_2.setOnClickListener(this);
        iv_comment_star_3.setOnClickListener(this);
        iv_comment_star_4.setOnClickListener(this);
        iv_comment_star_5.setOnClickListener(this);
        et_comment_content.addTextChangedListener(this);
    }


    private void postRequest(String customer_key, String customerName,
                             String customerImg, String orderNumber,
                             String goodsKey, String goodsName, String normName,
                             int amount, String content, String shopKey, String shopName) {
        /**
         * 建立请求表单，添加上传服务器的参数
         */
        RequestBody frombody = new FormBody.Builder()
//                .add("goodEvaluateKey",customer_key)
                .add("customerKey", customer_key)
                .add("customerName", customerName)
                .add("customerImg", customerImg)
                .add("orderNumber", orderNumber)
                .add("goodsKey", goodsKey)
                .add("goodsName", goodsName)
                .add("normName", normName)
                .add("amount", amount + "")
                .add("content", content)
                .add("shopKey", shopKey)
                .add("shopName", shopName)
                .build();


        Log.e("frombody==", frombody.toString());
        //发起请求
        final Request request = new Request.Builder()
                .url(AppConfig.REQUEST_URL + "/interfaces/addGoodsEvaluate")
                .post(frombody)
                .build();
        //新建一个线程，用于得到服务器响应的参数
        new Thread(new Runnable() {
            @Override
            public void run() {
                Response response = null;
                try {
                    response = client.newCall(request).execute();
                    if (response.isSuccessful()) {
                        //将服务器响应的参数response.body().string())发送到hanlder中，并更新ui
                        mHandler.obtainMessage(1, response.body().string()).sendToTarget();
                    } else {
                        throw new IOException("Unexpected code:" + response);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_submit:
                //评价提交
                validateComment();
                break;
            case R.id.iv_comment_star_1:
                currentStarCount = 1;
                break;

            case R.id.iv_comment_star_2:
                currentStarCount = 2;
                break;

            case R.id.iv_comment_star_3:
                currentStarCount = 3;
                break;

            case R.id.iv_comment_star_4:
                currentStarCount = 4;
                break;

            case R.id.iv_comment_star_5:
                currentStarCount = 5;
                break;

            default:
                break;
        }
        for (int i = 0, len = starList.size(); i < len; i++) {
            starList.get(i).setImageResource(i < currentStarCount ? R.mipmap.icon_comment_star_red : R.mipmap.icon_comment_star_gray);
        }
    }


    /**
     * 评价内容验证
     * .add("customerKey",customer_key)
     * .add("customerName",customerName)
     * .add("customerImg",customerImg)
     * .add("orderNumber",orderNumber)
     * .add("goodsKey ",goodsKey )
     * .add("goodsName",goodsName)
     * .add("normName",normName)
     * .add("amount",amount)
     * .add("content",content)
     * .add("shopKey",shopKey)
     * .add("shopName",shopName)
     */

    private void validateComment() {
        String content = et_comment_content.getText().toString();
        if (TextUtils.isEmpty(content)) {
            Toast.makeText(EvaluateActivity.this, "评论内容不能为空", Toast.LENGTH_SHORT).show();
            return;
        } else {
            String customerKey = SharedPreUtils.getPreStringInfo(this,"username");
            Intent intent = getIntent();
            OrderDetailBean item = (OrderDetailBean) intent.getSerializableExtra("evaluate_bean");
            String amount = item.getAmount();
            double amount_int = 1 ;
            if(!CommonUtils.isEmpty(amount)){
                amount_int = Double.parseDouble(amount);
            }
            postRequest(customerKey, customerKey, "customerImg", item.getOrderNumber(),
                    item.getGoodsKey(), item.getName(), item.getNormName(), (int) amount_int,
                    content, "shopKey", "shopName");
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        String content = et_comment_content.getText().toString();
        if (content.length() >= 255) {
            Toast.makeText(EvaluateActivity.this, "评论内容不能多于255个字符", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
    }

}
