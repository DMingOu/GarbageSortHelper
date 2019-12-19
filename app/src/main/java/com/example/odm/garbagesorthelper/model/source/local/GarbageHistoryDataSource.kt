package com.example.odm.garbagesorthelper.model.source.local

import com.example.odm.garbagesorthelper.model.entity.GarbageSearchHistory
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

/**
 * @description: 垃圾搜索结果数据源接口
 * @author: ODM
 * @date: 2019/11/10
 */
interface GarbageHistoryDataSource {
    val allGarbageHistory: Flowable<List<GarbageSearchHistory?>?>?
    fun deleteAllGarbageHistory(): Completable?
    fun insertGarbageHistory(vararg histories: GarbageSearchHistory?): Completable?
    fun delete(vararg histories: GarbageSearchHistory?): Completable?
    fun getGarbageHistoryByName(name: String?): Single<GarbageSearchHistory?>?
    fun loadAllByType(type: Int): Flowable<List<GarbageSearchHistory?>?>?
    fun getGarbageHistoryById(id: Int): Flowable<GarbageSearchHistory?>?
}