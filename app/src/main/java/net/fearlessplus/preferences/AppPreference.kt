package net.fearlessplus.preferences

import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import io.sbox.library.base.BasePreferences
import net.fearlessplus.core.FearlessApplication
import java.util.*


@Suppress("DEPRECATION")
class AppPreference: BasePreferences() {

    companion object {

        fun resetCommit() {
            base.setCommit("uuid", UUID.randomUUID().toString())
        }
        fun reset() {
            base.set("uuid", UUID.randomUUID().toString())
        }



        var isInitStart: Boolean = false
            get() {
                return base.get("isInitStart", true)
            }
            set(value) {
                field = base.setCommit("isInitStart", value)
            }


        var setttingNotiInstagram: Boolean = false
            get() {
                return base.get("setttingNotiInstagram", true)
            }
            set(value) {
                field = base.setCommit("setttingNotiInstagram", value)
            }
        var setttingNotiTwitter: Boolean = false
            get() {
                return base.get("setttingNotiTwitter", true)
            }
            set(value) {
                field = base.setCommit("setttingNotiTwitter", value)
            }

        var setttingNotiNotice: Boolean = false
            get() {
                return base.get("setttingNotiNotice", false)
            }
            set(value) {
                field = base.setCommit("setttingNotiNotice", value)
            }
        var setttingNotiCollection: Boolean = false
            get() {
                return base.get("setttingNotiCollection", false)
            }
            set(value) {
                field = base.setCommit("setttingNotiCollection", value)
            }
        var setttingNotiEvent: Boolean = false
            get() {
                return base.get("setttingNotiEvent", false)
            }
            set(value) {
                field = base.setCommit("setttingNotiEvent", value)
            }




        var storeUrl: String = ""
            get() = base.get("storeUrl", "https://play.google.com/store/apps/details?id=net.fearlessplus")
            set(value) {
                field = base.set("storeUrl", value)
            }

        var latestSemanticVersion: String = ""
            get() = base.get("latestSemanticVersion", "")
            set(value) {
                field = base.set("latestSemanticVersion", value)
            }

        var latestVersionId: Int = 0
            get() = base.get("latestVersionId", 0)
            set(value) {
                field = base.set("latestVersionId", value)
            }



        fun localeWrap(context: Context, applicContext: Context? = null, langCode: String = LocaleLanguageCode): Context {
            applicContext?.let {
                val activityRes = it.resources
                val activityConf = activityRes.configuration
                val newLocale = Locale(langCode)
                activityConf.setLocale(newLocale)
                activityRes.updateConfiguration(activityConf, activityRes.displayMetrics)
                FearlessApplication.changeBuilderContext(context)
            }

            context.resources?.apply {
                val config = configuration
                config.setLocale(Locale(langCode))
                updateConfiguration(config, displayMetrics)

                return context.createConfigurationContext(config)
            }
            return context
        }


        var languageCode: String = ""
            get() = base.get("languageCode", Locale.US.language + "-" + Locale.US.country)
            set(value) {
                field = base.set("languageCode", value)
            }

        var LocaleLanguageCode: String = "en"
            get() {
                var languageAndLocale = languageCode.split("-")
                return languageAndLocale[0]
            }

        var LocaleCode: Locale = Locale.US
            get() {
                var languageAndLocale = languageCode.split("-")
                return Locale(languageAndLocale[0], languageAndLocale[1])
            }




        var isNetWorkAvailable: Boolean = true
            get() = base.get("is_network_available", true)
            set(value) {
                field = base.set("is_network_available", value)
            }

        var themeMode: Int = getDefaultMode()
            get() = base.get("themeMode", getDefaultMode())
            set(value) {
                field = base.setCommit("themeMode", value)
            }

        fun getDefaultMode (): Int {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            } else {
                AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY
            }
        }

    }

}