package com.example.odm.garbagesorthelper.application;

import android.app.Application;
import android.content.Context;

import com.example.odm.garbagesorthelper.R;
import com.example.odm.garbagesorthelper.RootActivity;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import cat.ereza.customactivityoncrash.config.CaocConfig;

/**
 * description: Application类
 * @author: ODM
 * @date: 2019/9/18
 */
public class GarbageSortApplication extends Application {

    private  static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        initLogger();
        initCrashPage();
    }

    /**
     * Get context context.
     *
     * @return the context
     */
    public static  Context getContext(){
        return  mContext;
    }

    private void initLogger() {
        Logger.addLogAdapter(new AndroidLogAdapter());
    }

    //让用户使用时不直接闪退，而是跳到一个Activity，可以重启应用，但无法阻止或保存或上传闪退信息
    private void initCrashPage(){
        CaocConfig.Builder.create()
                .backgroundMode(CaocConfig.BACKGROUND_MODE_CRASH)
                .enabled(true)
                .showErrorDetails(true)
                .showRestartButton(true)
                .logErrorOnRestart(true)
                .trackActivities(true)
                .minTimeBetweenCrashesMs(2000)
                .errorDrawable(R.drawable.customactivityoncrash_error_image)
                .restartActivity(RootActivity.class)
                .errorActivity(null)
                .eventListener(null)
                .apply();
    }
}
