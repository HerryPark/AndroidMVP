package com.herry.androidmvp.lib.util

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.app.ActivityManager
import android.app.ActivityManager.RunningAppProcessInfo
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.PowerManager
import android.provider.Settings
import android.view.KeyEvent
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.annotation.IdRes
import androidx.annotation.RawRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction
import com.herry.androidmvp.lib.helper.ApiHelper
import java.io.ByteArrayOutputStream
import java.io.IOException

/**
 * Created by herry.park
 */
object AppUtil {
    fun pressBackKey(activity: Activity?): Boolean {
        if (null == activity) {
            return false
        }
        activity.runOnUiThread(
            Runnable { // pressed back
                activity.dispatchKeyEvent(
                    KeyEvent(
                        KeyEvent.ACTION_DOWN,
                        KeyEvent.KEYCODE_BACK
                    )
                )
                activity.dispatchKeyEvent(
                    KeyEvent(
                        KeyEvent.ACTION_UP,
                        KeyEvent.KEYCODE_BACK
                    )
                )
            })
        return true
    }

    fun pressBackKey(activity: Activity?, rootView: View?): Boolean {
        if (null == activity) {
            return false
        }
//        ViewUtil.hideSoftKeyboard(activity, rootView)
        activity.runOnUiThread(
            Runnable { // pressed back
                activity.dispatchKeyEvent(
                    KeyEvent(
                        KeyEvent.ACTION_DOWN,
                        KeyEvent.KEYCODE_BACK
                    )
                )
                activity.dispatchKeyEvent(
                    KeyEvent(
                        KeyEvent.ACTION_UP,
                        KeyEvent.KEYCODE_BACK
                    )
                )
            })
        return true
    }

    fun pressKey(activity: Activity?, keycode: Int): Boolean {
        if (null == activity) {
            return false
        }
        activity.runOnUiThread(
            Runnable { // pressed back
                activity.dispatchKeyEvent(
                    KeyEvent(
                        KeyEvent.ACTION_DOWN,
                        keycode
                    )
                )
                activity.dispatchKeyEvent(
                    KeyEvent(
                        KeyEvent.ACTION_UP,
                        keycode
                    )
                )
            })
        return true
    }

    fun isScreenOn(context: Context?): Boolean {
        if (null != context) {
            val powerManager: PowerManager? =
                context.getSystemService(Context.POWER_SERVICE) as PowerManager
            powerManager?.isInteractive
        }
        return false
    }

    fun wakeUpScreen(context: Context?, timeout: Long) {
        if (null != context) {
            val powerManager: PowerManager? =
                context.getSystemService(Context.POWER_SERVICE) as PowerManager
            if (null != powerManager) {
                val wakeLock = powerManager.newWakeLock(
                    PowerManager.ACQUIRE_CAUSES_WAKEUP, "MyApp::MyWakelockTag"
                )
                wakeLock.acquire(timeout)
            }
        }
    }

    /**
     * Indicates whether the specified action can be used as an intent. This
     * method queries the package manager for installed packages that can
     * respond to an intent with the specified action. If no suitable package is
     * found, this method returns false.
     * http://android-developers.blogspot.com/2009/01/can-i-use-this-intent.html
     *
     * @param context The application's environment.
     * @param action The Intent action to check for availability.
     *
     * @return True if an Intent with the specified action can be sent and
     * responded to, false otherwise.
     */
    fun isIntentAvailable(
        context: Context?,
        action: String?
    ): Boolean {
        if (null != context) {
            val packageManager = context.packageManager
            if (null != packageManager) {
                val intent = Intent(action)
                val list =
                    packageManager.queryIntentActivities(
                        intent,
                        PackageManager.MATCH_DEFAULT_ONLY
                    )
                return list.size > 0
            }
        }
        return false
    }

    fun isForegroundApp(
        context: Context?,
        packageName: String?
    ): Boolean {
        context ?: return false
        packageName ?: return false

        var _context: Context = context
        if (_context is Activity) {
            _context = context.applicationContext
        }
        val activityManager: ActivityManager = _context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val processes =
            activityManager.runningAppProcesses
        for (info in processes) {
            if (null != info) {
                if (info.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    if (null != info.processName && info.processName == packageName) {
                        return true
                    }
                }
            }
        }
        return false
    }

