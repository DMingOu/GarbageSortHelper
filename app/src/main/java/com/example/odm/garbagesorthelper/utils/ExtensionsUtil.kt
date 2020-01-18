package com.example.odm.garbagesorthelper.utils

import android.util.Log
import android.view.View
import android.widget.Checkable
import com.example.odm.garbagesorthelper.R

/**
 * @description: Kotlin 扩展属性工具类
 * @author: ODM
 * @date: 2019/12/20
 */

// 扩展点击事件的属性(重复点击时长)，通过setTag方法方便获取
var <T : View> T.lastClickTime: Long
    set(value) = setTag(R.id.tag_lastClickTime, value)
    get() = getTag(R.id.tag_lastClickTime) as? Long ?: 0
// 重复点击事件绑定,相当于绑定了点击事件
inline fun <T : View> T.singleClick(interval: Long = 500, crossinline block: (T) -> Unit) {
    val currentTimeMillis = System.currentTimeMillis()
    //两次点击事件时间间隔大于时间差 || 控件属于选择框一类可多次点击
    if (currentTimeMillis - lastClickTime > interval || this is Checkable) {
        lastClickTime = currentTimeMillis
        Log.e("单次点击事件扩展属性","执行View的点击事件")
        block(this)
    }
}