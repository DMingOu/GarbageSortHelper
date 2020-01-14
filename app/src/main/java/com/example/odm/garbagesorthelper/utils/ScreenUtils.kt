package com.example.odm.garbagesorthelper.utils

import android.content.Context
import android.util.DisplayMetrics
import android.view.WindowManager

/**
 * @description: 屏幕工具类
 * @author: ODM
 * @date: 2020/1/14
 */

fun getDeviceDP(context: Context ?){
    val wm = context?.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val dm = DisplayMetrics()
    wm.getDefaultDisplay().getMetrics(dm)
    val width = dm.widthPixels         // 屏幕宽度（像素）
    val height = dm.heightPixels       // 屏幕高度（像素）
    val density  = dm.density          // 屏幕密度（0.75 / 1.0 / 1.5）
    val densityDpi = dm.densityDpi     // 屏幕密度dpi（120 / 160 / 240）
    // 屏幕宽度算法:屏幕宽度（像素）/屏幕密度
    val screenWidth : Float =  width  / density  // 屏幕宽度(dp)
    val screenHeight = (height / density);// 屏幕高度(dp)

//        Log.d("h_bl", "屏幕宽度（像素）：" + width);
//        Log.d("h_bl", "屏幕高度（像素）：" + height);
//        Log.d("h_bl", "屏幕密度（0.75 / 1.0 / 1.5）：" + density);
//        Log.d("h_bl", "屏幕密度dpi（120 / 160 / 240）：" + densityDpi);
//        Log.d("h_bl", "屏幕宽度（dp）：" + screenWidth);
//        Log.d("h_bl", "屏幕高度（dp）：" + screenHeight);
}