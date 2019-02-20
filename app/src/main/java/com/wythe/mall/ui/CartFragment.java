package com.wythe.mall.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wythe.mall.R;
import com.wythe.mall.activity.LoginActivity;
import com.wythe.mall.adapter.shopcar.ShoppingCarAdapter;
import com.wythe.mall.base.BaseApplication;
import com.wythe.mall.beans.Goods;
import com.wythe.mall.beans.LoginBean;
import com.wythe.mall.beans.ShoppingCarDataBean;
import com.wythe.mall.http.datasource.MDataSource;
import com.wythe.mall.sqlite.LoadListCallBack;
import com.wythe.mall.utils.CommonUtils;
import com.wythe.mall.utils.RoundCornerDialog;
import com.wythe.mall.utils.SharedPreUtils;
import com.wythe.mall.utils.ToastUtil;
import com.wythe.mall.view.TitleBarView;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.fragment.app.Fragment;
import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Request;

/**
 * 购物车页面 ---
 */

public class CartFragment extends Fragment {

    private static final String TAG = "CartFragment";

    @Bind(R.id.elv_shopping_car)
    ExpandableListView elvShoppingCar;
    @Bind(R.id.iv_select_all)
    ImageView ivSelectAll;
    @Bind(R.id.ll_select_all)
    LinearLayout llSelectAll;
    @Bind(R.id.btn_order)
    Button btnOrder;
    @Bind(R.id.btn_delete)
    Button btnDelete;
    @Bind(R.id.tv_total_price)
    TextView tvTotalPrice;
    @Bind(R.id.rl_total_price)
    RelativeLayout rlTotalPrice;
    @Bind(R.id.rl)
    RelativeLayout rl;
    @Bind(R.id.iv_no_contant)
    ImageView ivNoContant;
    @Bind(R.id.rl_no_contant)
    RelativeLayout rlNoContant;
    @Bind(R.id.titleBarView)
    TitleBarView titleBarView;
    @Bind(R.id.tips)
    TextView tips;
    @Bind(R.id.login_comfirm_button)
    Button loginButton;
    private View view;

    private List<ShoppingCarDataBean.DatasBean> datas;
    private Context context;
    private ShoppingCarAdapter shoppingCarAdapter;

    public static CartFragment getInstance() {
        if (null == instance) {
            instance = new CartFragment();
        }
        return instance;
    }

