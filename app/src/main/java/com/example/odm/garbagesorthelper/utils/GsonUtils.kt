package com.example.odm.garbagesorthelper.utils

import com.google.gson.Gson
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken
import java.util.*

/**
 * description: Gson转换工具类
 * author: ODM
 * date: 2019/9/25
 */
class GsonUtils {
    companion object {
        private var gson: Gson? = null
        /**
         * 转成json
         *
         * @param object
         * @return
         */
        fun GsonString(`object`: Any?): String? {
            var gsonString: String? = null
            gsonString = gson ?.toJson(`object`)

            return gsonString
        }

        /**
         * 转成bean
         *
         * @param gsonString
         * @param cls
         * @return
         */
        fun <T> GsonToBean(gsonString: String?, cls: Class<T>?): T? {
            var t: T? = null
            t = gson?.fromJson(gsonString, cls)

            return t
        }

        /**
         * 转成list
         * 泛型在编译期类型被擦除导致报错
         *
         * @param gsonString
         * @param cls
         * @return
         */
        fun <T> GsonToList(gsonString: String?, cls: Class<T>?): List<T>? {
            val list: List<T>? = gson?.fromJson(gsonString, object : TypeToken<List<T>?>() {}.type)
            return list
        }

        /**
         * 转成list中有map的
         *
         * @param gsonString
         * @return
         */
        fun <T> GsonToListMaps(gsonString: String?): List<Map<String, T>>? {
            val list: List<Map<String, T>> ?= gson ?.fromJson(gsonString, object : TypeToken<List<Map<String?, T>?>?>() {}.type)
            return list
        }

        /**
         * 转成map的
         *
         * @param gsonString
         * @return
         */
        fun <T> GsonToMaps(gsonString: String?): Map<String, T>? {
            val map: Map<String, T> ?= gson?.fromJson(gsonString, object : TypeToken<Map<String?, T>?>() {}.type)
            return map
        }

        //单例
        init {
            if (gson == null) {
                gson = Gson()
            }
        }
    }

    /**
     * 转成list
     * 解决泛型问题
     *
     * @param json
     * @param cls
     * @param <T>
     * @return
    </T> */
    fun <T> jsonToList(json: String?, cls: Class<T>?): List<T> {
        val gson = Gson()
        val list: MutableList<T> = ArrayList()
        val array = JsonParser().parse(json).asJsonArray
        for (elem in array) {
            list.add(gson.fromJson(elem, cls))
        }
        return list
    }
}