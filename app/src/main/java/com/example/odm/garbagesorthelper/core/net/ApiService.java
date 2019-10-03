package com.example.odm.garbagesorthelper.core.net;

import com.example.odm.garbagesorthelper.model.entity.GarbageData;
import com.example.odm.garbagesorthelper.model.entity.ImageClassifyBean;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * 存放Retrofit会调用的具体请求方法 接口
 *
 * @author: ODM
 * @date: 2019 /8/17
 */
public interface   ApiService {

    /*
     * @其他声明
     * @请求方式("请求地址")
     * Observable<请求返回的实体> 请求方法名(请求参数)；
     */


    /**
     * The constant BASE_URL.
     */
    static String BASE_URL = "https://service.xiaoyuan.net.cn/garbage/index/";

    /**
     * https://service.xiaoyuan.net.cn/garbage/index/search?kw=奶茶杯
     *
     * @param garbageName 垃圾名称
     * @return 垃圾分类数据 garbage data
     */
    @GET("search")
    Observable<GarbageData> getGarbageData(@Query("kw") String garbageName);

    /**
     * The constant Base_Url_Image_Classify.
     */
// 百度图像识别 BaseUrl
    static String Base_Url_Image_Classify = "https://aip.baidubce.com/rest/2.0/image-classify/v2/advanced_general";

    /**
     * 调用百度识图API，获取图片识别信息
     *
     * @param url          百度图像识别 BaseUrl
     * @param access_token the access token 百度鉴权accessToken
     * @param requestBody  the request body 请求body
     * @return the image classify data 图片识别信息Json
     */
    @Headers({"Content-Type: application/x-www-form-urlencoded","Accept: application/json"})
    @POST
   Observable<ImageClassifyBean> getImageClassifyData(@Url String url, @Query("access_token") String access_token , @Body RequestBody requestBody);

}
