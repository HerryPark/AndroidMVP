package com.herry.androidmvp.sample.app.main

import com.herry.androidmvp.lib.mvp.IMvpView
import com.herry.androidmvp.sample.app.base.BaseMvpPresent

interface MainContract {
    interface View : IMvpView<Presenter> {
        fun onLaunched()
        fun onCounts(counts: Int)
    }

    abstract class Presenter : BaseMvpPresent<View>() {
        abstract fun increase()
        abstract fun decrease()
    }
}