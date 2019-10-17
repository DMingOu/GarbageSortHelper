package com.example.odm.garbagesorthelper.ui.about;

import android.content.DialogInterface;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.odm.garbagesorthelper.BR;
import com.example.odm.garbagesorthelper.R;
import com.example.odm.garbagesorthelper.base.BaseFragment;
import com.example.odm.garbagesorthelper.databinding.FragmentAboutBinding;
import com.example.odm.garbagesorthelper.databinding.FragmentAboutBindingImpl;
import com.orhanobut.logger.Logger;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;
import com.xuexiang.xui.widget.guidview.DismissListener;

/**
 * description: 我的页面View层
 * author: ODM
 * date: 2019/9/18
 */
public class AboutFragment extends BaseFragment {

  private   FragmentAboutBinding mBinding;
  private   AboutViewModel aboutViewModel;
  private   MaterialDialog.Builder dialogBuilder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initViewDataBinding(inflater , container);
        initView();
        initData();
        return mBinding.getRoot();
    }

    @Override
    public void initViewDataBinding(LayoutInflater inflater, @Nullable ViewGroup container) {
        aboutViewModel = ViewModelProviders.of(this).get(AboutViewModel.class);
        mBinding = DataBindingUtil.inflate(inflater ,getLayoutId() ,container ,false);
        mBinding.setViewModel(aboutViewModel);
        mBinding.setVariable(com.example.odm.garbagesorthelper.BR.viewModel,aboutViewModel);
        mBinding.setLifecycleOwner(this);
    }

    private void initData() {
        aboutViewModel.versionName.setValue(aboutViewModel.getVersion());
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_about;
    }

    private void initView(){
        if(mBinding != null){
            dialogBuilder  = new MaterialDialog.Builder(getContext());
            /*
             * 点击事件
             */
            mBinding.ralAboutAppAuthor.setOnClickListener(v -> showAuthorDialog());
            mBinding.ralAboutAppIntroduction.setOnClickListener(v-> showIntroductionDialog());
            mBinding.ralAboutAppUpdate.setOnClickListener(v-> Toast.makeText(getContext(),"已经是最新版本了",Toast.LENGTH_SHORT).show());
        }
    }

    private void showAuthorDialog() {
        if(dialogBuilder != null) {
            dialogBuilder.title("我是谁?")
                        .content("我是一名大二学生，因为兴趣使然，独立完成了此款垃圾分类小APP。".concat("\n欢迎您通过我的邮箱与我联系").concat("\nDMingOu@gmail.com"))
                        .positiveText(R.string.known)
                        .show();
        }
    }
    private void showIntroductionDialog() {
        if(dialogBuilder != null) {

            dialogBuilder.title("功能介绍")
                    .icon(getResources().getDrawable(R.drawable.module_about_dialog_icon_list))
                    .content("1、搜索关键词，获取关键词对应的垃圾分类"
                            .concat("\n").concat("2、物体拍照识别,获取对应的垃圾分类")
                            .concat("\n").concat("3、语音识别关键词，获取关键词对应的垃圾分类")
                            .concat("\n").concat("4、学习垃圾分类的基础知识与必要性" ))
                    .positiveText(R.string.known)
                    .show();

        }
    }
}
