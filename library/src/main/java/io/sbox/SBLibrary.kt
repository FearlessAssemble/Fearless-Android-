package io.sbox.library

import android.app.Application
import android.content.Context
import io.sbox.library.base.BasePreferences
import io.sbox.library.debug.Debugger
import io.sbox.library.extensions.trace

class SBLibrary {

    companion object {
        fun startInit(context: Context): Builder {
            BasePreferences.init(context)



            return Builder(context)
        }

    }


}

class Builder(val context: Context) {

    enum class AppStatus {
        // check from onResume,onStop
        BACKGROUND,                // app is background
        RETURNED_TO_FOREGROUND,    // app returned to foreground(or first launch) (in onResume
        FOREGROUND;                // app is foreground (in onStop
    }


    companion object {
        var context: Context? = null

        private var mAppStatus: AppStatus = AppStatus.FOREGROUND
        var isReturnedToForeground: Boolean = false
            get() = mAppStatus == AppStatus.RETURNED_TO_FOREGROUND
        var isBackground: Boolean = false
            get() = mAppStatus.ordinal == AppStatus.BACKGROUND.ordinal
        var isForeground: Boolean = false
            get() = mAppStatus.ordinal > AppStatus.BACKGROUND.ordinal

    }

    init {
        Builder.context = context

        if (context is Application) {
//            context.registerActivityLifecycleCallbacks(ActivityLifecycleCallbacks())


        }

    }

    fun isDebugMode(emails: ArrayList<String>? = null) {

        if (BuildConfig.DEBUG) {

            val savedUncaughtExceptionHandler =
                Thread.getDefaultUncaughtExceptionHandler()
            Thread.setDefaultUncaughtExceptionHandler { thread: Thread?, e: Throwable? ->
                trace("## CRASH ERROR ##################################")
                e?.printStackTrace()
                trace("#################################################")
                Debugger.createLog(context)

                savedUncaughtExceptionHandler.uncaughtException(thread, e)
            }
            Debugger.showNotification(context, emails)
        }
    }
/*


    inner class ActivityLifecycleCallbacks : Application.ActivityLifecycleCallbacks {
        var running: Int = 0

        override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
        }

        override fun onActivityStarted(activity: Activity?) {
            if (++running == 1) {
                mAppStatus = AppStatus.RETURNED_TO_FOREGROUND
            } else if (running > 1) {
                mAppStatus = AppStatus.FOREGROUND
            }
        }

        override fun onActivityResumed(activity: Activity?) {
        }

        override fun onActivityPaused(activity: Activity?) {
        }

        override fun onActivityStopped(activity: Activity?) {
            if (--running == 0) {
                mAppStatus = AppStatus.BACKGROUND
            }
        }

        override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {
        }

        override fun onActivityDestroyed(activity: Activity?) {
        }
    }

*/

}



