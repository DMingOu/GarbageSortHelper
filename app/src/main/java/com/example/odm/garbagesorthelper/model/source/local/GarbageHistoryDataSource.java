package com.example.odm.garbagesorthelper.model.source.local;

import com.example.odm.garbagesorthelper.model.entity.GarbageSearchHistory;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

/**
 * @description: 垃圾搜索结果数据源接口
 * @author: ODM
 * @date: 2019/11/10
 */
public interface GarbageHistoryDataSource {



    Flowable<List<GarbageSearchHistory>> getAllGarbageHistory() ;


    Completable deleteAllGarbageHistory();


    Completable insertGarbageHistory(GarbageSearchHistory... histories);

    Completable delete(GarbageSearchHistory... histories);


    Single<GarbageSearchHistory> getGarbageHistoryByName(String name);

    Flowable<List<GarbageSearchHistory>> loadAllByType(int type);

    Flowable<GarbageSearchHistory> getGarbageHistoryById(int id);
}
