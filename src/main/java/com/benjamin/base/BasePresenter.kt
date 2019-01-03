package com.benjamin.base


interface BasePresenter {

}
/*
open class BasePresenter<V : BaseView>(v: V) : BaseRequestListener {
    private var mReference: Reference<V>? = null

    val iView: V?
        get() = if (this.mReference == null) null else this.mReference!!.get()

    init {
        this.mReference = WeakReference(v)

    }

    fun destroy() {
        this.mReference!!.clear()
        this.mReference = null
    }

    override fun onTokenInvalid() {
        if (this.iView != null) {
            this.iView!!.onUserTokenInvalid()
        }
    }
}*/
