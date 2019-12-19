package com.example.odm.garbagesorthelper.ui.about

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
//import com.example.odm.garbagesorthelper.BR
import com.example.odm.garbagesorthelper.R
import com.example.odm.garbagesorthelper.base.BaseFragment
import com.example.odm.garbagesorthelper.databinding.FragmentAboutBinding
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog

/**
 * description: 我的页面View层
 * author: ODM
 * date: 2019/9/18
 */
class AboutFragment : BaseFragment() {
    private var mBinding: FragmentAboutBinding? = null
    private var aboutViewModel: AboutViewModel? = null
    private var dialogBuilder: MaterialDialog.Builder? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initViewDataBinding(inflater, container)
        initView()
        initData()
        return mBinding?.root
    }

    override val layoutId: Int
        get() = R.layout.fragment_about

    override fun initViewDataBinding(inflater: LayoutInflater, container: ViewGroup?) {
        aboutViewModel = ViewModelProviders.of(this).get(AboutViewModel::class.java)
        mBinding = DataBindingUtil.inflate(inflater, layoutId, container, false)
//        mBinding?.setViewModel(aboutViewModel)
//        mBinding?.setVariable(BR.viewModel, aboutViewModel)
        mBinding?.setLifecycleOwner(this)
    }

    private fun initData() {
        aboutViewModel?.versionName?.value = aboutViewModel?.version
    }



    private fun initView() {
        if (mBinding != null) {
            dialogBuilder = MaterialDialog.Builder(context!!)
            /*
             * 点击事件
             */
            mBinding?.ralAboutAppAuthor?.setOnClickListener { v: View? -> showAuthorDialog() }
            mBinding?.ralAboutAppIntroduction?.setOnClickListener { v: View? -> showIntroductionDialog() }
            mBinding?.ralAboutAppUpdate?.setOnClickListener { v: View? -> Toast.makeText(context, "已经是最新版本了", Toast.LENGTH_SHORT).show() }
        }
    }

    private fun showAuthorDialog() {
        if (dialogBuilder != null) {
            dialogBuilder!!.title("我是谁?")
                    .content("我是一名广东工业大学2018级信息安全2班的学生区德明，因为兴趣使然，独立完成了此款垃圾分类小APP。" + "\n" + resources.getString(R.string.welcome_contact_to_me) + "\n" + resources.getString(R.string.my_email_address))
                    .positiveText(R.string.known)
                    .show()
        }
    }

    private fun showIntroductionDialog() {
        if (dialogBuilder != null) {
            dialogBuilder!!.title("功能介绍")
                    .icon(resources.getDrawable(R.drawable.module_about_dialog_icon_list))
                    .content("1、搜索关键词，获取关键词对应的垃圾分类"
                            + "\n" + "2、物体拍照识别,获取对应的垃圾分类" + "\n" + "3、语音识别关键词，获取关键词对应的垃圾分类" + "\n" + "4、学习垃圾分类的基础知识与必要性")
                    .positiveText(R.string.known)
                    .show()
        }
    }
}