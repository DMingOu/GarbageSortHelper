package com.example.odm.garbagesorthelper.base

import android.app.Application
import androidx.lifecycle.*
import com.trello.rxlifecycle2.LifecycleProvider
import java.lang.ref.WeakReference

/**
 * description: ViewModel基类
 * author: ODM
 * date: 2019/9/17
 */
open class BaseViewModel<M : BaseModel?>(application: Application) : AndroidViewModel(application), LifecycleObserver {

    protected var model: M? = null

    //弱引用持有
    private var lifecycle: WeakReference<LifecycleProvider<*>>? = null

    //    public BaseViewModel(@NonNull Application application , M model) {
//        this(application);
//        this.model = model;
//    }

    /**
     * 注入RxLifecycle生命周期
     *
     * @param lifecycle
     */
    fun injectLifecycleProvider(lifecycle: LifecycleProvider<*>) {
        this.lifecycle = WeakReference(lifecycle)
    }

    val lifecycleProvider: LifecycleProvider<*>
        get() = lifecycle!!.get()!!

    override fun onCleared() {
        super.onCleared()
        model ?.onClear()

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
    fun onAny(owner: LifecycleOwner?, event: Lifecycle.Event?) {
    }


}