package com.herry.androidmvp.sample.app.main

import androidx.fragment.app.Fragment
import com.herry.androidmvp.sample.app.base.SingleActivity

class MainActivity : SingleActivity() {

    // sets fragment of activity
    override fun getBaseFragment(): Fragment? = MainFragment()
}
