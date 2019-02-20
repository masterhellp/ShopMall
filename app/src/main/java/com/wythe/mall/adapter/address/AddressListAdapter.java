package com.wythe.mall.adapter.address;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wythe.mall.R;
import com.wythe.mall.activity.address.AddAddressActivity;
import com.wythe.mall.adapter.IPullToRefreshListAdapter;
import com.wythe.mall.beans.NewAddressBean;
import com.wythe.mall.http.datasource.MDataSource;
import com.wythe.mall.sqlite.LoadListCallBack;
import com.wythe.mall.utils.CommonUtils;
import com.wythe.mall.utils.ToastUtil;

import java.util.HashMap;
import java.util.List;

import okhttp3.Request;

public class AddressListAdapter extends IPullToRefreshListAdapter<NewAddressBean> {


    private static final String TAG = "AddressListAdapter";

    private NewAddressBean defaultAddress;


    public AddressListAdapter(Context context) {
        super(context);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (null == convertView) {
            convertView = inflater.inflate(R.layout.address_items_layout, null);
            viewHolder = new ViewHolder();
            viewHolder.nameView = convertView.findViewById(R.id.address_name);
            viewHolder.phoneView = convertView.findViewById(R.id.address_num);
            viewHolder.addressView = convertView.findViewById(R.id.address_text);
            viewHolder.defaultImg = convertView.findViewById(R.id.address_defaultChecked);
            viewHolder.delView = convertView.findViewById(R.id.address_delImg);
            viewHolder.editView = convertView.findViewById(R.id.address_editImg);
            viewHolder.defaulrPart = convertView.findViewById(R.id.default_addressPart);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final NewAddressBean item = getItem(position);
        viewHolder.nameView.setText(item.getName());
        viewHolder.phoneView.setText(item.getPhone());
        viewHolder.addressView.setText(item.getAddress());
        if ("1".equals(item.getIsDefault())) {
            viewHolder.defaultImg.setImageResource(R.mipmap.ic_address_checked);
            defaultAddress = item;
        } else {
            viewHolder.defaultImg.setImageResource(R.mipmap.ic_address_unchecked);
        }

        //删除
        viewHolder.delView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(context)
                        .setTitle("友情提示:")
                        .setMessage("您确定要删除该地址么？")
                        .setPositiveButton("狠心删除",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(
                                            DialogInterface dialog,
                                            int which) {
                                        //删除该地址
                                        viewList.remove(item);
                                        notifyDataSetChanged();
                                        deleteAddress(item.getCustomerAddressKey());
                                    }

                                })
                        .setNegativeButton("我在想想",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(
                                            DialogInterface dialog,
                                            int which) {
                                        dialog.dismiss();
                                    }
                                }).show();
            }
        });
        //修改地址
        viewHolder.editView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(context, AddAddressActivity.class);
                intent.putExtra("newaddress", item);
                context.startActivity(intent);
            }
        });
        //设置为默认地址
        viewHolder.defaulrPart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //如果当前就是默认地址  不做处理
                if (item == defaultAddress) {
                    return;
                } else {
                    //当前地址为默认
                    item.setIsDefault("true");
                    defaultAddress = item;
                    notifyDataSetChanged();
                }
            }
        });
        return convertView;
    }

    static class ViewHolder {
        TextView nameView;
        TextView phoneView;
        TextView addressView;
        ImageView defaultImg;
        ImageView delView;
        ImageView editView;
        LinearLayout defaulrPart;
    }

    /**
     * 删除地址
     */
    private void deleteAddress(String addressId) {
        HashMap<String, String> map = new HashMap<>();
        map.put("model", "interfaces");
        map.put("method", "deleteCustomerAddress");
        map.put("customerAddressKey", addressId);
        MDataSource<NewAddressBean> dataSource = new MDataSource<>(context);
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
                Log.e(TAG, "loadString: " + string);
                if (!CommonUtils.isEmpty(string) && string.contains("成功")) {
                    ToastUtil.makeText(context, "删除成功！");
                }
            }
        });

    }

}
