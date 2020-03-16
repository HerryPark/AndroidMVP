package com.herry.androidmvp.sample.app.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.herry.androidmvp.R
import com.herry.androidmvp.lib.helper.ToastHelper
import com.herry.androidmvp.sample.app.base.BaseMvpView
import kotlinx.android.synthetic.main.main_fragment.view.*

class MainFragment: BaseMvpView<MainContract.View, MainContract.Presenter>(), MainContract.View {

    private var container : View? = null

    override fun onCreatePresenter(): MainContract.Presenter? = MainPresenter()

    override fun onCreatePresenterView(): MainContract.View = this

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (this.container == null) {
            this.container = inflater.inflate(R.layout.main_fragment, container, false)
            init(this.container)
        }
        return this.container
    }

    private fun init(view: View?) {
        view ?: return

        // decrease
        view.main_fragment_decrease?.setOnClickListener {
            presenter?.decrease()
        }

        // increase
        view.main_fragment_increase?.setOnClickListener {
            presenter?.increase()
        }
    }

    override fun onLaunched() {
        ToastHelper.showToast(activity, "launched main")
    }

    @SuppressLint("SetTextI18n")
    override fun onCounts(counts: Int) {
        container?.main_fragment_counts?.text = "Counts: $counts"
    }
}