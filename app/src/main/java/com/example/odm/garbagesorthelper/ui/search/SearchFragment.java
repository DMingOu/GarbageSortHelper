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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.example.odm.garbagesorthelper.BR;
import com.example.odm.garbagesorthelper.R;
import com.example.odm.garbagesorthelper.ui.RootActivity;
import com.example.odm.garbagesorthelper.application.GarbageSortApplication;
import com.example.odm.garbagesorthelper.base.BaseFragment;
import com.example.odm.garbagesorthelper.core.Constants;
import com.example.odm.garbagesorthelper.databinding.FragmentSearchBinding;
import com.example.odm.garbagesorthelper.model.entity.BannerData;
import com.example.odm.garbagesorthelper.utils.InjectorUtils;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.jeremyliao.liveeventbus.LiveEventBus;
import com.orhanobut.logger.Logger;
import com.stx.xhb.androidx.XBanner;
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
        initBanner();
        handleLiveEvent();
//        首先通过DataBindingUtil.inflate初始化binding对象，然后通过.getRoot()获取操作视图，并且在onCreateView中返回该视图。否则会导致binding不生效。
        return mBinding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
//        searchViewModel.searching = false;
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mIatDialog.destroy();
    }

    @Override
    public void initViewDataBinding( LayoutInflater inflater , @Nullable ViewGroup container) {
//        searchViewModel = ViewModelProviders.of(this).get(SearchViewModel.class);
        searchViewModel = InjectorUtils.provideSearchViewModelFactory(requireContext()).create(SearchViewModel.class);
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
                    if(mBinding.etSearch.getText() != null) {
                        searchViewModel.onSearch(mBinding.etSearch.getText().toString());
                        //搜索状态置为 true
                        searchViewModel.searching = true;
                        showLoadingDialog();
                        //点击键盘的搜索键后，清空内容，放弃焦点
                        mBinding.etSearch.clearFocus();
                        mBinding.etSearch.setText("");
                    }
                }
            }
            return true;
        });
    }

    private  void initRecorderDialog() {
//        // 初始化听写Dialog，如果只使用有UI听写功能，无需创建SpeechRecognizer
        mIatDialog = new RecognizerDialog(getActivity(), searchViewModel.mInitListener);
        searchViewModel.initRecorderDialog(mIatDialog);

    }

    /**
     * 初始化展示轮播图Banner
     */
    private  void  initBanner() {
        mBinding.banner.setBannerData(searchViewModel.getBannerDataList());

        mBinding.banner.loadImage(
          (XBanner banner, Object model, View view, int position) -> {
                Glide.with(SearchFragment.this)
                        .load(((BannerData) model).getXBannerUrl())
                        .placeholder(R.drawable.module_glide_load_default_image)
                        .error(R.drawable.module_search_cookiebar_fail_garbage)
                        .into((ImageView) view);

        });
    }

    /**
     * 观察viewModel的数据变动--改变view层的UI
     */
    private void initDataObserve() {
        //展示查询分类结果，同时取消Loading对话框的显示
        searchViewModel.sortedList.observe(this, dataBeans -> {
            if(dataBeans != null && dataBeans.size() > 0) {
                if( searchViewModel.searching) {
                    searchViewModel.searching = false;
                    showGarbageResultBar();
                }
            }
        });
        //跳转到拍摄页面
        searchViewModel.isOpenCamera.observe(this,  isOpenCamera -> {
            if (isOpenCamera) {

                RootActivity rootActivity = (RootActivity) getActivity();
                RxPermissions rxPermissions = new RxPermissions(rootActivity);
                if(rxPermissions.isGranted(Manifest.permission.CAMERA)&&rxPermissions.isGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                                                        &&rxPermissions.isGranted(Manifest.permission.READ_EXTERNAL_STORAGE)){
                    //跳转到拍摄页面
                    getFragmentManager().beginTransaction()
                            .add(R.id.root_fragment_container , new CameraFragment() , "CameraFragment")
                            .commitAllowingStateLoss();
                    searchViewModel.isOpenCamera.setValue(false);
                } else {
                    Toast.makeText(getActivity().getApplicationContext(),"未获取相关权限，无法开启拍照识别！",Toast.LENGTH_LONG).show();
                }
            }
        });
        //监控将从百度图像识别获取到物体关键词，调用垃圾分类API，显示结果
        searchViewModel.imageClassifyGarbage.observe(this, bean -> {
            String keyGarbageName = bean.getKeyword();
            if(searchViewModel.searching) {
//                Logger.d("开始垃圾搜索" + Thread.currentThread().getName());
                searchViewModel.onSearch(keyGarbageName);
            }

        });
        /*
         * 显示语音框--开启语音识别功能
         */
        mBinding.btnOpenRecorder.setOnClickListener(v -> {
            if(mIatDialog != null) {
                mIatDialog.show();
                searchViewModel.isOpenRecorder.setValue(true);
                //动态更换了讯飞自带对话框的底部文字，必须在dialog的show执行后更换，否则空指针报错
                TextView recorderDialogTextView = (TextView) mIatDialog.getWindow().getDecorView().findViewWithTag("textlink");
                recorderDialogTextView.setText(R.string.recorder_dialog_textview_text);
            } else {
                Log.e(TAG, "initDataObserve: 对话框未初始化" );
            }
        });
        /*
         * 观察语音识别的结果，调用垃圾分类搜索接口
         */
        searchViewModel.voiceGarbageName.observe(this , garbageName->{
                if(! "".equals(garbageName)) {
//                    showLoadingDialog();
                    if(searchViewModel.searching) {
                        searchViewModel.onSearch(garbageName);
                    }
                } else {
                    //开启语音识别后，若无法检测用户语音内容，会弹出Toast提醒
                    if( searchViewModel.isOpenRecorder.getValue()) {
                        Toast.makeText(getActivity().getApplicationContext() ,"无法识别您说的内容",Toast.LENGTH_SHORT).show();
                    }
                }
        });
        searchViewModel.isOpenRecorder.setValue(false);
    }

    /**
     * 处理 LiveEvent 事件
     */
    private void handleLiveEvent() {
        //Todo 暂时未解决LiveEventBus 发送一次事件却重复接收到相同事件的bug ，故暂时采用时间差方式
        Long currentTime  = System.currentTimeMillis();
        if( (currentTime - searchViewModel.liveEventTime > 100000) ) {
            LiveEventBus
                    .get(Constants.IMAGE_SUCCESS, String.class)
                    .observe(this, imageName -> {
                        searchViewModel.liveEventTime = currentTime;
                        //成功保存了拍摄照片，开启Loading对话框，调用百度识图接口查询（耗时）
                        showLoadingDialog();
                        searchViewModel.imageClassifyFromBaiDu(imageName);
                    });
        }



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
        cancelLoadingDialog();
    }


    /**
     * 弹出Loading对话框，提示用户等待
     */
    private void showLoadingDialog () {
            if(loadingDialog == null) {
                loadingDialog = new MaterialDialog.Builder(getContext())
                        .limitIconToDefaultSize()
                        .title(R.string.tips)
                        .content(R.string.content_wait_for_receive_data)
                        .progress(true, 0)
                        .progressIndeterminateStyle(false)
                        .show();
            } else {
                loadingDialog.show();

            }



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
