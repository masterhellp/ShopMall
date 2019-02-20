package com.wythe.mall.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.wythe.mall.R;
import com.wythe.mall.adapter.IndexGridAdapter;
import com.wythe.mall.beans.IndexGridBean;
import com.wythe.mall.utils.CommonUtils;
import com.wythe.mall.utils.ToastUtil;
import com.youth.banner.Banner;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.Bind;
import butterknife.ButterKnife;

public class IndexActivity extends BaseActivity {

    private static final String TAG = "IndexActivity";

    @Bind(R.id.index_recyclerView)
    RecyclerView indexRecyclerView;
    @Bind(R.id.search_logoImg)
    ImageView searchLogoImg;
    @Bind(R.id.search_icon)
    ImageView searchIcon;
    @Bind(R.id.search_inputPart)
    RelativeLayout searchInputPart;
    @Bind(R.id.search_box_layout)
    RelativeLayout searchBoxLayout;
    @Bind(R.id.index_banner)
    Banner banner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
        ButterKnife.bind(this);
        //设置banner
        setBanner();
        initView();
    }

    /**
     * 设置轮播图banner
     */
    private void setBanner() {
        //设置样式
        banner.setBannerStyle(Banner.CIRCLE_INDICATOR);
        banner.setIndicatorGravity(Banner.CENTER);
        banner.setDelayTime(5000);//设置轮播间隔时间
        banner.setOnBannerClickListener(new Banner.OnBannerClickListener() {//设置点击事件
            @Override
            public void OnBannerClick(View view, int position) {
                Log.e(TAG, "OnBannerClick  position:  " + position);
                //跳转到详情页 banner是从1开始技术的
//                AdBean item = adList.get(position - 1);
//                if (!CommonUtils.isEmpty(item.getGoodsKey())) {
//                    changeToDetaiLActivity(item.getGoodsKey());
//                }
            }
        });
        // R.mipmap.b, R.mipmap.c, R.mipmap.d

        String urlArray[] = new String[]{"https://m.360buyimg.com/babel/jfs/t1/30100/19/1243/148659/5c496b88E310b4ed7/c44f9a9f64145fbc.jpg",
                "https://m.360buyimg.com/babel/jfs/t1/15572/35/6197/108005/5c4adcdfEfdfe1c79/4212d9f8b8974767.jpg",
                "https://m.360buyimg.com/babel/jfs/t1/7641/21/13652/98317/5c47ccb6E3a0dfae2/2f0ad3859e600dae.jpg"};
        // 设置大小
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.width = CommonUtils.getScreenWidth(this);
        lp.height = lp.width / 2;
        banner.setLayoutParams(lp);
        banner.setImages(urlArray);

    }

    @Override
    protected void initView() {
        //加载数据内容
        GridLayoutManager manager = new GridLayoutManager(this, 5);
        indexRecyclerView.setLayoutManager(manager);
        IndexGridAdapter adapter = new IndexGridAdapter(R.layout.index_grid_items, getDataList());
        indexRecyclerView.setAdapter(adapter);
        //设置点击事件
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Log.e(TAG, "onItemClick - position    : " + position);
                if (position == 0) {
                    //跳转到mainActivity
                    startActivity(new Intent(IndexActivity.this, MainActivity.class));
                } else {
                    ToastUtil.makeText(IndexActivity.this, "暂未开放，敬请期待");
                }
            }
        });
    }

    private List<IndexGridBean> getDataList() {
        List<IndexGridBean> dataList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            IndexGridBean item = new IndexGridBean();
            switch (i) {
                case 0:
                    item.setImgUrl(R.mipmap.shucai);
                    item.setTitle("蔬菜");
                    break;
                case 1:

                    item.setImgUrl(R.mipmap.jiancai);
                    item.setTitle("建材");
                    break;
                case 2:

                    item.setImgUrl(R.mipmap.friend);
                    item.setTitle("交友");
                    break;
                case 3:
                    item.setImgUrl(R.mipmap.timg);
                    item.setTitle("待定");
                    break;
                case 4:
                    item.setImgUrl(R.mipmap.timg);
                    item.setTitle("待定");
                    break;
                case 5:
                    item.setImgUrl(R.mipmap.timg);
                    item.setTitle("暂未开放");
                    break;
                case 6:
                    item.setImgUrl(R.mipmap.timg);
                    item.setTitle("暂未开放");
                    break;

                case 7:
                    item.setImgUrl(R.mipmap.timg);
                    item.setTitle("暂未开放");
                    break;
                case 8:
                    item.setImgUrl(R.mipmap.timg);
                    item.setTitle("暂未开放");
                    break;
                case 9:
                    item.setImgUrl(R.mipmap.timg);
                    item.setTitle("暂未开放");
                    break;
                default:
            }
            dataList.add(item);
        }
        return dataList;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            new AlertDialog.Builder(this)
                    .setTitle("提示")
                    .setMessage(
                            "确定退出" + getResources().getText(R.string.app_name))
                    .setPositiveButton("确认退出",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    dialog.dismiss();
                                    Intent intent = new Intent(
                                            Intent.ACTION_MAIN);
                                    intent.addCategory(Intent.CATEGORY_HOME);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                    finish();
                                }
                            })
                    .setNegativeButton("继续使用",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    dialog.dismiss();
                                }
                            }).show();
        }
        return super.onKeyDown(keyCode, event);
    }
}
