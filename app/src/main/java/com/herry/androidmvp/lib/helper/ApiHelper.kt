package com.herry.androidmvp.lib.helper

import android.os.Build

class ApiHelper {

    companion object {
        fun hasLollipop(): Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP

        fun hasMarshmallow(): Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M

        fun hasNougat(): Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.N // 24

        fun hasNougatPlusPlus(): Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1 // 25

        fun hasOreo(): Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
    }
}