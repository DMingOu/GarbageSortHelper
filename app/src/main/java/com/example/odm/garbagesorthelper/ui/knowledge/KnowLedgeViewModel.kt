package com.example.odm.garbagesorthelper.ui.knowledge

import android.app.Application
import com.example.odm.garbagesorthelper.R
import com.example.odm.garbagesorthelper.base.BaseModel
import com.example.odm.garbagesorthelper.base.BaseViewModel

/**
 * description: 知识页面ViewModel
 * author: ODM
 * date: 2019/9/19
 */
class KnowLedgeViewModel(application: Application?) : BaseViewModel<BaseModel?>(application!!) {
    var screenHeight = 0
    var statusBarHeight = 0
    var tabHeight = 0
    //判断是否是recyclerView主动引起的滑动，true- 是，false- 否，否则由tablayout引起的
    var isRecyclerScroll = false
    //记录上一次位置，防止在同一内容块里滑动 重复定位到tablayout
    var lastPos = 0
    //用于recyclerView滑动到指定的位置
    var canScroll = false
    var scrollToPosition = 0
    var tabTxt = arrayOf("可回收垃圾", "有害垃圾", "干垃圾", "湿垃圾")
    var tabIcon = intArrayOf(R.drawable.module_search_cookiebar_recycle_garbage, R.drawable.module_search_cookiebar_harmful_garbage,
            R.drawable.module_search_cookiebar_dry_garbage, R.drawable.module_search_cookiebar_wet_garbage)
    var imgUrls = arrayOf(
            "https://ae01.alicdn.com/kf/H3e0cde875f9a4d64af8ea077842fc6a6S.jpg",
            "https://ae01.alicdn.com/kf/H5d580475bec84a3480af00c5ba8a9298v.jpg",
            "https://ae01.alicdn.com/kf/H9a598253c41148529676909b69f00a254.jpg",
            "https://ae01.alicdn.com/kf/H5290b15f527c4921b8350ab2783bfdf5p.jpg"
    )

    val finalHeight: Int
        get() = screenHeight - statusBarHeight - tabHeight
}