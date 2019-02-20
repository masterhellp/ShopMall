package com.wythe.mall.activity.address;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.smarttop.library.bean.City;
import com.smarttop.library.bean.County;
import com.smarttop.library.bean.Province;
import com.smarttop.library.bean.Street;
import com.smarttop.library.widget.AddressSelector;
import com.smarttop.library.widget.BottomDialog;
import com.smarttop.library.widget.OnAddressSelectedListener;
import com.wythe.mall.R;
import com.wythe.mall.activity.BaseActivity;
import com.wythe.mall.beans.NewAddressBean;
import com.wythe.mall.http.datasource.MDataSource;
import com.wythe.mall.sqlite.LoadListCallBack;
import com.wythe.mall.utils.CommonUtils;
import com.wythe.mall.utils.StatusBarUtil;
import com.wythe.mall.utils.ToastUtil;
import com.wythe.mall.view.TitleBarView;

import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Request;

/**
 * 查看收货地址  添加收货地址
 */
public class AddAddressActivity extends BaseActivity implements OnAddressSelectedListener
        , AddressSelector.OnDialogCloseListener {
    @Bind(R.id.address_titleView)
    TitleBarView titleBarView;
    @Bind(R.id.personMessage_nameView)
    EditText nameView;
    @Bind(R.id.personMessage_phoneView)
    EditText phoneView;
    @Bind(R.id.personMessage_addressView)
    TextView addressView;
    @Bind(R.id.choose_address)
    RelativeLayout chooseAddress;
    @Bind(R.id.detail_address)
    EditText detailAddress;
    @Bind(R.id.setting_default)
    RelativeLayout settingDefault;
    @Bind(R.id.add_submitButton)
    TextView addSubmitButton;
    @Bind(R.id.checkImg)
    ImageView checkImg;
    // 是否设置为默认地址
    private boolean isDefault = false;
    //选择地址弹出框
    private BottomDialog dialog;


    private String addressId = "";

    private String s;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);
        ButterKnife.bind(this);
        StatusBarUtil.setPaddingSmart(this, titleBarView);
        initView();
        initData();
    }

    private void initData() {
        Intent intent = getIntent();
        NewAddressBean bean = (NewAddressBean) intent.getSerializableExtra("newaddress");
        if (bean != null) {
            titleBarView.setTitle("修改地址");
            String name = bean.getName();
            String phone = bean.getPhone();
            String address = bean.getAddress();
            addressId = bean.getCustomerAddressKey();
            Log.e("传过来的姓名====", name);
            Log.e("传过来的姓名====", phone);
            Log.e("传过来的姓名====", address);
            nameView.setText(name);
            phoneView.setText(phone);
            phoneView.setSelection(phoneView.getText().length());
            //设置地址
            detailAddress.setText(address);
            if ("1".equals(bean.getIsDefault())) {
                checkImg.setImageResource(R.mipmap.ic_address_checked);
            } else {
                checkImg.setImageResource(R.mipmap.ic_address_unchecked);
            }
        } else {
            titleBarView.setTitle("新增地址");
        }
    }


    /**
     * 初始化布局文件
     */
    @Override
    public void initView() {
        //选择地址
        chooseAddress.setOnClickListener(this);
        //提交按钮
        addSubmitButton.setOnClickListener(this);
        //设置为默认地址
        settingDefault.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.choose_address:
                showDialog();
                break;
            case R.id.add_submitButton:
                //提交内容
                submitMessage();
                break;
            case R.id.setting_default:
                //默认地址
                isDefault = !isDefault;
                if (isDefault) {
                    checkImg.setImageResource(R.mipmap.ic_address_checked);
                } else {
                    checkImg.setImageResource(R.mipmap.ic_address_unchecked);

                }
                break;
            default:
        }
    }

    /**
     * 提交注册信息
     */
    private void submitMessage() {
        if (CommonUtils.checkIsNull(this, nameView, "请输入收货人姓名")) {
            return;
        }
        if (!CommonUtils.checkMobile(this, phoneView, "请输入合法的电话")) {

            return;
        }
        if (CommonUtils.checkIsNull(this, addressView, "请选择收货人地址")) {

            return;
        }
        if (CommonUtils.checkIsNull(this, detailAddress, "请填写详细地址")) {

            return;
        }
        //提交内容
        HashMap<String, String> map = new HashMap<>();
        map.put("model", "interfaces");
        if (!CommonUtils.isEmpty(addressId)) {
            map.put("method", "updateCustomerAddress");
            map.put("customerAddressKey", addressId);
        } else {
            map.put("method", "addCustomerAddress");
        }
        //
        map.put("customerKey", "51");
        map.put("name", nameView.getText().toString());
        map.put("phone", phoneView.getText().toString());
        map.put("address", addressView.getText().toString() + detailAddress.getText());
        map.put("isDefault", String.valueOf(isDefault));
        map.put("provinceId", "0");
        map.put("cityId", "0");
        map.put("areaId", "0");
        //接口提交  返回上一个页面
        MDataSource mDataSource = new MDataSource(AddAddressActivity.this);
        mDataSource.postData(map, new LoadListCallBack() {
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
                if (!CommonUtils.isEmpty(string) && string.contains("成功")) {
                    if (!CommonUtils.isEmpty(addressId)) {
                        ToastUtil.makeText(AddAddressActivity.this, "修改地址成功");
                    } else {
                        ToastUtil.makeText(AddAddressActivity.this, "添加地址成功");
                    }
                    Intent intent = getIntent();
                    if (null != intent) {
                        Log.e(TAG, "loadString: =---");
                        setResult(RESULT_OK, intent);
                    } else{
                        Log.e(TAG, "loadString: 12312312");
                    }
                    finish();
                } else {
                    if (!CommonUtils.isEmpty(addressId)) {
                        ToastUtil.makeText(AddAddressActivity.this, "修改地址失败");
                    } else {
                        ToastUtil.makeText(AddAddressActivity.this, "添加地址失败");
                    }
                }
            }
        });
    }


    private void showDialog() {
        if (null == dialog) {
            dialog = new BottomDialog(this);
            dialog.setOnAddressSelectedListener(this);
            dialog.setDialogDismisListener(this);
        }

        dialog.show();
    }

    @Override
    public void onAddressSelected(Province province, City city, County county, Street street) {
        s = (province == null ? "" : province.name) + (city == null ? "" : city.name) + (county == null ? "" : county.name) +
                (street == null ? "" : street.name);
        Log.e(TAG, "onAddressSelected:  s : " + s);
        addressView.setText(s);
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    @Override
    public void dialogclose() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }


}
