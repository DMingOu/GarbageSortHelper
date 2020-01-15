package com.example.odm.garbagesorthelper.application

import android.app.Application
import android.content.Context
import cat.ereza.customactivityoncrash.config.CaocConfig
import com.example.odm.garbagesorthelper.R
import com.example.odm.garbagesorthelper.ui.root.RootActivity
import com.iflytek.cloud.SpeechConstant
import com.iflytek.cloud.SpeechUtility
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
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

    private fun initRxJavaOnErrorHandle() {
        RxJavaPlugins.setErrorHandler {
            //Toast.makeText(this , it.message ,Toast.LENGTH_SHORT).show()
            Logger.d(it.localizedMessage?:"")
        }
    }

    companion object {
        /**
         * Get context context.
         *
         * @return the context
         */
        var context: Context? = null
            private set
    }


}