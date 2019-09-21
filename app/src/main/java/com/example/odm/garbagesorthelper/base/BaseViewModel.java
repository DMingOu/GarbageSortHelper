package com.example.odm.garbagesorthelper.base;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ViewModel;

import com.example.odm.garbagesorthelper.model.RepositoryManager;
import com.example.odm.garbagesorthelper.model.source.http.ApiService;
import com.example.odm.garbagesorthelper.model.source.http.HttpDataSource;
import com.example.odm.garbagesorthelper.model.source.http.HttpDataSourceImpl;
import com.example.odm.garbagesorthelper.model.source.local.LocalDataSource;
import com.example.odm.garbagesorthelper.model.source.local.LocalDataSourceImpl;
import com.orhanobut.logger.Logger;
import com.trello.rxlifecycle2.LifecycleProvider;

import java.lang.ref.WeakReference;

/**
 * description: ViewModel基类
 * author: ODM
 * date: 2019/9/17
 */
public  class BaseViewModel <M extends  BaseModel>extends AndroidViewModel implements LifecycleObserver {



    protected M model;


    //弱引用持有
    private WeakReference<LifecycleProvider> lifecycle;

    public BaseViewModel(@NonNull Application application) {
       super(application);

    }

//    public BaseViewModel(@NonNull Application application , M model) {
//        this(application);
//        this.model = model;
//    }



    /**
     * 注入RxLifecycle生命周期
     *
     * @param lifecycle
     */
    public void injectLifecycleProvider(LifecycleProvider lifecycle) {
        this.lifecycle = new WeakReference<>(lifecycle);
    }

    public LifecycleProvider getLifecycleProvider() {
        return lifecycle.get();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if(model != null) {
            model.onClear();
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
    public void onAny(LifecycleOwner owner, Lifecycle.Event event) {
    }



    public void setModel(M model) {
        this.model = model;
    }

}
