package com.example.odm.garbagesorthelper.ui.search;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Size;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.camera.core.CameraX;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageAnalysisConfig;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureConfig;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.core.PreviewConfig;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.odm.garbagesorthelper.BR;
import com.example.odm.garbagesorthelper.R;
import com.example.odm.garbagesorthelper.RootActivity;
import com.example.odm.garbagesorthelper.application.GarbageSortApplication;
import com.example.odm.garbagesorthelper.base.BaseFragment;
import com.example.odm.garbagesorthelper.core.Constants;
import com.example.odm.garbagesorthelper.databinding.FragmentSearchBinding;
import com.example.odm.garbagesorthelper.model.entity.ImageClassfyData;
import com.jeremyliao.liveeventbus.LiveEventBus;
import com.orhanobut.logger.Logger;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;
import com.xuexiang.xui.widget.popupwindow.bar.CookieBar;

import io.reactivex.functions.Consumer;

/**
 * description: 搜索页面View层
 * author: ODM
 * date: 2019/9/18
 */
public class SearchFragment extends BaseFragment {

    private FragmentSearchBinding mBinding;
    private SearchViewModel  searchViewModel;
    private MaterialDialog loadingDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initViewDataBinding(inflater ,container);
        initEditText();
        initDataObserve();
        handleLiveEvent();
//        首先通过DataBindingUtil.inflate初始化binding对象，然后通过.getRoot()获取操作视图，并且在onCreateView中返回该视图。否则会导致binding不生效。
        return mBinding.getRoot();
    }


    @Override
    public void initViewDataBinding( LayoutInflater inflater , @Nullable ViewGroup container) {
        searchViewModel = ViewModelProviders.of(this).get(SearchViewModel.class);
        mBinding =  DataBindingUtil.inflate(inflater,getLayoutId() ,container,false);
        mBinding.setViewModel(searchViewModel);
        mBinding.setVariable(BR.viewModel,searchViewModel);
        mBinding.setLifecycleOwner(this);
    }

    private void initEditText() {
        mBinding.etSearch.setOnEditorActionListener(( v,actionId, event) -> {
            if(actionId == EditorInfo.IME_ACTION_SEARCH && !"".equals(searchViewModel.garbageName.toString())) {
                //搜索内容非空且点击了搜索键后收起软键盘
                InputMethodManager manager = ((InputMethodManager) GarbageSortApplication.getContext().getSystemService(Context.INPUT_METHOD_SERVICE));
                if (manager != null) {
                    manager.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    //触发软键盘的点击事件
                    searchViewModel.onSearch(searchViewModel.garbageName.getValue());
                    showLoadingDialog();
                    //点击键盘的搜索键后，清空内容，放弃焦点
                    mBinding.etSearch.clearFocus();
                    mBinding.etSearch.setText("");
                }
            }
            return true;
        });
    }

    private void initDataObserve() {
        //展示输入框搜索结果，同时取消Loading框
        searchViewModel.sortedList.observe(this, dataBeans -> {
            if(dataBeans != null && dataBeans.size() > 0) {
                showGarbageResultBar();
                cancelLoadingDialog();
            }
        });
        //跳转到拍摄页面
        searchViewModel.isOpenCamera.observe(this,  isOpenCamera -> {
            if (isOpenCamera) {

                RootActivity rootActivity = (RootActivity) getActivity();
                RxPermissions rxPermissions = new RxPermissions(rootActivity);
                if(rxPermissions.isGranted(Manifest.permission.CAMERA)&&rxPermissions.isGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                                                        &&rxPermissions.isGranted(Manifest.permission.READ_EXTERNAL_STORAGE)){
                    rootActivity.setFragmentPosition(3);
                } else {
                    Toast.makeText(getActivity().getApplicationContext(),"未获取相关权限，无法开启拍照识别！",Toast.LENGTH_LONG).show();
                }

                searchViewModel.isOpenCamera.setValue(false);
            }
        });
        //将从百度图像识别获取到物体关键词，调用垃圾分类API，显示结果
        searchViewModel.imageClassfyGarbage.observe(this, bean -> {
            String keyGarbageName = bean.getKeyword();
            searchViewModel.onSearch(keyGarbageName);
        });

    }



    /**
     * 处理 LiveEvent 事件
     */
    private void handleLiveEvent() {
        LiveEventBus
                .get(Constants.IMAGE_SUCCESS, String.class)
                .observe(this, imageName -> {
                    //成功保存了拍摄照片
                    searchViewModel.imageClassfyFromBaidu(imageName);
                    showLoadingDialog();
                });
    }


    @Override
    public int getLayoutId() {
        return R.layout.fragment_search;
    }




    /**
     * 弹出Bar提示--用户搜索结果
     */
    private void showGarbageResultBar() {
        CookieBar.builder(getActivity())
                .setTitle(searchViewModel.sortedList.getValue().get(0).getCategory())
                .setIcon(searchViewModel.getGarbageIcon(searchViewModel.sortedList.getValue().get(0).getType()))
                .setMessage(searchViewModel.sortedList.getValue().get(0).getName() + "\n"+searchViewModel.sortedList.getValue().get(0).getRemark())
                .setLayoutGravity(Gravity.BOTTOM)
                .setAction(R.string.known, null)
                .setDuration(4000)
                .show();
    }


    /**
     * 弹出Loading对话框，提示用户等待
     */
    private void showLoadingDialog () {
            loadingDialog = new MaterialDialog.Builder(getContext())
                    .limitIconToDefaultSize()
                    .title(R.string.tips)
                    .content(R.string.content_wait_for_receive_data)
                    .progress(true, 0)
                    .progressIndeterminateStyle(false)
                    .show();
    }

    /**
     * 取消Loading对话框的显示
     */
    private void cancelLoadingDialog() {
        if(loadingDialog != null) {
            loadingDialog.dismiss();
            loadingDialog = null;
            }
        }
    }
