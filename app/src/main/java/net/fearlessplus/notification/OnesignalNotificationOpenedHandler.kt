package net.fearlessplus.notification

import com.onesignal.OSNotificationOpenResult
import com.onesignal.OneSignal
import net.fearlessplus.core.FearlessApplication
import net.fearlessplus.extensions.launchLink


open class OnesignalNotificationOpenedHandler : OneSignal.NotificationOpenedHandler {

    override fun notificationOpened(result: OSNotificationOpenResult) {
        try {
            FearlessApplication.instance?.launchLink(result.notification.payload.launchURL)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}