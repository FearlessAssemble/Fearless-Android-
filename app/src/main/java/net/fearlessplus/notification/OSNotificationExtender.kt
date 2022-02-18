package net.fearlessplus.notification

import android.app.Notification
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import androidx.core.app.NotificationCompat
import com.google.api.client.http.UrlEncodedContent.getContent
import com.onesignal.NotificationExtenderService
import com.onesignal.OSNotificationReceivedResult
import net.fearlessplus.R


@Suppress("DEPRECATION")
class OSNotificationExtender : NotificationExtenderService() {
    override fun onNotificationProcessing(receivedResult: OSNotificationReceivedResult): Boolean {

        val overrideSettings = OverrideSettings()
        overrideSettings.extender =
            NotificationCompat.Extender { builder: NotificationCompat.Builder ->
//                val soundUri: Uri =
//                    Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + applicationContext.packageName + "/" + R.raw.merry)
//                    Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + BuildConfig.APPLICATION_ID + "/" + R.raw.merry)
                builder.setSmallIcon(R.mipmap.ic_notification)  // noti icon
                builder.priority = Notification.PRIORITY_HIGH
//                builder.setSound(soundUri)
//                builder.setVibrate(null)
                builder
            }
        val displayedResult =
            displayNotification(overrideSettings)


        /*
        RepositoryProvider.getPreferences().addOneToNotificationsAmount()
        val pushObject: PushObject = PushObject.fromJson(pushNotification.payload.additionalData)
        val largeIcon =
            BitmapFactory.decodeResource(applicationContext.resources, R.drawable.ic_app)
        val soundUri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val overrideSettings = OverrideSettings()
        overrideSettings.androidNotificationId = ORDER_NOTIFICATION_ID
        overrideSettings.extender = NotificationCompat.Extender { builder ->
            builder.setContentTitle(getString(R.string.app_name))
                .setSmallIcon(R.drawable.ic_notification)
                .setLargeIcon(largeIcon)
                .setColor(resources.getColor(R.color.colorAccent))
                .setGroup(GROUP_NAME)
                .setAutoCancel(true)
                .setStyle(NotificationCompat.BigTextStyle().bigText(getContent(pushObject)))
                .setContentText(getContent(pushObject))
                .setPriority(Notification.PRIORITY_DEFAULT)
        }
        displayNotification(overrideSettings)
        */



        return true
    }
}