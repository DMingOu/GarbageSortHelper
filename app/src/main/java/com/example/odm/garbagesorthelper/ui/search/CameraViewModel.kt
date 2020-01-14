package com.example.odm.garbagesorthelper.ui.search

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.os.Environment
import android.util.DisplayMetrics
import android.util.Log
import android.util.Rational
import android.util.Size
import android.view.ScaleGestureDetector
import android.view.Surface
import android.view.WindowManager
import androidx.camera.core.*
import androidx.camera.extensions.HdrImageCaptureExtender
import androidx.camera.extensions.HdrPreviewExtender
import androidx.core.content.ContextCompat.getSystemService
import com.example.odm.garbagesorthelper.base.BaseModel
import com.example.odm.garbagesorthelper.base.BaseViewModel
import java.io.File

/**
 * description: 拍照页面ViewMoel层
 * author: ODM
 * date: 2019/9/25
 */
class CameraViewModel(application: Application?) : BaseViewModel<BaseModel?>(application!!) {
    var imageCapture: ImageCapture? = null
    var imageAnalysis: ImageAnalysis? = null
    var preview: Preview? = null
    var preViewWidth : Int  = 0
    var preViewHeigth : Int  = 0
    var imageFolder : File ?

    init {
        imageFolder = File(Environment.getExternalStorageDirectory().toString() + "/GarbageSortHelper/ImageCache/")
        if(imageFolder?.exists() == false) {
            imageFolder?.mkdirs()
        }
    }


    fun initCameraConfig() {
        //拍摄预览的配置config
        val configBuilder = PreviewConfig.Builder()
                .setLensFacing(CameraX.LensFacing.BACK)
                .setTargetAspectRatio(Rational(9,16))
                .setTargetRotation(Surface.ROTATION_0)
//                .setTargetResolution(Size(1080, 1920))
        val hdrPreviewExtender = HdrPreviewExtender.create(configBuilder)
        //拍摄预览，开启HDR，判断硬件条件是否支持开启，是则直接开启
        if (hdrPreviewExtender.isExtensionAvailable) {
            hdrPreviewExtender.enableExtension()
        }
        preview = Preview(configBuilder.build())
        //图片分析的配置config
        val imageAnalysisConfig = ImageAnalysisConfig.Builder().build()
        imageAnalysis = ImageAnalysis(imageAnalysisConfig)
        //图片拍摄的配置config
        val captureBuilder = ImageCaptureConfig.Builder().setLensFacing(CameraX.LensFacing.BACK)
        val hdrImageCaptureExtender = HdrImageCaptureExtender.create(captureBuilder)
        //拍摄照片，开启HDR，判断硬件条件是否支持开启，是则直接开启
        if (hdrImageCaptureExtender.isExtensionAvailable) {
            hdrImageCaptureExtender.isExtensionAvailable
        }
        imageCapture = ImageCapture(captureBuilder.build())
    }

    //保存指定名称的文件,绝对路径为"/storage/emulated/0/"+imageName
    fun createImageFile(imageName: String): File {
        return File(imageFolder, imageName)
    }


}