package com.wythe.mall.wxapi;







import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.wythe.mall.R;
import com.wythe.mall.activity.PayOkAndQRCodeActivity;
import com.wythe.mall.activity.orders.FirmOrderActivity;
import com.wythe.mall.activity.orders.MyOrderActivity;
import com.wythe.mall.util.Contancts;
import com.wythe.mall.utils.ToastUtil;
import com.wythe.mall.utils.WxPayConfig;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {
	
	private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";
	
    private IWXAPI api;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_result);
        
    	api = WXAPIFactory.createWXAPI(this, WxPayConfig.APP_ID,true);
        api.handleIntent(getIntent(), this);
    }

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
        api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {
	}

	@SuppressLint("LongLogTag")
	@Override
	public void onResp(BaseResp resp) {
		Log.d(TAG, "onPayFinish, errCode = " + resp.errCode);
		int code = resp.errCode;
		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
			Log.e(TAG, "onResp: " + code + resp.errStr);
			switch (code) {
				case 0:
					Intent intent = new Intent(WXPayEntryActivity.this, MyOrderActivity.class);
					startActivity(intent);
					finish();
					ToastUtil.makeText(WXPayEntryActivity.this, "支付成功");
					break;
				case -1:
					finish();
					// 支付失败 可能的原因：签名错误、未注册APPID、项目设置APPID不正确、注册的APPID与设置的不匹配、其他异常等
					ToastUtil.makeText(WXPayEntryActivity.this, "支付失败");
					break;
				case -2:
					finish();
					ToastUtil.makeText(WXPayEntryActivity.this, "支付取消");
					break;
			}
		}
//		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
//			AlertDialog.Builder builder = new AlertDialog.Builder(this);
//			builder.setTitle(R.string.app_tip);
//			builder.setMessage(getString(R.string.pay_result_callback_msg, String.valueOf(resp.errCode)));
//			builder.show();
//		}
	}
}