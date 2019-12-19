package com.example.odm.garbagesorthelper.base

import com.google.gson.annotations.SerializedName

/**
 * description: 返回结果的基类
 * author: ODM
 * date: 2019/9/18
 */
class BaseResult<T> {
    @SerializedName("errorCode")
    var resultCode = 0
    var errorMsg: String? = null
    var data: T? = null
        private set

    fun setData(data: T) {
        this.data = data
    }

    val isSuccess: Boolean
        get() = RESULT_SUCCESS == resultCode

    override fun toString(): String {
        return "BaseResult{" +
                "resultCode=" + resultCode +
                ", errorMsg='" + errorMsg + '\'' +
                ", data=" + data +
                '}'
    }

    companion object {
        const val RESULT_SUCCESS = 0
    }
}