package com.wythe.mall.activity.orders;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.wythe.mall.R;
import com.wythe.mall.activity.BaseActivity;
import com.wythe.mall.activity.PayOkAndQRCodeActivity;
import com.wythe.mall.activity.WeChatpayActivity;
import com.wythe.mall.activity.address.AddressListActivity;
import com.wythe.mall.adapter.shopcar.OrderAdapter;
import com.wythe.mall.beans.AliPayResult;
import com.wythe.mall.beans.Customer;
import com.wythe.mall.beans.Goods;
import com.wythe.mall.beans.NewAddressBean;
import com.wythe.mall.beans.OrdersBean;
import com.wythe.mall.beans.PayBean;
import com.wythe.mall.beans.PayResultBean;
import com.wythe.mall.beans.ShoppingCarDataBean;
import com.wythe.mall.beans.WXPayBean;
import com.wythe.mall.http.datasource.MDataSource;
import com.wythe.mall.sqlite.LoadListCallBack;
import com.wythe.mall.util.PayResult;
import com.wythe.mall.utils.AppConfig;
import com.wythe.mall.utils.CommonUtils;
import com.wythe.mall.utils.DateUtil;
import com.wythe.mall.utils.GsonUtils;
import com.wythe.mall.utils.OrderInfoUtil2_0;
import com.wythe.mall.utils.PaymentHelper;
import com.wythe.mall.utils.SharedPreUtils;
import com.wythe.mall.utils.ToastUtil;
import com.wythe.mall.utils.WXPayUtils;
import com.wythe.mall.view.MyExpandableListView;
import com.wythe.mall.view.TitleBarView;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Request;

/**
 * 确认订单页面
 * <p>
 * 在该页面发起订单结算 等。
 */

public class FirmOrderActivity extends BaseActivity {

    /**
     * 支付宝支付业务：入参app_id
     */
    public static final String APPID = "2019011462945987";

    /**
     * 支付宝账户登录授权业务：入参pid值
     */
    public static final String PID = "2088431185459264";
    /**
     * 支付宝账户登录授权业务：入参target_id值
     */
    public static final String TARGET_ID = "1111111";

    /** 商户私钥，pkcs8格式 */
    /** 如下私钥，RSA2_PRIVATE 或者 RSA_PRIVATE 只需要填入一个 */
    /** 如果商户两个都设置了，优先使用 RSA2_PRIVATE */
    /** RSA2_PRIVATE 可以保证商户交易在更加安全的环境下进行，建议使用 RSA2_PRIVATE */
    /** 获取 RSA2_PRIVATE，建议使用支付宝提供的公私钥生成工具生成， */
    /**
     * 工具地址：https://doc.open.alipay.com/docs/doc.htm?treeId=291&articleId=106097&docType=1
     */
    private static final int SDK_PAY_FLAG = 1001;


