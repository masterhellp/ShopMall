package com.wythe.mall.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.smarttop.library.utils.LogUtil;
import com.wythe.mall.R;
import com.wythe.mall.beans.RegisterBean;
import com.wythe.mall.utils.Api;
import com.wythe.mall.utils.AppConfig;
import com.wythe.mall.utils.CommonUtils;
import com.wythe.mall.utils.GotoActivity;
import com.wythe.mall.utils.MallDialog;
import com.wythe.mall.utils.MallDialogFactory;
import com.wythe.mall.utils.PhoneUtil;
import com.wythe.mall.utils.ToastUtil;
import com.wythe.mall.view.MyEditText;
import com.wythe.mall.view.TitleBarView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.smssdk.EventHandler;
import cn.smssdk.OnSendMessageHandler;
import cn.smssdk.SMSSDK;
import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by wythe on 2016/7/9.
 */
public class RegisterActivity extends BaseActivity {
    public static final String TAG = RegisterActivity.class.getSimpleName();
    @Bind(R.id.regist_zhuCeXieYi)
    TextView registZhuCeXieYi;
    @Bind(R.id.regist_yinSiTiaoKuan)
    TextView registYinSiTiaoKuan;
    @Bind(R.id.regist_checkView)
    LinearLayout registXieYiPart;
    @Bind(R.id.regist_xieyiButton)
    ImageView xieYiButton;
    @Bind(R.id.customService)
    TextView customService;

    private MyEditText etPhoneNumber;
    private MyEditText etPasswordNumber;
    private MyEditText etNameNumber;//用户名
    TitleBarView titleBarView;
    private Button get_verify_code;//获取验证码
    private int time = 120;
    private MyEditText input_message_code;
    //是否同意注册协议
    private boolean isAgreePact = true;

