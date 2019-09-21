package com.example.odm.garbagesorthelper.net;

import com.example.odm.garbagesorthelper.model.entity.Garbage;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * 存放Retrofit会调用的具体请求方法 接口
 * @author: ODM
 * @date: 2019/8/17
 */
public interface   ApiService {

    /*
     * @其他声明
     * @请求方式("请求地址")
     * Observable<请求返回的实体> 请求方法名(请求参数)；
     */


   static String BASE_URL = "http://gs.choviwu.top/garbage/";

    /**
     * http://gs.choviwu.top/garbage/getGarbage?garbageName=奶茶
     *
     * @param garbageName garbage
     * @return 垃圾分类数据
     */
   @GET("getGarbage")
   Observable<Garbage> getGarbageData(@Query("garbageName") String garbageName);

}
