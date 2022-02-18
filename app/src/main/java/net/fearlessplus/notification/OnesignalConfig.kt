package net.fearlessplus.notification

import android.content.Context
import com.onesignal.OneSignal
import net.fearlessplus.notification.OnesignalNotificationOpenedHandler
import net.fearlessplus.notification.OnesignalNotificationReceivedHandler
import net.fearlessplus.core.FearlessApplication.Companion.isDebugMode

class OnesignalConfig {

    companion object {
        var isDidSetDeviceToken = false

        private const val mIsDebug = false

        fun initOnesignal(context: Context) {
            isDidSetDeviceToken = false

            try {

                OneSignal.startInit(context)
                    .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                    .unsubscribeWhenNotificationsAreDisabled(true)
                    .setNotificationOpenedHandler(OnesignalNotificationOpenedHandler())
                    .setNotificationReceivedHandler(OnesignalNotificationReceivedHandler())
                    .init()

                if (isDebugMode) {
                    OneSignal.getTags { tags -> OneSignal.sendTags(tags) }
                    OneSignal.sendTag("PUSH_TEST", "1")
                    OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    }

}
