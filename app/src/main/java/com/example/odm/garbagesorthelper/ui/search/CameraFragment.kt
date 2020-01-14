package com.example.odm.garbagesorthelper.ui.search

import android.annotation.SuppressLint
import android.graphics.Matrix
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.camera.core.*
import androidx.camera.core.ImageCapture.ImageCaptureError
import androidx.camera.core.Preview.OnPreviewOutputUpdateListener
import androidx.camera.view.TextureViewMeteringPointFactory
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProviders
//import com.example.odm.garbagesorthelper.BR
import com.example.odm.garbagesorthelper.R
import com.example.odm.garbagesorthelper.base.BaseFragment
import com.example.odm.garbagesorthelper.base.IBackInterface
import com.example.odm.garbagesorthelper.base.IHideInterface
import com.example.odm.garbagesorthelper.core.Constants
import com.example.odm.garbagesorthelper.ui.Camera.CameraViewModel
import com.example.odm.garbagesorthelper.widget.FocusCircleView
import com.jeremyliao.liveeventbus.LiveEventBus
import com.orhanobut.logger.Logger
import com.xuexiang.xui.widget.button.shadowbutton.ShadowButton
import java.io.File

/**
 * description: 拍摄界面V层
 * author: ODM
 * date: 2019/9/25
 */
class CameraFragment : BaseFragment() {
    private var cameraViewModel: CameraViewModel? = null

    //控件变量
    private var btnCapture : ShadowButton ?= null
    private var containerCamera :   TextureView  ?=  null
    private var focusCircle : FocusCircleView ?= null

    private var hideInterface : IHideInterface ?= null

    private var preViewOutput : Preview.PreviewOutput ?= null

