package com.example.odm.garbagesorthelper.model.source.net

import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.functions.Function
import java.net.UnknownHostException
import java.util.concurrent.TimeUnit

/**
 * Observerable 对象链式调用
 * 请求失败重试机制
 * @author: ODM
 * @date: 2019/8/17
 */
class RetryFunction(//延迟重试的时间
        private val retryDelaySeconds: Int, //最大重试次数
        private val retryCountMax: Int) : Function<Observable<Throwable?>, ObservableSource<*>> {
    //记录当前重试次数
    private var retryCount = 0

    @Throws(Exception::class)
    override fun apply(throwableObservable: Observable<Throwable?>): ObservableSource<*> { //方案一：使用全局变量来控制重试次数，重试3次后不再重试，通过代码显式回调onError结束请求
        return throwableObservable.flatMap(object : Function<Throwable?, ObservableSource<*>> {
            @Throws(Exception::class)
            override fun apply(throwable: Throwable): ObservableSource<*> { //如果失败的原因是UnknownHostException（DNS解析失败，当前无网络），则没必要重试，直接回调error结束请求即可
                if (throwable is UnknownHostException) {
                    return Observable.error<Any>(throwable)
                }
                //没超过最大重试次数的话则进行重试
                return if (++retryCount <= retryCountMax) { //延迟retryDelaySeconds后开始重试
                    Observable.timer(retryDelaySeconds.toLong(), TimeUnit.SECONDS)
                } else Observable.error<Any>(throwable)
            }
        })
        /*        方案二：使用zip控制重试次数，重试3次后不再重试（会隐式回调onComplete结束请求，但我需要的是回调onError，所以采用方案一）
            return Observable.zip(throwableObservable,Observable.range(1, retryCountMax),new BiFunction<Throwable, Integer, Throwable>() {
                @Override
                public Throwable apply(Throwable throwable, Integer integer) throws Exception {
                    LogUtil.e("ljy",""+integer);
                    return throwable;
                }
            }).flatMap(new Function<Throwable, ObservableSource<?>>() {
                @Override
                public ObservableSource<?> apply(Throwable throwable) throws Exception {
                    if (throwable instanceof UnknownHostException) {
                        return Observable.error(throwable);
                    }
                    return Observable.timer(retryDelaySeconds, TimeUnit.SECONDS);
                }
            });*/
    }

}