package com.example.odm.garbagesorthelper.utils

import android.content.Context
import com.example.odm.garbagesorthelper.model.SearchDataRepository
import com.example.odm.garbagesorthelper.model.SearchDataRepository.Companion.getInstance
import com.example.odm.garbagesorthelper.model.source.local.GarbageHistoryDatabase.Companion.getInstance
import com.example.odm.garbagesorthelper.model.source.local.LocalGarbageHistoryDataSource
import com.example.odm.garbagesorthelper.ui.home.HomeViewModelFactory
import com.example.odm.garbagesorthelper.ui.search.SearchViewModelFactory

/**
 * description: 依赖注入类
 * author: ODM
 * date: 2019/10/20
 */
object InjectorUtils {
    private fun getSearchRepository(context: Context): SearchDataRepository? {
        val dataSource = provideGarbageHistoryDataSource(context)
        return getInstance(dataSource)
    }

    fun provideSearchViewModelFactory(context: Context): SearchViewModelFactory {
        val repository = getSearchRepository(context)
        return SearchViewModelFactory(repository!!)
    }

    fun provideHomeViewModelFactory(context: Context) : HomeViewModelFactory {
        val repository = getSearchRepository(context)
        return HomeViewModelFactory(repository!!)
    }

    fun provideGarbageHistoryDataSource(context: Context): LocalGarbageHistoryDataSource {
        //取得 RoomDataBase 数据库
        val database = getInstance(context)
        //将可以操作的 DAO 层传入
        //实例化可以操作的 LocalGarbageHistoryDataSource 对象方便对数据库进行操作
        return LocalGarbageHistoryDataSource(database.garbageHistoryDao())
    }
}