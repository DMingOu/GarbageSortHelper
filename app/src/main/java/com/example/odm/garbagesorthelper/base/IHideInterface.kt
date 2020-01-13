package com.example.odm.garbagesorthelper.base

import androidx.fragment.app.Fragment

/**
 * @description: 隐藏页面中的特定的View
 * @author: ODM
 * @date: 2020/1/13
 */
interface IHideInterface {

    fun hideTitleBar()

    fun hideBottomNavigation()

    fun showTitleBar()

    fun showBottomNavigation()
}