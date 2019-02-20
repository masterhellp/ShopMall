package com.wythe.mall.base;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import com.jakewharton.picasso.OkHttp3Downloader;
import com.mob.MobSDK;
import com.squareup.picasso.Picasso;
import com.wythe.mall.utils.AppConfig;
import com.wythe.mall.utils.CommonUtils;
import com.wythe.mall.utils.SharedPreUtils;
import com.wythe.mall.utils.StringUtils;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import androidx.multidex.MultiDex;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;


/**
 * Created by ningwang on 2017/1/16.
 */
/**
 * 全局应用程序类：用于保存和调用全局应用配置及访问网络数据
 */
public class AppContext extends BaseApplication {

    public static final String KEY_DEX2_SHA1 = "dex2-SHA1-Digest";

    private static AppContext instance;




    private boolean hasPatch = false;
    /**
     * 屏幕宽高度
     */
    private int ScreenWidth;
    private int ScreenHeight;

    private String TAG = "AppContext";



    /**
     * 获得当前app运行的AppContext
     *
     * @return
     */
    public static AppContext getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        MobSDK.init(this, "2993515bd85e8", "53c93b52f514593e1741c859057f32e5");
        // 图片处理库Picasso初始化
        Picasso picasso = new Picasso.Builder(this)
                .downloader(new OkHttp3Downloader(new File(AppConfig.DEFAULT_SAVE_IMAGE_PATH)))
                .build();
        Picasso.setSingletonInstance(picasso);

    }


    /**
     * 获取App安装包信息
     *
     * @return
     */
    public PackageInfo getPackageInfo() {
        PackageInfo info = null;
        try {
            info = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace(System.err);
        }
        if (info == null)
            info = new PackageInfo();
        return info;
    }



    public int getScreenWidth() {
        return ScreenWidth;
    }

    public void setScreenWidth(int screenWidth) {
        ScreenWidth = screenWidth;
    }

    public int getScreenHeight() {
        return ScreenHeight;
    }

    public void setScreenHeight(int screenHeight) {
        ScreenHeight = screenHeight;
    }

    private String customerHeadImg = " ";

    public String getCustomerHeadImg() {
        return customerHeadImg;
    }

    public void setCustomerHeadImg(String customerHeadImg) {
        this.customerHeadImg = customerHeadImg;
    }

    private String customerId = "-1";

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }


    private String token = "";

    public String getToken() {
        return token;
    }



    public static String createGUID() {
        UUID uuid = UUID.randomUUID();
        String str = uuid.toString();
        str = str.replace("-", "");
        return str;
    }



    /**
     * 分包处理
     */
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

        // 华为4.4手机dexhu悠哈
        if (!quickStart() && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {//>=5.0的系统默认对dex进行oat优化
            if (needWait(base)){
                waitForDexopt(base);
            }
            MultiDex.install(this);
        }

    }

    public boolean quickStart() {
        if (StringUtils.contains(getCurProcessName(this), ":mini")) {
            Log.e("loadDex", ":mini start!");
            return true;
        }
        return false;
    }

    //neead wait for dexopt ?
    private boolean needWait(Context context) {
        String flag = get2thDexSHA1(context);
        SharedPreferences sp = context.getSharedPreferences(
                getPackageInfo().versionName, MODE_MULTI_PROCESS);
        String saveValue = sp.getString(KEY_DEX2_SHA1, "");
        return !StringUtils.equals(flag, saveValue);
    }

    /**
     * Get classes.dex file signature
     *
     * @param context
     * @return
     */
    private String get2thDexSHA1(Context context) {
        ApplicationInfo ai = context.getApplicationInfo();
        String source = ai.sourceDir;
        try {
            JarFile jar = new JarFile(source);
            Manifest mf = jar.getManifest();
            Map<String, Attributes> map = mf.getEntries();
            Attributes a = map.get("classes2.dex");
            return a.getValue("SHA1-Digest");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // optDex finish
    public void installFinish(Context context) {
        SharedPreferences sp = context.getSharedPreferences(
                getPackageInfo().versionName, MODE_MULTI_PROCESS);
        sp.edit().putString(KEY_DEX2_SHA1, get2thDexSHA1(context)).commit();
    }


    public static String getCurProcessName(Context context) {
        try {
            int pid = android.os.Process.myPid();
            ActivityManager mActivityManager = (ActivityManager) context
                    .getSystemService(Context.ACTIVITY_SERVICE);
            for (ActivityManager.RunningAppProcessInfo appProcess : mActivityManager
                    .getRunningAppProcesses()) {
                if (appProcess.pid == pid) {
                    return appProcess.processName;
                }
            }
        } catch (Exception e) {
            // ignore
        }
        return null;
    }

    public void waitForDexopt(Context base) {
        Intent intent = new Intent();
        Class<?> clazz = null;
        try {
            clazz = Class.forName("com.wythe.mall.base.LoadResActivity");
            intent.setClass(base, clazz);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            base.startActivity(intent);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        long startWait = System.currentTimeMillis();
        long waitTime = 10 * 1000;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB_MR1) {
            waitTime = 20 * 1000;//实测发现某些场景下有些2.3版本有可能10s都不能完成optdex
        }
        while (needWait(base)) {
            try {
                long nowWait = System.currentTimeMillis() - startWait;
                if (nowWait >= waitTime) {
                    return;
                }
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 配置 APP 保存图片/语音/文件/log等数据的目录
     * 这里示例用SD卡的应用扩展存储目录
     */
    static String getAppCacheDir(Context context) {
        String storageRootPath = null;
        try {
            // SD卡应用扩展存储区(APP卸载后，该目录下被清除，用户也可以在设置界面中手动清除)，
            // 请根据APP对数据缓存的重要性及生命周期来决定是否采用此缓存目录.
            // 该存储区在API 19以上不需要写权限，即可配置
            // <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" android:maxSdkVersion="18"/>
            if (context.getExternalCacheDir() != null) {
                storageRootPath = context.getExternalCacheDir().getCanonicalPath();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (TextUtils.isEmpty(storageRootPath)) {
            // SD卡应用公共存储区(APP卸载后，该目录不会被清除，
            // 下载安装APP后，缓存数据依然可以被加载。SDK默认使用此目录)，该存储区域需要写权限!
            //storageRootPath = Environment.getExternalStorageDirectory() + "/" + DemoCache.getContext().getPackageName();
        }

        return storageRootPath;
    }

//    public static String uid() {
//        //首先判断有没有uid 保存在SharedPreUtils里面
//        String appUID = SharedPreUtils.getPreStringInfo(instance, AppConfig.APP_SAVE_UID);
//        if (CommonUtils.isEmpty(appUID)) {
//            appUID = CommonUtils.getDeviceId(instance);
//            SharedPreUtils.setStringToPre(instance, AppConfig.APP_SAVE_UID, appUID);
//        }
//        return appUID;
//    }

}

