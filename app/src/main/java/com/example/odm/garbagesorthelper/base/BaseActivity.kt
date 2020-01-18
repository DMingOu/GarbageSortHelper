package com.example.odm.garbagesorthelper.base

import android.app.Activity
import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.IBinder
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
//import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import java.util.*

/**
 * description: Activity基类
 * @author: ODM
 * @date: 2019/9/17
 */
abstract class BaseActivity : AppCompatActivity(), IBaseView {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //锁定旋转屏幕--只能竖屏使用
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    /**
     * 初始化页面的ViewModel和DataBinding
     */
//    abstract fun initViewDataBinding()

    /**
     * 返回页面布局的ID
     *
     * @return the layout id
     */
    abstract val layoutId: Int

    /**
     * 事件分发
     * 子Activity继承此方法就可以点击除输入框其他地方隐藏软键盘
     * @param ev
     * @return
     */
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (ev.action == MotionEvent.ACTION_DOWN) {
            val v = currentFocus
            if (isShouldHideKeyboard(v ?: View(this), ev)) {
                hideKeyboard(this, v?.windowToken)
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    companion object {
        /**
         * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时则不能隐藏
         */
        fun isShouldHideKeyboard(v: View, event: MotionEvent): Boolean {
            if (v is EditText) {
                val l = intArrayOf(0, 0)
                v.getLocationInWindow(l)
                //得到输入框在屏幕中上下左右的位置
                val left = l[0]
                val top = l[1]
                val bottom = top + v.getHeight()
                val right = left + v.getWidth()
                return !(event.x > left && event.x < right && event.y > top && event.y < bottom)
            }
            return false
        }

        /**
         * 获取InputMethodManager，隐藏软键盘
         */
        fun hideKeyboard(activity: Activity, token: IBinder?) {
            if (token != null) {
                val im = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                Objects.requireNonNull(im).hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS)
            }
        }
    }
}