package com.example.odm.garbagesorthelper.ui.root

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import android.transition.Fade
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation
import com.example.odm.garbagesorthelper.R
import com.example.odm.garbagesorthelper.base.BaseActivity
import com.example.odm.garbagesorthelper.base.IBackInterface
import com.example.odm.garbagesorthelper.base.IHideInterface
import com.orhanobut.logger.Logger
import com.tbruyelle.rxpermissions2.RxPermissions
import com.wuhenzhizao.titlebar.widget.CommonTitleBar
import com.xuexiang.xui.utils.StatusBarUtils


class RootActivity : BaseActivity(), IBackInterface ,IHideInterface{
    private var rootViewModel: RootViewModel? = null
    private var rootBottomNavigation : AHBottomNavigation?= null
    private var rootToolbar : CommonTitleBar ?= null

    companion object {
        private const val TAG = "RootActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //设置沉浸式状态栏
        StatusBarUtils.translucent(this)
        setContentView(R.layout.activity_root)
        rootViewModel = ViewModelProviders.of(this).get(RootViewModel::class.java)

        //添加Fragment对象数据，加载 Fragment
        initFragmentData()
        //手动设置展示第一个Fragment
        setFragmentByPosition(0)
        initViews()
        initPermissions()
    }

    private fun initFragmentData() {
        rootViewModel?.initFragmentData()
    }



    fun setFragmentByPosition(position: Int) {

        val ft = supportFragmentManager.beginTransaction()
        //设置进场、退场动画
//        ft.setCustomAnimations(R.anim.push_left_in, R.anim.push_right_out, R.anim.push_left_out, R.anim.push_right_out)
        ft.addToBackStack(null)
        val targetFragment : Fragment = rootViewModel?.mFragments?.get(position) ?: Fragment()
        val lastFragment : Fragment = rootViewModel?.mFragments?.get(rootViewModel?.lastFragmentIndex ?: 0) ?: Fragment()
        ft.hide(lastFragment)
        //如果目标Fragment已经添加，则remove掉重新加入
        if (targetFragment.isAdded) {
            supportFragmentManager.beginTransaction()
                    .remove(targetFragment)
                    .commitNow()
            Logger.d("targetFragment 被remove    pos : $position    的Fragment ，被添加进去")
            //ft.add(R.id.root_fragment_container, targetFragment).commitNow()
        }
        ft.replace(R.id.root_fragment_container, targetFragment)
                .show(targetFragment)
                .commitAllowingStateLoss()
        //立刻执行操作
        supportFragmentManager.executePendingTransactions()
        rootViewModel?.lastFragmentIndex = position


    }

    @SuppressLint("CheckResult")
    fun initPermissions() {
        //动态获取拍摄,录音权限
        val rxPermissions = RxPermissions(this)
        rxPermissions.request( Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.CAMERA )
                .subscribe { aBoolean ->
                    //检查权限是否获取，提醒用户
                    if (aBoolean) {
                        Log.e(TAG, "accept: 动态申请 权限回调 true")
                    } else {
                        Log.e(TAG, "accept: 动态申请 权限回调 false")
                        Toast.makeText(this@RootActivity, "未授权应用相关权限，将无法使用相关识别功能！请在设置中打开", Toast.LENGTH_LONG).show()
                    }
                }
    }

    /**
     * 监听返回键事件
     * Note：若不重写此 onBackPressed 函数会导致 supportFragmentManager.beginTransaction().addToBackStack(null) 添加后回退键会回到上一个添加的Fragment
     */
    override fun onBackPressed() { //若当前页面为拍摄页面，监听返回键事件--返回拍摄页面
        super.onBackPressed()
        finish()
    }


    fun initViews() {
        rootBottomNavigation = findViewById(R.id.root_bottom_navigation)
        rootToolbar = findViewById(R.id.root_toolbar)

        // 底部导航添加子项
        rootBottomNavigation?.addItem(rootViewModel?.createBottomNavigationItem(R.string.bottom_navigation_search, R.drawable.root_bottom_search, R.color.bottom_navigation_search))
        rootBottomNavigation?.addItem(rootViewModel?.createBottomNavigationItem(R.string.bottom_navigation_knowledge, R.drawable.root_bottom_knowledge, R.color.bottom_navigation_knowledge))
        rootBottomNavigation?.addItem(rootViewModel?.createBottomNavigationItem(R.string.bottom_navigation_about, R.drawable.root_bottom_about, R.color.bottom_navigation_about))
        rootBottomNavigation?.isColored = true

        // 监控底部导航栏的点击事件
        rootBottomNavigation?.setOnTabSelectedListener { position: Int, wasSelected: Boolean ->
            //当前点击tab与目前所处tab不同才触发Fragment事务
            if(! wasSelected) {
                setFragmentByPosition(position)
                //改变底部导航栏的颜色
                rootViewModel?.changeFragmentTitleBarColor(position)
                rootToolbar?.setBackgroundColor(resources.getColor(rootViewModel?.titlebarColor?.value?.toInt() ?: 0  )  )
            }
            true
        }

    }
    /**
     * 退场动画方法失效 ，原因可能为startActivity的intent的flags设置了 Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
     */
    fun setupWindowAnimations() {
        val fade =  Fade()
        fade.duration = 1500
        getWindow().enterTransition = fade
    }


    override val layoutId: Int
        get() = R.layout.activity_root

    override fun setSelectedBackFragment(fragment: Fragment ?) {
        rootViewModel?.backFragment = fragment
    }

    override fun hideTitleBar() {
        rootToolbar?.visibility = View.GONE
    }

    override fun hideBottomNavigation() {
        rootBottomNavigation?.visibility = View.GONE
    }

    override fun showTitleBar() {
        if(rootToolbar?.visibility == View.GONE ||rootToolbar?.visibility ==  View.INVISIBLE) {
            rootToolbar?.visibility = View.VISIBLE
        }
    }

    override fun showBottomNavigation() {
        if(rootBottomNavigation?.visibility == View.GONE ||rootToolbar?.visibility ==  View.INVISIBLE) {
            rootBottomNavigation?.visibility = View.VISIBLE
        }
    }
}