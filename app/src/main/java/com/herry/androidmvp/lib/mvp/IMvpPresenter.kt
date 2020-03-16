package com.herry.androidmvp.lib.mvp

interface IMvpPresenter<in V> {
    fun onAttach(view: V)

    fun onDetach()

    fun onLaunch()

    fun onPause()
}