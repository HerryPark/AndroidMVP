package com.herry.androidmvp.lib.util

import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.WindowManager
import com.herry.androidmvp.lib.helper.ApiHelper

object ViewUtil {
    fun makeFullScreenWithTransparentStatusBar(activity: Activity?) {
        activity ?: return
        activity.window ?: return

        val window = activity.window
        if (ApiHelper.hasLollipop()) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            val decorView = window.decorView
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                decorView.systemUiVisibility =
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            } else {
                decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            }
            window.statusBarColor = Color.TRANSPARENT
        }
    }
}