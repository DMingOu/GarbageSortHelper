package com.example.odm.garbagesorthelper.ui.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.odm.garbagesorthelper.R
import com.example.odm.garbagesorthelper.model.SearchDataRepository
import com.example.odm.garbagesorthelper.model.entity.*
import com.example.odm.garbagesorthelper.model.entity.GarbageData.DataBean
import com.example.odm.garbagesorthelper.model.entity.ImageClassifyBean.ResultBean
import com.example.odm.garbagesorthelper.model.source.net.HttpThrowable
import com.example.odm.garbagesorthelper.model.source.net.ObserverManager
import com.example.odm.garbagesorthelper.utils.GsonUtils
import com.iflytek.cloud.*
import com.iflytek.cloud.ui.RecognizerDialog
import com.iflytek.cloud.ui.RecognizerDialogListener
import com.orhanobut.logger.Logger
import io.reactivex.CompletableObserver
import io.reactivex.SingleObserver
import io.reactivex.disposables.Disposable
import java.util.*

/**
 * description: 搜索页面ViewModel
 * author: ODM
 * date: 2019/9/19
 */
class SearchViewModel(
//    public SearchViewModel(Application application) {
//        super(application);
//    }


private val repository: SearchDataRepository) : ViewModel() {
    /**
     * 用户搜索框搜索内容--垃圾名
     */
    var garbageName: LiveData<String> = MutableLiveData()
    /**
     * 开启摄像头的变量
     */
    var isOpenCamera = MutableLiveData(false)
    /**
     * 开启麦克风的控制变量
     */
    var isOpenRecorder = MutableLiveData(false)
    /**
     * 用户搜索结果--分类垃圾列表
     */
    var sortedList = MutableLiveData<List<DataBean>>()
    /*
     * 垃圾分类识别实体类对象
     */
    var imageClassifyGarbage = MutableLiveData<ResultBean>()
    /**
     * 语音识别结果的垃圾名
     */
    var voiceGarbageName = MutableLiveData("")
    var searching = false
    var liveEventTime: Long = 0
    /**
     * 初始化语音听写监听器。
     */
    var mInitListener = InitListener { code ->
        if (code != ErrorCode.SUCCESS) {
            Logger.d("初始化失败，错误码：$code,请点击网址https://www.xfyun.cn/document/error-code查询解决方案")
        }
    }
    /**
     * 初始化听写UI监听器 讯飞，处理数据
     */
    var mRecognizerDialogListener: RecognizerDialogListener = object : RecognizerDialogListener {
        /**
         * 接收语音听写回调信息
         * @param recognizerResult 回调结果
         * @param b 是否翻译
         */
        override fun onResult(recognizerResult: RecognizerResult, b: Boolean) {
            Logger.d(recognizerResult.resultString.toString())
            val data = GsonUtils.GsonToBean(recognizerResult.resultString, VoiceRecognizedData::class.java)
            if ("。" == data?.ws?.get(0)?.cw?.get(0)?.w ?: "。") {
                Logger.d("接收到结果为  。 ,则排除掉它")
            } else {
                searching = true
                voiceGarbageName.setValue(getGarbageFromVoiceRecdata(data ))
            }
        }

        /**
         * 识别回调错误.
         */
        override fun onError(error: SpeechError) {
            if (error.errorCode == 14002) {
                Logger.d(error.getPlainDescription(true) + "\n请确认是否已开通翻译功能")
            } else {
                Logger.d(error.getPlainDescription(true))
            }
        }
    }

    /**
     * 调用垃圾分类查询接口，通过垃圾名 获取垃圾所属分类
     *
     * @param garbageName the garbage name
     */
    fun onSearch(garbageName: String) {
        repository.getGarbageDataResult(garbageName)
                ?.subscribe(object : ObserverManager<GarbageData?>() {
                    override fun onError(httpThrowable: HttpThrowable ) {
                        Logger.d("异常原因:  " + httpThrowable.message)
                    }
                    

                    override fun onNext(t: GarbageData?) {
                        sortedList.value = t?.data
                        findGarbageSearchHistory(garbageName, t!!.data[0].type)
                    }

//                    override fun onNext(garbageData: GarbageData) {
//                        if (garbageData != null) { //将成功查询到的 列表加入 垃圾分类列表中
////                                Logger.d("返回搜索结果   " + garbageData.getData().get(0).getName() );
//                            sortedList.value = garbageData.data
//                            findGarbageSearchHistory(garbageName, garbageData.data[0].type)
//                        }
//                    }
                })
    }

    fun clearResultList() {
        sortedList.value = ArrayList()
        Logger.d("搜索结果列表的大小  " + sortedList.value!!.size)
    }

    /**
     * 不同的垃圾类别(int) 返回不同的垃圾分类图标
     *
     * @param garbageType the garbage type
     * @return the int
     */
    fun getGarbageIcon(garbageType: Int): Int {
        return when (garbageType) {
            0 -> R.drawable.module_search_cookiebar_fail_garbage
            1 -> R.drawable.module_search_cookiebar_dry_garbage
            2 -> R.drawable.module_search_cookiebar_wet_garbage
            3 -> R.drawable.module_search_cookiebar_recycle_garbage
            4 -> R.drawable.module_search_cookiebar_harmful_garbage
            else -> R.drawable.module_search_cookiebar_fail_garbage
        }
    }

    fun openCamera() {
        isOpenCamera.value = true
    }

    /**
     * 调用百度的接口获取图片识别的数据
     *
     * @param imageName the image name
     */
    fun imageClassifyFromBaiDu(imageName: String?) {
        repository.getImageClassifyResult(imageName ?: "")
                ?.subscribe(object : ObserverManager<ImageClassifyBean?>() {
                    override fun onError(httpThrowable: HttpThrowable) {
                        Logger.d(httpThrowable.message)
                    }

                    override fun onNext(t: ImageClassifyBean?) {
                        //非null才赋值
                        t?.result?.get(0)?.let {
                            imageClassifyGarbage.postValue(it)
                            searching = true
                            Logger.d("百度图像分析 " + it)
                        }
                    }
                })
    }

    private fun getGarbageFromVoiceRecdata(recognizedData: VoiceRecognizedData ?): String ?{
        return recognizedData?.ws?.get(0)?.cw?.get(0)?.w
    }

    fun initRecorderDialog(mIatDialog: RecognizerDialog ?) {
        mIatDialog?.setParameter(SpeechConstant.RESULT_TYPE, "json")
        //设置语音输入语言，zh_cn为简体中文
        mIatDialog?.setParameter(SpeechConstant.LANGUAGE, "zh_cn")
        //设置结果返回语言
        mIatDialog?.setParameter(SpeechConstant.ACCENT, "mandarin")
        // 设置语音前端点:静音超时时间，单位ms，即用户多长时间不说话则当做超时处理
//取值范围{1000～10000}
        mIatDialog?.setParameter(SpeechConstant.VAD_BOS, "4500")
        //设置语音后端点:后端点静音检测时间，单位ms，即用户停止说话多长时间内即认为不再输入，
//自动停止录音，范围{0~10000}
        mIatDialog?.setParameter(SpeechConstant.VAD_EOS, "1500")
        //高阶动态修正-->会导致接收多条结果，适合实时显示说话内容，暂不启用此功能
//            mIatDialog.setParameter("dwa", "wpgs");
//开始识别并设置监听器
        mIatDialog?.setListener(mRecognizerDialogListener)
    }

    /*
     * 初始化页面轮播图数据
     */
    val bannerDataList: List<BannerData>
        get() {
            val dataList: MutableList<BannerData> = ArrayList()
            val a = BannerData("https://raw.githubusercontent.com/DMingOu/Markdown-Picture-repository/master/img/20191003225959.png")
            val b = BannerData("https://raw.githubusercontent.com/DMingOu/Markdown-Picture-repository/master/img/20191003230434.png")
            val c = BannerData("https://raw.githubusercontent.com/DMingOu/Markdown-Picture-repository/master/img/20191003230859.png")
            val d = BannerData("https://raw.githubusercontent.com/DMingOu/Markdown-Picture-repository/master/img/20191003232017.png")
            dataList.add(a)
            dataList.add(b)
            dataList.add(c)
            dataList.add(d)
            return dataList
        }

    /**
     * 插入垃圾搜索历史
     * @param garbageName
     * @param garbageType
     */
    fun insertGarbageSearchHistory(garbageName: String, garbageType: Int) {
        repository.insertGarbageHistory(garbageName, garbageType)
                ?.subscribe(object : CompletableObserver {
                    override fun onSubscribe(d: Disposable) {}
                    override fun onComplete() {
                    Log.e(TAG, "垃圾搜索历史插入完成  $garbageName")
                    }

                    override fun onError(e: Throwable) {
                        Logger.d("新数据插入失败原因" + e.message)
                    }
        })
    }

    /**
     * 根据垃圾名称查找出对象
     * @param garbageName
     * @param garbageType
     */
    fun findGarbageSearchHistory(garbageName: String, garbageType: Int ) {
        repository.getGarbageHistoryByName(garbageName)
                ?.subscribe(object : SingleObserver<GarbageSearchHistory?> {
                    override fun onSubscribe(d: Disposable) {}
                    override fun onSuccess(garbageSearchHistory: GarbageSearchHistory) { //有则删除掉
                        repository.deleteGarbageHistory(garbageSearchHistory)
                        Log.e(TAG, "删掉了名为 " + garbageSearchHistory.garbageName + "垃圾")
                        insertGarbageSearchHistory(garbageName, garbageType)
                    }

                    override fun onError(e: Throwable) {
                        Log.e(TAG, "onError: 无法找到Name为" + e.message + " 的垃圾")
                        insertGarbageSearchHistory(garbageName, garbageType)
                    }
                })
    }

    /**
     * 获取Room中所有 垃圾搜索历史
     *
     */
    val allGarbageSearchHistory: Unit
        get() {
            val disposable = repository.allGarbageHistory?.subscribe { garbageSearchHistories ->

                garbageSearchHistories?.let {
                    val stringBuilder = StringBuilder()
                    for (g in it){
                        stringBuilder.append(g?.garbageName)
                        stringBuilder.append("  ")
                    }
                    Log.e(TAG, "数据库展示： $stringBuilder")
                }

            }
        }

    companion object {
        private const val TAG = "SearchViewModel"
    }

    init {
        allGarbageSearchHistory
    }
}

