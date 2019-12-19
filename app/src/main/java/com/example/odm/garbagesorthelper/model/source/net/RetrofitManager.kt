package com.example.odm.garbagesorthelper.model.source.net

import android.util.Log
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * @author: ODM
 * @date: 2019/8/17
 */
/**
 * 线程安全的单例类,用于请求网络
 */
class RetrofitManager private constructor() {
    private val retrofit: Retrofit
    /**
     * 获取访问http的service
     * @return HttpService
     */
    val apiService: ApiService

    companion object {
        private var retrofitManager: RetrofitManager? = null
        private const val DEFAULT_TIME_OUT = 8
        /**
         * 超时时间，默认为8秒
         * 有需要就用SP存储
         */
        var timeoutTime = DEFAULT_TIME_OUT
            private set
        /**
         * 服务器ip地址
         */
        var baseUrl: String = ApiService.Companion.BASE_URL
        val cookieStore = HashMap<String, List<Cookie>>()
        /**
         * 获取网络管理的manager
         * @return 该单例类
         */
        val instance: RetrofitManager?
            get() {
                if (retrofitManager == null) {
                    synchronized(Any::class.java) {
                        if (retrofitManager == null) {
                            retrofitManager = RetrofitManager()
                        }
                    }
                }
                return retrofitManager
            }

        /**
         * 重新设置服务器和超时时间
         * @param timeout 超时时间
         * @param url 服务器地址
         */
        fun setTimeoutAndUrl(timeout: Int, url: String) {
            timeoutTime = timeout
            Log.d("RetorfitManager", "" + timeoutTime)
            baseUrl = url
            //重新生成service
            retrofitManager = RetrofitManager()
        }

    }

    init {
        val builder = OkHttpClient.Builder()
                .cookieJar(object : CookieJar {
                    override fun saveFromResponse(httpUrl: HttpUrl, list: List<Cookie>) {
                        cookieStore[httpUrl.host] = list
                    }

                    override fun loadForRequest(httpUrl: HttpUrl): List<Cookie> {
                        val cookies = cookieStore[httpUrl.host]
                        return cookies ?: ArrayList()
                    }
                })
                .retryOnConnectionFailure(true)
                .connectTimeout(timeoutTime.toLong(), TimeUnit.SECONDS)
                .writeTimeout(timeoutTime.toLong(), TimeUnit.SECONDS)
                .readTimeout(timeoutTime.toLong(), TimeUnit.SECONDS)
        //创建Retrofit
        retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(builder.build())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        apiService = retrofit.create(ApiService::class.java)
    }
}