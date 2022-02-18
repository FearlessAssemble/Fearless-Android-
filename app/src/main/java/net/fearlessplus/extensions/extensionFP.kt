package net.fearlessplus.extensions

import android.content.Context
import io.sbox.library.extensions.browse


fun Context.launchLink(url: String) {
    try {
        var launchUrl = url

        var checkUrl = launchUrl.replace("https://", "")
        if (checkUrl.startsWith("gall.dcinside.com")
            || checkUrl.startsWith("m.dcinside.com")
            || checkUrl.startsWith("www.dcinside.com")
        ) {
            try {
                packageManager.getLaunchIntentForPackage("com.dcinside.app")?.let {
                    launchUrl = "dcapp://$checkUrl"
                }
            } catch (e: Exception) {}
        }

        browse(launchUrl)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}
