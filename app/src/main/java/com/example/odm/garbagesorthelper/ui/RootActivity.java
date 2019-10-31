package com.example.odm.garbagesorthelper.ui;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import android.Manifest;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.odm.garbagesorthelper.R;
import com.example.odm.garbagesorthelper.base.BaseActivity;
import com.example.odm.garbagesorthelper.base.IBackInterface;
import com.example.odm.garbagesorthelper.databinding.ActivityRootBinding;
import com.example.odm.garbagesorthelper.ui.search.CameraFragment;
import com.orhanobut.logger.Logger;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.xuexiang.xui.utils.StatusBarUtils;

import io.reactivex.functions.Consumer;

public class RootActivity extends BaseActivity  implements IBackInterface {

    ActivityRootBinding rootBinding;
    RootViewModel rootViewModel;


    private static final String TAG = "RootActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置沉浸式状态栏
        StatusBarUtils.translucent(this);
        StatusBarUtils.setStatusBarLightMode(this);
        setContentView(R.layout.activity_root);
        initViewDataBinding();
        initFragmentData();
        initBottomNavigation();
        initPermissions();
    }


    private void initFragmentData() {
        if(rootViewModel != null) {
            rootViewModel.initFragmentData();
        }
    }

    public void initBottomNavigation() {
        // 底部导航添加子项
        rootBinding.rootBottomNavigation.addItem(rootViewModel.createBottomNavigationItem(R.string.bottom_navigation_knowledge, R.drawable.root_bottom_knowledge, R.color.bottom_navigation_knowledge));
        rootBinding.rootBottomNavigation.addItem(rootViewModel.createBottomNavigationItem(R.string.bottom_navigation_search, R.drawable.root_bottom_search, R.color.bottom_navigation_search));
        rootBinding.rootBottomNavigation.addItem(rootViewModel.createBottomNavigationItem(R.string.bottom_navigation_about, R.drawable.root_bottom_about, R.color.bottom_navigation_about));
        rootBinding.rootBottomNavigation.setCurrentItem(1);
        this.setFragmentPosition(1);
        rootBinding.rootBottomNavigation.setColored(true);
        // 监控底部导航栏的点击事件
        rootBinding.rootBottomNavigation.setOnTabSelectedListener( (position,wasSelected)->{
            rootViewModel.changeFragmentTitleBarColor(position);
            rootBinding.rootToolbar.setBackgroundColor(getResources().getColor(rootViewModel.titlebarColor.getValue().intValue()));
            setFragmentPosition(position);
            return true;
        });

    }

    @Override
    public void initViewDataBinding() {
        rootBinding = DataBindingUtil.setContentView(this ,getLayoutId());
        rootViewModel  = ViewModelProviders.of(this).get(RootViewModel.class);
        rootBinding.setLifecycleOwner(this);
        rootBinding.setVariable(com.example.odm.garbagesorthelper.BR.viewModel ,rootViewModel);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_root;
    }

    public void setFragmentPosition(int position) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment targetFragment = rootViewModel.mFragments.get(position);
        Fragment lastFragment = rootViewModel.mFragments.get(rootViewModel.lastFragmentIndex);
        rootViewModel.lastFragmentIndex = position;
        ft.hide(lastFragment);
        //如果目标Fragment已经添加，则remove掉重新加入
        if (!targetFragment.isAdded()) {
            getSupportFragmentManager().beginTransaction().remove(targetFragment).commit();
            Logger.d("pos : " + position + "    的Fragment ，被添加进去");
            ft.add(R.id.root_fragment_container, targetFragment);
        }

        ft.replace(R.id.root_fragment_container ,targetFragment);
        ft.show(targetFragment);
        ft.commitAllowingStateLoss();
        //防止重复添加
        getSupportFragmentManager().executePendingTransactions();
    }

    @SuppressLint("CheckResult")
    public void initPermissions() {
        //动态获取拍摄,录音权限
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.RECORD_AUDIO)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        //检查权限是否获取，提醒用户
                        if (aBoolean) {
                            Log.e(TAG, "accept: 动态申请 权限回调 true" );

                        } else {
                            Log.e(TAG, "accept: 动态申请 权限回调 false" );
                            Toast.makeText(RootActivity.this,"未授权应用相关权限，将无法使用拍照识别功能！",Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    /**
     * 监听返回键事件
     */
    @Override
    public void onBackPressed() {

        //若当前页面为拍摄页面，监听返回键事件--返回拍摄页面
        if(rootViewModel.backFragment != null  && ((CameraFragment) rootViewModel.backFragment).onBackPressed()) {

            setFragmentPosition(1);
            rootViewModel.backFragment = null;
        } else {
        //若当前页面为其他页面，监听返回键事件--退出APP
            super.onBackPressed();
            finish();
        }

    }

    @Override
    public void setSelectedBackFragment(Fragment fragment) {
        rootViewModel.backFragment  = fragment;
    }
}
