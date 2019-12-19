package com.example.odm.garbagesorthelper.model.source.net

/**
 * 网络异常状态类
 *
 * @author: ODM
 * @date: 2019/8/17
 */
class HttpThrowable internal constructor(var errorType: Int, override var message: String, var throwable: Throwable) : Exception(throwable) {

    companion object {
        /**
         * 未知错误
         */
        const val UNKNOWN = 1000
        /**
         * 解析错误
         */
        const val PARSE_ERROR = 1001
        /**
         * 连接错误
         */
        const val CONNECT_ERROR = 1002
        /**
         * DNS解析失败（无网络）
         */
        const val NO_NET_ERROR = 1003
        /**
         * 连接超时错误
         */
        const val TIME_OUT_ERROR = 1004
        /**
         * 网络（协议）错误
         */
        const val HTTP_ERROR = 1005
        /**
         * 证书错误
         */
        const val SSL_ERROR = 1006
    }

}