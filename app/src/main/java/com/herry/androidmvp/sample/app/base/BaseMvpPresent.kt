package com.herry.androidmvp.sample.app.base

import com.herry.androidmvp.lib.mvp.IMvpPresenter

abstract class BaseMvpPresent<V>: IMvpPresenter<V> {

    protected var view: V? = null
        private set

    private var launched = false

    override fun onAttach(view: V) {
        this.view = view
    }

    override fun onDetach() {
        this.view = null
    }

    override fun onLaunch() {
        this.view?.let {
            if(!launched) {
                launched = true
                onLaunched(it)
            } else {
                onResume(it)
            }
        }
    }

    override fun onPause() {
    }

    abstract fun onLaunched(view: V)

    protected open fun onResume(view: V) {}

    protected open fun launched(function: () -> Unit) {
        if (view != null) {
            function.invoke()
        }
    }
}