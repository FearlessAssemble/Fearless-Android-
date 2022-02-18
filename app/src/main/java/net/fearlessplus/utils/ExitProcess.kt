package net.fearlessplus.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import net.fearlessplus.ui.base.BaseActivity
import kotlin.system.exitProcess


class ExitProcess {
    companion object {
        const val EXTRA_KEY_DONT_FINISH_ACTIVITY = "extra_key_dont_finish_activity"
        const val INTENT_FINISH_ACTIVITY = "intent_finish_activity"
        fun restartApp(context: Context) {
            (context as BaseActivity).finishAffinity()
            val packageManager = context.packageManager
            val intent = packageManager.getLaunchIntentForPackage(context.packageName)
            val componentName = intent!!.component
            val mainIntent: Intent = Intent.makeRestartActivityTask(componentName)
            context.startActivity(mainIntent)
            exitProcess(0)
        }

        fun endApp(context: Context) {
            if (Build.VERSION.SDK_INT >= 21) {
                (context as BaseActivity).finishAndRemoveTask()
            }
            (context as BaseActivity).finishAffinity()
            System.runFinalization()
            exitProcess(0)
        }
    }

    class ExitActivity : Activity() {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
//            if (Build.VERSION.SDK_INT >= 21) {
//                finishAndRemoveTask()
//            }
//
//            finishAffinity()
//            System.runFinalization()
//            exitProcess(0)
//            Process.killProcess(Process.myPid())
//            finish()
        }
    }
}