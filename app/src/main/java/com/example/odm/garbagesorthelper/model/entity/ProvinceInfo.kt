package com.example.odm.garbagesorthelper.model.entity
import com.google.gson.annotations.SerializedName


/**
 * @description: 城市区域信息实体类
 * @author: ODM
 * @date: 2020/1/16
 */
data class ProvinceInfo(
    @SerializedName("city")
    val city: List<City>,
    @SerializedName("name")
    val name: String
)

data class City(
    @SerializedName("area")
    val area: List<String>,
    @SerializedName("name")
    val name: String
)