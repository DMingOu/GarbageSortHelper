package com.example.odm.garbagesorthelper.model.source.local

import com.example.odm.garbagesorthelper.model.entity.GarbageSearchHistory
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

/**
 * @description: 垃圾搜索历史数据源
 * @author: ODM
 * @date: 2019/11/10
 */
class LocalGarbageHistoryDataSource(private val mDao: GarbageHistoryDao) : GarbageHistoryDataSource {
    override val allGarbageHistory: Flowable<List<GarbageSearchHistory?>?>?
        get() = mDao.allGarbageHistory

    override fun deleteAllGarbageHistory(): Completable? {
        return mDao.deleteAllGarbageHistory()
    }

    override fun insertGarbageHistory(vararg histories: GarbageSearchHistory?): Completable? {
        return mDao.insertGarbageHistory(*histories)
    }

    override fun delete(vararg histories: GarbageSearchHistory?): Completable? {
        return mDao.delete(*histories)
    }

    override fun getGarbageHistoryByName(name: String?): Single<GarbageSearchHistory?>? {
        return mDao.getGarbageHistoryByName(name)
    }

    override fun loadAllByType(type: Int): Flowable<List<GarbageSearchHistory?>?>? {
        return mDao.loadAllByType(type)
    }

    override fun getGarbageHistoryById(id: Int): Flowable<GarbageSearchHistory?>? {
        return mDao.getGarbageHistoryById(id)
    }

}