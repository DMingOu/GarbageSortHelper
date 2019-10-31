package com.example.odm.garbagesorthelper.ui.search;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.camera.core.CameraInfoUnavailableException;
import androidx.camera.core.CameraX;
import androidx.camera.core.FocusMeteringAction;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.MeteringPoint;
import androidx.camera.core.Preview;
import androidx.camera.view.TextureViewMeteringPointFactory;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;

import com.example.odm.garbagesorthelper.R;
import com.example.odm.garbagesorthelper.base.BaseFragment;
import com.example.odm.garbagesorthelper.base.IBackInterface;
import com.example.odm.garbagesorthelper.core.Constants;
import com.example.odm.garbagesorthelper.databinding.FragmentCameraBinding;
import com.jeremyliao.liveeventbus.LiveEventBus;

import java.io.File;


/**
 * description: 拍摄界面V层
 * author: ODM
 * date: 2019/9/25
 */
public class CameraFragment extends BaseFragment {

    private CameraViewModel cameraViewModel ;
    private FragmentCameraBinding mBinding;

    private static final String TAG = "CameraFragment";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initViewDataBinding(inflater ,container);
        initView();
        initCamera();
        View view =  mBinding.getRoot();
        IBackInterface backInterface;
        if(!(getActivity() instanceof IBackInterface)){
            throw new ClassCastException("Hosting Activity must implement BackHandledInterface");
        }else{
            backInterface = (IBackInterface)getActivity();
        }
        backInterface.setSelectedBackFragment(this);
        return view;
    }



    @Override
    public void initViewDataBinding(LayoutInflater inflater, @Nullable ViewGroup container) {
        cameraViewModel = ViewModelProviders.of(this).get(CameraViewModel.class);
        mBinding =  DataBindingUtil.inflate(inflater,getLayoutId() ,container,false);
        mBinding.setViewModel(cameraViewModel);
        mBinding.setVariable(com.example.odm.garbagesorthelper.BR.viewModel,cameraViewModel);
        mBinding.setLifecycleOwner(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_camera;
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initView() {


        mBinding.btnCamera.setOnClickListener( v->{
            //创建要存照片的File
            String imageName = "QG7777777.png";
            cameraViewModel.imageCapture.takePicture(cameraViewModel.createImageFile(imageName), new ImageCapture.OnImageSavedListener() {
                @SuppressLint("CheckResult")
                @Override
                public void onImageSaved(@NonNull File file) {
                    //与搜索页面通信，成功保存了拍摄图片
                    LiveEventBus.get(Constants.IMAGE_SUCCESS).post(imageName);
                    //返回搜索页面
                    FragmentManager manager = getFragmentManager();
                    //通过FragmentManager管理器获取被标记的CameraFragment
                    Fragment fragment1 = manager.findFragmentByTag("CameraFragment");
                    if (fragment1 != null) {
                        //开始事务 通过remove清除指定的fragment，并提交
                        manager.beginTransaction().remove(fragment1).commit();
                    }
                }
                @Override
                public void onError(@NonNull ImageCapture.ImageCaptureError imageCaptureError, @NonNull String message, @Nullable Throwable cause) {
                    //与搜索页面通信，发送失败的原因
                    String errorMsg = "发生了未知的错误";
                    if(cause != null) {
                        errorMsg = cause.getMessage();
                    }
                    LiveEventBus.get(Constants.IMAGE_FAILURE).post(errorMsg);
                }
            });
        });
    }

    @SuppressLint({"CheckResult", "ClickableViewAccessibility"})
    private void initCamera(){
        //加载CameraX 配置信息
        cameraViewModel.initCameraConfig();
        CameraX.bindToLifecycle(getSelf(), cameraViewModel.preview, cameraViewModel.imageAnalysis, cameraViewModel.imageCapture);
        //图片分析
        cameraViewModel.imageAnalysis.setAnalyzer(new ImageAnalysis.Analyzer() {
            @Override
            public void analyze(ImageProxy image, int rotationDegrees) {
//                Rect cropRect =  image.getCropRect();
//                Logger.d(cropRect.exactCenterX());
            }
        });
        //图片预览
        cameraViewModel.preview.setOnPreviewOutputUpdateListener(new Preview.OnPreviewOutputUpdateListener() {
            @Override
            public void onUpdated(Preview.PreviewOutput output) {
                Log.e(TAG, "onUpdated: 更新拍摄视图" );
                mBinding.containerCamera.setSurfaceTexture(output.getSurfaceTexture());
            }
        });
        //点击拍照区域对焦
        mBinding.containerCamera.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int eventAction = event.getAction();
                switch (eventAction) {
                    case MotionEvent.ACTION_DOWN:
                        //启动点击对焦自定义View
                        mBinding.focusCircle.focusStart(mBinding.focusCircle , event.getX() ,event.getY());
                        TextureViewMeteringPointFactory pointFactory = new TextureViewMeteringPointFactory(mBinding.containerCamera);
                        MeteringPoint meteringPoint = pointFactory.createPoint(event.getX(),event.getY());
                        FocusMeteringAction action = FocusMeteringAction.Builder
                                .from(meteringPoint)
                                .build();
                        try {
                            CameraX.getCameraControl(CameraX.LensFacing.BACK).startFocusAndMetering(action);
                        } catch (CameraInfoUnavailableException e) {
                            e.printStackTrace();
                        }
                        break;
                    case MotionEvent.ACTION_MOVE://移动
                        break;
                    case MotionEvent.ACTION_UP://松开
                        //对焦View关闭
                        mBinding.focusCircle.focusCompleted();
                        break;
                }
                return true;
            }
        });

    }

    private CameraFragment getSelf() {
        return this;
    }



    /**
     * 用于返回是否需要实现监听
     * 返回true，则让宿主Activity处理本次返回事件
     */
    public boolean onBackPressed() {
        return  true;
    }


}