    fun getTopActivityName(context: Context?): String? {
        if (null == context) {
            return null
        }
        val am: ActivityManager? = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        var topActivityName: String? = null
        if (null != am) {
            val tasks = am.runningAppProcesses
            if (null != tasks && null != tasks[0]) {
                if (tasks[0]!!.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    topActivityName = tasks[0]!!.processName
                }
            }
        }
        return topActivityName
    }

    fun isRunningActivity(
        context: Context?,
        activityClass: Class<*>?
    ): Boolean {
        if (null == context || null == activityClass || null == activityClass.canonicalName) {
            return false
        }
        val activityManager =
            context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
                ?: return false
        val tasks =
            activityManager.getRunningTasks(Int.MAX_VALUE)
        if (null != tasks) {
            for (task in tasks) {
                if (null != task && null != task.baseActivity) {
                    if (activityClass.canonicalName
                            .equals(task.baseActivity!!.className, ignoreCase = true)
                    ) return true
                }
            }
        }
        return false
    }

    fun isRunningApp(context: Context?): Boolean {
        if (null == context || null == context.packageName) {
            return false
        }
        val activityManager =
            context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
                ?: return false
        val processInfo =
            activityManager.runningAppProcesses
        if (null != processInfo) {
            for (index in processInfo.indices) {
                if (null != processInfo[index] && null != processInfo[index]!!.processName) {
                    if (processInfo[index]!!.processName == context.packageName) {
                        return true
                    }
                }
            }
        }
        return false
    }

    fun isInstalledApplication(
        context: Context?,
        packageName: String?
    ): Boolean {
        context ?: return false
        if (packageName.isNullOrBlank()) return false

        val pm = context.packageManager ?: return false
        try {
            pm.getApplicationInfo(packageName, PackageManager.GET_META_DATA)
        } catch (e: Exception) {
            return false
        }
        return true
    }

