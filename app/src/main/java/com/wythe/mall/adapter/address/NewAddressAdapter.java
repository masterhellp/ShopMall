package com.wythe.mall.adapter.address;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wythe.mall.R;
import com.wythe.mall.activity.address.AddAddressActivity;
import com.wythe.mall.beans.NewAddressBean;

import java.util.List;

public class NewAddressAdapter extends BaseAdapter {
    private Context context;
    private List<NewAddressBean> data;

    public NewAddressAdapter(Context context,List<NewAddressBean> data){
        this.context=context;
        this.data=data;
    }


    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View convertView, ViewGroup viewGroup) {
        final ViewHolder viewHolder;
        if (null == convertView) {
            convertView = LayoutInflater.from(context).inflate(R.layout.address_items_layout, null);
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
        viewHolder.nameView.setText(data.get(i).getName());
        viewHolder.phoneView.setText(data.get(i).getPhone());
        viewHolder.addressView.setText(data.get(i).getAddress());
        if (!data.get(i).getIsDefault().equals("0")) {
            viewHolder.defaultImg.setImageResource(R.mipmap.ic_address_checked);
//            defaultAddress = item;
        } else {
            viewHolder.defaultImg.setImageResource(R.mipmap.ic_address_unchecked);
        }
        //绑定点击事件 删除按钮

        //删除
        viewHolder.delView.setOnClickListener(new View.OnClickListener() {

            int num=i;
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

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
                                            Log.e("删除", "删除");
                                            data.remove(num);
                                            notifyDataSetChanged();
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
            int edit_num=i;
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(context, AddAddressActivity.class);
                intent.putExtra("newaddress", data.get(edit_num));
                context.startActivity(intent);
            }
        });

//        viewHolder.delView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                new AlertDialog.Builder(context)
//                        .setTitle("友情提示:")
//                        .setMessage("您确定要删除该地址么？")
//                        .setPositiveButton("狠心删除",
//                                new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(
//                                            DialogInterface dialog,
//                                            int which) {
//                                        //删除该地址
////                                        viewList.remove(item);
////                                        viewHolder.delView.getTag()
//                                        data.get(which).getResults().getList().remove(which);
//                                        data.notifyAll();
//                                    }
//
//                                })
//                        .setNegativeButton("我在想想",
//                                new DialogInterface.OnClickListener() {
//
//                                    @Override
//                                    public void onClick(
//                                            DialogInterface dialog,
//                                            int which) {
//                                        dialog.dismiss();
//                                    }
//                                }).show();
//
//            }
//        });
//        // 编辑按钮
//        viewHolder.editView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //删除该地址
//                Intent intent = new Intent(context, AddAddressActivity.class);
//                intent.putExtra("currentAddress", data.get(i).getResults().getList().get(i));
//                context.startActivity(intent);
//            }
//        });
//        //设置为默认地址
//        viewHolder.defaulrPart.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //如果当前就是默认地址  不做处理
//                if (defaultAddress == item) {
//                    return;
//                } else {
//                    //当前地址为默认
//                    defaultAddress.setDefault(false);
//                    item.setDefault(true);
//                    defaultAddress = item;
//                    notifyDataSetChanged();
//                }
//            }
//        });



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
}
