package com.example.odm.garbagesorthelper.utils;

import android.content.Context;

import com.example.odm.garbagesorthelper.model.SearchDataRepository;
import com.example.odm.garbagesorthelper.model.source.local.GarbageHistoryDatabase;
import com.example.odm.garbagesorthelper.model.source.local.LocalGarbageHistoryDataSource;
import com.example.odm.garbagesorthelper.ui.search.SearchViewModelFactory;

/**
 * description: 依赖注入类
 * author: ODM
 * date: 2019/10/20
 */
public class InjectorUtils {

    private static SearchDataRepository getSearchRepository(Context context) {
        LocalGarbageHistoryDataSource dataSource = provideGarbageHistoryDataSource(context);
        return SearchDataRepository.getInstance(dataSource);

    }

    public static SearchViewModelFactory provideSearchViewModelFactory(Context context) {
        SearchDataRepository repository = getSearchRepository(context);
        return new  SearchViewModelFactory (repository);
    }

    public static LocalGarbageHistoryDataSource provideGarbageHistoryDataSource(Context context) {
        //取得 RoomDataBase
        GarbageHistoryDatabase database = GarbageHistoryDatabase.getInstance(context);
        //将可以操作的 DAO 传入
        //实例化可以操作的 LocalGarbageHistoryDataSource 对象方便对数据库进行操作
        return new LocalGarbageHistoryDataSource(database.garbageHistoryDao());
    }

}