    fun setTranslucentStatusBar(window: Window?) {
        if (null == window) {
            return
        }
        val sdkInt = Build.VERSION.SDK_INT
        if (sdkInt >= Build.VERSION_CODES.LOLLIPOP) {
            setTranslucentStatusBarLollipop(window)
        } /* else if (sdkInt >= Build.VERSION_CODES.KITKAT) {
//            setTranslucentStatusBarKiKat(window);
        }*/
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun setTranslucentStatusBarLollipop(window: Window?) {
        if (null != window && null != window.context) {
//            window.statusBarColor = ViewUtil.getColor(
//                window.context,
//                android.R.color.transparent
//            )
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private fun setTranslucentStatusBarKiKat(window: Window?) {
        window?.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
    }

    fun readRawToString(context: Context?, @RawRes rawID: Int): String? {
        var result: String? = null
        val byteArray = readRawToByteArray(context, rawID)
        if (null != byteArray) {
            result = String(byteArray)
        }
        return result
    }

    fun readRawToByteArray(
        context: Context?,
        @RawRes rawID: Int
    ): ByteArray? {
        if (null == context) {
            return null
        }
        val resources = context.resources ?: return null
        val inputStream = resources.openRawResource(rawID)
        val byteArrayOutputStream = ByteArrayOutputStream()
        var result: ByteArray? = null
        var byteIndex: Int
        try {
            byteIndex = inputStream.read()
            while (byteIndex != -1) {
                byteArrayOutputStream.write(byteIndex)
                byteIndex = inputStream.read()
            }
            result = byteArrayOutputStream.toByteArray()
            inputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return result
    }

    fun restartApplication(callActivity: Activity?, targetClass: Class<*>?) {
        if (null == callActivity || null == targetClass) {
            return
        }
        val intent = Intent(callActivity, targetClass)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        callActivity.startActivity(intent)
    }

    fun setFragment(
        activity: FragmentActivity?,
        @IdRes containerViewId: Int,
        fragment: Fragment?,
        option: FragmentAddingOption?
    ) {
        if (null == activity || null == fragment || null == option) {
            return
        }
        if (0 == containerViewId) {
            return
        }
        val fragmentManager =
            activity.supportFragmentManager ?: return
        val fragmentTransaction =
            fragmentManager.beginTransaction()
        if (setFragment(fragmentTransaction, containerViewId, fragment, option)) {
            fragmentTransaction.commit()
        }
    }

    fun setFragment(
        transaction: FragmentTransaction?,
        @IdRes containerViewId: Int,
        fragment: Fragment?,
        option: FragmentAddingOption
    ): Boolean {
        if (null == transaction) {
            return false
        }
        val customAnimations = option.customAnimations
        if (null != customAnimations) {
            val enter = customAnimations.enter
            val exit = customAnimations.exit
            val popEnter = customAnimations.popEnter
            val popExit = customAnimations.popExit
            transaction.setCustomAnimations(enter, exit, popEnter, popExit)
        } else {
            @SuppressLint("WrongConstant")
            when (option.transit) {
                FragmentTransaction.TRANSIT_NONE,
                FragmentTransaction.TRANSIT_FRAGMENT_OPEN,
                FragmentTransaction.TRANSIT_FRAGMENT_CLOSE,
                FragmentTransaction.TRANSIT_FRAGMENT_FADE -> {
                    transaction.setTransition(option.transit)
                }
            }
        }
        if (option.isReplace) {
            transaction.replace(containerViewId, fragment!!, option.tag)
        } else {
            transaction.add(containerViewId, fragment!!, option.tag)
        }
        if (option.isAddToBackStack) {
            transaction.addToBackStack(option.tag)
        }
        return true
    }

    fun setChildFragment(
        base: Fragment?,
        @IdRes containerViewId: Int,
        child: Fragment?,
        option: FragmentAddingOption?
    ) {
        if (null == base || null == child || null == option) {
            return
        }
        if (0 == containerViewId) {
            return
        }
        val fragmentManager = base.childFragmentManager
        val fragmentTransaction =
            fragmentManager.beginTransaction()
        if (setFragment(fragmentTransaction, containerViewId, child, option)) {
            fragmentTransaction.commit()
        }
    }

    fun <T> findFragmentByTag(activity: FragmentActivity?, fragmentClass: Class<T>?): T? {
        if (null == activity || null == fragmentClass) {
            return null
        }
        val tag = fragmentClass.simpleName
        return findFragmentByTag(activity, fragmentClass, tag)
    }

    fun <T> findFragmentByTag(
        activity: FragmentActivity?,
        fragmentClass: Class<T>?,
        tag: String?
    ): T? {
        if (null == activity || null == fragmentClass || null == tag) {
            return null
        }
        val fragmentManager =
            activity.supportFragmentManager ?: return null
        val fragment = fragmentManager.findFragmentByTag(tag)
        return if (null != fragment && fragmentClass.isInstance(fragment)) {
            fragmentClass.cast(fragment)
        } else null
    }

    fun <T> findFragmentByTag(activity: FragmentActivity?, tag: String?): T? {
        if (null == activity || null == tag) {
            return null
        }
        val fragmentManager =
            activity.supportFragmentManager ?: return null
        val fragment = fragmentManager.findFragmentByTag(tag)
        return if (null != fragment) {
            fragment as T
        } else null
    }

    fun isCanDrawOverlays(context: Context): Boolean {
        return ApiHelper.hasMarshmallow() && Settings.canDrawOverlays(context)
    }

    @JvmOverloads
    fun gotoMarket(
        activity: Activity?,
        packageName: String,
        checkAppInstall: Boolean = true
    ): Boolean {
        if (null == activity) {
            return false
        }
        var gotoMarket = true
        if (checkAppInstall) {
            gotoMarket = !isInstalledApplication(activity, packageName)
        }
        if (gotoMarket) {
            try {
                val uri =
                    Uri.parse("market://details?id=$packageName")
                activity.startActivity(Intent(Intent.ACTION_VIEW, uri))
                return true
            } catch (ignore: ActivityNotFoundException) {
            }
        }
        return false
    }

    @Throws(Exception::class)
    fun getMetaDataValueFormManifest(
        context: Context,
        key: String
    ): String {
        var apiKey = ""
        val e = context.packageName
        val ai = context
            .packageManager
            .getApplicationInfo(e, PackageManager.GET_META_DATA)
        val bundle = ai.metaData
        if (bundle != null) {
            apiKey = bundle.getString(key, "")
        }
        return apiKey
    }
}