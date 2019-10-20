package com.example.odm.garbagesorthelper.utils;

import android.content.Context;

import com.example.odm.garbagesorthelper.model.SearchDataRepository;
import com.example.odm.garbagesorthelper.ui.search.SearchViewModelFactory;

/**
 * description: 依赖注入类
 * author: ODM
 * date: 2019/10/20
 */
public class InjectorUtils {

    private static SearchDataRepository getSearchRepository(Context context) {
        return SearchDataRepository.getInstance();

    }

    public static SearchViewModelFactory provideSearchViewModelFactory(Context context) {
        SearchDataRepository repository = getSearchRepository(context);
        return new  SearchViewModelFactory (repository);
    }

}
