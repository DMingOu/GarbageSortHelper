package com.example.odm.garbagesorthelper.ui.search

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Matrix
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.*
import android.widget.RelativeLayout
import androidx.camera.core.*
import androidx.camera.core.ImageCapture.ImageCaptureError
import androidx.camera.core.Preview.OnPreviewOutputUpdateListener
import androidx.camera.core.Preview.PreviewOutput
import androidx.camera.view.TextureViewMeteringPointFactory
import androidx.core.content.ContextCompat.getSystemService
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProviders
//import com.example.odm.garbagesorthelper.BR
import com.example.odm.garbagesorthelper.R
import com.example.odm.garbagesorthelper.base.BaseFragment
import com.example.odm.garbagesorthelper.base.IBackInterface
import com.example.odm.garbagesorthelper.base.IHideInterface
import com.example.odm.garbagesorthelper.core.Constants
import com.example.odm.garbagesorthelper.ui.RootActivity
import com.example.odm.garbagesorthelper.widget.FocusCircleView
import com.jeremyliao.liveeventbus.LiveEventBus
import com.orhanobut.logger.Logger
import com.xuexiang.xui.widget.button.shadowbutton.ShadowButton
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.File
import java.util.concurrent.TimeUnit

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
            throw ClassCastException("活动Activity 必须继承 BackHandledInterface")
        } else {
            activity as IHideInterface
        }

        return inflater.inflate(R.layout.fragment_camera ,container ,false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        getDeviceDP()
        initViews()
        initCamera()
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
        //动态为预览区域进行设置宽高，以调整到最佳预览
        val rl = containerCamera?.layoutParams as RelativeLayout.LayoutParams
        rl.width =  cameraViewModel?.preViewWidth ?: 0
        rl.height = cameraViewModel?.preViewHeigth ?: 0
        Logger.d("动态设置预览区域的 高度 "  + rl.height + "   宽度 " + rl.width)
        rl.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE)
        containerCamera?.layoutParams = rl

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

    @SuppressLint("CheckResult", "ClickableViewAccessibility")
    private fun initCamera() {
        //加载CameraX 配置信息
        cameraViewModel?.initCameraConfig()
        CameraX.bindToLifecycle(self, cameraViewModel?.preview, cameraViewModel?.imageAnalysis, cameraViewModel?.imageCapture)
        //图片分析
        cameraViewModel?.imageAnalysis?.analyzer = ImageAnalysis.Analyzer { image, rotationDegrees ->
            //                Rect cropRect =  image.getCropRect();
        }
        //图片预览
        cameraViewModel?.preview?.onPreviewOutputUpdateListener = OnPreviewOutputUpdateListener { output ->
            Log.e(TAG, "onUpdated: 更新拍摄视图")
            containerCamera?.surfaceTexture = output.surfaceTexture
            //隐藏和显示特定的View
            btnCapture?.visibility = View.VISIBLE
            hideInterface?.hideBottomNavigation()
            // 设置图像预览画面随手机旋转--但是方法暂时无效
            updateTransform()

            //获取屏幕预览的中心点
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


//public void getAndroiodScreenProperty() {
//        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
//        DisplayMetrics dm = new DisplayMetrics();
//        wm.getDefaultDisplay().getMetrics(dm);
//        int width = dm.widthPixels;         // 屏幕宽度（像素）
//        int height = dm.heightPixels;       // 屏幕高度（像素）
//        float density = dm.density;         // 屏幕密度（0.75 / 1.0 / 1.5）
//        int densityDpi = dm.densityDpi;     // 屏幕密度dpi（120 / 160 / 240）
//        // 屏幕宽度算法:屏幕宽度（像素）/屏幕密度
//        int screenWidth = (int) (width / density);  // 屏幕宽度(dp)
//        int screenHeight = (int) (height / density);// 屏幕高度(dp)
//
//
//        Log.d("h_bl", "屏幕宽度（像素）：" + width);
//        Log.d("h_bl", "屏幕高度（像素）：" + height);
//        Log.d("h_bl", "屏幕密度（0.75 / 1.0 / 1.5）：" + density);
//        Log.d("h_bl", "屏幕密度dpi（120 / 160 / 240）：" + densityDpi);
//        Log.d("h_bl", "屏幕宽度（dp）：" + screenWidth);
//        Log.d("h_bl", "屏幕高度（dp）：" + screenHeight);
//    }

    fun getDeviceDP(){
        val wm = activity?.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val dm = DisplayMetrics()
        wm.getDefaultDisplay().getMetrics(dm)
        val width = dm.widthPixels         // 屏幕宽度（像素）
        val height = dm.heightPixels       // 屏幕高度（像素）
        val density  = dm.density          // 屏幕密度（0.75 / 1.0 / 1.5）
        val densityDpi = dm.densityDpi     // 屏幕密度dpi（120 / 160 / 240）
        // 屏幕宽度算法:屏幕宽度（像素）/屏幕密度
        val screenWidth : Float =  width  / density  // 屏幕宽度(dp)
        val screenHeight = (height / density);// 屏幕高度(dp)
        cameraViewModel?.preViewWidth = width
        cameraViewModel?.preViewHeigth = width * 4 / 3 - 100
//        Log.d("h_bl", "屏幕宽度（像素）：" + width);
//        Log.d("h_bl", "屏幕高度（像素）：" + height);
//        Log.d("h_bl", "屏幕密度（0.75 / 1.0 / 1.5）：" + density);
//        Log.d("h_bl", "屏幕密度dpi（120 / 160 / 240）：" + densityDpi);
//        Log.d("h_bl", "屏幕宽度（dp）：" + screenWidth);
//        Log.d("h_bl", "屏幕高度（dp）：" + screenHeight);
    }

    companion object {
        private const val TAG = "CameraFragment"
    }
}