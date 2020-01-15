package com.example.odm.garbagesorthelper.ui.Camera

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.TextureView
import android.view.View
import android.widget.Button
import androidx.camera.core.*
import androidx.camera.view.TextureViewMeteringPointFactory
import androidx.lifecycle.ViewModelProviders
import com.example.odm.garbagesorthelper.R
import com.example.odm.garbagesorthelper.core.Constants
import com.example.odm.garbagesorthelper.widget.FocusCircleView
import com.jeremyliao.liveeventbus.LiveEventBus
import com.orhanobut.logger.Logger
import com.xuexiang.xui.utils.StatusBarUtils
import com.xuexiang.xui.widget.button.shadowbutton.ShadowButton
import java.io.File

class CameraActivity : AppCompatActivity() {

    private var cameraViewModel: CameraViewModel? = null

    //控件变量
    private var btnCapture : Button ?= null
    private var containerCamera :   TextureView?=  null
    private var focusView : FocusCircleView?= null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //沉浸式状态栏
        StatusBarUtils.translucent(this)
        setContentView(R.layout.activity_camera)
        cameraViewModel = ViewModelProviders.of(this).get(CameraViewModel::class.java)
        initViews()
        initCamera()
    }


    fun initViews(){
        btnCapture = findViewById(R.id.btnCapture)
        containerCamera = findViewById(R.id.previewContainer)
        focusView = findViewById(R.id.focusView)

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
                finish()
            }

            override fun onError(imageCaptureError: ImageCapture.ImageCaptureError, message: String, cause: Throwable?) {
                //与搜索页面通信，发送失败的原因
                val errorMsg = cause?.message ?:"发生了未知的错误"
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
           CameraX.bindToLifecycle(this, cameraViewModel?.preview, cameraViewModel?.imageAnalysis, cameraViewModel?.imageCapture)
       }
       //图片分析
   //        cameraViewModel?.imageAnalysis?.analyzer = ImageAnalysis.Analyzer { image, rotationDegrees ->
   //            //                Rect cropRect =  image.getCropRect();
   //        }
       //图片预览
       cameraViewModel?.preview?.onPreviewOutputUpdateListener = Preview.OnPreviewOutputUpdateListener { output ->

           containerCamera?.surfaceTexture = output.surfaceTexture
           //隐藏和显示特定的View
           btnCapture?.visibility = View.VISIBLE
           // 设置图像预览画面随手机旋转--但是方法暂时无效
           //updateTransform()
       }
       //点击拍照区域对焦
       containerCamera?.setOnTouchListener { v, event ->
           val eventAction = event.action
           when (eventAction) {
               MotionEvent.ACTION_DOWN -> {
                   //启动点击对焦自定义View
                   focusView?.focusStart(event.x ,event.y)
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
               MotionEvent.ACTION_UP -> focusView?.focusCompleted()
           }
           true
       }
   }


   }
