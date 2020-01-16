package com.example.odm.garbagesorthelper.ui.about

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.odm.garbagesorthelper.base.BaseModel
import com.example.odm.garbagesorthelper.base.BaseViewModel
import com.example.odm.garbagesorthelper.model.entity.ProvinceInfo


/**
 * description: 关于页面ViewModel
 * author: ODM
 * date: 2019/9/19
 */
class AboutViewModel(application: Application?) : BaseViewModel<BaseModel?>(application!!) {
    public var versionName = MutableLiveData<String>()



     var options1Items: MutableList<ProvinceInfo> ?= null
     var options2Items: MutableList<List<String>> ?= null
     var options3Items: MutableList<List<List<String>>> ?= null

     var mHasLoaded = false

    init {

    }

    private fun loadData(provinceInfo: List<ProvinceInfo>) { //加载数据
        /**
         * 添加省份数据
         */
        options1Items = provinceInfo.toMutableList()
        //遍历省份（第一级）
        for (info in provinceInfo) { //该省的城市列表（第二级）
            val cityList: MutableList<String> = ArrayList()
            //该省的所有地区列表（第三级）
            val areaList: MutableList<List<String>> = ArrayList()
            for (city in info.city) { //添加城市
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
            /**
             * 添加城市数据
             */
            options2Items?.add(cityList)
            /**
             * 添加地区数据
             */
            options3Items?.add(areaList)
        }

        mHasLoaded = true
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
            "version  $version"
        } catch (e: Exception) {
            e.printStackTrace()
            "无法得到当前版本"
        }
}