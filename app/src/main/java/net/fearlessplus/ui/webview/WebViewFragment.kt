package net.fearlessplus.ui.webview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import androidx.core.os.bundleOf
import net.fearlessplus.R
import io.sbox.library.extensions.*
import net.fearlessplus.ui.base.BaseFragment
import kotlinx.android.synthetic.main.component_appbar.*
import kotlinx.android.synthetic.main.fragment_webview.*

class WebViewFragment : BaseFragment() {

    companion object {
        @JvmStatic
        fun newInstance(bundle: Bundle?) =
            WebViewFragment().apply {
                arguments = bundle
            }

        @JvmStatic
        fun getBundle(
            url: String,
            title: String = "",
            isFullscreen: Boolean = false
        ) = bundleOf(
            "URL" to url,
            "APPBAR_TITLE" to title,
            "FullScreen" to isFullscreen
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_webview, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        fragment_webview_close.safeClick {
            activity?.finish()
        }

        webview.settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW

        arguments?.let {
            val url = it.getString("URL") ?: ""

            webview.loadUrl(url, getCustomHeaders())

            try {

                webview.setOnListener(object : GCWebView.OnListener {
                    override fun onPageFinished(view: WebView?, title: String?) {
                        if (appbar_title.isNotNull) {
                            appbar_title.text = title
                        }
                    }
                })

                if (appbar_title.isNotNull) {
                    appbar_title.text = it.getString("APPBAR_TITLE")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

fun getCustomHeaders(): MutableMap<String, String> {
    val extraHeaders: MutableMap<String, String> =
        HashMap()
//    extraHeaders["User-Agent"] = ""

    return extraHeaders
}



