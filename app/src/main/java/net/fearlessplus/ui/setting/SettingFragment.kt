package net.fearlessplus.ui.setting

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.onesignal.OneSignal
import io.sbox.library.extensions.caseApi
import io.sbox.library.extensions.delayed
import io.sbox.library.extensions.safeClick
import io.sbox.library.extensions.visible
import kotlinx.android.synthetic.main.component_appbar.*
import kotlinx.android.synthetic.main.component_appbar.view.*
import kotlinx.android.synthetic.main.fragment_setting.*
import kotlinx.android.synthetic.main.fragment_setting.view.*
import kotlinx.android.synthetic.main.item_toggle_button.view.*
import kotlinx.android.synthetic.main.setting_theme_sheet.view.*
import net.fearlessplus.BuildConfig
import net.fearlessplus.R
import net.fearlessplus.extensions.toColorFromRes
import net.fearlessplus.extensions.toStringFromRes
import net.fearlessplus.preferences.AppPreference
import net.fearlessplus.ui.base.BaseActivity
import net.fearlessplus.ui.base.BaseFragment


class SettingFragment : BaseFragment() {

    companion object {
        @JvmStatic
        fun newInstance(bundle: Bundle?) =
            SettingFragment().apply {
                arguments = bundle
            }
    }

    private var bottomSheetThemeDialog: BottomSheetDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val v = inflater.inflate(R.layout.fragment_setting, container, false)

        v.apply {
            setting_noti_instagram.toggle_button.isChecked = AppPreference.setttingNotiInstagram
            setting_noti_twitter.toggle_button.isChecked = AppPreference.setttingNotiTwitter

            setting_noti_notice.toggle_button.isChecked = AppPreference.setttingNotiNotice
            setting_noti_collection.toggle_button.isChecked = AppPreference.setttingNotiCollection
            setting_noti_event.toggle_button.isChecked = AppPreference.setttingNotiEvent
        }

        context?.let { context ->
            val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val themeView = layoutInflater.inflate(R.layout.setting_theme_sheet, null)

            bottomSheetThemeDialog = BottomSheetDialog(context)
            bottomSheetThemeDialog?.setContentView(themeView)

            themeView?.apply {
                setting_theme_light.safeClick {
                    changeTheme(AppCompatDelegate.MODE_NIGHT_NO)
                }
                setting_theme_dark.safeClick {
                    changeTheme(AppCompatDelegate.MODE_NIGHT_YES)
                }
                setting_theme_default.safeClick {
                    changeTheme(AppPreference.getDefaultMode())
                }
            }
        }




        OneSignal.sendTag("Instagram", if (AppPreference.setttingNotiInstagram) "1" else "0")
        OneSignal.sendTag("Twitter", if (AppPreference.setttingNotiTwitter) "1" else "0")
        OneSignal.sendTag("GalleryNotice", if (AppPreference.setttingNotiNotice) "1" else "0")
        OneSignal.sendTag("GalleryFund", if (AppPreference.setttingNotiCollection) "1" else "0")
        OneSignal.sendTag("GalleryEvent", if (AppPreference.setttingNotiEvent) "1" else "0")

        return v
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        activity?.let {
            if(it is BaseActivity) {
                it.setStatusBarColor(R.color.primaryDark.toColorFromRes)
            }
        }

        appbar_title.setText(R.string.setting)
        appbar.appbar_line.visible = false
        appbar.setAppbar(true)

        opensource_licenses.safeClick {
            activity?.apply {
                startActivity(Intent(this, OssLicensesMenuActivity::class.java))
                OssLicensesMenuActivity.setActivityTitle("Opensource Licenses")
            }
        }

        setting_version_text.text = "version ${BuildConfig.VERSION_NAME}"

        setting_noti_instagram.toggle_button.setOnCheckedChangeListener { buttonView, isChecked ->
            AppPreference.setttingNotiInstagram = isChecked
            OneSignal.sendTag("Instagram", if (isChecked) "1" else "0")
        }
        setting_noti_twitter.toggle_button.setOnCheckedChangeListener { buttonView, isChecked ->
            AppPreference.setttingNotiTwitter = isChecked
            OneSignal.sendTag("Twitter", if (isChecked) "1" else "0")
        }

        setting_noti_notice.toggle_button.setOnCheckedChangeListener { buttonView, isChecked ->
            AppPreference.setttingNotiNotice = isChecked
            OneSignal.sendTag("GalleryNotice", if (isChecked) "1" else "0")
        }
        setting_noti_collection.toggle_button.setOnCheckedChangeListener { buttonView, isChecked ->
            AppPreference.setttingNotiCollection = isChecked
            OneSignal.sendTag("GalleryFund", if (isChecked) "1" else "0")
        }
        setting_noti_event.toggle_button.setOnCheckedChangeListener { buttonView, isChecked ->
            AppPreference.setttingNotiEvent = isChecked
            OneSignal.sendTag("GalleryEvent", if (isChecked) "1" else "0")
        }



        caseApi(Build.VERSION_CODES.N, {
            dark_mode_layout.visibility = View.GONE
        }) {
            dark_mode_layout.safeClick {
                bottomSheetThemeDialog?.show()
            }

            dark_mode_text.text = getModeName()
            dark_mode_layout.visibility = View.VISIBLE
        }
    }


    fun getModeName(): String {
        return when (AppPreference.themeMode) {
            AppCompatDelegate.MODE_NIGHT_NO -> R.string.light_mode.toStringFromRes
            AppCompatDelegate.MODE_NIGHT_YES -> R.string.dark_mode.toStringFromRes
            else -> {
                R.string.system_default.toStringFromRes
            }
        }
    }

    private fun changeTheme(mode: Int) {
        bottomSheetThemeDialog?.hide()
        AppPreference.themeMode = mode
        dark_mode_text.text = getModeName()
        delayed {
            AppCompatDelegate.setDefaultNightMode(AppPreference.themeMode)
        }
    }



}

