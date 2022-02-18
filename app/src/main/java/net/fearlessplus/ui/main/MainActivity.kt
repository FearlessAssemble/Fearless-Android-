package net.fearlessplus.ui.main

import android.content.res.ColorStateList
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatDelegate
import androidx.transition.Fade
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.google.gson.Gson
import com.onesignal.OneSignal
import io.sbox.library.extensions.*
import kotlinx.android.synthetic.main.activity_main.*
import net.fearlessplus.BuildConfig
import net.fearlessplus.R
import net.fearlessplus.core.EventType
import net.fearlessplus.extensions.toStringFromRes
import net.fearlessplus.model.AppInfoModel
import net.fearlessplus.preferences.AppPreference
import net.fearlessplus.ui.base.BaseActivity
import net.fearlessplus.ui.main.aboutbg.AboutBgFragment
import net.fearlessplus.ui.main.bgtube.BgTubeFragment
import net.fearlessplus.ui.main.gallery.GalleryFragment
import net.fearlessplus.ui.main.information.InformationFragment


class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        caseApi(Build.VERSION_CODES.N) {
            AppCompatDelegate.setDefaultNightMode(AppPreference.themeMode)
        }

        FirebaseRemoteConfig.getInstance().apply {
            val configSettings = FirebaseRemoteConfigSettings.Builder().apply {
                minimumFetchIntervalInSeconds = 0
            }.build()
            setConfigSettingsAsync(configSettings)
//            setDefaultsAsync(mapOf())

            var cacheExpiration: Long = 3600
            fetch(cacheExpiration)
                .addOnCompleteListener {
                    if (it.isSuccessful()) {
                        fetchAndActivate()

                        try {
                            // packageManager.getPackageInfo(packageName, 0)?.versionName
                            val vo =
                                Gson().fromJson(getString("app_info"), AppInfoModel::class.java)
                            vo.sync()

                            if (vo.data.isUpdateImmediate) {
                                showDialog(
                                    R.string.update.toStringFromRes,
                                    R.string.update_message_immediate.toStringFromRes,
                                    R.string.update.toStringFromRes to { openAppInPlayStore() },
                                    R.string.close.toStringFromRes to { finish() },
                                    enableCancel = false
                                )
                            } else if (vo.data.isUpdateFlexible) {
                                showSnackBar(
                                    R.string.update_message_flexible.toStringFromRes,
                                    R.string.update.toStringFromRes to { openAppInPlayStore() },
                                    R.string.later.toStringFromRes to null
                                )
                            }

                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }
        }

        var fade = Fade()
        fade.excludeTarget(android.R.id.statusBarBackground, true)
        fade.excludeTarget(android.R.id.navigationBarBackground, true)

        val states = arrayOf(
            intArrayOf(-android.R.attr.state_checked),
            intArrayOf(android.R.attr.state_checked)
        )
        val colors = intArrayOf(getColorFromRes(R.color.gray02), getColorFromRes(R.color.orange))
        nav_view.itemIconTintList = ColorStateList(states, colors)
        nav_view.itemTextColor = ColorStateList(states, colors)

//        nav_view.setupWithNavController(findNavController(R.id.nav_host_fragment))

        nav_view.setOnNavigationItemSelectedListener {
            replaceFragment(it)
        }

        nav_view.selectedItemId = savedInstanceState?.let { savedInstanceState ->
            savedInstanceState.getInt("nav_view", R.id.navigation_bg_tube)
        } ?: if(BuildConfig.DEBUG) R.id.navigation_gallery else R.id.navigation_bg_tube

        nav_view.setOnNavigationItemReselectedListener {
            dispatcherEvent(EventType.NAVIGATION_RESELECTED)
        }

        if (AppPreference.isInitStart) {
            AppPreference.isInitStart = false
            OneSignal.sendTag("Instagram", if (AppPreference.setttingNotiInstagram) "1" else "0")
            OneSignal.sendTag("Twitter", if (AppPreference.setttingNotiTwitter) "1" else "0")
            OneSignal.sendTag("GalleryNotice", if (AppPreference.setttingNotiNotice) "1" else "0")
            OneSignal.sendTag("GalleryFund", if (AppPreference.setttingNotiCollection) "1" else "0")
            OneSignal.sendTag("GalleryEvent", if (AppPreference.setttingNotiEvent) "1" else "0")
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("nav_view", nav_view.selectedItemId)
    }

    override fun onBackPressed() {
        super.onBackPressed()
//        finish()
    }


    private fun replaceFragment(item: MenuItem): Boolean {

        val itemId = item.itemId
        val tag = itemId.toString()
        supportFragmentManager.findFragmentByTag(tag) ?: when (itemId) {
            R.id.navigation_gallery -> GalleryFragment()
            R.id.navigation_bg_tube -> BgTubeFragment()
//            R.id.navigation_event -> EventFragment()
            R.id.navigation_information -> InformationFragment()
            R.id.navigation_about_bg -> AboutBgFragment()
            else -> null
        }?.let { fragment ->
            supportFragmentManager.beginTransaction().apply {
                if (fragment.isAdded) show(fragment).commit()
                else replace(R.id.nav_host_fragment, fragment, tag).commit()
                return true
            }

        }
        return false
    }

}