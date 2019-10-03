package com.example.odm.garbagesorthelper.model.entity;

import com.stx.xhb.androidx.entity.SimpleBannerInfo;

/**
 * description: 轮播图数据实体类
 * author: ODM
 * date: 2019/10/3
 */
public class BannerData extends SimpleBannerInfo {

    Object url;

    public BannerData(Object Url) {
        url = Url;
    }

    @Override
    public Object getXBannerUrl() {
        return url;
    }
}
