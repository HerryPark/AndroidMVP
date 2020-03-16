package com.herry.androidmvp.sample.app.base.nav

import android.content.Context
import android.os.Bundle
import com.herry.androidmvp.sample.app.base.activity_caller.module.ACError
import com.herry.androidmvp.lib.mvp.IMvpPresenter
import com.herry.androidmvp.lib.mvp.IMvpView

abstract class NavView<V: IMvpView<P>, P: IMvpPresenter<V>>: NavFragment(), IMvpView<P> {
    override var presenter: P? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.presenter = this.presenter ?: onCreatePresenter()
        this.presenter?.onAttach(onCreatePresenterView()) ?: finishAndResults(null)
    }

    override fun onDestroy() {
        super.onDestroy()

        presenter?.onDetach()
    }

    override fun onResume() {
        super.onResume()

        presenter?.onLaunch()
    }

    override fun onPause() {
        presenter?.onPause()

        super.onPause()
    }

    override fun getViewContext(): Context? = context

    override fun error(throwable: Throwable) {
        aC?.call(
            ACError.Caller(throwable) {
                onError(it)
            }
        )
    }

    open fun onError(throwable: Throwable) {

    }

    abstract fun onCreatePresenter(): P?

    abstract fun onCreatePresenterView(): V

    override fun onTransitionStart() {
        super.onTransitionStart()

        if(presenter is NavPresent<*>) {
            (presenter as NavPresent<*>).navTransitionStart()
        }
    }

    override fun onTransitionEnd() {
        super.onTransitionEnd()

        if(presenter is NavPresent<*>) {
            (presenter as NavPresent<*>).navTransitionEnd()
        }
    }
}