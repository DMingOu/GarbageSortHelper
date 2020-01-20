package com.example.odm.garbagesorthelper.model.entity
import com.google.gson.annotations.SerializedName


/**
 * @description: 百度图像识别接口AccessToken实体类
 * @author: ODM
 * @date: 2020/1/20
 */
data class accessTokenImageClassify(
    @SerializedName("access_token")
    val accessToken: String,
    @SerializedName("expires_in")
    val expiresIn: Int,
    @SerializedName("refresh_token")
    val refreshToken: String,
    @SerializedName("scope")
    val scope: String,
    @SerializedName("session_key")
    val sessionKey: String,
    @SerializedName("session_secret")
    val sessionSecret: String
)