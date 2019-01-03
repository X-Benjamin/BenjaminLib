package com.benjamin.base

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

abstract class BaseFragment : Fragment(), BaseView {
    protected var TAG = ""
    protected abstract fun getLayoutId(): Int

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        this.TAG = this.javaClass.simpleName
        return if (getLayoutId() != 0) inflater.inflate(getLayoutId(), container, false)
        else super.onCreateView(inflater, container, savedInstanceState)
    }

//    private fun <T : View> View.bindView(id: Int): Lazy<T> {
//        return lazy { findViewById<T>(id) }
//    }

    override fun onUserTokenInvalid() {}
}