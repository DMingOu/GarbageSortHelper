package com.example.odm.garbagesorthelper.model.source.local;

import com.example.odm.garbagesorthelper.model.entity.GarbageSearchHistory;
import com.orhanobut.logger.Logger;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

/**
 * @description: 垃圾搜索历史数据源
 * @author: ODM
 * @date: 2019/11/10
 */
public class LocalGarbageHistoryDataSource implements GarbageHistoryDataSource {

    private final GarbageHistoryDao mDao;

    public LocalGarbageHistoryDataSource(GarbageHistoryDao garbageHistoryDao) {
        this.mDao = garbageHistoryDao;
    }

    @Override
    public Flowable<List<GarbageSearchHistory>> getAllGarbageHistory() {
        return mDao.getAllGarbageHistory();
    }

    @Override
    public Completable deleteAllGarbageHistory() {
         return mDao.deleteAllGarbageHistory();
    }

    @Override
    public Completable insertGarbageHistory(GarbageSearchHistory... histories) {

        return mDao.insertGarbageHistory(histories);
    }


    @Override
    public Completable delete(GarbageSearchHistory... histories) {
        return mDao.delete(histories);
    }

    @Override
    public Single<GarbageSearchHistory> getGarbageHistoryByName(String name) {
        return mDao.getGarbageHistoryByName(name);
    }

    @Override
    public  Flowable<List<GarbageSearchHistory>> loadAllByType(int type) {
        return mDao.loadAllByType(type);
    }

    @Override
    public Flowable<GarbageSearchHistory> getGarbageHistoryById(int id) {
        return mDao.getGarbageHistoryById(id);
    }
}