    @Bind(R.id.buy_name)
    TextView buyName;
    @Bind(R.id.phone_num)
    TextView phoneNum;
    @Bind(R.id.address_more)
    ImageView addressMore;
    @Bind(R.id.address_text)
    TextView addressText;
    @Bind(R.id.addressPart)
    RelativeLayout addressPart;
    @Bind(R.id.payImg)
    ImageView payImg;
    @Bind(R.id.goods_listView)
    MyExpandableListView goodsListView;
    @Bind(R.id.message_passText)
    EditText messagePassText;
    @Bind(R.id.goods_num)
    TextView goodsNum;
    @Bind(R.id.goods_money)
    TextView goodsMoney;
    @Bind(R.id.totalMoney)
    TextView totalMoney;
    @Bind(R.id.submit_orders)
    TextView submitOrders;
    @Bind(R.id.firmOrder_titleBarView)
    TitleBarView titleBarView;
    @Bind(R.id.wx_select_img)
    ImageView wxSelectImg;
    @Bind(R.id.pay_wx)
    RelativeLayout payWx;
    @Bind(R.id.zfb_select_img)
    ImageView zfbSelectImg;
    @Bind(R.id.pay_zfb)
    RelativeLayout payZfb;
    @Bind(R.id.address_detailPart)
    LinearLayout addressDetailPart;
    @Bind(R.id.hasNoDefault)
    TextView hasNoDefault;
    private OrderAdapter adapter;
    //选中的订单页面
    private List<ShoppingCarDataBean.DatasBean> dataList = new ArrayList<>();
    private Double totalPrice;
    //支付方式 -- 默认微信
    private String pay_type = "0";
    private PayResultBean bean;
    NewAddressBean item;
    private String qr_Message;
    private int goodsSize = 0 ;
    private IWXAPI api;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    String result = (String) msg.obj;
                    Gson gson = new Gson();
                    bean = gson.fromJson(result, PayResultBean.class);
                    Log.e("支付宝私匙==", bean.getPrivateKeys());
                    aliPay(bean.getPrivateKeys(), result);
                    break;
                case SDK_PAY_FLAG:
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    //同步获取结果
                    String resultInfo = payResult.getResult();
                    Log.e("Pay", "payResult:" + payResult);
                    Log.e("Pay", "resultInfo:" + resultInfo);
                    String resultStatus = payResult.getResultStatus();

                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        Gson pay_Gson=new Gson();
                        AliPayResult bean=pay_Gson.fromJson(resultInfo,AliPayResult.class);
                        Log.e("or_no",bean.getAlipay_trade_app_pay_response().getOut_trade_no());
                        Log.e("no",bean.getAlipay_trade_app_pay_response().getTrade_no());
                        addPay(resultInfo, new Gson().toJson(dataList));

