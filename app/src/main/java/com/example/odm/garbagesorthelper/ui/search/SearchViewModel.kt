package com.example.odm.garbagesorthelper.ui.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.odm.garbagesorthelper.R
import com.example.odm.garbagesorthelper.model.SearchDataRepository
import com.example.odm.garbagesorthelper.model.entity.*
import com.example.odm.garbagesorthelper.model.entity.GarbageData.DataBean
import com.example.odm.garbagesorthelper.model.entity.ImageClassifyBean.ResultBean
import com.example.odm.garbagesorthelper.model.source.net.HttpThrowable
import com.example.odm.garbagesorthelper.model.source.net.ObserverManager
import com.example.odm.garbagesorthelper.utils.GsonUtils
import com.iflytek.cloud.*
import com.iflytek.cloud.ui.RecognizerDialog
import com.iflytek.cloud.ui.RecognizerDialogListener
import com.orhanobut.logger.Logger
import io.reactivex.CompletableObserver
import io.reactivex.SingleObserver
import io.reactivex.disposables.Disposable
import java.util.*

/**
 * description: 搜索页面ViewModel
 * author: ODM
 * date: 2019/9/19
 */
class SearchViewModel(private val repository: SearchDataRepository) : ViewModel() {


    /**
     * 用户搜索框搜索内容--垃圾名
     */
    var currentSearch: String ?= null

    /**
     * 用户搜索结果--分类垃圾列表
     */
    var sortedList = MutableLiveData<List<DataBean>>()



    var searching = false

    var liveEventTime: Long = 0


    private var _searchHistoryData  = MutableLiveData<MutableList<GarbageSearchHistory>>()
    val searchHistoryData : LiveData<MutableList<GarbageSearchHistory>>  = _searchHistoryData

    companion object {
        private const val TAG = "SearchViewModel"
    }

    init {
        allGarbageSearchHistory
    }


    /**
     * 调用垃圾分类查询接口，通过垃圾名 获取垃圾所属分类
     *
     * @param garbageName the garbage name
     */
    fun onSearch(garbageName: String) {

        repository.getGarbageDataResult(garbageName)
                ?.subscribe(object : ObserverManager<GarbageData?>() {
                    override fun onError(httpThrowable: HttpThrowable ) {
                        Logger.d("异常原因:  " + httpThrowable.message)
                    }
                    

                    override fun onNext(t: GarbageData?) {
//                        sortedList.value = null
                        sortedList.value = t?.data
                        findGarbageSearchHistory(garbageName, t?.data?.get(0)?.type ?: 5 )
                    }

                })
    }

    fun clearResultList() {
        sortedList.value = ArrayList()
        Logger.d("搜索结果列表的大小  " + sortedList.value!!.size)
    }

    /**
     * 不同的垃圾类别(int) 返回不同的垃圾分类图标
     *
     * @param garbageType the garbage type
     * @return the int
     */
    fun getGarbageIcon(garbageType: Int): Int {
        return when (garbageType) {
            0 -> R.drawable.module_search_cookiebar_fail_garbage
            1 -> R.drawable.module_search_cookiebar_dry_garbage
            2 -> R.drawable.module_search_cookiebar_wet_garbage
            3 -> R.drawable.module_search_cookiebar_recycle_garbage
            4 -> R.drawable.module_search_cookiebar_harmful_garbage
            else -> R.drawable.module_search_cookiebar_fail_garbage
        }
    }

    fun getGarbageClass(garbageType: Int) : Int {
        return when(garbageType) {
            0 ->  R.string.other_garbage
            1 ->  R.string.dry_garbage
            2 ->  R.string.wet_garbage
            3 ->  R.string.recycle_garbage
            4 ->  R.string.harmful_garbage
            else -> R.string.other_garbage
        }
    }


    /**
     * 插入垃圾搜索历史
     * @param garbageName
     * @param garbageType
     */
    fun insertGarbageSearchHistory(garbageName: String, garbageType: Int) {
        repository.insertGarbageHistory(garbageName, garbageType)
                ?.subscribe(object : CompletableObserver {
                    override fun onSubscribe(d: Disposable) {}
                    override fun onComplete() {
                    Log.e(TAG, "垃圾搜索历史插入完成  $garbageName")
                    }

                    override fun onError(e: Throwable) {
                        Logger.d("新数据插入失败原因  " + e.message)
                    }
        })
    }

    /**
     * 根据垃圾名称查找出对象
     * @param garbageName
     * @param garbageType
     */
    fun findGarbageSearchHistory(garbageName: String, garbageType: Int ) {
        repository.getGarbageHistoryByName(garbageName)
                ?.subscribe(object : SingleObserver<GarbageSearchHistory?> {
                    override fun onSubscribe(d: Disposable) {}
                    override fun onSuccess(garbageSearchHistory: GarbageSearchHistory) {
                        //有则删除掉
                        repository.deleteGarbageHistory(garbageSearchHistory)
                        Log.e(TAG, "删掉了名为 " + garbageSearchHistory.garbageName + " 的垃圾")
                        insertGarbageSearchHistory(garbageName, garbageType)
                    }

                    override fun onError(e: Throwable) {
                        Log.e(TAG, "onError: 无法找到Name为  " + e.message + " 的垃圾")
                        insertGarbageSearchHistory(garbageName, garbageType)
                    }
                })
    }

    fun deleteAllSearchHistory(){
        repository.deleteAllGarbageHistory()
    }

    /**
     * 获取Room中所有 垃圾搜索历史
     *
     */
    private val allGarbageSearchHistory: Unit
        get() {
            val disposable = repository.allGarbageHistory?.subscribe { garbageSearchHistories ->

                garbageSearchHistories?.let {
/*                    val stringBuilder = StringBuilder()
                    for (g in it){
                        stringBuilder.append(g?.garbageName)
                        stringBuilder.append("  ")
                    }
                    Log.e(TAG, "数据库展示： $stringBuilder")*/
                    val data = it.toMutableList()
                    data.reverse()
                    _searchHistoryData.value =  data
                }

            }
        }


}

