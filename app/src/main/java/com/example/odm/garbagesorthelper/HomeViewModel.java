package com.example.odm.garbagesorthelper;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.odm.garbagesorthelper.base.BaseViewModel;
import com.orhanobut.logger.Logger;

/**
 * description: 首页ViewModel
 * author: ODM
 * date: 2019/9/18
 */
public class HomeViewModel extends BaseViewModel {

    public HomeViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<String> garbageName = new MutableLiveData<>();

    public void search() {
        Logger.d("用户选择搜索的内容是" + garbageName);
    }



}
