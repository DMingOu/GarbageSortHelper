package com.example.odm.garbagesorthelper.ui.search;

import android.Manifest;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.example.odm.garbagesorthelper.BR;
import com.example.odm.garbagesorthelper.R;
import com.example.odm.garbagesorthelper.RootActivity;
import com.example.odm.garbagesorthelper.application.GarbageSortApplication;
import com.example.odm.garbagesorthelper.base.BaseFragment;
import com.example.odm.garbagesorthelper.core.Constants;
import com.example.odm.garbagesorthelper.databinding.FragmentSearchBinding;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.jeremyliao.liveeventbus.LiveEventBus;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;
import com.xuexiang.xui.widget.popupwindow.bar.CookieBar;

/**
 * description: 搜索页面View层
 * author: ODM
 * date: 2019/9/18
 */
public class SearchFragment extends BaseFragment {
    private static final String TAG = "SearchFragment";
    private FragmentSearchBinding mBinding;
    private SearchViewModel  searchViewModel;
    private MaterialDialog loadingDialog;
    private RecognizerDialog mIatDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initViewDataBinding(inflater ,container);
        initEditText();
        initRecorderDialog();
        initDataObserve();
        handleLiveEvent();
//        首先通过DataBindingUtil.inflate初始化binding对象，然后通过.getRoot()获取操作视图，并且在onCreateView中返回该视图。否则会导致binding不生效。
        return mBinding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mIatDialog.destroy();
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

    private  void initRecorderDialog() {
//        // 初始化听写Dialog，如果只使用有UI听写功能，无需创建SpeechRecognizer
//        // 使用UI听写功能，请根据sdk文件目录下的notice.txt,放置布局文件和图片资源
        mIatDialog = new RecognizerDialog(getActivity(), searchViewModel.mInitListener);
        if(mIatDialog != null) {
            //以下为dialog设置听写参数
            mIatDialog.setParameter(SpeechConstant.RESULT_TYPE, "json");
            //设置语音输入语言，zh_cn为简体中文
            mIatDialog.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
            //设置结果返回语言
            mIatDialog.setParameter(SpeechConstant.ACCENT, "mandarin");
            // 设置语音前端点:静音超时时间，单位ms，即用户多长时间不说话则当做超时处理
            //取值范围{1000～10000}
            mIatDialog.setParameter(SpeechConstant.VAD_BOS, "4500");
            //设置语音后端点:后端点静音检测时间，单位ms，即用户停止说话多长时间内即认为不再输入，
            //自动停止录音，范围{0~10000}
            mIatDialog.setParameter(SpeechConstant.VAD_EOS, "1500");
            mIatDialog.setParameter("dwa", "wpgs");
            //开始识别并设置监听器
            mIatDialog.setListener(searchViewModel.mRecognizerDialogListener);
            Log.e(TAG, "initRecorderDialog: 初始成功" );
        } else {
            Log.e(TAG, "initRecorderDialog: 初始失败" );
        }
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
            }
        });
        //监控将从百度图像识别获取到物体关键词，调用垃圾分类API，显示结果
        searchViewModel.imageClassfyGarbage.observe(this, bean -> {
            String keyGarbageName = bean.getKeyword();
            searchViewModel.onSearch(keyGarbageName);
        });

        searchViewModel.isOpenRecorder.observe(this , isOpenRecorder -> {
            if(isOpenRecorder) {
                if(mIatDialog != null) {
                    mIatDialog.show();
                } else {
                    Log.e(TAG, "initDataObserve: 对话框未初始化" );
                }
            }
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
