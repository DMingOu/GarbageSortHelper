package com.example.odm.garbagesorthelper.ui.home

import android.Manifest
import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.odm.garbagesorthelper.R
import com.example.odm.garbagesorthelper.base.BaseFragment
import com.example.odm.garbagesorthelper.core.Constants
import com.example.odm.garbagesorthelper.model.entity.BannerData
import com.example.odm.garbagesorthelper.model.entity.GarbageData.DataBean
import com.example.odm.garbagesorthelper.model.entity.ImageClassifyBean.ResultBean
import com.example.odm.garbagesorthelper.ui.camera.CameraActivity
import com.example.odm.garbagesorthelper.ui.root.RootActivity
import com.example.odm.garbagesorthelper.ui.search.SearchActivity
import com.example.odm.garbagesorthelper.utils.InjectorUtils
import com.example.odm.garbagesorthelper.widget.ClearEditText
import com.iflytek.cloud.ui.RecognizerDialog
import com.jeremyliao.liveeventbus.LiveEventBus
import com.orhanobut.logger.Logger
import com.stx.xhb.androidx.XBanner
import com.tbruyelle.rxpermissions2.RxPermissions
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog
import com.xuexiang.xui.widget.popupwindow.bar.CookieBar

/**
 * description: 搜索页面View层
 * author: ODM
 * date: 2019/9/18
 */
class HomeFragment : BaseFragment() {
    private var viewModel: HomeViewModel? = null
    private var loadingDialog: MaterialDialog ?= null
    private var mIatDialog: RecognizerDialog ?= null

    //控件变量
    lateinit var etSearch : ClearEditText
    lateinit var banner : XBanner
    lateinit var btnOpenRecorder : Button
    lateinit var btnOpenCamera : Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initViewDataBinding(inflater, container)

        //        首先通过DataBindingUtil.inflate初始化binding对象，然后通过.getRoot()获取操作视图，并且在onCreateView中返回该视图。否则会导致binding不生效。
        return layoutInflater.inflate(R.layout.fragment_home , container ,false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initViews()
        initEditText()
        initRecorderDialog()
        initDataObserve()
        initBanner()
        handleLiveEvent()
    }

    override fun onDestroy() {
        super.onDestroy()
        mIatDialog?.destroy()
    }

