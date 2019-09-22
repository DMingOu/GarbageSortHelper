package com.example.odm.garbagesorthelper;

import android.app.Application;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.example.odm.garbagesorthelper.base.BaseViewModel;

/**
 * description: Root根页面ViewModel
 * author: ODM
 * date: 2019/9/18
 */
public class RootViewModel extends BaseViewModel {

    public RootViewModel( Application application) {
        super(application);
    }

    public MutableLiveData<Integer> titlebarColor = new MutableLiveData<>(R.color.bottom_navigation_search);


    /**
     * 动态改变了标题栏的背景颜色
     * @param position
     */
    public void changeFragment(int position) {
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
