package com.example.odm.garbagesorthelper.ui.about

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.odm.garbagesorthelper.base.BaseModel
import com.example.odm.garbagesorthelper.base.BaseViewModel

/**
 * description: 关于页面ViewModel
 * author: ODM
 * date: 2019/9/19
 */
class AboutViewModel(application: Application?) : BaseViewModel<BaseModel?>(application!!) {
    public var versionName = MutableLiveData<String>()
    /**
     * 获取版本号
     * @return 当前应用的版本号
     */
    val version: String
        get() = try {
            val manager = getApplication<Application>().packageManager
            val info = manager.getPackageInfo(getApplication<Application>().packageName, 0)
            val version = info.versionName
            "version  $version"
        } catch (e: Exception) {
            e.printStackTrace()
            "无法得到当前版本"
        }
}