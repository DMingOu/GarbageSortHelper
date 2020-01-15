package com.example.odm.garbagesorthelper.model.source.local

import androidx.room.*
import com.example.odm.garbagesorthelper.model.entity.GarbageSearchHistory
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

/**
 * @description: 垃圾搜索历史DAO层
 * @author: ODM
 * @date: 2019/11/10
 */
@Dao
interface GarbageHistoryDao {
    /**
     * 查询获得所有数据
     * @return
     */
    @get:Query("SELECT * FROM garbage")
    val allGarbageHistory: Flowable<MutableList<GarbageSearchHistory> >?

    /**
     * 删除全部数据
     */
    @Query("DELETE FROM garbage")
    fun deleteAllGarbageHistory(): Completable?

    /**
     * 一次插入单或多条数据
     * 如果数据已经存在则进行更新
     * Completable 可以看作是 RxJava 的 Runnable 接口
     * 但他只能调用 onComplete 和 onError 方法，不能进行 map、flatMap 等操作
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertGarbageHistory(vararg histories: GarbageSearchHistory?): Completable?

    /**
     * 删除单或多个数据
     * @param histories
     */
    @Delete
    fun delete(vararg histories: GarbageSearchHistory?): Completable?

    /**
     * 根据id字段查找数据
     * @param id
     * @return
     */
    @Query("SELECT * FROM garbage WHERE garbageid= :id ")
    fun getGarbageHistoryById(id: Int): Flowable<GarbageSearchHistory?>?

    @Query("SELECT * FROM garbage WHERE garbagetype IN (:type)")
    fun loadAllByType(type: Int): Flowable<List<GarbageSearchHistory?>?>?

    @Query("SELECT * FROM garbage WHERE garbagename= :name")
    fun getGarbageHistoryByName(name: String?): Single<GarbageSearchHistory?>?
}