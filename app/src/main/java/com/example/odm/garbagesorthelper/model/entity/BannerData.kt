package com.example.odm.garbagesorthelper.model.entity

import com.stx.xhb.androidx.entity.SimpleBannerInfo

/**
 * description: 轮播图数据实体类
 * author: ODM
 * date: 2019/10/3
 */
 data class BannerData(var url: Any) : SimpleBannerInfo() {
    override fun getXBannerUrl(): Any {
        return url
    }

}