package com.example.odm.garbagesorthelper.ui

import android.app.Application
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem
import com.example.odm.garbagesorthelper.R
import com.example.odm.garbagesorthelper.base.BaseModel
import com.example.odm.garbagesorthelper.base.BaseViewModel
import com.example.odm.garbagesorthelper.ui.about.AboutFragment
import com.example.odm.garbagesorthelper.ui.knowledge.KnowLedgeFragment
import com.example.odm.garbagesorthelper.ui.search.SearchFragment
import com.orhanobut.logger.Logger
import java.util.*

/**
 * description: Root根页面ViewModel
 * author: ODM
 * date: 2019/9/18
 */
class RootViewModel(application: Application?) : BaseViewModel<BaseModel?>(application!!) {
    //Fragment对象列表
    var mFragments: MutableList<Fragment>? = null
    var backFragment: Fragment? = null
    //目前展示Fragment的位置
    var lastFragmentIndex = 0
    //标题栏颜色
    var titlebarColor = MutableLiveData(R.color.bottom_navigation_search)

    /**
     * 制造底部导航的子项item
     * @param titleRes 标题资源引用
     * @param drawableRes 图片资源引用
     * @param colorRes 颜色资源引用
     * @return 底部导航的子项item
     */
    fun createBottomNavigationItem(@StringRes titleRes: Int, @DrawableRes drawableRes: Int, @ColorRes colorRes: Int): AHBottomNavigationItem {
        return AHBottomNavigationItem(titleRes, drawableRes, colorRes)
    }

    /**
     * 初始化Fragment对象列表
     */
    fun initFragmentData() {
        mFragments = ArrayList()
        mFragments?.add(SearchFragment())
        mFragments?.add(KnowLedgeFragment())
        mFragments?.add(AboutFragment())
    }

    /**
     * 动态改变了标题栏的背景颜色
     * @param position 目标Fragment位置
     */
    fun changeFragmentTitleBarColor(position: Int) {
        when (position) {
            0 -> titlebarColor.setValue(R.color.bottom_navigation_search)
            1 -> titlebarColor.setValue( R.color.bottom_navigation_knowledge)
            2 -> titlebarColor.setValue(R.color.bottom_navigation_about)
            else -> {
            }
        }
    }
}