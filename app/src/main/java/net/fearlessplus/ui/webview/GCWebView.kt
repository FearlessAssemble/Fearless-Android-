package net.fearlessplus.ui.webview

import android.annotation.TargetApi
import android.content.Context
import android.graphics.Bitmap
import android.net.http.SslError
import android.os.Build
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.View
import android.webkit.*
import io.sbox.library.extensions.trace
import net.fearlessplus.core.FearlessApplication.Companion.isDebugMode

class GCWebView : WebView {

    var mListener: OnListener? = null

    fun setOnListener(listener: OnListener) {
        mListener = listener
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (canGoBack()) {
                goBack()
                return true
            } else {
                if (mChromeClient != null) mChromeClient!!.onHideCustomView()
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    constructor(context: Context) : super(context) {
        initialize(context, null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initialize(context, attrs)
    }

    var currentUrl: String? = ""
    var extraHeaders: Map<String, String>? = null

    private var mChromeClient: CGWebChromeClient? = null
    var mViewClient: CGWebViewClient? = null

    override fun loadUrl(url: String) {
        currentUrl = url
        super.loadUrl(url)
    }

    override fun loadUrl(url: String, additionalHttpHeaders: MutableMap<String, String>) {
        currentUrl = url
        extraHeaders = additionalHttpHeaders
        super.loadUrl(url, additionalHttpHeaders)
    }

    override fun destroy() {
        super.destroy()
        CookieManager.getInstance().removeAllCookies(null)
        CookieManager.getInstance().flush()
    }

    private fun initialize(context: Context, attrs: AttributeSet?) {

        setWebContentsDebuggingEnabled(isDebugMode)

        //ScrollBar見えるのを設定
        var visibleScrollBar = true
        isVerticalScrollBarEnabled = visibleScrollBar
        isHorizontalScrollBarEnabled = visibleScrollBar
        isScrollbarFadingEnabled = visibleScrollBar
        scrollBarStyle = if (visibleScrollBar)
            View.SCROLLBARS_OUTSIDE_OVERLAY
        else
            View.SCROLLBARS_INSIDE_OVERLAY


        settings.apply {
            javaScriptEnabled = true //このfunctionは危険性があります。(XSSの攻撃)

            var enableZoom = true //Zoom機能を使う
            setSupportZoom(enableZoom) //拡大、縮小機能を使います。
            builtInZoomControls = enableZoom //基本のZoomのアイコンを使います。
            displayZoomControls = !enableZoom

            var enableMultipleWindows = true //多重窓を使う
            setSupportMultipleWindows(enableMultipleWindows) //多重窓は使わないように
            javaScriptCanOpenWindowsAutomatically = enableMultipleWindows //JavaScriptが自動的に窓を開かないように

            var enableCache = true //HTML5のApplicationCacheを使う
            allowFileAccess = enableCache //WebViewの中にApplicationファイルを使う
            domStorageEnabled = enableCache //DOM storage APIを使う (HTML5にローカルの保存所を使うなら）
            cacheMode = WebSettings.LOAD_DEFAULT
            setAppCacheEnabled(enableCache) //App Cache使う
            setAppCachePath("${context.cacheDir}") //App Cacheの位置を設定

            loadWithOverviewMode = true //コンテンツがWebViewより大きかったら大きさを調整させます。
            useWideViewPort = true //wide viewportを使います。
            loadsImagesAutomatically = true //WebViewがアプリに登録させたイメージを自動的でロード機能を使います。
            defaultTextEncodingName = "UTF-8" //基本文字Encoderを設定
            setNeedInitialFocus(false) //初期のfocusを設定しないように
        }
        mViewClient = CGWebViewClient()
        mChromeClient = CGWebChromeClient()
        webViewClient = mViewClient as CGWebViewClient
        webChromeClient = mChromeClient
    }

    interface OnListener {
        fun onPageFinished(view: WebView?, title: String?)
    }

    inner class CGWebViewClient : WebViewClient() {

        var startUrl: String? = ""


        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
            startUrl = url
        }

        override fun shouldInterceptRequest(
            view: WebView?,
            request: WebResourceRequest?
        ): WebResourceResponse? {
            return null
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)

        }

        override fun onReceivedSslError(
            view: WebView?,
            handler: SslErrorHandler?,
            error: SslError?
        ) {
            super.onReceivedSslError(view, handler, error)
            trace("### onReceivedSslError: $error")
            handler?.proceed()
        }

        override fun shouldOverrideUrlLoading(view: WebView?, url: String): Boolean {
            trace("### shouldOverrideUrlLoading -> url")
            view?.loadUrl(url, getCustomHeaders())
            return true
        }

        @TargetApi(Build.VERSION_CODES.N)
        override fun shouldOverrideUrlLoading(
            view: WebView?,
            request: WebResourceRequest?
        ): Boolean {
            trace("### shouldOverrideUrlLoading -> WebResourceRequest")
            // webviewのみのスキーム
            var url: String = request?.url.toString()

            view?.loadUrl(url, getCustomHeaders())
            return true
        }

    }

    inner class CGWebChromeClient : WebChromeClient() {
        override fun onReceivedTitle(view: WebView?, title: String) {
            super.onReceivedTitle(view, title)
            val webTitle = if (title.startsWith("http://") or title.startsWith("https://")) {
                ""
            } else {
                title
            }
            if (mListener != null) mListener!!.onPageFinished(view, webTitle)
            trace("### onReceivedTitle: $webTitle, ${view?.title}")

        }
    }
}


