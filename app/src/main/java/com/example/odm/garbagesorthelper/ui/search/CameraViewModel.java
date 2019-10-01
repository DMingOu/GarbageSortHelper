package com.example.odm.garbagesorthelper.ui.search;
import android.app.Application;
import android.os.Environment;
import android.util.Size;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageAnalysisConfig;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureConfig;
import androidx.camera.core.Preview;
import androidx.camera.core.PreviewConfig;
import com.example.odm.garbagesorthelper.base.BaseViewModel;
import com.example.odm.garbagesorthelper.model.RepositoryManager;


import java.io.File;


/**
 * description: 拍照页面ViewMoel层
 * author: ODM
 * date: 2019/9/25
 */
public class CameraViewModel extends BaseViewModel<RepositoryManager> {

    ImageCapture imageCapture;
    ImageAnalysis imageAnalysis;
    Preview preview;


    public CameraViewModel(Application application) {
        super(application);
    }

     void initCameraConfig() {

        PreviewConfig previewConfig = new PreviewConfig.Builder().build();
         preview = new Preview(previewConfig);
        ImageAnalysisConfig imageAnalysisConfig = new ImageAnalysisConfig.Builder().setTargetResolution(new Size(1080,2248)).build();
         imageAnalysis = new ImageAnalysis(imageAnalysisConfig);
        ImageCaptureConfig imageCaptureConfig = new ImageCaptureConfig.Builder().build();
        imageCapture = new ImageCapture(imageCaptureConfig);
    }

     File createImageFile(String imageName) {
        return new File(Environment.getExternalStorageDirectory(),imageName);
    }



}
