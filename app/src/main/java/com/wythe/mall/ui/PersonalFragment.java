package com.wythe.mall.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wythe.mall.R;
import com.wythe.mall.activity.LoginActivity;
import com.wythe.mall.activity.MainActivity;
import com.wythe.mall.activity.MessageCenterActivity;
import com.wythe.mall.activity.MoreSettingActivity;
import com.wythe.mall.activity.WeChatpayActivity;
import com.wythe.mall.activity.address.NewAddressActivity;
import com.wythe.mall.activity.orders.MyOrderActivity;
import com.wythe.mall.utils.AppConfig;
import com.wythe.mall.utils.CommonUtils;
import com.wythe.mall.utils.GotoActivity;
import com.wythe.mall.utils.SharedPreUtils;
import com.wythe.mall.utils.ToastUtil;
import com.wythe.mall.view.TitleBarView;

import java.io.IOException;

import androidx.fragment.app.Fragment;
import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class PersonalFragment extends Fragment implements View.OnClickListener {

    @Bind(R.id.who_and_say_hello)
    TextView nameView;
    @Bind(R.id.logout_button)
    Button logoutOutButton;
    private View view;
    private TitleBarView titleBarView;
    private LinearLayout mine_order;//我的订单
    private LinearLayout mine_Money;
    private LinearLayout layout_myAddress;//我的地址
    final OkHttpClient client = new OkHttpClient();
    private Context context;
    public static PersonalFragment instance;

    public static PersonalFragment getInstance() {
        if (null == instance) {
            instance = new PersonalFragment();
        }
        return instance;
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                String ReturnMessage = (String) msg.obj;
                Log.e("获取的返回信息:", ReturnMessage);
//                LoginBean bean=new Gson().fromJson(ReturnMessage,LoginBean.class);
//                String code=bean.getCode();
//                Log.e("code===",code+"");
//                if(code.equals("200")){
//                }else{
//                }

            }

        }
    };

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_personal, container, false);
        ButterKnife.bind(this, view);
        initView();
        instance = this;
        return view;
    }

    private void initView() {
        layout_myAddress = view.findViewById(R.id.layout_myAddress);
        layout_myAddress.setOnClickListener(this);
//        mine_Money = view.findViewById(R.id.mine_Money);
//        mine_Money.setOnClickListener(this);
        mine_order = view.findViewById(R.id.mine_order);
        mine_order.setOnClickListener(this);
        titleBarView = view.findViewById(R.id.titleBarView);
        titleBarView.setTitle("个人中心");
        titleBarView.showEditButton(false);
        titleBarView.showBackButton(false);
        view.findViewById(R.id.personal_click_for_login).setOnClickListener(this);
        //退出按钮点击事件
        logoutOutButton.setOnClickListener(this);
    }


    @Override
    public void onResume() {
        super.onResume();
        //登录未登录界面切换
        if (CommonUtils.isLogin(context)) {
            view.findViewById(R.id.personal_for_not_login).setVisibility(View.GONE);
            view.findViewById(R.id.personal_for_login_info).setVisibility(View.VISIBLE);
            nameView.setText(SharedPreUtils.getPreStringInfo(context, "userName"));
            logoutOutButton.setVisibility(View.VISIBLE);
        } else {
            view.findViewById(R.id.personal_for_not_login).setVisibility(View.VISIBLE);
            view.findViewById(R.id.personal_for_login_info).setVisibility(View.GONE);
            logoutOutButton.setVisibility(View.GONE);
        }
    }

    private void postRequest(int goodsCount, String shopkey, String orderName, String shopLogo, String shopName, int payType, String customerKey
            , String addressKey, String consignee, String address, String phone, double totalMoney, double discountMoney, double activeMoney, double freightMoney) {
        /**
         * 建立请求表单，添加上传服务器的参数
         */
        RequestBody frombody = new FormBody.Builder()
                .add("goodsCount", goodsCount + "")
                .add("shopkey", shopkey)
                .add("orderName", orderName)
                .add("shopLogo", shopLogo)
                .add("shopName", shopName)
                .add("payType", payType + "")
                .add("customerKey", customerKey)
                .add("addressKey", addressKey)
                .add("consignee", consignee)
                .add("address", address)
                .add("phone", phone)
                .add("totalMoney", totalMoney + "")
                .add("discountMoney", discountMoney + "")
                .add("activeMoney", activeMoney + "")
                .add("freightMoney", freightMoney + "")
                .build();


//        Log.e("frombody==",frombody.toString());
        //发起请求
        final Request request = new Request.Builder()
                .url(AppConfig.REQUEST_URL + "interfaces/addOrder")
//                .url("http://4k134lmwer.51tcp.cc:63153/interfaces/applogin?userName=admin&password=111111")
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
            case R.id.personal_click_for_login:
                GotoActivity.gotoActiviy(getActivity(), LoginActivity.class);
                break;
            case R.id.mine_order:
                if (CommonUtils.isLogin(getActivity())) {
                    GotoActivity.gotoActiviy(getActivity(), MyOrderActivity.class);
                } else {
                    ToastUtil.makeText(getActivity(), "您还没有登陆哦！");
                }
                break;
//            case R.id.mine_Money:
////                postRequest(1500, "shopkey", "shopkey", "shopkey", "shopkey", 1, "shopkey"
////                        , "shopkey", "shopkey", "shopkey", "shopkey", 5.55, 6.66, 7.77, 8.88
////                );
//                Intent intent=new Intent(getActivity(),WeChatpayActivity.class);
//                startActivity(intent);
//
//                break;

            case R.id.layout_myAddress:
                if (CommonUtils.isLogin(getActivity())) {
                    GotoActivity.gotoActiviy(getActivity(), NewAddressActivity.class);
                } else {
                    ToastUtil.makeText(getActivity(), "您还没有登陆哦！");
                }

                break;
            case R.id.logout_button:
                // 退出登錄
                SharedPreUtils.clearDatas(context);
                SharedPreUtils.setStringToPre(context,"username","");
                logoutOutButton.setVisibility(View.GONE);
                view.findViewById(R.id.personal_for_not_login).setVisibility(View.VISIBLE);
                view.findViewById(R.id.personal_for_login_info).setVisibility(View.GONE);
                break;
            default:
        }
    }
}
