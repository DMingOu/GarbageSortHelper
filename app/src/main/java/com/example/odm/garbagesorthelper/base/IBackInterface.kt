package com.example.odm.garbagesorthelper.base

import androidx.fragment.app.Fragment

/**
 * description: 监听Fragment返回事件接口
 * author: ODM
 * date: 2019/10/20
 */
interface IBackInterface {
    /**
     * 为宿主Activity设置当前被选中的按下了返回键的Fragment
     * @param fragment 按下了返回键的Fragment
     */
    fun setSelectedBackFragment(fragment: Fragment?)
}