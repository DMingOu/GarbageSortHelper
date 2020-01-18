package com.example.odm.garbagesorthelper.ui.welcome

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.transition.Slide
import com.example.odm.garbagesorthelper.R
import com.example.odm.garbagesorthelper.base.BaseActivity
import com.example.odm.garbagesorthelper.ui.root.RootActivity
import com.example.odm.garbagesorthelper.utils.SharePreferencesUtil
import com.orhanobut.logger.Logger
import com.xuexiang.xui.utils.StatusBarUtils
import io.reactivex.Observable
import site.gemus.openingstartanimation.OpeningStartAnimation
import site.gemus.openingstartanimation.RedYellowBlueDrawStrategy

import java.util.concurrent.TimeUnit
import kotlin.system.exitProcess


class WelcomeActivity : BaseActivity() {

    companion object{
        val TAG = "WelcomeActivity"
    }
    //控制 当前时刻是否进入 首页
    var isBoost : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //沉浸式状态栏
        StatusBarUtils.translucent(this)
        setContentView(layoutId)
//        setupWindowAnimations()
        showWelcomePage()
    }

    override fun onRestart() {
        super.onRestart()
        Logger.d("onRestart ")
        if(isBoost) {
            enterHomePage()
        }
    }

    override val layoutId: Int
        get() = R.layout.activity_welcome

    private fun showWelcomePage() {
        if(SharePreferencesUtil.getInstance().contains("isSkipWelcomeAnimation")
                && SharePreferencesUtil.getInstance().getBoolean("isSkipWelcomeAnimation")) {
            //直接进入页面
            enterHomePage()
            isBoost = true
        }else {
            //初始化 欢迎页动画
            val openingStartAnimation = OpeningStartAnimation.Builder(this)
                    .setAppIcon(getDrawable(R.drawable.icon_garbagesort_app)) //设置图标
                    .setColorOfAppIcon( Color.GREEN) //设置绘制图标线条的颜色
                    .setAppName("垃圾分类小助手") //设置app名称
                    .setColorOfAppName(R.color.colorPrimary) //设置app名称颜色
                    .setAppStatement("让垃圾分类也能轻松愉快") //设置一句话描述
                    .setColorOfAppStatement(Color.BLACK) // 设置一句话描述的颜色
                    .setAnimationInterval(2500) // 设置动画时间间隔
                    .setDrawStategy(RedYellowBlueDrawStrategy())
                    .setAnimationFinishTime(2500) // 设置动画的消失时长
                    .create()
            //设置延时任务 2600ms 后 跳转到 首页
            Observable.timer(2600, TimeUnit.MILLISECONDS).subscribe {
                isBoost = true
                enterHomePage()
            }.isDisposed
            //启动 欢迎页动画
            openingStartAnimation.show(this)
        }
    }

    private fun enterHomePage() {
        val intent = Intent()
        //必须同时设置这两个 Flag 才可以生效返回不进入 欢迎页
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
//            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.setClass(this, RootActivity::class.java)
        startActivity(intent)
        //设置 转场到 主活动 的动画为 淡入淡出
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        exitProcess(0)
    }

    /**
     * 转场动画方法失效 ，原因可能为 intent的flags设置了 Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
     */
    private fun setupWindowAnimations() {
        val slide =  Slide()
        slide.duration = 1500
        window.exitTransition = slide
    }



}
