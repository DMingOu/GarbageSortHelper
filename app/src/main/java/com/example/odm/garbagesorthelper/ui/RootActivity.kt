package com.example.odm.garbagesorthelper.ui

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
//import com.example.odm.garbagesorthelper.BR
import com.example.odm.garbagesorthelper.R
import com.example.odm.garbagesorthelper.base.BaseActivity
import com.example.odm.garbagesorthelper.base.IBackInterface
import com.example.odm.garbagesorthelper.databinding.ActivityRootBinding
import com.example.odm.garbagesorthelper.ui.RootActivity
import com.example.odm.garbagesorthelper.ui.search.CameraFragment
import com.orhanobut.logger.Logger
import com.tbruyelle.rxpermissions2.RxPermissions
import com.xuexiang.xui.utils.StatusBarUtils

class RootActivity : BaseActivity(), IBackInterface {
    //DataBinding变量
    var rootBinding: ActivityRootBinding? = null
    var rootViewModel: RootViewModel? = null


    companion object {
        private const val TAG = "RootActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initPermissions()
        //设置沉浸式状态栏
        StatusBarUtils.translucent(this)
        StatusBarUtils.setStatusBarLightMode(this)
        setContentView(R.layout.activity_root)
        initViewDataBinding()
        initFragmentData()
        initBottomNavigation()
    }

    private fun initFragmentData() {
        rootViewModel?.initFragmentData()
    }



    override fun initViewDataBinding() {
        rootBinding = DataBindingUtil.setContentView(this, layoutId)
        rootViewModel = ViewModelProviders.of(this).get(RootViewModel::class.java)
//        rootBinding?.setVariable(BR.viewModel, rootViewModel)
    }

    fun setFragmentPosition(position: Int) {
        val ft = supportFragmentManager.beginTransaction()
        //设置进场、退场动画
        ft.setCustomAnimations(R.anim.push_right_in, R.anim.push_right_out, R.anim.push_right_in, R.anim.push_right_out)
        val targetFragment : Fragment = rootViewModel?.mFragments?.get(position) ?: Fragment()
        val lastFragment : Fragment = rootViewModel?.mFragments?.get(rootViewModel?.lastFragmentIndex ?: 0) ?: Fragment()
        rootViewModel?.lastFragmentIndex = position
        ft.hide(lastFragment)
        //如果目标Fragment已经添加，则remove掉重新加入
        if (!targetFragment.isAdded) {
            supportFragmentManager.beginTransaction().remove(targetFragment).commit()
            Logger.d("pos : $position    的Fragment ，被添加进去")
            ft.add(R.id.root_fragment_container, targetFragment)
        }
        ft.replace(R.id.root_fragment_container, targetFragment)
        ft.show(targetFragment)
        ft.commitAllowingStateLoss()
        //防止重复添加
        supportFragmentManager.executePendingTransactions()
    }

    @SuppressLint("CheckResult")
    fun initPermissions() { //动态获取拍摄,录音权限
        val rxPermissions = RxPermissions(this)
        rxPermissions.request(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO)
                .subscribe { aBoolean ->
                    //检查权限是否获取，提醒用户
                    if (aBoolean) {
                        Log.e(TAG, "accept: 动态申请 权限回调 true")
                    } else {
                        Log.e(TAG, "accept: 动态申请 权限回调 false")
                        Toast.makeText(this@RootActivity, "未授权应用相关权限，将无法使用拍照识别功能！", Toast.LENGTH_LONG).show()
                    }
                }
    }

    /**
     * 监听返回键事件
     */
    override fun onBackPressed() { //若当前页面为拍摄页面，监听返回键事件--返回拍摄页面
        if (rootViewModel?.backFragment != null && (rootViewModel?.backFragment as CameraFragment).onBackPressed()) {
            setFragmentPosition(1)
            rootViewModel?.backFragment = null
        } else {
            //若当前页面为其他页面，监听返回键事件--退出APP
            super.onBackPressed()
            finish()
        }
    }


    fun initBottomNavigation() { // 底部导航添加子项
        rootBinding?.rootBottomNavigation?.addItem(rootViewModel?.createBottomNavigationItem(R.string.bottom_navigation_search, R.drawable.root_bottom_search, R.color.bottom_navigation_search))
        rootBinding?.rootBottomNavigation?.addItem(rootViewModel?.createBottomNavigationItem(R.string.bottom_navigation_knowledge, R.drawable.root_bottom_knowledge, R.color.bottom_navigation_knowledge))
        rootBinding?.rootBottomNavigation?.addItem(rootViewModel?.createBottomNavigationItem(R.string.bottom_navigation_about, R.drawable.root_bottom_about, R.color.bottom_navigation_about))
        rootBinding?.rootBottomNavigation?.currentItem = 0
        setFragmentPosition(0)
        rootBinding?.rootBottomNavigation?.isColored = true
        // 监控底部导航栏的点击事件
        rootBinding?.rootBottomNavigation?.setOnTabSelectedListener { position: Int, wasSelected: Boolean ->
            rootViewModel?.changeFragmentTitleBarColor(position)
            rootBinding?.rootToolbar?.setBackgroundColor(resources.getColor(rootViewModel?.titlebarColor?.value?.toInt() ?: 0  )  )
            setFragmentPosition(position)
            true
        }
    }

    override val layoutId: Int
        get() = R.layout.activity_root

    override fun setSelectedBackFragment(fragment: Fragment ?) {
        rootViewModel?.backFragment = fragment
    }

}