package com.wythe.mall.utils;

import android.os.Environment;

public class AppConfig {
//    public static final String REQUEST_URL = "http://192.168.3.211:7000/";
    public static final String REQUEST_URL = "http://203.104.36.171:7000/market/";
//    public static final String REQUEST_URL = "http://y2s3384021.imwork.net/";
    // 默认存放图片的路径
    public static final String DEFAULT_SAVE_IMAGE_PATH = Environment
            .getExternalStorageDirectory() + "/mall/imgs/";

    // 首页adapter数据类型
    public static final int INDEX_HOT_GOODS = 0;

    public static final int INDEX_RECOM_GOODS = 1;


    //页面跳转request_cord 添加地址
    public static final int REQUESTCODE_ADDRESS = 1001;

    public static final int REQUESTCODE_FIRMORDER_TO_ADDRESSLIST = 1002;


}