    final OkHttpClient client = new OkHttpClient();

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                String ReturnMessage = (String) msg.obj;
                Log.e("获取的返回信息:", ReturnMessage);
                RegisterBean bean = new Gson().fromJson(ReturnMessage, RegisterBean.class);
                String code = bean.getCode();
                if (code.equals("200")) {
                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    ToastUtil.makeText(RegisterActivity.this, "注册失败！");
                }

            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        initView();
        initMobSDK();
    }

    private void initMobSDK() {
        EventHandler eh = new EventHandler() {
            @Override
            public void afterEvent(int event, int result, Object data) {
                if (result == SMSSDK.RESULT_COMPLETE) {
                    // 回调完成
                    Log.i("TAG", "回调完成");
                    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                        // 提交验证码成功
                        LogUtil.e("TAG", "提交验证码成功");
                        savaUserInfo();
                    } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                        Log.i("TAG", "event == SMSSDK.EVENT_GET_VERIFICATION_CODE");
                        // 获取验证码成功
                        LogUtil.e("验证码", "获取成功");
//						ContextUtil.toast("获取验证码成功");
                    } else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
                        // 返回支持发送验证码的国家列表
                    }
                } else {
                    Log.i("TAG", "失败");
                    ((Throwable) data).printStackTrace();
                }
            }
        };
        SMSSDK.registerEventHandler(eh); // 注册短信回调
    }


    @Override
    protected void initView() {
//        super.initView();
        etNameNumber = (MyEditText) findViewById(R.id.register_name_editText);
        input_message_code = (MyEditText) findViewById(R.id.input_message_code);
//        ((TextView)findViewById(R.id.titleText)).setText(R.string.quick_register);
        titleBarView = (TitleBarView) findViewById(R.id.titleBarView);
        titleBarView.setTitle("注册");
        get_verify_code = (Button) findViewById(R.id.get_verify_code);
        get_verify_code.setOnClickListener(this);
        findViewById(R.id.register_button).setOnClickListener(this);
        etPhoneNumber = (MyEditText) findViewById(R.id.register_number_editText);
        etPasswordNumber = (MyEditText) findViewById(R.id.register_password_editText);
        // 注册协议和隐藏条款绑定事件
        registZhuCeXieYi.setOnClickListener(this);
        registYinSiTiaoKuan.setOnClickListener(this);
        registXieYiPart.setOnClickListener(this);
        //联系客服
        customService.setOnClickListener(this);

    }

    // 改变获取验证码按钮的点击状态
    Handler handler = new Handler();

    @SuppressWarnings("unused")
    private void changeButtonState() {
        get_verify_code.setClickable(false);
        time = 120;
        Runnable run = new Runnable() {
            @Override
            public void run() {
                if (time >= 0) {
                    get_verify_code.setText(time + "s后可重发");
                    handler.postDelayed(this, 1000);
                    time--;
                } else {
                    get_verify_code.setText("获取验证码");
                    get_verify_code.setClickable(true);
                }
            }
        };
        handler.postDelayed(run, 1000);
    }

    @Override
    public void onClick(View v) {
        final String phone = etPhoneNumber.getText().toString();
        switch (v.getId()) {
            case R.id.get_verify_code:
                if (!TextUtils.isEmpty(phone)) {
                    String message = getResources().getString(R.string.regist_sendTo) + "\n\n" + etPhoneNumber.getText().toString();
                    final MallDialog mallDialog = MallDialogFactory.getInstance().
                            createMallDialogWithStyle2(this, message, "取消", "确定");
                    mallDialog.setOnLeftButtonClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mallDialog.dismiss();
                        }
                    });
                    changeButtonState();
                    SMSSDK.getVerificationCode("86", phone, new OnSendMessageHandler() {
                        @Override
                        public boolean onSendMessage(String arg0, String arg1) {
                            boolean flag = PhoneUtil.isTel(phone);
                            return !flag;
                        }
                    });
                    mallDialog.show();
                } else {
                    ToastUtil.makeText(RegisterActivity.this, "手机号码不能为空");
                }
                break;
            case R.id.register_button:
                if (isAgreePact) {
                    String code = input_message_code.getText().toString();
                    if (PhoneUtil.isTel(phone)) {
                        SMSSDK.submitVerificationCode("86", phone, code);
//                    postRequest();
                    } else {
                        Toast.makeText(RegisterActivity.this, "号码格式不正确", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    ToastUtil.makeText(this, "请同意注册协议和隐私条款后方可注册");
                }
                break;
            case R.id.regist_yinSiTiaoKuan:
            case R.id.regist_zhuCeXieYi:
                //跳转到隐私条款页面
                GotoActivity.gotoActiviy(this, PactActivity.class);
                break;
            case R.id.regist_checkView:
                isAgreePact = !isAgreePact;
                if (isAgreePact) {
                    xieYiButton.setImageResource(R.mipmap.ic_address_checked);
                } else {
                    xieYiButton.setImageResource(R.mipmap.ic_address_unchecked);
                }
                break;
            case R.id.customService:
                //联系客服
                CommonUtils.openKeFu(this);
                break;
            default:

        }
    }

    private void savaUserInfo() {
        String phone = etPhoneNumber.getText().toString().trim();
        String password = etPasswordNumber.getText().toString().trim();
        String username = etNameNumber.getText().toString().trim();
        if (!TextUtils.isEmpty(phone) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(username)) {
            postRequest(phone, password, username);
        } else {
            ToastUtil.makeText(RegisterActivity.this, "输入不能为空");
        }


    }


    /**
     * phone 手机号
     * password 密码
     * username 用户名
     */
    private void postRequest(String phone, String password, String username) {
        /**
         * 建立请求表单，添加上传服务器的参数
         */
        RequestBody frombody = new FormBody.Builder()
                .add("phone", phone)
                .add("password", password)
                .add("username", username)
                .build();
        Log.e("frombody==", frombody.toString());
        //发起请求
        final Request request = new Request.Builder()
                .url(AppConfig.REQUEST_URL + "interfaces/userRegister?userName=" + username + "&tel=" + phone + "&password=" + password)
//                .url("http://4k134lmwer.51tcp.cc:63153/interfaces/login?userName=admin&password=111111")
//                .post(frombody)
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
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterAllEventHandler();
    }

    private void sendMsg() {
        OkHttpUtils.post()
                .url(Api.API_SERVER + Api.SENDMSG)
                .addParams("mobile", etPhoneNumber.getText().toString().trim())
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e(TAG, e.getMessage());
                        Toast.makeText(RegisterActivity.this, "获取验证码失败！", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            Log.i(TAG, response);
                            JSONObject json = new JSONObject(response);

                            int state = json.getInt("state_code");
                            String msg = json.getString("msg");
                            if (state != 200) {
                                Toast.makeText(RegisterActivity.this, "获取验证码失败！" + msg, Toast.LENGTH_LONG).show();
                                return;
                            }
                            String verfiCode = json.getJSONObject("data").getString("code");
                            Intent intent = new Intent(RegisterActivity.this,
                                    RegisterMessageCodeActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("phoneNum", etPhoneNumber.getText().toString().trim());
                            bundle.putString("verifyCode", verfiCode);
                            GotoActivity.gotoActiviy(RegisterActivity.this, RegisterMessageCodeActivity.class, bundle);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
}
