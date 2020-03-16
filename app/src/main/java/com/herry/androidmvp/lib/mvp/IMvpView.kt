package com.herry.androidmvp.lib.mvp

import android.content.Context

interface IMvpView<P> {
    var presenter: P?

    /**
     * Context
     */
    fun getViewContext(): Context?

    /**
     * Shows loading view
     */
    fun showViewLoading()

    /**
     * Hides loading view
     * @param cancel if cancel is true, it will be show "cancel" view and then hide it.
     * if success is true, it will be show "done" view and then hide it.
     */
    fun hideViewLoading(success: Boolean)

    fun error(throwable: Throwable)
}