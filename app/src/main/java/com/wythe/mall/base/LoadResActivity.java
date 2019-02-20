package com.wythe.mall.base;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.wythe.mall.R;
import com.wythe.mall.utils.PrintLog;

import androidx.multidex.MultiDex;

/**
 * Created by ningwang on 16/2/19.
 */
public class LoadResActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super .onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN , WindowManager.LayoutParams.FLAG_FULLSCREEN );
        overridePendingTransition(R.anim.null_anim, R.anim.null_anim);
        setContentView(R.layout.layout_load);
        new LoadDexTask().execute();
    }
    class LoadDexTask extends AsyncTask {
        @Override
        protected Object doInBackground(Object[] params) {
            try {
                MultiDex.install(getApplication());
                PrintLog.printInfor("loadDex", "install finish");
                ((AppContext) getApplication()).installFinish(getApplication());
            } catch (Exception e) {
                PrintLog.printInfor("loadDex", e.getLocalizedMessage());
            }
            return null;
        }
        @Override
        protected void onPostExecute(Object o) {
            PrintLog.printInfor("loadDex", "get install finish");
            finish();
            System.exit( 0);
        }
    }
    @Override
    public void onBackPressed() {
        //cannot backpress
    }
}