                        Toast.makeText(FirmOrderActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                    } else {
                        PayFail(bean.getOut_trade_no());
                        Toast.makeText(FirmOrderActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }

        }
    };


    private void addPay(String payJson, String goodslist) {
        /**
         * 建立请求表单，添加上传服务器的参数
         */
        HashMap<String, String> map = new HashMap<>();
        map.put("model", "interfaces");
        map.put("method", "payOkAndCreateQrCode");
        map.put("payJson", payJson);
        Log.e("payJson==", payJson);
        map.put("goodsList", goodslist);
        Log.e("size :", "商品数量：" + goodsSize);
        map.put("goodsCount",goodsSize+"" ) ;
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
                Log.e(TAG, "loadString 支付成功返回----- : " + string);
                Intent intent = new Intent(FirmOrderActivity.this, PayOkAndQRCodeActivity.class);
                Log.e("二维码信息", string);
                intent.putExtra("qrmessage", string);
                startActivity(intent);
                finish();
              /*  Bitmap bitmap=QRCodeUtil.createQRCodeBitmap(string,480);
                final MallDialog mallDialog = MallDialogFactory.getInstance().
                        createMallDialogWithStyle6(FirmOrderActivity.this,"二维码展示", bitmap, "取消", "确定");
                mallDialog.setOnLeftButtonClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mallDialog.dismiss();
                    }
                });
                mallDialog.show();*/
            }
        });
    }

    private void PayFail(String order_No) {
        /**
         * 建立请求表单，添加上传服务器的参数
         */
        HashMap<String, String> map = new HashMap<>();
        map.put("model", "interfaces");
        map.put("method", "payFaild");
        map.put("serialNumber", order_No);
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
                Log.e(TAG, "loadString 支付失败----- : " + string);

            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firm_order);
        ButterKnife.bind(this);
        initView();
    }

    @Override
    protected void initView() {
        //设置标题
        titleBarView.setTitle("确认订单");
        addressPart.setOnClickListener(this);
        //设置列表数据
        adapter = new OrderAdapter(this);
        //设置数据
        List<Goods> goodsList =
                (List<Goods>) getIntent().getSerializableExtra("selectList");

        totalPrice = getIntent().getDoubleExtra("total_price", 0f);

        Log.e(TAG, "initView: goodList :" + goodsList.size() + " pricee : " + totalPrice);

//        //选中的商品列表 -- 对数据进行封装
        for (int i = 0; i < goodsList.size(); i++) {
            Goods item = goodsList.get(i);
            goodsSize = goodsSize + item.getAmount() ;
            //当前商品的店铺名称
            String shopKey = item.getShopkey();
//            item.getLogo()
            if (null == dataList || dataList.isEmpty()) {
                ShoppingCarDataBean.DatasBean datasBean = new ShoppingCarDataBean.DatasBean();
                datasBean.setShopName(item.getShopName());
                datasBean.setShopKey(shopKey);
                if (null == datasBean.getGoods() || datasBean.getGoods().isEmpty()) {
                    List<Goods> goodListInShop = new ArrayList<>();
                    goodListInShop.add(item);
                    datasBean.setGoods(goodListInShop);
                }
                dataList.add(datasBean);
            } else {
                //如果不为空的话
                boolean hasExit = false;
                for (int j = 0; j < dataList.size(); j++) {
                    ShoppingCarDataBean.DatasBean temp = dataList.get(j);
                    if (shopKey.equals(temp.getShopKey())) {
                        hasExit = true;
                        temp.getGoods().add(item);
                        break;
                    }
                }
                if (!hasExit) {
                    ShoppingCarDataBean.DatasBean datasBean = new ShoppingCarDataBean.DatasBean();
                    datasBean.setShopName(item.getShopName());
                    datasBean.setShopKey(item.getShopkey());
                    if (null == datasBean.getGoods() || datasBean.getGoods().isEmpty()) {
                        List<Goods> goodListInShop = new ArrayList<>();
                        goodListInShop.add(item);
                        datasBean.setGoods(goodListInShop);
                    } else {
                        datasBean.getGoods().add(item);
                    }
                    dataList.add(datasBean);
                }
            }
        }
        goodsListView.setAdapter(adapter);
        adapter.setData(dataList);
//        // 默认全部展开
        int groupCount = goodsListView.getCount();
        for (int i = 0; i < groupCount; i++) {
            goodsListView.expandGroup(i);
        }
        //总价
        totalPrice = getIntent().getDoubleExtra("total_price", 0f);
        totalMoney.setText("合计：￥" + totalPrice);
        //提交订单
        submitOrders.setOnClickListener(this);
        // 微信支付点击事件
        payWx.setOnClickListener(this);
        // 支付宝
        payZfb.setOnClickListener(this);
        //选择地址点击事件
        hasNoDefault.setOnClickListener(this);
        //获取默认的邮寄地址
        loadDefaultAddress();
    }


    /**
     * 获取默认地址
     * <p>
     * 提交商品时使用该默认地址
     */
    private void loadDefaultAddress() {
        HashMap<String, String> map = new HashMap<>();
        map.put("model", "interfaces");
        map.put("method", "getDefaultAddress");
        String userName = SharedPreUtils.getPreStringInfo(this, "username");
        if (CommonUtils.isEmpty(userName)) {
            userName = "51";
        }
        map.put("customerKey", userName);
        MDataSource<NewAddressBean> dataSource = new MDataSource<>(map, this);
        dataSource.getDetail(new LoadListCallBack<NewAddressBean>() {
            @Override
            public void loadList(List<NewAddressBean> list) {

            }

            @Override
            public void systemError(Request request, String errorInfo, Exception e) {
                // 加载失败 让用户自己去选择地址
                hasNoDefault.setVisibility(View.VISIBLE);
                addressDetailPart.setVisibility(View.GONE);
            }

            @Override
            public void retLoad(String code) {

            }

            @Override
            public void loadString(String string) {
                Log.e(TAG, "loadString: " + Thread.currentThread().getName());
                String results = GsonUtils.getNoteJsonString(string, "results");
                if (!CommonUtils.isEmpty(results)) {
                    item = new Gson().fromJson(results, NewAddressBean.class);
                    Log.e(TAG, "loadString: " + item.getAddress());
                    if (null != item) {
                        // 设置默认地址
                        setDefaultAddress();
                    }
                }

            }
        });
    }


    private void createPreOrders() {
        // 将数据提交到后台 ----生成支付订单
        HashMap<String, String> map = new HashMap<>();
        map.put("model", "interfaces");
        map.put("method", "addOrder");
        //将要购买的商品生成JSON数据
        Gson gson = new Gson();
        //转换成json数据，再保存
        String strJson = gson.toJson(dataList);
        map.put("goodsList", strJson);
        map.put("goodsCount",goodsSize+"" ) ;
        String userName = SharedPreUtils.getPreStringInfo(this, "userName");
        if (!CommonUtils.isEmpty(userName)) {
            map.put("customerKey", userName);
        }
        //总价
        map.put("totalMoney", totalPrice + "");
        // 设置支付方式
        map.put("payType", pay_type);
        //
        if (CommonUtils.checkIsNull(this, buyName, "请设置收货人姓名")) {
            return;
        }
        map.put("buyName", buyName.getText().toString());
        if (CommonUtils.checkIsNull(this, phoneNum, "请设置收货人电话")) {
            return;
        }
        map.put("phone", phoneNum.getText().toString());
        if (CommonUtils.checkIsNull(this, addressText, "请设置收货人地址")) {
            return;
        }
        if (null == item) {
            ToastUtil.makeText(this, "请选择收货地址");
            return;
        }
        map.put("address", addressText.getText().toString());
        map.put("addressKey", item.getCustomerAddressKey());
        // 设置留言
        map.put("totalMoney", totalPrice + "");
        map.put("discountMoney", 0 + "");
        map.put("activeMoney", totalPrice + "");
        map.put("freightMoney", 15 + "");
        MDataSource<PayBean> dataSource = new MDataSource<>(this);
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
                //返回prepayId sign等
                Log.e("提交订单====", string.toString());
                Message msg = Message.obtain();
                msg.what = 1;
                msg.obj = string;
                handler.sendMessage(msg);
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
//                        string="{\n" +
//                                "\t\"nonce_str\": \"1 gQtJTKStQCsHqXz\",\n" +
//                                "\t\"package\": \" Sign = WXPay\",\n" +
//                                "\t\"appid \": \"wx3b788a4850538962\",\n" +
//                                "\t\"total_fee\": \"null\",\n" +
//                                "\t\"sign\": \"6AEE61893FD99BF75EFC47FEEA7E52B4\",\n" +
//                                "\t\"trade_type\": \"APP\",\n" +
//                                "\t\"return_msg \": \" OK\",\n" +
//                                "\t\"result_code \": \" SUCCESS\",\n" +
//                                "\t\" mch_id\": \"1524021131\",\n" +
//                                "\t\" return_code\": \"SUCCESS\",\n" +
//                                "\t\"prepay_id\": \" wx282036300717644f2bfa36410581673543\",\n" +
//                                "\t\"timestamp\": 1548678992 \"\n" +
//                                "}";
                        WXPayBean bean=new Gson().fromJson(string,WXPayBean.class);
                        PaymentHelper paymentHelper=new PaymentHelper();
////                        Log.e("微信支付", string);
                        paymentHelper.startWeChatPay(FirmOrderActivity.this,bean);
//                        WXPayUtils wxPayUtils=new WXPayUtils(bean);
//                        wxPayUtils.toWXPayNotSign(FirmOrderActivity.this,bean);
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
                        Toast.makeText(FirmOrderActivity.this, "字符为空", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Log.e("PAY_GET", "异常：" + e.getMessage());
                    Toast.makeText(FirmOrderActivity.this, "异常：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.addressPart:
            case R.id.hasNoDefault:
                Intent intent = new Intent(this, AddressListActivity.class);
                startActivityForResult(intent, AppConfig.REQUESTCODE_FIRMORDER_TO_ADDRESSLIST);
                break;
            case R.id.submit_orders:
                //去付款 发起支付请求
                if (pay_type.equals("0")) {
//                    submitOrdersToServer();
//                    api = WXAPIFactory.createWXAPI(this, "wx3b788a4850538962");
//                    api.registerApp("wx3b788a4850538962");
//                    if (!api.isWXAppInstalled()) {
//                        ToastUtil.makeText(FirmOrderActivity.this,"你没有安装微信");
//                        return;
//                    }


                    getWXPayCode();
                } else {

                    createPreOrders();
                }
                break;
            case R.id.pay_wx:
                pay_type = "0";
                wxSelectImg.setImageResource(R.mipmap.ic_address_checked);
                zfbSelectImg.setImageResource(R.mipmap.ic_address_unchecked);
                break;
            case R.id.pay_zfb:

                pay_type = "1";
                wxSelectImg.setImageResource(R.mipmap.ic_address_unchecked);
                zfbSelectImg.setImageResource(R.mipmap.ic_address_checked);

                break;
            default:
        }
    }

    private void aliPay(String RSA2_PRIVATE, String content) {
        boolean rsa2 = (RSA2_PRIVATE.length() > 0);
        Map<String, String> params = OrderInfoUtil2_0.buildOrderParamMap(APPID, rsa2, content);
        String orderParam = OrderInfoUtil2_0.buildOrderParam(params);

//        String privateKey = rsa2 ? RSA2_PRIVATE : RSA_PRIVATE;
        String sign = OrderInfoUtil2_0.getSign(params, RSA2_PRIVATE, true);
        final String orderInfo = orderParam + "&" + sign;

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(FirmOrderActivity.this);
                Map<String, String> result = alipay.payV2(orderInfo, true);
                Log.i("msp", result.toString());

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                handler.sendMessage(msg);
            }
        };

        Thread payThread = new Thread(payRunnable);
        payThread.start();

    }