    public static CartFragment instance;


    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                String ReturnMessage = (String) msg.obj;
                Log.e("获取的返回信息:", ReturnMessage);
                LoginBean bean = new Gson().fromJson(ReturnMessage, LoginBean.class);
                String code = bean.getCode();
                Log.e("code===", code + "");
                if (code.equals("200")) {
                } else {
                }

            }

        }
    };

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (null == view) {
            view = inflater.inflate(R.layout.fragment_cart, container, false);
            ButterKnife.bind(this, view);
        } else {
            if (null != view.getParent()) {
                ((ViewGroup) view.getParent()).removeAllViews();
            }

        }
        instance = this;
        return view;
    }

    //每次进入页面都需要刷新数据
    @Override
    public void onResume() {
        super.onResume();
        //判断用户是否登录了  没登陆去登录先
        if (CommonUtils.isLogin(context)) {
            rlNoContant.setVisibility(View.GONE);
            loginButton.setVisibility(View.GONE);
            rl.setVisibility(View.VISIBLE);
            elvShoppingCar.setVisibility(View.VISIBLE);
            tips.setText(getString(R.string.no_goods_incart));
            loginButton.setOnClickListener(null);
            initExpandableListView();
            initData();

        } else {
            Log.e(TAG, "onResume: 没有登录");
            // 没有登录 显示登录提示
            rlNoContant.setVisibility(View.VISIBLE);
            loginButton.setVisibility(View.VISIBLE);
            rl.setVisibility(View.GONE);
            elvShoppingCar.setVisibility(View.GONE);
            tips.setText("登录后方可查看购物车");
            loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //去登陆
                    startActivity(new Intent(context, LoginActivity.class));
                }
            });
        }
    }



    /**
     * 初始化数据
     */
    private void initData() {
        //使用Gson解析购物车数据，
        loadDataList();
    }

    private void loadDataList() {
        HashMap<String, String> map = new HashMap<>();
        map.put("model", "interfaces");
        map.put("method", "shoppingCartList");
        // customerKey
        //用户token
        String token = BaseApplication.getCustomer().getToken();
        if (CommonUtils.isEmpty(token)) {
            token = SharedPreUtils.getPreStringInfo(context, "token");
        }
        String username = SharedPreUtils.getPreStringInfo(getActivity(), "userName");
        Log.e("获取购物车产品列表===", username);
        map.put("userId", username);
        Type mType = new TypeToken<List<ShoppingCarDataBean.DatasBean>>() {
        }.getType();
        MDataSource<ShoppingCarDataBean.DatasBean> dataSource = new MDataSource<>("shopCartList",
                false, false, map, getActivity(), ShoppingCarDataBean.DatasBean.class, mType);
        if (dataSource.getDataList().isEmpty()) {
            dataSource.refreshData(listCallBack);
        } else {
            //使用缓存数据
            dataSource.refreshData(listCallBack);
        }
    }

    /**
     * 设置获取广告的回调
     */
    private LoadListCallBack<ShoppingCarDataBean.DatasBean> listCallBack = new LoadListCallBack<ShoppingCarDataBean.DatasBean>() {
        @Override
        public void loadList(List<ShoppingCarDataBean.DatasBean> list) {
            Log.e(TAG, "loadList:--- " + list.size());
            datas = list;
            initExpandableListViewData(list);
        }

        @Override
        public void systemError(Request request, String errorInfo, Exception e) {

        }

        @Override
        public void retLoad(String code) {

        }

        @Override
        public void loadString(String string) {

        }
    };
    private TextView tvTitlebarRight;

    /**
     * 初始化ExpandableListView
     * 创建数据适配器adapter，并进行初始化操作
     */
    private void initExpandableListView() {
        //设置标题和编辑按钮
        titleBarView.setTitle("购物车");
        titleBarView.showEditButton(true);
        //隐藏返回按钮
        titleBarView.showBackButton(false);
        //设置编辑按钮
        tvTitlebarRight = titleBarView.getTvTitlebarRight();
        tvTitlebarRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeButton();
            }
        });
        //设置购物车列表内容
        shoppingCarAdapter = new ShoppingCarAdapter(context, llSelectAll, ivSelectAll, btnOrder, btnDelete, rlTotalPrice, tvTotalPrice);
        elvShoppingCar.setAdapter(shoppingCarAdapter);
        //删除的回调
        shoppingCarAdapter.setOnDeleteListener(new ShoppingCarAdapter.OnDeleteListener() {
            @Override
            public void onDelete() {
                initDelete();
                /**
                 * 实际开发中，在此请求删除接口，删除成功后，
                 * 通过initExpandableListViewData（）方法刷新购物车数据。
                 * 注：通过bean类中的DatasBean的isSelect_shop属性，判断店铺是否被选中；
                 *                  GoodsBean的isSelect属性，判断商品是否被选中，
                 *                  （true为选中，false为未选中）
                 */
            }
        });

        //修改商品数量的回调
        shoppingCarAdapter.setOnChangeCountListener(new ShoppingCarAdapter.OnChangeCountListener() {
            @Override
            public void onChangeCount(String goods_id) {
                /**
                 * 实际开发中，在此请求修改商品数量的接口，商品数量修改成功后，
                 * 通过initExpandableListViewData（）方法刷新购物车数据。
                 */
            }
        });
    }

    /**
     * 初始化ExpandableListView的数据
     * 并在数据刷新时，页面保持当前位置
     *
     * @param datas 购物车的数据
     */
    private void initExpandableListViewData(List<ShoppingCarDataBean.DatasBean> datas) {
        if (datas != null && datas.size() > 0) {
            Log.e(TAG, "initExpandableListViewData: " + datas.get(0).getShopName());
            //刷新数据时，保持当前位置
            shoppingCarAdapter.setData(datas);
            //使所有组展开
            for (int i = 0; i < shoppingCarAdapter.getGroupCount(); i++) {
                elvShoppingCar.expandGroup(i);
            }

            //使组点击无效果
            elvShoppingCar.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

                @Override
                public boolean onGroupClick(ExpandableListView parent, View v,
                                            int groupPosition, long id) {
                    return true;
                }
            });
            tvTitlebarRight.setVisibility(View.VISIBLE);
            tvTitlebarRight.setText("编辑");
            rlNoContant.setVisibility(View.GONE);
            elvShoppingCar.setVisibility(View.VISIBLE);
            rl.setVisibility(View.VISIBLE);
            rlTotalPrice.setVisibility(View.VISIBLE);
            btnOrder.setVisibility(View.VISIBLE);
            btnDelete.setVisibility(View.GONE);
        } else {
            tvTitlebarRight.setVisibility(View.GONE);
            rlNoContant.setVisibility(View.VISIBLE);
            elvShoppingCar.setVisibility(View.GONE);
            rl.setVisibility(View.GONE);
        }
    }

    /**
     * 判断是否要弹出删除的dialog
     * 通过bean类中的DatasBean的isSelect_shop属性，判断店铺是否被选中；
     * GoodsBean的isSelect属性，判断商品是否被选中，
     */
    private void initDelete() {
        //判断是否有店铺或商品被选中
        //true为有，则需要刷新数据；反之，则不需要；
        boolean hasSelect = false;
        //创建临时的List，用于存储没有被选中的购物车数据
        List<ShoppingCarDataBean.DatasBean> datasTemp = new ArrayList<>();
        //需要删除的数据
        List<Goods> deleteList = new ArrayList<>();
        for (int i = 0; i < datas.size(); i++) {
            List<Goods> goods = datas.get(i).getGoods();
            boolean isSelect_shop = datas.get(i).getIsSelect_shop();
            if (isSelect_shop) {
                hasSelect = true;
                //跳出本次循环，继续下次循环。
                deleteList.addAll(goods);
                continue;
            } else {
                datasTemp.add(datas.get(i));
                datasTemp.get(datasTemp.size() - 1).setGoods(new ArrayList<Goods>());
            }

            for (int y = 0; y < goods.size(); y++) {
                Goods goodsBean = goods.get(y);
                boolean isSelect = goodsBean.isSelect();
                if (isSelect) {
                    hasSelect = true;
                    deleteList.add(goodsBean);
                } else {
                    datasTemp.get(datasTemp.size() - 1).getGoods().add(goodsBean);
                }
            }
        }

        if (hasSelect) {
            showDeleteDialog(datasTemp, deleteList);
        } else {
            ToastUtil.makeText(context, "请选择要删除的商品");
        }
    }

    /**
     * 展示删除的dialog（可以自定义弹窗，不用删除即可）
     *
     * @param datasTemp
     */
    private void showDeleteDialog(final List<ShoppingCarDataBean.DatasBean> datasTemp, final List<Goods> deleteList) {
        View view = View.inflate(context, R.layout.dialog_two_btn, null);
        final RoundCornerDialog roundCornerDialog = new RoundCornerDialog(context, 0, 0, view, R.style.RoundCornerDialog);
        roundCornerDialog.show();
        roundCornerDialog.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
        roundCornerDialog.setOnKeyListener(keylistener);//设置点击返回键Dialog不消失

        TextView tv_message =  view.findViewById(R.id.tv_message);
        TextView tv_logout_confirm =  view.findViewById(R.id.tv_logout_confirm);
        TextView tv_logout_cancel =  view.findViewById(R.id.tv_logout_cancel);
        tv_message.setText("确定要删除商品吗？");

        for (int i = 0; i < deleteList.size(); i++) {
            Goods item = deleteList.get(i);
            Log.e(TAG, "showDeleteDialog: " + item.getGoodsName() + "--" + item.getName() + " --" + item.getNormName());
        }

        //确定
        tv_logout_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                roundCornerDialog.dismiss();
                datas = datasTemp;
                initExpandableListViewData(datas);
                //
                deleteInServer(deleteList);
            }
        });
        //取消
        tv_logout_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                roundCornerDialog.dismiss();
            }
        });
    }

    DialogInterface.OnKeyListener keylistener = new DialogInterface.OnKeyListener() {
        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
                return true;
            } else {
                return false;
            }
        }
    };

    private void changeButton() {
        String edit = tvTitlebarRight.getText().toString().trim();
        if (edit.equals("编辑")) {
            tvTitlebarRight.setText("完成");
            rlTotalPrice.setVisibility(View.GONE);
            btnOrder.setVisibility(View.GONE);
            btnDelete.setVisibility(View.VISIBLE);
        } else {
            tvTitlebarRight.setText("编辑");
            rlTotalPrice.setVisibility(View.VISIBLE);
            btnOrder.setVisibility(View.VISIBLE);
            btnDelete.setVisibility(View.GONE);
        }
    }

    /**
     * 删除购物车的商品
     *
     * @param deleteList
     */
    private void deleteInServer(List<Goods> deleteList) {
        if (!deleteList.isEmpty()) {
            HashMap<String, String> map = new HashMap<>();
            map.put("model", "interfaces");
            map.put("method", "deleteShoppingCart");
            String deleteKyes = "";
            for (int i = 0; i < deleteList.size(); i++) {
                Goods item = deleteList.get(i);
                deleteKyes = deleteKyes +  item.getCartKey() + ",";
            }
            deleteKyes = deleteKyes.substring(0, deleteKyes.length() - 1);
            Log.e(TAG, "deleteInServer: " + deleteKyes);
            map.put("cartKey", deleteKyes);
            MDataSource<Goods> dataSource = new MDataSource<>(context);
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
                }
            });
        }

    }

}
