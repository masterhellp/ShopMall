package com.wythe.mall.utils;

import android.app.Activity;
import android.util.Log;

import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.wythe.mall.beans.WXPayBean;

public class PaymentHelper {
    /**
     * 微信支付
     */
    public void startWeChatPay(Activity activity, WXPayBean payReponse) {
        if (activity == null || payReponse == null)
            return;
        if (!WxPayConfig.APP_ID.equals(payReponse.getAppid()))
            return;

        IWXAPI wxapi = WXAPIFactory.createWXAPI(activity, WxPayConfig.APP_ID, true);
        // 将该app注册到微信
        wxapi.registerApp(WxPayConfig.APP_ID);
        if (!wxapi.isWXAppInstalled()) {
            Log.e("===","你没有安装微信");
            return;
        }
        //我们把请求到的参数全部给微信
        PayReq req = new PayReq(); //调起微信APP的对象
        req.appId = payReponse.getAppid();
        Log.e("getAppid: ",payReponse.getAppid());
        req.partnerId = payReponse.getPartnerid();
        Log.e("getPartnerid: ",payReponse.getPartnerid());
        req.prepayId = payReponse.getPrepayid();
        Log.e("getPrepayid: ",payReponse.getPrepayid());
        req.nonceStr = payReponse.getNoncestr();
        Log.e("getNonce_str: ",payReponse.getNoncestr());
        req.timeStamp = payReponse.getTimestamp();
        Log.e("getTimestamp: ",payReponse.getTimestamp());
        req.packageValue = payReponse.getPackageX(); //Sign=WXPay
        Log.e("getPackageX: ",payReponse.getPackageX());
        req.sign = payReponse.getSign();
        Log.e("getSign: ",payReponse.getSign());




        wxapi.sendReq(req); //发送调起微信的请求
    }
}
