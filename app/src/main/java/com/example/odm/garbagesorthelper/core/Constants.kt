package com.example.odm.garbagesorthelper.core

/**
 * description: 常量类
 * author: ODM
 * date: 2019/9/25
 */
object Constants {
    const val IMAGE_SUCCESS = "IMAGE_SUCCESS"
    const val IMAGE_FAILURE = "IMAGE_FAILURE"
    //百度图片识别所需accessToken,30天会过期一次 ，看情况写方法结合 SP 存储和获取新的accessToken
    var accessToken_baidu = "24.7c8cf402215aac15868f373915051af3.2592000.1579362608.282335-17340855"
}