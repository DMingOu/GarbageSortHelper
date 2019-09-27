package com.example.odm.garbagesorthelper;

import android.app.Application;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.example.odm.garbagesorthelper.base.BaseViewModel;
import com.example.odm.garbagesorthelper.ui.about.AboutFragment;
import com.example.odm.garbagesorthelper.ui.knowledge.KnowLedgeFragment;
import com.example.odm.garbagesorthelper.ui.search.CameraFragment;
import com.example.odm.garbagesorthelper.ui.search.SearchFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * description: Root根页面ViewModel
 * author: ODM
 * date: 2019/9/18
 */
public class RootViewModel extends BaseViewModel {

    //Fragment对象列表
    List<Fragment> mFragments;

    //目前展示Fragment的位置
    int lastFragmentIndex;

    //标题栏颜色
    MutableLiveData<Integer> titlebarColor = new MutableLiveData<>(R.color.bottom_navigation_search);


    public RootViewModel( Application application) {
        super(application);
    }


    /**
     * 制造底部导航的子项item
     * @param titleRes 标题资源引用
     * @param drawableRes 图片资源引用
     * @param colorRes 颜色资源引用
     * @return 底部导航的子项item
     */
     AHBottomNavigationItem createBottomNavigationItem(@StringRes int titleRes, @DrawableRes int drawableRes, @ColorRes int colorRes) {

        return new AHBottomNavigationItem(titleRes ,drawableRes ,colorRes);
    }

    /**
     * 初始化Fragment对象列表
     */
      void initFragmentData() {
        mFragments = new ArrayList<>();
        mFragments.add(new KnowLedgeFragment());
        mFragments.add(new SearchFragment());
        mFragments.add(new AboutFragment());
        mFragments.add(new CameraFragment());
    }


    /**
     * 动态改变了标题栏的背景颜色
     * @param position 目标Fragment位置
     */
     void changeFragment(int position) {
        switch (position){
            case 0 :
                titlebarColor.setValue(R.color.bottom_navigation_knowledge);
                break;
            case 1:
                titlebarColor.setValue(R.color.bottom_navigation_search);
                break;
            case 2:
                titlebarColor.setValue(R.color.bottom_navigation_about);
                break;
            default:
                break;
        }
    }
}
