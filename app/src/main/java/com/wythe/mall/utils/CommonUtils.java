package com.wythe.mall.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.wythe.mall.activity.LoginActivity;

import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommonUtils {

    private static final String TAG = "CommonUtils";

    /**
     * 判断字符串是否为空
     *
     * @param inputString
     * @return
     */
    public static Boolean isEmpty(String inputString) {
        if ("null".equals(inputString) || inputString == null || (inputString.trim().length() == 0)) {
            return true;
        }
        return false;
    }

    // 获取屏幕宽度  单位是 px
    public static int getScreenWidth(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.widthPixels;
    }

    // 获取屏幕高度
    public static int getScreenHeight(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.heightPixels;
    }

    /**
     * dp转成PX
     *
     * @param context
     * @param dpValue
     * @return
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * px 转DP
     *
     * @param context
     * @param pxValue
     * @return
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }


    /**
     * 判断一个input控件的值 是否为空
     *
     * @param con
     * @param view   可输入控件
     * @param prompt 为空时的提示内容
     * @return false:不为空  true:为空
     */
    public static boolean checkIsNull(Context con, TextView view, String prompt) {
        CharSequence text = view.getText();
        if (null != text && text.toString().trim().length() > 0) {
            return false;
        } else {
            if (!isEmpty(prompt)) {
                Toast.makeText(con, prompt, Toast.LENGTH_SHORT).show();
            }
            return true;
        }
    }


    // 设置webview的基本属性
    public static void setBasicProperties(Context context, WebView webView) {
        try {
            // 设置webView属性
            webView.setLongClickable(true);
            // 如果系统版本大于4.4 开始硬件加速
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                //硬件加速
                webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
            } else {
                //关闭硬件加速
                webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            }
            webView.getSettings().setDatabaseEnabled(true);
            String dir = context.getApplicationContext().getDir("database", Context.MODE_PRIVATE).getPath();
            //启用地理定位
            webView.getSettings().setGeolocationEnabled(true);
            //设置定位的数据库路径
            webView.getSettings().setGeolocationDatabasePath(dir);
            webView.getSettings().setAppCacheEnabled(true);
            webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
            webView.getSettings().setAppCacheMaxSize(8 * 1024 * 1024);
            webView.getSettings().setAllowFileAccess(true);
            webView.getSettings().setDefaultTextEncodingName("UTF-8");
            webView.getSettings().setJavaScriptEnabled(true);
            webView.getSettings().setBlockNetworkImage(true);
            //添加自适应屏幕
            webView.getSettings().setUseWideViewPort(true);
            webView.getSettings().setLoadWithOverviewMode(true);

            webView.getSettings().setGeolocationDatabasePath(context.getFilesDir().getPath());
            //启用地理定位
            webView.getSettings().setGeolocationEnabled(true);
            //最重要的方法，一定要设置，这就是出不来的主要原因
            webView.getSettings().setDomStorageEnabled(true);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
            }
            webView.setFocusable(true);
        } catch (Exception e) {
        }
    }

    /**
     * 判断 用户是否安装QQ客户端
     */
    public static boolean isQQClientAvailable(Context context) {
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equalsIgnoreCase("com.tencent.qqlite") || pn.equalsIgnoreCase("com.tencent.mobileqq")) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 判断 Uri是否有效
     */
    public static boolean isValidIntent(Context context, Intent intent) {
        PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> activities = packageManager.queryIntentActivities(intent, 0);
        return !activities.isEmpty();
    }

    /**
     * 打开QQ客服
     *
     * @param context
     */
    public static void openKeFu(Context context) {
        // 跳转之前，可以先判断手机是否安装QQ
        if (isQQClientAvailable(context)) {
            // 跳转到客服的QQ
            String url = "mqqwpa://im/chat?chat_type=wpa&uin=151802218&version=1";
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            // 跳转前先判断Uri是否存在，如果打开一个不存在的Uri，App可能会崩溃
            if (isValidIntent(context, intent)) {
                context.startActivity(intent);
            }
        } else {
            Toast.makeText(context, "请安装最新版QQ客户端", Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * 判断用户是否登录
     * @param context
     * @return true:已登录  false:未登录
     */
    public static boolean isLogin(Context context) {

        String login_info = SharedPreUtils.getPreStringInfo(context, "userName");
        if (!isEmpty(login_info)) {
            return true;
        } else {
            return false;
        }

    }

    /**
     * 跳转到登录页面
     *
     * @param context
     * @return
     */
    public static Boolean skipLoginPage(Context context) {
        if (!isLogin(context)) {
            //
            context.startActivity(new Intent(context, LoginActivity.class));
            return true;
        }
        return false;
    }

    /**
     * @param con
     * @param view
     * @param prompt
     * @return
     *  手机号校验只校验11位数字
     */

    public static boolean checkMobile(Context con, EditText view, String prompt) {

        String regExp = "^[1][0-9]{10}$";

        Pattern p = Pattern.compile(regExp);

        CharSequence text = view.getText();
        if (null != text && text.toString().trim().length() == 11) {
            Matcher m = p.matcher(text.toString());
            if (!m.matches()) {
                if (!isEmpty(prompt)) {
                    Toast.makeText(con, prompt, Toast.LENGTH_SHORT).show();
                }
            }
            return m.matches();
        } else {
            if (!isEmpty(prompt)) {
                Toast.makeText(con, prompt, Toast.LENGTH_SHORT).show();
            }
            return false;
        }
    }

    /**
     * 判断网络条件是否正常
     * @param mContext
     * @return
     */
    public static boolean isNetOk(Context mContext) {
        if (null != mContext) {
            ConnectivityManager connectivityManager = (ConnectivityManager) mContext
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            @SuppressLint("MissingPermission") NetworkInfo activeNetInfo = connectivityManager
                    .getActiveNetworkInfo();
            return activeNetInfo != null;
        }

        return true;
    }

    /**
     * deviceID的组成为：渠道标志+识别符来源标志+hash后的终端识别符
     * <p/>
     * 渠道标志为：
     * 1，andriod（a）
     * <p/>
     * 识别符来源标志：
     * 1， wifi mac地址（wifi）；
     * 2， IMEI（imei）；
     * 3， 序列号（sn）；
     * 4， id：随机码。若前面的都取不到时，则随机生成一个随机码，需要缓存。
     *
     * @param context
     * @return
     */
    public static String getDeviceId(Context context) {

        StringBuilder deviceId = new StringBuilder();
        // 渠道标志
        try {
            //IMEI（imei）
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            @SuppressLint("MissingPermission") String imei = tm.getDeviceId();
            if (!isEmpty(imei)) {
                deviceId.append(imei);
                PrintLog.printError("imei : ", imei);

                String radom32ID = MD5.GetMD5Code(deviceId.toString());
                PrintLog.printError("radom32ID : ", radom32ID);
                return radom32ID;
            }

            //序列号（sn）
            @SuppressLint("MissingPermission") String sn = tm.getSimSerialNumber();
            if (!isEmpty(sn)) {
                StringBuilder snID = new StringBuilder();
                snID.append(sn);
                PrintLog.printError("sn : ", snID.toString());
                String radom32ID = MD5.GetMD5Code(snID.toString());
                return radom32ID;
            }


            //wifi mac地址
            WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo info = wifi.getConnectionInfo();
            String wifiMac = info.getMacAddress();
            if (!isEmpty(wifiMac)) {
                deviceId.append(wifiMac);
                PrintLog.printError("wifi : ", deviceId.toString());
                return MD5.GetMD5Code(deviceId.toString());
            }


            //如果上面都没有， 则生成一个id：随机码
            String uuid = getUUID(context);
            if (!isEmpty(uuid)) {
                deviceId.append(uuid);
                PrintLog.printError("getDeviceId : ", deviceId.toString());
                return MD5.GetMD5Code(deviceId.toString());
            }
        } catch (Exception e) {

            e.printStackTrace();
            PrintLog.printError(TAG, e.getMessage());
        }

        String uuid = getUUID(context);
        if (!isEmpty(uuid)) {
            deviceId.append(uuid);
            PrintLog.printError("getDeviceId : ", deviceId.toString());
            return MD5.GetMD5Code(deviceId.toString());
        }
        return deviceId.toString();
    }

    /**
     * 得到全局唯一UUID
     */
    public static String getUUID(Context context) {

        String uuid = SharedPreUtils.getPreStringInfo(context, "uuid");

        if (isEmpty(uuid)) {
            uuid = UUID.randomUUID().toString();
            SharedPreUtils.setStringToPre(context, "uuid", uuid);
        }

        PrintLog.printError("uuid", "getUUID : " + uuid);
        return uuid;
    }


    // 去掉字符串中的引号
    public static String removeQuotes(String msg) {
        if (!CommonUtils.isEmpty(msg)) {
            msg = msg.replaceAll("“", "");
            msg = msg.replaceAll("”", "");
            msg = msg.replaceAll("\"", "");
        }
        return msg;
    }

    // 禁止连续点击
    /**
     * 放置按钮连续点击的方法
     */
    private static long lastClickTime = 0;

    public synchronized static boolean isFastClick() {
        long time = System.currentTimeMillis();
        if (time - lastClickTime < 2000) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    /**
     * 多少秒之内不能连续点击
     *
     * @param intervalTime 间隔时间
     * @return
     */
    public synchronized static boolean isFastClick(long intervalTime) {
        long time = System.currentTimeMillis();
        if (time - lastClickTime < intervalTime) {
            return true;
        }
        lastClickTime = time;
        return false;
    }


}
