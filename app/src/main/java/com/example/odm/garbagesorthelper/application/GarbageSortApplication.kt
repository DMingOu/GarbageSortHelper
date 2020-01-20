package com.example.odm.garbagesorthelper.application

import android.app.Application
import android.content.Context
import android.os.Environment
import android.util.Log
import cat.ereza.customactivityoncrash.config.CaocConfig
import com.example.odm.garbagesorthelper.R
import com.example.odm.garbagesorthelper.ui.root.RootActivity
import com.iflytek.cloud.SpeechConstant
import com.iflytek.cloud.SpeechUtility
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.tencent.bugly.Bugly
import com.tencent.bugly.beta.Beta
import com.xuexiang.xui.XUI
import io.reactivex.plugins.RxJavaPlugins


/**
 * description: Application类
 * @author: ODM
 * @date: 2019/9/18
 */
class GarbageSortApplication : Application() {


    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        initBuglySDK()
        initLogger()
        initCrashPage()
        initXUIFramework()
        initXunFeiRecord()
        initRxJavaOnErrorHandle()
    }

    private fun initLogger() {
        Logger.addLogAdapter(AndroidLogAdapter())
    }

    //让用户使用时不直接闪退，而是跳到一个Activity，可以重启应用，但无法阻止或保存或上传闪退信息
    private fun initCrashPage() {
        CaocConfig.Builder.create()
                .backgroundMode(CaocConfig.BACKGROUND_MODE_CRASH)
                .enabled(true)
                .showErrorDetails(true)
                .showRestartButton(true)
                .logErrorOnRestart(true)
                .trackActivities(true)
                .minTimeBetweenCrashesMs(2000)
                .errorDrawable(R.drawable.customactivityoncrash_error_image)
                .restartActivity(RootActivity::class.java)
                .errorActivity(null)
                .eventListener(null)
                .apply()
    }

    /**
     * 初始化UI框架
     * XUI
     */
    private fun initXUIFramework() { //初始化UI框架
        XUI.init(this)
        XUI.debug(true)
    }

    /**
     * 初始化讯飞语音识别框架
     */
    private fun initXunFeiRecord() {
        SpeechUtility.createUtility(this, SpeechConstant.APPID + "=5d92eae0")
    }

    private fun initBuglySDK(){

        Beta.initDelay = 0
        /*
         * 设置状态栏小图标，smallIconId为项目中的图片资源Id;
         */
        Beta.smallIconId = R.drawable.icon_garbagesort_app
       /*
         * 设置sd卡的Download为更新资源保存目录;
         * 后续更新资源会保存在此目录，需要在manifest中添加WRITE_EXTERNAL_STORAGE权限;
         */
        Beta.storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        /**
         * 只允许在MainActivity上显示更新弹窗，其他activity上不显示弹窗;
         * 不设置会默认所有activity都可以显示弹窗;
         */
        /**
         * 只允许在MainActivity上显示更新弹窗，其他activity上不显示弹窗;
         * 不设置会默认所有activity都可以显示弹窗;
         */
        Beta.canShowUpgradeActs.add(RootActivity::class.java)
        /*
         * true表示初始化时自动检查升级;
         * false表示不会自动检查升级,需要手动调用Beta.checkUpgrade()方法;
         */
        Beta.autoCheckUpgrade = true
        /*
         * true表示app启动自动初始化升级模块;
         * false不会自动初始化;
         * 开发者如果担心sdk初始化影响app启动速度，可以设置为false，
         * 在后面某个时刻手动调用Beta.init(getApplicationContext(),false);
         */
        Beta.enableHotfix = false

        //统一初始化Bugly包括Beta
        Bugly.init(context, "1e7ded2891", false)

    }

    /**
     * 全局捕获RxJava异常
     */
    private fun initRxJavaOnErrorHandle() {
        RxJavaPlugins.setErrorHandler {

            Log.e("RxJavaException" , it.localizedMessage ?: "")
        }
    }

    companion object {
        /**
         * 获取Context上下文
         *
         * @return the context
         */
        var context: Context? = null
            private set
    }


}