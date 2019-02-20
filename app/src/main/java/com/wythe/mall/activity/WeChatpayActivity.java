package com.wythe.mall.activity;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tencent.mm.opensdk.constants.Build;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.wythe.mall.R;
import com.wythe.mall.activity.orders.FirmOrderActivity;
import com.wythe.mall.base.BaseApplication;
import com.wythe.mall.beans.Customer;
import com.wythe.mall.beans.WXPayBean;
import com.wythe.mall.http.datasource.MDataSource;
import com.wythe.mall.sqlite.LoadListCallBack;
import com.wythe.mall.util.Contancts;
import com.wythe.mall.util.Util;
import com.wythe.mall.utils.AppConfig;
import com.wythe.mall.utils.CommonUtils;
import com.wythe.mall.utils.Constants;
import com.wythe.mall.utils.GotoActivity;
import com.wythe.mall.utils.GsonUtils;
import com.wythe.mall.utils.PaymentHelper;
import com.wythe.mall.utils.SharedPreUtils;
import com.wythe.mall.utils.ToastUtil;
import com.wythe.mall.utils.WXPayUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class WeChatpayActivity extends BaseActivity implements View.OnClickListener {
    private IWXAPI api;

    Button appayBtn;


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.appay_btn:
                getWX();
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_we_chatpay);

        api = WXAPIFactory.createWXAPI(this, "wxb4ba3c02aa476ea1");
        api.registerApp("wxb4ba3c02aa476ea1");

        appayBtn = (Button) findViewById(R.id.appay_btn);
        appayBtn.setOnClickListener(this);

        Button checkPayBtn = (Button) findViewById(R.id.check_pay_btn);
        checkPayBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                boolean isPaySupported = api.getWXAppSupportAPI() >= Build.PAY_SUPPORTED_SDK_INT;
                Toast.makeText(WeChatpayActivity.this, String.valueOf(isPaySupported), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getWXPayCode() {
        /**
         * 建立请求表单，添加上传服务器的参数
         */
        HashMap<String, String> map = new HashMap<>();
        map.put("model", "interfaces");
        map.put("method", "WXPaySign");
//        //将要购买的商品生成JSON数据
//        Gson gson = new Gson();
//        //转换成json数据，再保存
//        String strJson = gson.toJson(dataList);
//        map.put("goodsList", strJson);
//        map.put("goodsCount",goodsSize+"" ) ;
//        String userName = SharedPreUtils.getPreStringInfo(this, "userName");
//        if (!CommonUtils.isEmpty(userName)) {
//            map.put("customerKey", userName);
//        }
//        //总价
//        map.put("totalMoney", totalPrice + "");
//        // 设置支付方式
//        map.put("payType", pay_type);
//        //
//        if (CommonUtils.checkIsNull(this, buyName, "请设置收货人姓名")) {
//            return;
//        }
//        map.put("buyName", buyName.getText().toString());
//        if (CommonUtils.checkIsNull(this, phoneNum, "请设置收货人电话")) {
//            return;
//        }
//        map.put("phone", phoneNum.getText().toString());
//        if (CommonUtils.checkIsNull(this, addressText, "请设置收货人地址")) {
//            return;
//        }
//        if (null == item) {
//            ToastUtil.makeText(this, "请选择收货地址");
//            return;
//        }
//        map.put("address", addressText.getText().toString());
//        map.put("addressKey", item.getCustomerAddressKey());
//        // 设置留言
//        map.put("totalMoney", totalPrice + "");
//        map.put("discountMoney", 0 + "");
//        map.put("activeMoney", totalPrice + "");
//        map.put("freightMoney", 15 + "");
        MDataSource<Customer> dataSource = new MDataSource<>(map, this);
        dataSource.getDetail(new LoadListCallBack() {
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
                try {
                    Log.e("微信支付====", string);
                    if (!TextUtils.isEmpty(string)) {
//                        string = string.substring(2, string.length() - 2);
                        WXPayBean bean=new Gson().fromJson(string,WXPayBean.class);
//                        PaymentHelper paymentHelper=new PaymentHelper();
////                        Log.e("微信支付", string);
//                        paymentHelper.startWeChatPay(FirmOrderActivity.this,bean);
                        WXPayUtils wxPayUtils=new WXPayUtils(bean);
                        wxPayUtils.toWXPayNotSign(WeChatpayActivity.this,bean);
//                        JSONObject json = new JSONObject(string);
//                        //if(null != json && !json.has("retcode") ){
//                        PayReq req = new PayReq();
//                        //req.appId = "wxf8b4f85f3a794e77";  // 测试用appId
//                        req.appId = json.getString("appid");
//                        Log.e("appID:", json.getString("appid"));
//                        req.partnerId = json.getString("partnerid");
//                        req.prepayId = json.getString("prepayid");
//                        req.nonceStr = json.getString("nonce_str");
//                        req.timeStamp = json.getString("timestamp");
//                        req.packageValue = json.getString("package");
//                        req.sign = json.getString("sign");
//                        req.extData = "app data"; // optional
//                        Toast.makeText(FirmOrderActivity.this, "正常调起支付", Toast.LENGTH_SHORT).show();
//                        // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
//                        api.sendReq(req);
//                        PayReq req = new PayReq();
//                        Log.e("========", "============");
//                        //req.appId = "wxf8b4f85f3a794e77";  // 测试用appId
//                        req.appId = bean.getAppid();
//                        req.partnerId = bean.getPartnerid();
//                        req.prepayId = bean.getPrepayid();
//                        req.nonceStr = bean.getNoncestr();
//                        req.timeStamp = bean.getTimestamp();
//                        req.packageValue = bean.getHarden();
//                        req.sign = bean.getSign();
//                        // req.extData			= "app data"; // optional
//                        Toast.makeText(WeChatpayActivity.this, "正常调起支付", Toast.LENGTH_SHORT).show();
//                        // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
////                            api.registerApp("wx3b788a4850538962");
//                        api.sendReq(req);

                    } else {
                        Log.d("PAY_GET", "字符为空");
                        Toast.makeText(WeChatpayActivity.this, "字符为空", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Log.e("PAY_GET", "异常：" + e.getMessage());
                    Toast.makeText(WeChatpayActivity.this, "异常：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void getWX(){
//        String url = "https://wxpay.wxutil.com/pub_v2/app/app_pay.php";
        Button payBtn = (Button) findViewById(R.id.appay_btn);
//        payBtn.setEnabled(false);
//        Toast.makeText(WeChatpayActivity.this, "获取订单中...", Toast.LENGTH_SHORT).show();
        try{
//            byte[] buf = Util.httpGet(url);
//            Log.e("数组长度===",buf.length+"");
//            if (buf != null && buf.length > 0) {
//                        String content = "{\"appid\":\"wx3b788a4850538962\",\"partnerid\":\"1524021131\",\"package\":\"Sign=WXPay\",\"noncestr\":\"517a34a36b22f2a7a569584e6043fa5e\",\"timestamp\":1548231914,\"prepayid\":\"wx23162514478149089f73b1a82692037634\",\"sign\":\"27dc68bd46b3eed3889e15de4ff43f05\"}";
                String content="{\"appid\":\"wxb4ba3c02aa476ea1\",\"partnerid\":\"1900006771\",\"package\":\"Sign=WXPay\",\"noncestr\":\"2342736b5bbf717fa671c19c366385d9\",\"timestamp\":1548680411,\"prepayid\":\"wx282100118123423f19850ed03630402038\",\"sign\":\"657AE936F1EF22F9543B8A8AF4F68C9F\"}";
                Log.e("get server pay params:",content);
                JSONObject json = new JSONObject(content);
                if(null != json && !json.has("retcode") ){
                    PayReq req = new PayReq();
                    Log.e("========","============");
                    //req.appId = "wxf8b4f85f3a794e77";  // 测试用appId
                    req.appId			= json.getString("appid");
                    req.partnerId		= json.getString("partnerid");
                    req.prepayId		= json.getString("prepayid");
                    req.nonceStr		= json.getString("noncestr");
                    req.timeStamp		= json.getString("timestamp");
                    req.packageValue	= json.getString("package");
                    req.sign			= json.getString("sign");
                    // req.extData			= "app data"; // optional
                    Toast.makeText(WeChatpayActivity.this, "正常调起支付", Toast.LENGTH_SHORT).show();
                    // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
//                            api.registerApp("wx3b788a4850538962");
                    api.sendReq(req);
                }else{
                    Log.d("PAY_GET", "返回错误"+json.getString("retmsg"));
                    Toast.makeText(WeChatpayActivity.this, "返回错误"+json.getString("retmsg"), Toast.LENGTH_SHORT).show();
                }
//            }else{
//                Log.d("PAY_GET", "服务器请求错误");
//                Toast.makeText(WeChatpayActivity.this, "服务器请求错误", Toast.LENGTH_SHORT).show();
//            }
        }catch(Exception e){
            Log.e("PAY_GET", "异常："+e.getMessage());
            Toast.makeText(WeChatpayActivity.this, "异常："+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        payBtn.setEnabled(true);
    }

}
