package net.fearlessplus.ui.component

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import com.google.android.material.appbar.AppBarLayout
import io.sbox.library.extensions.safeClick
import io.sbox.library.extensions.showBottomSheetDialog
import io.sbox.library.extensions.visible
import kotlinx.android.synthetic.main.component_appbar.view.*
import net.fearlessplus.extensions.newFragment
import net.fearlessplus.ui.main.MainActivity
import net.fearlessplus.ui.notification.NotificationHistoryView
import net.fearlessplus.ui.setting.SettingFragment

class FPAppBarLayout : AppBarLayout {

    constructor(context: Context) : super(context) {
        initialize(context, null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initialize(context, attrs)
    }

    val activity: Activity?

    init {
        activity = if (context is Activity) context as Activity else null
    }

    private fun initialize(context: Context, attrs: AttributeSet?) {


    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        appbar_notification_layout.visible = activity is MainActivity
        appbar_notification.safeClick {
            context.showBottomSheetDialog(NotificationHistoryView(context))
        }
    }

    fun setAppbar(back: Boolean = false, notification: Boolean = false, setting: Boolean = false, gallery: Boolean = false) {
        if(back) appbar_notification_layout.visible = false

        appbar_back.visible = back
        appbar_setting.visible = setting
        appbar_gallery_type.visible = gallery

        when {
            back -> {
                appbar_back.safeClick {
                    activity?.onBackPressed()
                }
            }
            setting -> {
                appbar_setting.safeClick {
                    context.newFragment<SettingFragment>(null)
                }
            }
        }
    }
}