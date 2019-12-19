package com.example.odm.garbagesorthelper.model.source.net

import android.util.Log
import com.orhanobut.logger.Logger
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

/**
 * Observer的继承类,优化了对各类网络异常的处理
 * @author: ODM
 * @date: 2019/8/17
 */
abstract class ObserverManager<T> : Observer<T> {
    override fun onSubscribe(d: Disposable) {
        Log.d(TAG, "onSubscribe: ")
    }

    override fun onError(e: Throwable) {
        if (e is Exception) {
            Logger.d("此错误的信息: " + e.message)
            onError(ThrowableHandler.handleThrowable(e))
        } else {
            onError(HttpThrowable(HttpThrowable.Companion.UNKNOWN, "未知错误", e))
        }
    }

    override fun onComplete() {}
    /**
     * 强制实现，异常的处理
     * @param httpThrowable 网络异常
     */
    abstract fun onError(httpThrowable: HttpThrowable)

    /**
     * 强制实现，对回调数据的处理
     */
    abstract override fun onNext(t: T)

    companion object {
        private const val TAG = "ObserverManager"
    }
}