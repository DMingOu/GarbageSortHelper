package com.example.odm.garbagesorthelper.base;


import com.example.odm.garbagesorthelper.model.source.http.ApiService;

/**
 * description: 数据存储Repository基类(暂时没用)
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
