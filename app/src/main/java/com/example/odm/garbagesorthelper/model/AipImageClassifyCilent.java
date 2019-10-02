package com.example.odm.garbagesorthelper.model;

import com.baidu.aip.imageclassify.AipImageClassify;

/**
 * @author ODM
 * 百度图像识别的客户端单例类
 */
public class AipImageClassifyCilent {


    private static class SingletonInstance {
        //设置APPID/AK/SK
         static final String APP_ID = "17340855";
         static final String API_KEY = "VxsAQEj0IqXzYQP4EdhMep65";
         static final String SECRET_KEY = "VVZG3lFByluPHwNk4mcF6LkpqEgxoUPv";
        private static final AipImageClassify INSTANCE = new AipImageClassify( APP_ID, API_KEY, SECRET_KEY);
    }

    public static AipImageClassify getInstance() {
        return SingletonInstance.INSTANCE;
    }
}