package com.example.odm.garbagesorthelper.application;

import android.app.Application;
import android.content.Context;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

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
        Logger.addLogAdapter(new AndroidLogAdapter());
    }

    /**
     * Get context context.
     *
     * @return the context
     */
    public static  Context getContext(){
        return  mContext;
    }
}
