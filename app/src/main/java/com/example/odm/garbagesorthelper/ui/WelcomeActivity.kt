package com.example.odm.garbagesorthelper.ui

import android.Manifest
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.odm.garbagesorthelper.R
import com.orhanobut.logger.Logger
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.Observable
import io.reactivex.functions.Consumer
import site.gemus.openingstartanimation.LineDrawStrategy
import site.gemus.openingstartanimation.OpeningStartAnimation
import site.gemus.openingstartanimation.RedYellowBlueDrawStrategy

import java.util.*
import java.util.concurrent.TimeUnit


class WelcomeActivity : AppCompatActivity() {

    companion object{
        val TAG = "WelcomeActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
        showWelcomePage()
    }

    override fun onStart() {
        super.onStart()

    }

    fun showWelcomePage() {

        val openingStartAnimation = OpeningStartAnimation.Builder(this)
                .setAppIcon(getDrawable(R.drawable.icon_garbagesort_app)) //设置图标
                .setColorOfAppIcon(Color.GREEN) //设置绘制图标线条的颜色
                .setAppName("垃圾分类小助手") //设置app名称
                .setColorOfAppName(Color.BLUE) //设置app名称颜色
                .setAppStatement("让垃圾分类也可以轻松愉快") //设置一句话描述
                .setColorOfAppStatement(Color.BLACK) // 设置一句话描述的颜色
                .setAnimationInterval(2500) // 设置动画时间间隔
                .setDrawStategy(RedYellowBlueDrawStrategy())
                .setAnimationFinishTime(3000) // 设置动画的消失时长
                .create()
        Logger.d("展示开屏动画")
        Observable.timer(2600, TimeUnit.MILLISECONDS).subscribe {
            val intent = Intent()
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            intent.setClass(this,RootActivity::class.java)
            Logger.d("进入主页面")
            startActivity(intent)
        }.isDisposed
//        Observable.timer(2000, TimeUnit.MILLISECONDS)
//                .subscribe(Consumer {
//                    initUserPermissions()
//                }).isDisposed


//        val task: TimerTask = object : TimerTask() {
//            override fun run() { //要执行的操作
//                initUserPermissions()
//            }
//        }
//        val timer = Timer()
//        timer.schedule(task, 2000) //2秒后执行TimeTask的run方法

        openingStartAnimation.show(this)
    }




}
