package com.example.odm.garbagesorthelper;

import androidx.camera.core.CameraX;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import android.Manifest;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.example.odm.garbagesorthelper.base.BaseActivity;
import com.example.odm.garbagesorthelper.databinding.ActivityRootBinding;
import com.example.odm.garbagesorthelper.ui.about.AboutFragment;
import com.example.odm.garbagesorthelper.ui.knowledge.KnowLedgeFragment;
import com.example.odm.garbagesorthelper.ui.search.CameraFragment;
import com.example.odm.garbagesorthelper.ui.search.SearchFragment;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.xuexiang.xui.utils.StatusBarUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Consumer;

public class RootActivity extends BaseActivity {

    ActivityRootBinding rootBinding;
    RootViewModel rootViewModel;

    List<Fragment> mFragments;
    private int lastFragmentIndex;
    private static final String TAG = "RootActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置沉浸式状态栏
        StatusBarUtils.translucent(this);
        StatusBarUtils.setStatusBarLightMode(this);
        setContentView(R.layout.activity_root);
        initFragmentData();
        initViewDataBinding();
        initBottomNavigation();
        initPermission();

    }

    private  void initFragmentData() {
        mFragments = new ArrayList<>();
        mFragments.add(new KnowLedgeFragment());
        mFragments.add(new SearchFragment());
        mFragments.add(new AboutFragment());
        mFragments.add(new CameraFragment());
    }


    public void initBottomNavigation() {
        // 创造底部导航的子项
        AHBottomNavigationItem item1 = new AHBottomNavigationItem(R.string.bottom_navigation_knowledge, R.drawable.root_bottom_knowledge, R.color.bottom_navigation_knowledge);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem(R.string.bottom_navigation_search, R.drawable.root_bottom_search, R.color.bottom_navigation_search);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem(R.string.bottom_navigation_about, R.drawable.root_bottom_about, R.color.bottom_navigation_about);

        // 底部导航添加子项
        rootBinding.rootBottomNavigation.addItem(item1);
        rootBinding.rootBottomNavigation.addItem(item2);
        rootBinding.rootBottomNavigation.addItem(item3);
        rootBinding.rootBottomNavigation.setCurrentItem(1);
        setFragmentPosition(1);
        rootBinding.rootBottomNavigation.setColored(true);
        // 监控底部导航栏的点击事件
        rootBinding.rootBottomNavigation.setOnTabSelectedListener( (position,wasSelected)->{
            Toast.makeText(getApplicationContext(),"点击了第 " + position + "个页面",Toast.LENGTH_SHORT).show();
            rootViewModel.changeFragment(position);
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
        rootBinding.setVariable(BR.viewModel ,rootViewModel);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_root;
    }

    public void setFragmentPosition(int position) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment currentFragment = mFragments.get(position);
        Fragment lastFragment = mFragments.get(lastFragmentIndex);
        lastFragmentIndex = position;
        ft.hide(lastFragment);
        if (!currentFragment.isAdded()) {
            getSupportFragmentManager().beginTransaction().remove(currentFragment).commit();
            ft.add(R.id.root_fragment_container, currentFragment);
        }
        ft.show(currentFragment);
        ft.commitAllowingStateLoss();
    }

    @SuppressLint("CheckResult")
    public void initPermission() {
        //动态获取拍摄权限
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE)
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


}