    companion object {
        private const val TAG = "CameraFragment"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initViewDataBinding(inflater, container)

        val backInterface: IBackInterface?
        backInterface = if (activity !is IBackInterface) {
            throw ClassCastException("活动Activity 必须继承 BackHandledInterface")
        } else {
            activity as IBackInterface?
        }
        backInterface?.setSelectedBackFragment(this)

        hideInterface = if (activity !is IHideInterface) {
            throw ClassCastException("活动Activity 必须继承 IHideInterface")
        } else {
            activity as IHideInterface
        }

        return inflater.inflate(R.layout.fragment_camera ,container ,false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initViews()
        initCamera()
    }

    /*
     * 在 onDestroyView 中解除绑定
     * 当CameraFragment返回键触发时解除CameraX的绑定，
     * 当处于开启摄像头情况时最小化App返回时不会触发解除绑定
     */
    override fun onDestroyView() {
        super.onDestroyView()
        CameraX.unbindAll()
    }

    override fun onDestroy() {
        super.onDestroy()
        hideInterface?.showBottomNavigation()
        hideInterface?.showTitleBar()
    }

    override fun initViewDataBinding(inflater: LayoutInflater, container: ViewGroup?) {
        cameraViewModel = ViewModelProviders.of(this).get(CameraViewModel::class.java)
    }

    override val layoutId: Int
        get() = R.layout.fragment_camera


    @SuppressLint("ClickableViewAccessibility")
    private fun initViews() {
        btnCapture = activity?.findViewById(R.id.btnCamera)
        containerCamera = activity?.findViewById(R.id.containerCamera)
        focusCircle = activity?.findViewById(R.id.focusCircle)

        btnCapture?.setOnClickListener { v: View? ->
            //创建要存照片的File
            Logger.d("点击了拍照按钮")
            val currentTime = System.currentTimeMillis()
            val imageName = "garbagesorthelper_$currentTime.png"
            cameraViewModel?.imageCapture?.takePicture(cameraViewModel?.createImageFile(imageName), object : ImageCapture.OnImageSavedListener {
                @SuppressLint("CheckResult")
                override fun onImageSaved(file: File) { //与搜索页面通信，成功保存了拍摄图片
                    LiveEventBus.get(Constants.IMAGE_SUCCESS).post(imageName)
                    //返回搜索页面
                    val manager: FragmentManager? = getFragmentManager()
                    //通过FragmentManager管理器获取被标记的CameraFragment
                    val fragment1: Fragment? = manager?.findFragmentByTag("CameraFragment")
                    if (fragment1 != null) { //开始事务 通过remove清除指定的fragment，并提交
                        manager.beginTransaction().remove(fragment1).commit()
                    }
                }

                override fun onError(imageCaptureError: ImageCaptureError, message: String, cause: Throwable?) {
                    //与搜索页面通信，发送失败的原因
                    var errorMsg: String? = "发生了未知的错误"
                    if (cause != null) {
                        errorMsg = cause.message
                    }
                    Logger.d("拍照发生错误  " + message)
                    LiveEventBus.get(Constants.IMAGE_FAILURE).post(errorMsg)
                }
            })
        }
    }

    @SuppressLint("CheckResult", "ClickableViewAccessibility", "RestrictedApi")
    private fun initCamera() {
        //加载CameraX 配置信息
        cameraViewModel?.initCameraConfig()
        //CameraX未绑定 UseCase 则绑定
        if( ! CameraX.isBound(cameraViewModel?.preview) && ! CameraX.isBound(cameraViewModel?.imageCapture) && ! CameraX.isBound(cameraViewModel?.imageAnalysis)) {
            CameraX.bindToLifecycle(self, cameraViewModel?.preview, cameraViewModel?.imageAnalysis, cameraViewModel?.imageCapture)
        }
        //图片分析
//        cameraViewModel?.imageAnalysis?.analyzer = ImageAnalysis.Analyzer { image, rotationDegrees ->
//            //                Rect cropRect =  image.getCropRect();
//        }
        //图片预览
        cameraViewModel?.preview?.onPreviewOutputUpdateListener = OnPreviewOutputUpdateListener { output ->
            Log.e(TAG, "onUpdated: 更新拍摄视图")
            preViewOutput = output
            containerCamera?.surfaceTexture = output.surfaceTexture
            //隐藏和显示特定的View
            btnCapture?.visibility = View.VISIBLE
            hideInterface?.hideBottomNavigation()
            // 设置图像预览画面随手机旋转--但是方法暂时无效
            updateTransform()
        }
        //点击拍照区域对焦
        containerCamera?.setOnTouchListener { v, event ->
            val eventAction = event.action
            when (eventAction) {
                MotionEvent.ACTION_DOWN -> {
                    //启动点击对焦自定义View
                    focusCircle?.let {
                        it.focusStart(event.x ,event.y)
                    }
                    val meteringPoint = containerCamera?.let {
                        TextureViewMeteringPointFactory(it).createPoint(event.x , event.y)
                    }
//                    val pointFactory = TextureViewMeteringPointFactory(mBinding!!.containerCamera)
//                    val meteringPoint = pointFactory.createPoint(event.x, event.y)
                    meteringPoint?.let {

                        try {
                            CameraX.getCameraControl(CameraX.LensFacing.BACK)
                                    .startFocusAndMetering(FocusMeteringAction.Builder.from(it).build())
                        } catch (e: CameraInfoUnavailableException) {
                            e.printStackTrace()
                        }
                    }

                }
                MotionEvent.ACTION_MOVE -> {
                }
                //对焦取消
                MotionEvent.ACTION_UP -> focusCircle?.focusCompleted()
            }
            true
        }
    }

    private val self: CameraFragment
        get() = this

    /**
     * 用于返回是否需要实现监听
     * 返回true，则让宿主Activity处理本次返回事件
     */
    fun onBackPressed(): Boolean {

        return true
    }

    /**
     * 图像预览旋转事件
     */
    private fun updateTransform() {
        val mx = Matrix()
        val w = containerCamera?.measuredWidth?.toFloat()
        val h = containerCamera?.measuredHeight?.toFloat()
        val cX = w?.div(2f)
        val cY = h?.div(2f)
        val rotationDgr: Int
        val rotation = containerCamera?.rotation?.toInt()
        rotationDgr = when (rotation) {
            Surface.ROTATION_0 -> 0
            Surface.ROTATION_90 -> 90
            Surface.ROTATION_180 -> 180
            Surface.ROTATION_270 -> 270
            else -> return
        }
        mx.postRotate(rotationDgr.toFloat(), cX?:0f, cY?:0f)
        containerCamera?.setTransform(mx)
    }






}