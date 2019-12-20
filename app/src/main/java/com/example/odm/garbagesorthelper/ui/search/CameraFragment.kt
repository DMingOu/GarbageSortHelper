package com.example.odm.garbagesorthelper.ui.search

import android.annotation.SuppressLint
import android.graphics.Matrix
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.camera.core.*
import androidx.camera.core.ImageCapture.ImageCaptureError
import androidx.camera.core.Preview.OnPreviewOutputUpdateListener
import androidx.camera.core.Preview.PreviewOutput
import androidx.camera.view.TextureViewMeteringPointFactory
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProviders
//import com.example.odm.garbagesorthelper.BR
import com.example.odm.garbagesorthelper.R
import com.example.odm.garbagesorthelper.base.BaseFragment
import com.example.odm.garbagesorthelper.base.IBackInterface
import com.example.odm.garbagesorthelper.core.Constants
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


//    private var mBinding: FragmentCameraBinding? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initViewDataBinding(inflater, container)
        val backInterface: IBackInterface?
        backInterface = if (activity !is IBackInterface) {
            throw ClassCastException("Hosting Activity must implement BackHandledInterface")
        } else {
            activity as IBackInterface?
        }
        backInterface!!.setSelectedBackFragment(this)
        return inflater.inflate(R.layout.fragment_camera ,container ,false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initViews()
        initCamera()
    }

    override fun initViewDataBinding(inflater: LayoutInflater, container: ViewGroup?) {
        cameraViewModel = ViewModelProviders.of(this).get(CameraViewModel::class.java)
//        mBinding = DataBindingUtil.inflate(inflater, layoutId, container, false)
//        mBinding?.setViewModel(cameraViewModel)
//        mBinding?.setVariable(BR.viewModel, cameraViewModel)
//        mBinding?.setLifecycleOwner(this)
    }

    override val layoutId: Int
        get() = R.layout.fragment_camera


    @SuppressLint("ClickableViewAccessibility")
    private fun initViews() {
        btnCapture = activity?.findViewById(R.id.btnCamera)
        containerCamera = activity?.findViewById(R.id.containerCamera)
        focusCircle = activity?.findViewById(R.id.focusCircle)

        btnCapture?.setOnClickListener { v: View? ->
            Logger.d("点击了拍照按钮")
            //创建要存照片的File
            val currentTime = System.currentTimeMillis()
            val imageName = "garbagesorthelper_$currentTime.png"
            cameraViewModel?.imageCapture?.takePicture(cameraViewModel?.createImageFile(imageName), object : ImageCapture.OnImageSavedListener {
                @SuppressLint("CheckResult")
                override fun onImageSaved(file: File) { //与搜索页面通信，成功保存了拍摄图片
                    LiveEventBus.get(Constants.IMAGE_SUCCESS).post(imageName)
                    //返回搜索页面
                    val manager: FragmentManager? = getFragmentManager()
                    //通过FragmentManager管理器获取被标记的CameraFragment
                    val fragment1: Fragment? = manager!!.findFragmentByTag("CameraFragment")
                    if (fragment1 != null) { //开始事务 通过remove清除指定的fragment，并提交
                        manager.beginTransaction().remove(fragment1).commit()
                    }
                }

                override fun onError(imageCaptureError: ImageCaptureError, message: String, cause: Throwable?) { //与搜索页面通信，发送失败的原因
                    var errorMsg: String? = "发生了未知的错误"
                    if (cause != null) {
                        errorMsg = cause.message
                    }
                    LiveEventBus.get(Constants.IMAGE_FAILURE).post(errorMsg)
                }
            })
        }
    }

    @SuppressLint("CheckResult", "ClickableViewAccessibility")
    private fun initCamera() { //加载CameraX 配置信息
        cameraViewModel?.initCameraConfig()
        CameraX.bindToLifecycle(self, cameraViewModel?.preview, cameraViewModel?.imageAnalysis, cameraViewModel?.imageCapture)
        //图片分析
        cameraViewModel?.imageAnalysis?.analyzer = ImageAnalysis.Analyzer { image, rotationDegrees ->
            //                Rect cropRect =  image.getCropRect();
        }
        //图片预览
        cameraViewModel?.preview?.onPreviewOutputUpdateListener = object : OnPreviewOutputUpdateListener {
            override fun onUpdated(output: PreviewOutput) {
                Log.e(TAG, "onUpdated: 更新拍摄视图")
                containerCamera?.surfaceTexture = output.surfaceTexture
                // 设置图像预览画面随手机旋转--但是方法暂时无效
                updateTransform()

                //                //获取屏幕预览的中心点
//                float centerX = (float) mBinding.containerCamera.getWidth() / 2 ;
//                float centerY = (float) mBinding.containerCamera.getHeight() / 2;
//                // 计算旋转角度
//                int rotationDegrees = 0;
//                Logger.d("output的旋转角度为" + output.getRotationDegrees());
//                if(mBinding.containerCamera!=null && mBinding.containerCamera.getDisplay()!=null) {
//                    switch (mBinding.containerCamera.getDisplay().getRotation()) {
//                        case Surface.ROTATION_0:
//                            rotationDegrees = 0;
//                            break;
//                        case Surface.ROTATION_90:
//                            rotationDegrees = 90;
//                            break;
//                        case Surface.ROTATION_180 :
//                            rotationDegrees = 180;
//                            break;
//                        case Surface.ROTATION_270:
//                            rotationDegrees = 270;
//                            break;
//                        default:
//                            break;
//                    }
//                } else {
//                    Logger.d("Texture的Display为空");
//                }
//                Matrix matrix =  new Matrix();
//                matrix.postRotate((float)-rotationDegrees, centerX, centerY);
//                // 将变换应用在预览上
//                mBinding.containerCamera.setTransform(matrix);
            }
        }
        //点击拍照区域对焦
        containerCamera?.setOnTouchListener { v, event ->
            val eventAction = event.action
            when (eventAction) {
                MotionEvent.ACTION_DOWN -> {
                    //启动点击对焦自定义View
                    focusCircle?.let {
                        it.focusStart(it , event.x ,event.y)
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

    companion object {
        private const val TAG = "CameraFragment"
    }
}