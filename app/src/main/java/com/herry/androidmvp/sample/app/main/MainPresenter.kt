package com.herry.androidmvp.sample.app.main

class MainPresenter : MainContract.Presenter() {
    private var counts: Int = 0

    override fun onLaunched(view: MainContract.View) {
        launched {
            // draws view to UI thread
            view.onLaunched()
            view.onCounts(counts)
        }
    }

    override fun increase() {
        view?.getViewContext() ?: return

        view?.onCounts(++counts)
    }

    override fun decrease() {
        view?.getViewContext() ?: return

        view?.onCounts(--counts)
    }
}