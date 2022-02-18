package net.fearlessplus.ui.base

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import io.sbox.library.extensions.trace
import io.sbox.library.utils.NetworkStateLiveData
import net.fearlessplus.preferences.AppPreference
import io.sbox.library.extensions.hideLoading


@Suppress("DEPRECATION")
abstract class BaseActivity : AppCompatActivity() {


    companion object {
        var activityStack: ArrayList<BaseActivity>? = null

        val hasActivity: Boolean
            get() {
                activityStack?.let {
                    return it.size > 0
                }
                return false
            }

        fun getTopActivity(): BaseActivity? {
            return activityStack?.let {
                it.getOrNull(it.size - 1)
            }
            return null
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppPreference.localeWrap(baseContext, applicationContext)

        if (activityStack == null) activityStack = ArrayList()
        activityStack!!.add(this)

//        setStatusBarColor(getColorFromRes(R.color.bg_base))

        var networkStateLiveData = NetworkStateLiveData(applicationContext)

        networkStateLiveData.observe(this, Observer { networkState ->
            trace("### networkStateLiveData -> $networkState : ${AppPreference.isNetWorkAvailable}")
            AppPreference.isNetWorkAvailable = networkState


        })

    }



    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(AppPreference.localeWrap(newBase, newBase.applicationContext))
    }


    fun setStatusBarColor(color: Int) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = color

            val decor = window.decorView
            //decor.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            val isNightMode =
                (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES
            decor.systemUiVisibility =
                if (isNightMode) 0 else View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }

    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
    }


    override fun onResume() {
        super.onResume()
    }

    override fun onStop() {
        super.onStop()
        hideLoading()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (activityStack != null) {
            activityStack!!.remove(this)
        }
    }

    open fun setPageTitle(pageTitle: String) {
//        appbar_title?.text = pageTitle
    }
}



