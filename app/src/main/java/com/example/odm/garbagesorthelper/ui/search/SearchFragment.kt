package com.example.odm.garbagesorthelper.ui.search

import android.Manifest
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.odm.garbagesorthelper.R
import com.example.odm.garbagesorthelper.application.GarbageSortApplication
import com.example.odm.garbagesorthelper.base.BaseFragment
import com.example.odm.garbagesorthelper.core.Constants
//import com.example.odm.garbagesorthelper.databinding.FragmentSearchBinding
import com.example.odm.garbagesorthelper.model.entity.BannerData
import com.example.odm.garbagesorthelper.model.entity.GarbageData.DataBean
import com.example.odm.garbagesorthelper.model.entity.ImageClassifyBean.ResultBean
import com.example.odm.garbagesorthelper.ui.Camera.CameraActivity
import com.example.odm.garbagesorthelper.ui.RootActivity
import com.example.odm.garbagesorthelper.ui.search.SearchFragment
import com.example.odm.garbagesorthelper.utils.InjectorUtils
import com.example.odm.garbagesorthelper.widget.ClearEditText
import com.iflytek.cloud.ui.RecognizerDialog
import com.jeremyliao.liveeventbus.LiveEventBus
import com.stx.xhb.androidx.XBanner
import com.tbruyelle.rxpermissions2.RxPermissions
import com.xuexiang.xui.widget.button.ButtonView
import com.xuexiang.xui.widget.button.shadowbutton.ShadowButton
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog
import com.xuexiang.xui.widget.popupwindow.bar.CookieBar

/**
 * description: 搜索页面View层
 * author: ODM
 * date: 2019/9/18
 */
class SearchFragment : BaseFragment() {
//    private var mBinding: FragmentSearchBinding? = null
    private var searchViewModel: SearchViewModel? = null
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
        return layoutInflater.inflate(R.layout.fragment_search , container ,false)
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

