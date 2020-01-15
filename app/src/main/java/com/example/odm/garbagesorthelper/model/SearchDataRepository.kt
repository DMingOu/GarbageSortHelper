package com.example.odm.garbagesorthelper.model

import com.example.odm.garbagesorthelper.core.Constants
import com.example.odm.garbagesorthelper.model.entity.GarbageData
import com.example.odm.garbagesorthelper.model.entity.GarbageSearchHistory
import com.example.odm.garbagesorthelper.model.entity.ImageClassifyBean
import com.example.odm.garbagesorthelper.model.source.local.LocalGarbageHistoryDataSource
import com.example.odm.garbagesorthelper.model.source.net.ApiService
import com.example.odm.garbagesorthelper.model.source.net.RetrofitManager
import com.example.odm.garbagesorthelper.utils.Base64Util
import com.example.odm.garbagesorthelper.utils.FileUtil
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import java.io.IOException
import java.io.UnsupportedEncodingException
import java.net.URLEncoder

class SearchDataRepository private constructor(private val historyDataSource: LocalGarbageHistoryDataSource) {
    /**
     * 联网获取垃圾分类搜索结果
     * @param garbageName 垃圾名称
     * @return Observable对象
     */
    fun getGarbageDataResult(garbageName: String?): Observable<GarbageData?>?  {
        return RetrofitManager.instance
                ?.apiService
                ?.getGarbageData(garbageName)
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
    }

    /**
     * 联网获取图片识别结果
     * @param imageName 图片名
     * @return Observable对象
     */
    fun getImageClassifyResult(imageName: String): Observable<ImageClassifyBean?> ?{
        val filePath = "/storage/emulated/0/GarbageSortHelper/ImageCache/$imageName"
        var imgData: ByteArray = ByteArray(0)
        try {
            imgData = FileUtil.readFileByBytes(filePath)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        val imgStr_Base64 = Base64Util.encode(imgData)
        var imgParam: String? = null
        try {
            imgParam = URLEncoder.encode(imgStr_Base64, "UTF-8")
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }
        val param = "image=$imgParam"
        val body = RequestBody.create("application/x-www-form-urlencoded; charset=utf-8".toMediaTypeOrNull(), param)
        //请求数据
        return RetrofitManager.instance
                ?.apiService
                ?.getImageClassifyData(ApiService.Base_Url_Image_Classify, Constants.accessToken_baidu, body)
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())

    }

    /**
     * 获取所有 垃圾搜索历史
     * Flowable 背压，第一次启动后，每次Flowable内部数据有变动，都会执行一次方法
     * @return 数据库中所有垃圾搜索分类历史
     */
    val allGarbageHistory: Flowable<MutableList<GarbageSearchHistory>> ?
        get() = historyDataSource
                .allGarbageHistory
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())

    fun getGarbageHistoryByName(name: String?): Single<GarbageSearchHistory?> ? {
        return historyDataSource.getGarbageHistoryByName(name)
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
    }

    /**
     * 根据垃圾名称和垃圾种类，插入数据库
     * @param garbageName
     * @param garbageType
     * @return
     */
    fun insertGarbageHistory(garbageName: String?, garbageType: Int): Completable ? {
        val history = GarbageSearchHistory(garbageName!!, garbageType, System.currentTimeMillis().toInt())
        return historyDataSource
                .insertGarbageHistory(history)
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
    }

    fun deleteAllGarbageHistory() {
        historyDataSource.deleteAllGarbageHistory()
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe()
    }

    fun deleteGarbageHistory(vararg histories: GarbageSearchHistory?) {
        historyDataSource.delete(*histories)
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe()
    }

    companion object {
        @Volatile
        private var singleton: SearchDataRepository? = null

        @JvmStatic
        fun getInstance(dataSource: LocalGarbageHistoryDataSource): SearchDataRepository? {
            if (singleton == null) {
                synchronized(SearchDataRepository::class.java) {
                    if (singleton == null) {
                        singleton = SearchDataRepository(dataSource)
                    }
                }
            }
            return singleton
        }
    }

}