//    private void submitOrdersToServer() {
//        //在服务端签名
//        WXPayUtils.WXPayBuilder builder = new WXPayUtils.WXPayBuilder();
//        builder.setAppId("123")
//                .setPartnerId("56465")
//                .setPrepayId("41515")
//                .setPackageValue("5153")
//                .setNonceStr("5645")
//                .setTimeStamp("56512")
//                .setSign("54615")
//                .build().toWXPayNotSign(this);
//    }

    /**
     * 付款失败
     */
    private void testPayFail() {
        Type type = new TypeToken<List<OrdersBean>>() {
        }.getType();
        List<OrdersBean> waitPayList = SharedPreUtils.getListData(this,
                "wait_to_fail", type);
        OrdersBean ordersBean = new OrdersBean();
        //默认显示第一项的内容
        ShoppingCarDataBean.DatasBean datasBean = dataList.get(0);
        if (dataList.size() > 1) {
            ordersBean.setShopName(datasBean.getShopName() + "...");
        } else {
            ordersBean.setShopName(datasBean.getShopName());
        }
        //当前时间-- 2012-12-12 12：12
        ordersBean.setCreateTime(DateUtil.getCurrentTime("yyyy-MM-dd HH:mm"));
        //商家图片
        ordersBean.setImage("http://pic.qingk.cn/image/z/2017/11/d3e00ce33ffb4a4e9519b2ff1a077c27_310.jpg");
        //商家状态
        ordersBean.setStatus("waitPay");
        // 购买商品的数量
        int buyNum = 0;
        //本地目前没有数据
        for (int i = 0; i < dataList.size(); i++) {
            ShoppingCarDataBean.DatasBean temp = dataList.get(i);
            //设置购买商品数量--
            for (int j = 0; j < temp.getGoods().size(); j++) {
                int goodNum = Integer.parseInt(temp.getGoods().get(j).getGoods_num());
                buyNum += goodNum;
            }
        }
        String content = datasBean.getGoods().get(0).getName() + "等" + buyNum + "件商品";
        ordersBean.setContent(content);
        ordersBean.setPrice(totalPrice + "");
        waitPayList.add(0, ordersBean);
        SharedPreUtils.putListData(this,
                "wait_to_fail", waitPayList);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == AppConfig.REQUESTCODE_FIRMORDER_TO_ADDRESSLIST) {
                //修改的地址
                item = (NewAddressBean) data.getSerializableExtra("address");
                if (null != item) {
                    setDefaultAddress();
                }
            }
        }
    }

    private void setDefaultAddress() {
        // 设置新的收货人以及地址
        buyName.setText("收货人：" + item.getName());
        phoneNum.setText(item.getPhone());
        addressText.setText(item.getAddress());
        hasNoDefault.setVisibility(View.GONE);
        addressDetailPart.setVisibility(View.VISIBLE);
    }

}