      override fun initViews() {
        etSearch = activity?.findViewById(R.id.etSearch) ?: ClearEditText(activity)
        banner = activity?.findViewById(R.id.banner) ?: XBanner(activity)
        btnOpenCamera = activity?.findViewById(R.id.btnOpenCamera) ?: Button(activity)
        btnOpenRecorder = activity?.findViewById(R.id.btnOpenRecorder) ?: Button(activity)

        /*
         * 语音按钮的点击事件
         * 显示语音框--开启语音识别功能
         */
    btnOpenRecorder.setOnClickListener { v: View? ->

        val rxPermissions = RxPermissions(activity?: RootActivity())
        rxPermissions.request(Manifest.permission.RECORD_AUDIO).subscribe {
            if (it) {
                    if (rxPermissions.isGranted(Manifest.permission.RECORD_AUDIO) && rxPermissions.isGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            && rxPermissions.isGranted(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        mIatDialog?.show()
                        viewModel?.isOpenRecorder?.value = true
                        //动态更换了讯飞自带对话框的底部文字，必须在dialog的show执行后更换，否则空指针报错
                        val recorderDialogTextView = mIatDialog?.window?.decorView?.findViewWithTag<View>("textlink") as TextView
                        recorderDialogTextView.setText(R.string.recorder_dialog_textview_text)
                    }
                } else {
                    Toast.makeText(activity?.applicationContext, "未获取相关权限，无法开启语音识别！", Toast.LENGTH_LONG).show()
                 }
              }
           }

        /**
         * 拍照按钮的点击事件
         * 设置跳转拍摄开关为true->跳转拍摄页面
         */
        btnOpenCamera.setOnClickListener {
//            viewModel?.openCamera()
           val rxPermissions = RxPermissions(activity?: RootActivity())

                        if ( rxPermissions.isGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                && rxPermissions.isGranted(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                            Logger.d("跳转到拍摄页面")
                            val sharedView = btnOpenCamera
                            val transitionName = getString(R.string.share_view_button_camera)

                            val transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(activity, sharedView, transitionName)
                            startActivity(Intent(this.context,CameraActivity::class.java), transitionActivityOptions.toBundle())
//                            viewModel?.isOpenCamera?.setValue(false)
                        } else {
                            Toast.makeText(activity?.applicationContext, "未获取相关权限，无法开启拍照识别！请在应用管理中打开", Toast.LENGTH_LONG).show()
                        }
                }
        }

     fun initViewDataBinding(inflater: LayoutInflater, container: ViewGroup?) {
        viewModel = InjectorUtils.provideHomeViewModelFactory(requireContext()).create(HomeViewModel::class.java)
    }



    override val layoutId: Int
        get() = R.layout.fragment_home

    private fun initEditText() {
        //点击搜索框跳转 SearchActivity 事件
        etSearch.setOnClickListener {
            val sharedView = it;
            val transitionName = getString(R.string.share_view_edittext_search);
            val transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(activity, sharedView, transitionName);
            startActivity(Intent(this.context, SearchActivity::class.java), transitionActivityOptions.toBundle());
        }
    }

    private fun initRecorderDialog() {
        // 初始化听写Dialog，如果只使用有UI听写功能，无需创建SpeechRecognizer
        mIatDialog = RecognizerDialog(activity, viewModel?.mInitListener)
        viewModel?.initRecorderDialog(mIatDialog)
    }

    /**
     * 初始化展示轮播图Banner
     */
    private fun initBanner() {
        banner.setBannerData(viewModel!!.bannerDataList)
        banner.loadImage { banner: XBanner?, model: Any, view: View?, position: Int ->
            Glide.with(this@HomeFragment)
                    .load((model as BannerData).xBannerUrl)
                    .placeholder(R.drawable.module_glide_load_default_image)
                    .error(R.drawable.banner_error_load)
                    .into((view as ImageView))
        }
    }

    /**
     * 观察viewModel的数据变动--改变view层的UI
     */
    private fun initDataObserve() { //展示查询分类结果，同时取消Loading对话框的显示
        viewModel?.sortedList?.observe(this, Observer { dataBeans: List<DataBean?>? ->
            if (dataBeans?.isEmpty() == false) {
                if (viewModel?.searching ?: false) {
                    viewModel?.searching = false
                    showGarbageResultBar()
                }
            }
        })

        //监控将从百度图像识别获取到物体关键词，调用垃圾分类API，显示结果
        viewModel?.imageClassifyGarbage?.observe(this, Observer { bean: ResultBean ->
            val keyGarbageName = bean.keyword
            if (viewModel?.searching ?: false) {
                viewModel?.onSearch(keyGarbageName)
            }
        })


        /*
         * 观察语音识别的结果，调用垃圾分类搜索接口
         */
         viewModel?.voiceGarbageName?.observe(this, Observer { garbageName: String ->
            if ("" != garbageName) {
                //showLoadingDialog();
                if (viewModel?.searching ?: false) {
                    viewModel?.onSearch(garbageName)
                }
            } else {
                //开启语音识别后，若无法检测用户语音内容，会弹出Toast提醒
                if (viewModel?.isOpenRecorder?.value ?: false) {
                    Toast.makeText(activity?.applicationContext, "无法识别您说的内容", Toast.LENGTH_SHORT).show()
                }
            }
        })
        viewModel?.isOpenRecorder?.value = false
    }

    /**
     * 处理 LiveEvent 事件
     */
    private fun handleLiveEvent() {
        //Todo 暂时未解决LiveEventBus 发送一次事件却重复接收到相同事件的bug ，故暂时采用时间差方式
        val currentTime = System.currentTimeMillis()
        val liveEventTime = viewModel?.liveEventTime ?: 0L
        if ( (currentTime - liveEventTime)    > 100000) {
            LiveEventBus
                    .get(Constants.IMAGE_SUCCESS, String::class.java)
                    .observe(this, Observer { imageName: String? ->
                        viewModel?.liveEventTime = currentTime
                        //成功保存了拍摄照片，开启Loading对话框，调用百度识图接口查询（耗时）
                        showLoadingDialog()
                        viewModel?.imageClassifyFromBaiDu(imageName)
                    })
        }
    }


    /**
     * 弹出Bar提示--用户搜索结果
     */
    private fun showGarbageResultBar() {
        CookieBar.builder(activity)
                .setTitle(viewModel?.sortedList?.value?.get(0)?.category)
                .setIcon(viewModel?.getGarbageIcon(viewModel?.sortedList?.value?.get(0)?.type ?: 0 ) ?: 0)
                .setMessage(viewModel?.sortedList?.value?.get(0)?.name + "\n" + viewModel?.sortedList?.value?.get(0)?.remark)
                .setLayoutGravity(Gravity.BOTTOM)
                .setAction(R.string.known, null)
                .setDuration(4000)
                .show()
        cancelLoadingDialog()
    }

    /**
     * 弹出Loading对话框，提示用户等待
     */
    private fun showLoadingDialog() {
        if (loadingDialog == null) {
            loadingDialog = MaterialDialog.Builder(context!!)
                    .limitIconToDefaultSize()
                    .title(R.string.tips)
                    .content(R.string.content_wait_for_receive_data)
                    .progress(true, 0)
                    .progressIndeterminateStyle(false)
                    .show()
        } else {
            loadingDialog?.show()
        }
    }

    /**
     * 取消Loading对话框的显示
     */
    private fun cancelLoadingDialog() {
            loadingDialog?.dismiss()
    }

    companion object {
        private const val TAG = "SearchFragment"
    }
}