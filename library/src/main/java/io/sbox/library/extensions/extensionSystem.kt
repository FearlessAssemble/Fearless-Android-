package io.sbox.library.extensions

import android.app.Activity
import android.content.Intent
import android.content.IntentSender.SendIntentException
import android.net.Uri
import android.os.Build
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.android.play.core.tasks.Task


fun Activity.updateRequest(updateType: Int = AppUpdateType.FLEXIBLE) {
    /*
    var fakeAppUpdateManager = FakeAppUpdateManager(this)
        fakeAppUpdateManager.setUpdatePriority(AppUpdateType.IMMEDIATE)
        fakeAppUpdateManager.setUpdateAvailable(2)
        fakeAppUpdateManager.userAcceptsUpdate()
        fakeAppUpdateManager.downloadStarts()
        fakeAppUpdateManager.downloadCompletes()
     */


    val appUpdateManager = AppUpdateManagerFactory.create(getApplicationContext())
    val appUpdateInfoTask: Task<AppUpdateInfo> = appUpdateManager.appUpdateInfo

    var listener = InstallStateUpdatedListener {
        if (it.installStatus() == InstallStatus.DOWNLOADED) {
            trace(
                it.installErrorCode(),
                it.installStatus(),
                it.packageName()
            )
        }
    }
    appUpdateManager?.registerListener(listener)

    appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
        val UPDATE_REQUEST_CODE = 999
        if (appUpdateInfo.updateAvailability() === UpdateAvailability.UPDATE_AVAILABLE // For a flexible update, use AppUpdateType.FLEXIBLE
            && appUpdateInfo.isUpdateTypeAllowed(updateType)
        ) {
            try {
                appUpdateManager.startUpdateFlowForResult(
                    appUpdateInfo, this, AppUpdateOptions.newBuilder(updateType)
                        .setAllowAssetPackDeletion(true)
                        .build(), UPDATE_REQUEST_CODE
                )
            } catch (e: SendIntentException) {
                e.printStackTrace()
            }
        } else if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
            //popupSnackbarForCompleteUpdate
        } else if (appUpdateInfo.updateAvailability()
            == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS
        ) {
            appUpdateManager?.startUpdateFlowForResult(
                appUpdateInfo,
                updateType,
                this,
                UPDATE_REQUEST_CODE
            )
        }
    }
}


fun Activity.openAppInPlayStore() {
    val url = "market://details?id=$packageName"
    val uri = Uri.parse(url)

    fun openStore(func: () -> Unit): Boolean? {
        try {
            func.invoke()
            return true
        } catch (e: Exception) {
            return null
        }
    }

    openStore {
        val goToMarketIntent = Intent(Intent.ACTION_VIEW, uri)
        var flags =
            Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_ACTIVITY_MULTIPLE_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        flags = if (Build.VERSION.SDK_INT >= 21) {
            flags or Intent.FLAG_ACTIVITY_NEW_DOCUMENT
        } else {
            flags or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        goToMarketIntent.addFlags(flags)
        startActivity(goToMarketIntent)
    }?.let {
        return
    }

    openStore {
        val intent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse(url)
        )
        startActivity(intent)
    }
}