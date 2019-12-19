package com.example.odm.garbagesorthelper.model.source.net

import com.example.odm.garbagesorthelper.model.entity.GarbageData
import com.example.odm.garbagesorthelper.model.entity.ImageClassifyBean
import io.reactivex.Observable
import okhttp3.RequestBody
import retrofit2.http.*

/**
 * 存放Retrofit会调用的具体请求方法 接口
 *
 * @author: ODM
 * @date: 2019 /8/17
 */
interface ApiService {
    /**
     * https://service.xiaoyuan.net.cn/garbage/index/search?kw=奶茶杯
     *
     * @param garbageName 垃圾名称
     * @return Observable对象
     */
    @GET("search")
    fun getGarbageData(@Query("kw") garbageName: String?): Observable<GarbageData?>?

    /**
     * 调用百度识图API，获取图片识别信息
     *
     * @param url          百度图像识别 BaseUrl
     * @param access_token the access token 百度鉴权accessToken
     * @param requestBody  the request body 请求body
     * @return the image classify data 图片识别信息Json
     */
    @Headers("Content-Type: application/x-www-form-urlencoded", "Accept: application/json")
    @POST
    fun getImageClassifyData(@Url url: String?, @Query("access_token") access_token: String?, @Body requestBody: RequestBody?): Observable<ImageClassifyBean?>?

    companion object {
        /*
     * @其他声明
     * @请求方式("请求地址")
     * Observable<请求返回的实体> 请求方法名(请求参数)；
     */
        /**
         * The constant BASE_URL.
         */
        const val BASE_URL = "https://service.xiaoyuan.net.cn/garbage/index/"
        /**
         * The constant Base_Url_Image_Classify.
         */
// 百度图像识别 BaseUrl
        const val Base_Url_Image_Classify = "https://aip.baidubce.com/rest/2.0/image-classify/v2/advanced_general"
    }
}