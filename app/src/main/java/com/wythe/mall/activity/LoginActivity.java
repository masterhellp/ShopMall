package com.wythe.mall.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.wythe.mall.R;
import com.wythe.mall.base.BaseApplication;
import com.wythe.mall.beans.Customer;
import com.wythe.mall.http.datasource.MDataSource;
import com.wythe.mall.sqlite.LoadListCallBack;
import com.wythe.mall.utils.CommonUtils;
import com.wythe.mall.utils.GotoActivity;
import com.wythe.mall.utils.GsonUtils;
import com.wythe.mall.utils.SharedPreUtils;
import com.wythe.mall.utils.StatusBarUtil;
import com.wythe.mall.utils.ToastUtil;

import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Request;

/**
 * Created by wythe on 2016/7/8.
 */
public class LoginActivity extends BaseActivity {

    @Bind(R.id.login_nameDelImg)
    ImageView loginNameDelImg;
    @Bind(R.id.login_username)
    EditText edUsername;

    @Bind(R.id.login_passDelImg)
    ImageView loginPassDelImg;
    @Bind(R.id.login_passEyesImg)
    ImageView loginPassEyesImg;

    @Bind(R.id.login_password)
    EditText edPassword;

    @Bind(R.id.login_comfirm_button)
    TextView btnLogin;
    @Bind(R.id.register_link)
    TextView registerLink;
    @Bind(R.id.login_page_find_password)
    TextView loginPageFindPassword;
    @Bind(R.id.login_backButton)
    ImageView loginBackButton;
    //是否显示密码
    private boolean isShowText = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StatusBarUtil.immersive(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        initView();
    }

    @Override
    protected void initView() {
        //登录
        btnLogin.setOnClickListener(this);
        //注册
        registerLink.setOnClickListener(this);
        //找回密码
        loginPageFindPassword.setOnClickListener(this);
        // 是否明文显示密码
        loginPassEyesImg.setVisibility(View.GONE);
        // 删除按钮监听事件
        loginNameDelImg.setOnClickListener(this);
        loginPassDelImg.setOnClickListener(this);
        //返回按钮
        loginBackButton.setOnClickListener(this);
        //输入框监听 是否显示删除按钮
        edUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s)) {
                    loginNameDelImg.setVisibility(View.GONE);
                } else {
                    loginNameDelImg.setVisibility(View.VISIBLE);
                }
            }
        });
        // 密码输入监听
        edPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s)) {
                    loginPassDelImg.setVisibility(View.GONE);
                } else {
                    loginPassDelImg.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void savaUserInfo() {
        String phone = edUsername.getText().toString().trim();
        String password = edPassword.getText().toString().trim();
        if (!TextUtils.isEmpty(phone) && !TextUtils.isEmpty(password)) {
            postRequest(phone, password);
        } else {
            ToastUtil.makeText(LoginActivity.this, "输入不能为空");
        }
    }

    /**
     * phone 手机号
     * password 密码
     * username 用户名
     */
    private void postRequest(final String phone, String password) {
        /**
         * 建立请求表单，添加上传服务器的参数
         */
        HashMap<String, String> map = new HashMap<>();
        map.put("model", "interfaces");
        map.put("method", "appLogin");
        map.put("username", phone);
        map.put("password", password);
        MDataSource<Customer> dataSource = new MDataSource<>(this);
        dataSource.postData(map, new LoadListCallBack() {
            @Override
            public void loadList(List list) {

            }

            @Override
            public void systemError(Request request, String errorInfo, Exception e) {
                Log.e(TAG, "systemError: " + errorInfo);
                e.printStackTrace();
            }

            @Override
            public void retLoad(String code) {

            }

            @Override
            public void loadString(String string) {
                Log.e(TAG, "loadString 登录结果 : " + string);
                String result = GsonUtils.getNoteJsonString(string, "result");
                if (!CommonUtils.isEmpty(result)) {
                    String token = GsonUtils.getNoteJsonString(result, "token");
                    //"phone":0,
                    //    		"name":"张三",
                    //    		"id":1,
                    //    		"account":"admin",
                    //    		"token":"
                    if (!CommonUtils.isEmpty(token)) {
                        //保存用户登录信息
                        SharedPreUtils.setStringToPre(LoginActivity.this,
                                "userName", phone);
                        SharedPreUtils.setStringToPre(LoginActivity.this,
                                "token", token);
                        Customer customer = BaseApplication.getCustomer();
                        customer.setToken(token);
                        customer.setCustomerName(phone);
                        GotoActivity.gotoActiviy(LoginActivity.this, MainActivity.class);
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.login_passDelImg:
                edPassword.setText("");
                break;
            case R.id.login_nameDelImg:
                edUsername.setText("");
                break;
            case R.id.login_passEyesImg:
                isShowText = !isShowText;
                if (isShowText) {
                    // 设置EditText的密码为可见的
                    edPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    // 设置密码为隐藏的
                    edPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                break;
            case R.id.login_comfirm_button: {
                savaUserInfo();
                break;
            }
            case R.id.register_link: {
                GotoActivity.gotoActiviy(LoginActivity.this, RegisterActivity.class);
                break;
            }
            case R.id.login_page_find_password: {
                GotoActivity.gotoActiviy(LoginActivity.this, FindPasswordActivity.class);
                break;
            }
            case R.id.login_backButton:
                finish();
                break;
        }
    }
}
