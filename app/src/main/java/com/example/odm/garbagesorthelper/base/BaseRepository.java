package com.example.odm.garbagesorthelper.base;

import com.example.odm.garbagesorthelper.api.ApiService;

/**
 * description: 数据存储Repository基类
 * author: ODM
 * date: 2019/9/17
 */
public class BaseRepository {

    private ApiService apiService;

    public BaseRepository(ApiService apiService){
        this.apiService = apiService;
    }

    public ApiService getApiService() {
        return apiService;
    }

    public boolean isSuccess(BaseResult result){
        return result!=null && result.isSuccess();
    }
}
