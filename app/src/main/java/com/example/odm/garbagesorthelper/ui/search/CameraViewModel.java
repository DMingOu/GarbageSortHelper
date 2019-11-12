package com.example.odm.garbagesorthelper.ui.search;
import android.app.Application;
import android.os.Environment;
import android.util.Size;
import android.view.Surface;

import androidx.camera.core.CameraX;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageAnalysisConfig;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureConfig;
import androidx.camera.core.Preview;
import androidx.camera.core.PreviewConfig;
import androidx.camera.extensions.HdrImageCaptureExtender;
import androidx.camera.extensions.HdrPreviewExtender;

import com.example.odm.garbagesorthelper.base.BaseViewModel;


import java.io.File;


/**
 * description: 拍照页面ViewMoel层
 * author: ODM
 * date: 2019/9/25
 */
public class CameraViewModel extends BaseViewModel {

    ImageCapture imageCapture;
    ImageAnalysis imageAnalysis;
    Preview preview;


    public CameraViewModel(Application application) {
        super(application);
    }

     void initCameraConfig() {
         //拍摄预览的配置config
         PreviewConfig.Builder configBuilder = new PreviewConfig.Builder()
                                                    .setLensFacing(CameraX.LensFacing.BACK)
                                                    .setTargetRotation(Surface.ROTATION_0)
                                                    .setTargetResolution(new Size(1080 ,1920));
         HdrPreviewExtender hdrPreviewExtender = HdrPreviewExtender.create(configBuilder);
         //拍摄预览，开启HDR，判断硬件条件是否支持开启，是则直接开启
         if(hdrPreviewExtender.isExtensionAvailable()) {
             hdrPreviewExtender.enableExtension();
         }
         preview = new Preview(configBuilder.build());


         //图片分析的配置config
        ImageAnalysisConfig imageAnalysisConfig = new ImageAnalysisConfig.Builder().setTargetResolution(new Size(1080,2248)).build();
        imageAnalysis = new ImageAnalysis(imageAnalysisConfig);
        //图片拍摄的配置config
        ImageCaptureConfig.Builder captureBuilder = new ImageCaptureConfig.Builder().setLensFacing(CameraX.LensFacing.BACK);
        HdrImageCaptureExtender hdrImageCaptureExtender = HdrImageCaptureExtender.create(captureBuilder);
         //拍摄照片，开启HDR，判断硬件条件是否支持开启，是则直接开启
        if(hdrImageCaptureExtender.isExtensionAvailable()) {
             hdrImageCaptureExtender.isExtensionAvailable();
         }
        imageCapture = new ImageCapture(captureBuilder.build());
    }


     //保存指定名称的文件,绝对路径为"/storage/emulated/0/"+imageName
     File createImageFile(String imageName) {
        return new File(Environment.getExternalStorageDirectory(),imageName);
    }


}
