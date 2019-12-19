package com.example.odm.garbagesorthelper.base

import com.example.odm.garbagesorthelper.model.source.net.ApiService

/**
 * description: 数据存储Repository基类(暂时没用)
 * author: ODM
 * date: 2019/9/17
 */
class BaseRepository(val apiService: ApiService) {

    fun isSuccess(result: BaseResult<*>?): Boolean {
        return result != null && result.isSuccess
    }

}