package com.example.odm.garbagesorthelper.model;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;

import com.example.odm.garbagesorthelper.base.BaseModel;
import com.example.odm.garbagesorthelper.model.source.http.HttpDataSource;
import com.example.odm.garbagesorthelper.model.source.local.LocalDataSource;

/**
 * description: MVVM的Model层，统一模块的数据仓库，包含网络数据和本地数据。一个应用可以有多个Repositor
 * 继承BaseModel
 * author: ODM
 * date: 2019/9/19
 */
public class RepositoryManager extends BaseModel implements HttpDataSource , LocalDataSource {

    private volatile static RepositoryManager INSTANCE = null;
    private final HttpDataSource mHttpDataSource;

    private final LocalDataSource mLocalDataSource;

    private RepositoryManager(@NonNull HttpDataSource httpDataSource,
                           @NonNull LocalDataSource localDataSource) {
        this.mHttpDataSource = httpDataSource;
        this.mLocalDataSource = localDataSource;
    }

    public static RepositoryManager getInstance(HttpDataSource httpDataSource,
                                             LocalDataSource localDataSource) {
        if (INSTANCE == null) {
            synchronized (RepositoryManager.class) {
                if (INSTANCE == null) {
                    INSTANCE = new RepositoryManager(httpDataSource, localDataSource);
                }
            }
        }
        return INSTANCE;
    }

    @VisibleForTesting
    public static void destroyInstance() {
        INSTANCE = null;
    }

    @Override
    public void saveSomething(String something) {
        mLocalDataSource.saveSomething(something);
    }
}
