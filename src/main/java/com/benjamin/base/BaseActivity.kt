package com.benjamin.base

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.benjamin.app.AppConfig
import com.benjamin.app.AppManager

abstract class BaseActivity : AppCompatActivity(), BaseView {
    var TAG = ""

    protected val activity: Activity
        get() = this

    protected abstract fun getLayoutId(): Int

    override fun onCreate(savedInstanceState: Bundle?) {
        this.TAG = this.javaClass.name
        AppManager.getAppManager().addActivity(this)
        AppConfig.init(this)
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        initData()
    }

    open fun initData() {
    }

    override fun onDestroy() {
        AppManager.getAppManager().finishActivity(this)
        super.onDestroy()
    }

    protected fun gotoActivity(clazz: Class<*>, bundle: Bundle?) {
        val intent = Intent(this, clazz)
        if (bundle != null) {
            intent.putExtras(bundle)
        }

        this.startActivity(intent)
    }

    protected fun gotoActivity(clazz: Class<*>) {
        this.gotoActivity(clazz, null as Bundle?)
    }

    protected fun gotoActivity(className: String) {
        this.gotoActivity(className, null as Bundle?)
    }

    protected fun gotoActivity(className: String, bundle: Bundle?) {
        try {
            val intent = Intent(this, Class.forName(className))
            if (bundle != null) {
                intent.putExtras(bundle)
            }

            this.startActivity(intent)
        } catch (var4: Exception) {
            var4.printStackTrace()
        }

    }

    protected fun gotoActivityForResult(clazz: Class<*>, bundle: Bundle?, requestCode: Int) {
        val intent = Intent(this, clazz)
        if (bundle != null) {
            intent.putExtras(bundle)
        }

        this.startActivityForResult(intent, requestCode)
    }

    protected fun gotoActivityForResult(clazz: Class<*>, requestCode: Int) {
        this.gotoActivityForResult(clazz, null as Bundle?, requestCode)
    }

//    private fun <T : View> View.bindView(id: Int): Lazy<T> {
//        return lazy { findViewById<T>(id) }
//    }

    protected fun <V : View> bindView(id: Int): Lazy<V> = lazy { findViewById<V>(id) }

    override fun onUserTokenInvalid() {}
}