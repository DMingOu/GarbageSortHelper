package com.example.odm.garbagesorthelper.ui.search;

import android.app.Application;

import androidx.lifecycle.MutableLiveData;

import com.example.odm.garbagesorthelper.R;
import com.example.odm.garbagesorthelper.base.BaseViewModel;
import com.example.odm.garbagesorthelper.model.RepositoryManager;
import com.example.odm.garbagesorthelper.model.entity.GarbageData;
import com.example.odm.garbagesorthelper.net.HttpThrowable;
import com.example.odm.garbagesorthelper.net.ObserverManager;
import com.example.odm.garbagesorthelper.net.RetrofitManager;
import com.orhanobut.logger.Logger;

import java.util.List;

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
    public MutableLiveData<List<GarbageData.DataBean>> sortedList = new MutableLiveData<>();


    public void onSearch() {


        RetrofitManager.getInstance()
                .getApiService()
                .getGarbageData(garbageName.getValue())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ObserverManager<GarbageData>() {
                    @Override
                    public void onError(HttpThrowable httpThrowable) {
                        Logger.d("异常原因:  "+httpThrowable.message);
                    }

                    @Override
                    public void onNext(GarbageData garbageData) {
                        if (garbageData != null) {
                                //将成功查询到的 列表加入 垃圾分类列表中
                                sortedList.setValue(garbageData.getData());
                        }
                    }
                });
    }

    public int getGarbageIcon(int garbageType){
        switch (garbageType) {

            case 0:
                return R.drawable.module_search_cookiebar_fail_garbage;
            case 1 :
                return R.drawable.module_search_cookiebar_dry_garbage;

            case 2 :
                return R.drawable.module_search_cookiebar_wet_garbage;

            case 3:
                return R.drawable.module_search_cookiebar_recycle_garbage;

            case 4:
                return R.drawable.module_search_cookiebar_harmful_garbage;
            default:
                return R.drawable.module_search_cookiebar_fail_garbage;

        }
    }

}
