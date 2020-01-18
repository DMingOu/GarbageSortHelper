package com.example.odm.garbagesorthelper.ui.about

import android.app.Application
import android.os.Environment
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.CacheDiskStaticUtils
import com.blankj.utilcode.util.FileUtils
import com.bumptech.glide.Glide
import com.example.odm.garbagesorthelper.base.BaseModel
import com.example.odm.garbagesorthelper.base.BaseViewModel
import com.example.odm.garbagesorthelper.model.entity.ProvinceInfo
import com.example.odm.garbagesorthelper.utils.FileUtil
import com.example.odm.garbagesorthelper.utils.GsonUtils
import com.example.odm.garbagesorthelper.utils.ResourceUtil
import com.example.odm.garbagesorthelper.utils.SharePreferencesUtil
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.orhanobut.logger.Logger
import com.tencent.bugly.beta.Beta
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.Scheduler
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.io.File
import java.util.*
import kotlin.collections.ArrayList


/**
 * description: 关于页面ViewModel
 * author: ODM
 * date: 2019/9/19
 */
class AboutViewModel(application: Application?) : BaseViewModel<BaseModel?>(application!!) {
     var versionName = MutableLiveData<String>()

    var versionUpdateInfo : String

    //Note：不初始化MutableList，就算后面add ，也始终会是null
     var options1Items: MutableList<String> = ArrayList()
     var options2Items: MutableList<MutableList<String>> = ArrayList()
     var options3Items: MutableList<MutableList<MutableList<String>>> = ArrayList()

     var mHasLoaded = false

    init {
        val data = ResourceUtil.readAssets2String("province.json")
        val type = object : TypeToken<List<ProvinceInfo>>() {}.type
        loadData(Gson().fromJson<List<ProvinceInfo>>(data ,  type))
        if(SharePreferencesUtil.getInstance().getString("address_picked") == "") {
            SharePreferencesUtil.getInstance().put("address_picked" , "未选择地址")
        }
        versionName.value = version

        /***** 获取升级信息 *****/
        val upgradeInfo  = Beta.getUpgradeInfo()

        if (upgradeInfo == null) {
            versionUpdateInfo = "已是最新版本"
        } else {
            versionUpdateInfo = upgradeInfo.versionName+"."+upgradeInfo.versionCode + " 有更新"
        }
    }


    //加载数据
    private fun loadData(provinceInfo: List<ProvinceInfo>) {

        //遍历省份（第一级）
        for (province in provinceInfo) {
            //该省的城市列表（第二级）
            val cityList: MutableList<String> = ArrayList()
            //该省的所有地区列表（第三级）
            val areaList: MutableList<MutableList<String>> = ArrayList()
            for (city in province.city) {
                //添加城市
                val cityName: String = city.name
                cityList.add(cityName)
                //该城市的所有地区列表
                val cityAreaList: MutableList<String> = ArrayList()
                //如果无地区数据，建议添加空字符串，防止数据为null 导致三个选项长度不匹配造成崩溃
                if (city.area == null || city.area.size === 0) {
                    cityAreaList.add("")
                } else {
                    cityAreaList.addAll(city.area)
                }
                //添加该省所有地区数据
                areaList.add(cityAreaList)
            }
            options1Items.add(province.name)
            //添加城市数据
            options2Items.add(cityList)
            //添加地区数据
            options3Items.add(areaList)
        }

        mHasLoaded = true
    }

    fun retainAddressPickedValue(value : String){
        SharePreferencesUtil.getInstance().put("address_picked" , value)
    }

    fun getRetainAddressValue() : String {
        return  SharePreferencesUtil.getInstance().getString("address_picked" , "未选择区域")
    }


    /**
     * 获取版本号
     * @return 当前应用的版本号
     */
    val version: String
        get() = try {
            val manager = getApplication<Application>().packageManager
            val info = manager.getPackageInfo(getApplication<Application>().packageName, 0)
            val version = info.versionName
            val versionCode = info.versionCode
            "version  ${version}.${versionCode}"
        } catch (e: Exception) {
            e.printStackTrace()
            "无法获取当前版本"
        }

    /**
     * 清除了Glide的缓存 和 拍摄照片的保存
     */
    fun cleanCache (glide : Glide) {
        Observable.create<Boolean> {
            FileUtils.deleteFilesInDir(File(Environment.getExternalStorageDirectory().toString() + "/GarbageSortHelper/ImageCache/"))
            glide.clearDiskCache()
            glide.clearMemory()
        }   .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {}
            .isDisposed
    }

    fun getUpdateInfo() : String {
        /***** 获取升级信息 *****/
        val upgradeInfo  = Beta.getUpgradeInfo()

        if (upgradeInfo == null) {
            versionUpdateInfo = "已是最新版本"
        } else {
            versionUpdateInfo = upgradeInfo.versionName+"."+upgradeInfo.versionCode + " 有更新"
        }
        return  versionUpdateInfo
    }

}