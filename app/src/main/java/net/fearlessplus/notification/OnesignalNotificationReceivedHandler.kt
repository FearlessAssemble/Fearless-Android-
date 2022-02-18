package net.fearlessplus.notification

import com.onesignal.OSNotification
import com.onesignal.OneSignal


class OnesignalNotificationReceivedHandler : OneSignal.NotificationReceivedHandler {
    override fun notificationReceived(notification: OSNotification?) {

        notification?.displayType?.apply {
            OSNotification.DisplayType.Notification
        }


        /*
        history save...

        val data = notification!!.payload.additionalData
        var openoption: String
        var openurl: String
        var isfcm: String
        var pushno: String
        if (data != null) {
            openoption = data.optString("openoption", null)
            openurl = data.optString("openurl", null)
            isfcm = data.optString("isfcm", null)
            pushno = data.optString("pushno", null)
            trace("/OneSignal", "customkey set with value: $data")
        }
        */
    }
}