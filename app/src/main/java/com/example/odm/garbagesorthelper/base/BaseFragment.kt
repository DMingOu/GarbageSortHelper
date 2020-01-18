package com.example.odm.garbagesorthelper.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

/**
 * description: Fragment基类
 * author: ODM
 * date: 2019/9/19
 */
abstract class BaseFragment : Fragment(), IBaseView {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    /**
     * 初始化页面的ViewModel和DataBinding
     */
//     fun initViewDataBinding(inflater: LayoutInflater, container: ViewGroup?)

    abstract fun initViews()
    /**
     * 返回页面布局的ID
     *
     * @return the layout id
     */
    abstract val layoutId: Int
}