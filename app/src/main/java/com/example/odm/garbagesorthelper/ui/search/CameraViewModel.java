package com.example.odm.garbagesorthelper.ui.search;

import android.app.Application;
import android.os.Environment;
import android.util.Log;
import android.util.Size;

import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageAnalysisConfig;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureConfig;
import androidx.camera.core.Preview;
import androidx.camera.core.PreviewConfig;
import androidx.lifecycle.MutableLiveData;

import com.example.odm.garbagesorthelper.base.BaseViewModel;
import com.example.odm.garbagesorthelper.model.AipImageClassifyCilent;
import com.example.odm.garbagesorthelper.model.RepositoryManager;
import com.example.odm.garbagesorthelper.model.entity.ImageClassfyData;
import com.example.odm.garbagesorthelper.utils.GsonUtils;
import com.orhanobut.logger.Logger;

import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

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
        File photoFile = new File(Environment.getExternalStorageDirectory(),imageName);
        return photoFile;
    }



}
