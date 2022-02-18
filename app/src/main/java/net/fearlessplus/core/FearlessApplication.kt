package net.fearlessplus.core

import android.app.Application
import android.content.Context
import android.os.Build
import android.webkit.WebView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import com.bumptech.glide.Glide
import com.facebook.stetho.Stetho
import io.sbox.library.base.BasePreferences
import net.fearlessplus.BuildConfig
import net.fearlessplus.notification.OnesignalConfig

class FearlessApplication : Application() {

    companion object {
        var instance: FearlessApplication? = null
            private set

        var isDebugMode: Boolean = false

        fun changeBuilderContext(context: Context) {

        }
    }

    override fun onCreate() {
        super.onCreate()

        instance = this
        isDebugMode = BuildConfig.DEBUG

        val appLifecycleObserver = AppLifecycleObserver()
        ProcessLifecycleOwner.get().lifecycle.addObserver(appLifecycleObserver)

        if (isDebugMode) {
            Stetho.initializeWithDefaults(this)
        }
        BasePreferences.init(this)

        OnesignalConfig.initOnesignal(this)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            try {
                WebView.setDataDirectorySuffix(getProcessName())
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }



    }


    class AppLifecycleObserver : LifecycleObserver {
        @OnLifecycleEvent(Lifecycle.Event.ON_START)
        open fun onEnterForeground() {
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
        open fun onEnterBackground() {

        }
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)

        Glide.get(this).trimMemory(level)
    }

}



