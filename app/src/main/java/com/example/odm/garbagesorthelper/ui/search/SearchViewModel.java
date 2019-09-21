package com.example.odm.garbagesorthelper.ui.search;

import android.app.Application;

import androidx.lifecycle.MutableLiveData;

import com.example.odm.garbagesorthelper.base.BaseViewModel;
import com.example.odm.garbagesorthelper.model.RepositoryManager;
import com.example.odm.garbagesorthelper.model.entity.Garbage;
import com.example.odm.garbagesorthelper.net.HttpThrowable;
import com.example.odm.garbagesorthelper.net.ObserverManager;
import com.example.odm.garbagesorthelper.net.RetrofitManager;
import com.example.odm.garbagesorthelper.net.RetryFunction;
import com.orhanobut.logger.Logger;

import java.util.List;
import java.util.Random;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * description: 搜索页面ViewModel
 * author: ODM
 * date: 2019/9/19
 */
public class SearchViewModel extends BaseViewModel<RepositoryManager> {



    public SearchViewModel(Application application) {
        super(application);
    }


    /**
     * 用户搜索内容--垃圾名
     */
    public MutableLiveData<String> garbageName = new MutableLiveData<>();

    /**
     * 用户搜索结果--分类垃圾列表
     */
    public MutableLiveData<List> sortedList = new MutableLiveData<>();

    public void onSearch() {



        RetrofitManager.getInstance()
                .getApiService()
                .getGarbageData(garbageName.getValue())
                .retryWhen(new RetryFunction(3 , 3))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ObserverManager<Garbage>() {
                    @Override
                    public void onError(HttpThrowable httpThrowable) {
//                        Logger.d("异常原因:  "+httpThrowable.message);
                    }

                    @Override
                    public void onNext(Garbage garbageData) {
                        if (garbageData != null&& "fail".equals(garbageData.getMsg())) {
                            Logger.d(garbageData.getData().get(0).getGtype());
                        }
                    }
                });

    }




}
