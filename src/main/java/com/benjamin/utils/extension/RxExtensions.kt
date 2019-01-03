package com.benjamin.utils.extension

import com.benjamin.http.ErrorHandleObserver
import com.benjamin.http.exception.HttpException
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.schedulers.Schedulers

/**
 * @author  Ben
 * @date 2018/12/29
 */


fun <T> Observable<T>.io2main(): Observable<T> {
    return compose { upstream -> upstream.subscribeOn(Schedulers.io()).observeOn(mainThread()) }
}

//fun <T> io2main(): ObservableTransformer<T, T> {
//    return ObservableTransformer { upstream -> upstream.subscribeOn(Schedulers.io()).observeOn(mainThread()) }
//}

fun <T> Observable<T>.subscribeBy(onSuccess: (data: T) -> Unit) {
    return subscribeBy(onSuccess, {})
}

fun <T> Observable<T>.subscribeBy(onSuccess: (data: T) -> Unit, onFailure: (e: HttpException) -> Unit) {
    return subscribe(onSuccess, onFailure)
}

private fun <T> Observable<T>.subscribe(onSuccess: (data: T) -> Unit, onFailure: (e: HttpException) -> Unit) {
    /*return subscribe(object : Observer<T> {
        override fun onComplete() {
        }

        override fun onSubscribe(d: Disposable) {
        }

        override fun onNext(t: T) {
        }

        override fun onError(e: Throwable) {
        }

    })*/
    return subscribe(object : ErrorHandleObserver<T>() {
        override fun onSuccess(t: T) {
            onSuccess.invoke(t)
        }

        override fun onFailure(exception: HttpException) {
            onFailure.invoke(exception)
        }

    })
}