    fun initViews() {
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
                        searchViewModel?.isOpenRecorder?.value = true
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
            searchViewModel?.openCamera()
            }
        }

    override fun initViewDataBinding(inflater: LayoutInflater, container: ViewGroup?) { //        searchViewModel = ViewModelProviders.of(this).get(SearchViewModel.class);
        searchViewModel = InjectorUtils.provideSearchViewModelFactory(requireContext()).create(SearchViewModel::class.java)
    }



    override val layoutId: Int
        get() = R.layout.fragment_search

    private fun initEditText() {
        etSearch.setOnEditorActionListener { v: TextView, actionId: Int, event: KeyEvent? ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH && "" != searchViewModel?.garbageName.toString()) { //搜索内容非空且点击了搜索键后收起软键盘
                val manager = GarbageSortApplication.context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                manager.hideSoftInputFromWindow(v.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
                //触发软键盘的点击事件
                searchViewModel?.onSearch(etSearch.text.toString())
                //搜索状态置为 true
                searchViewModel?.searching = true
                showLoadingDialog()
                //点击键盘的搜索键后，清空内容，放弃焦点
                etSearch.clearFocus()
                etSearch.setText("")

            }
            true
        }
    }

    private fun initRecorderDialog() {
        // 初始化听写Dialog，如果只使用有UI听写功能，无需创建SpeechRecognizer
        mIatDialog = RecognizerDialog(activity, searchViewModel?.mInitListener)
        searchViewModel?.initRecorderDialog(mIatDialog)
    }

    /**
     * 初始化展示轮播图Banner
     */
    private fun initBanner() {
        banner.setBannerData(searchViewModel!!.bannerDataList)
        banner.loadImage { banner: XBanner?, model: Any, view: View?, position: Int ->
            Glide.with(this@SearchFragment)
                    .load((model as BannerData).xBannerUrl)
                    .placeholder(R.drawable.module_glide_load_default_image)
                    .error(R.drawable.error_image)
                    .into((view as ImageView))
        }
    }

    /**
     * 观察viewModel的数据变动--改变view层的UI
     */
    private fun initDataObserve() { //展示查询分类结果，同时取消Loading对话框的显示
        searchViewModel?.sortedList?.observe(this, Observer { dataBeans: List<DataBean?>? ->
            if (dataBeans?.isEmpty() == false) {
                if (searchViewModel?.searching ?: false) {
                    searchViewModel?.searching = false
                    showGarbageResultBar()
                }
            }
        })
        //跳转到拍摄页面
        searchViewModel?.isOpenCamera?.observe(this, Observer { isOpenCamera: Boolean ->
            if (isOpenCamera) {
//                val rootActivity = activity as RootActivity
                val rxPermissions = RxPermissions(activity?: RootActivity())
                rxPermissions.request(Manifest.permission.CAMERA).subscribe {
                    if (it) {
                        if (rxPermissions.isGranted(Manifest.permission.CAMERA) && rxPermissions.isGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                && rxPermissions.isGranted(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                            //跳转到拍摄页面
/*                            val cameraFragment  = CameraFragment()
                            fragmentManager?.beginTransaction()
                                    ?.setCustomAnimations(R.anim.push_up_in ,R.anim.push_down_out)
                                    ?.add(R.id.rl_root, cameraFragment, "CameraFragment")
                                    ?.commitAllowingStateLoss()*/

                            val sharedView = btnOpenCamera;
                            val transitionName = getString(R.string.share_view_button_camera);

                            val transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(activity, sharedView, transitionName);
                            startActivity(Intent(this.context,CameraActivity::class.java), transitionActivityOptions.toBundle());
//                            startActivity(Intent(this.context,CameraActivity::class.java))
                            searchViewModel?.isOpenCamera?.setValue(false)
                        } else {
                            Toast.makeText(activity?.applicationContext, "未获取相关权限，无法开启拍照识别！请在应用管理中打开", Toast.LENGTH_LONG).show()
                        }
                    } else {
                        Toast.makeText(activity?.applicationContext, "未获取相关权限，无法开启拍照识别！", Toast.LENGTH_LONG).show()
                    }
                }
            }
        })
        //监控将从百度图像识别获取到物体关键词，调用垃圾分类API，显示结果
        searchViewModel?.imageClassifyGarbage?.observe(this, Observer { bean: ResultBean ->
            val keyGarbageName = bean.keyword
            if (searchViewModel?.searching ?: false) {
                searchViewModel?.onSearch(keyGarbageName ?: "")
            }
        })


        /*
         * 观察语音识别的结果，调用垃圾分类搜索接口
         */
         searchViewModel?.voiceGarbageName?.observe(this, Observer { garbageName: String ->
            if ("" != garbageName) {
                //showLoadingDialog();
                if (searchViewModel?.searching ?: false) {
                    searchViewModel?.onSearch(garbageName)
                }
            } else {
                //开启语音识别后，若无法检测用户语音内容，会弹出Toast提醒
                if (searchViewModel?.isOpenRecorder?.value ?: false) {
                    Toast.makeText(activity?.applicationContext, "无法识别您说的内容", Toast.LENGTH_SHORT).show()
                }
            }
        })
        searchViewModel?.isOpenRecorder?.value = false
    }

    /**
     * 处理 LiveEvent 事件
     */
    private fun handleLiveEvent() {
        //Todo 暂时未解决LiveEventBus 发送一次事件却重复接收到相同事件的bug ，故暂时采用时间差方式
        val currentTime = System.currentTimeMillis()
        val liveEventTime = searchViewModel?.liveEventTime ?: 0L
        if ( (currentTime - liveEventTime)    > 100000) {
            LiveEventBus
                    .get(Constants.IMAGE_SUCCESS, String::class.java)
                    .observe(this, Observer { imageName: String? ->
                        searchViewModel?.liveEventTime = currentTime
                        //成功保存了拍摄照片，开启Loading对话框，调用百度识图接口查询（耗时）
                        showLoadingDialog()
                        searchViewModel?.imageClassifyFromBaiDu(imageName)
                    })
        }
    }


    /**
     * 弹出Bar提示--用户搜索结果
     */
    private fun showGarbageResultBar() {
        CookieBar.builder(activity)
                .setTitle(searchViewModel?.sortedList?.value?.get(0)?.category)
                .setIcon(searchViewModel?.getGarbageIcon(searchViewModel?.sortedList?.value?.get(0)?.type ?: 0 ) ?: 0)
                .setMessage(searchViewModel?.sortedList?.value?.get(0)?.name + "\n" + searchViewModel?.sortedList?.value?.get(0)?.remark)
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