package com.example.odm.garbagesorthelper.model.source.local;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.odm.garbagesorthelper.model.entity.GarbageSearchHistory;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

/**
 * @description: 垃圾搜索历史DAO层
 * @author: ODM
 * @date: 2019/11/10
 */

@Dao
public interface GarbageHistoryDao {

    /**
     * 查询获得所有数据
     * @return
     */
    @Query("SELECT * FROM garbage")
    Flowable<List<GarbageSearchHistory>> getAllGarbageHistory();

    /**
     * 删除全部数据
     */
    @Query("DELETE FROM garbage")
    Completable deleteAllGarbageHistory();


    /**
     * 一次插入单或多条数据
     * 如果数据已经存在则进行更新
     * Completable 可以看作是 RxJava 的 Runnable 接口
     * 但他只能调用 onComplete 和 onError 方法，不能进行 map、flatMap 等操作
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertGarbageHistory(GarbageSearchHistory... histories);

    /**
     * 删除单或多个数据
     * @param histories
     */
    @Delete
    Completable delete(GarbageSearchHistory... histories);

    /**
     * 根据id字段查找数据
     * @param id
     * @return
     */
    @Query("SELECT * FROM garbage WHERE garbageid= :id ")
    Flowable<GarbageSearchHistory> getGarbageHistoryById(int id);


    @Query("SELECT * FROM garbage WHERE garbagetype IN (:type)")
    Flowable<List<GarbageSearchHistory>> loadAllByType(int type);

    @Query("SELECT * FROM garbage WHERE garbagename= :name")
    Single<GarbageSearchHistory> getGarbageHistoryByName(String name);
}
