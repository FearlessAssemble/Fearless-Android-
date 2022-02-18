package net.fearlessplus.network.base


import com.facebook.stetho.okhttp3.StethoInterceptor
import com.google.gson.GsonBuilder
import io.sbox.library.extensions.hideLoading
import io.sbox.library.extensions.trace
import net.fearlessplus.core.FearlessApplication
import net.fearlessplus.core.FearlessApplication.Companion.isDebugMode
import net.fearlessplus.model.base.BaseResponse
import net.fearlessplus.network.debug.LocalResponseInterceptor
import net.fearlessplus.network.send
import net.fearlessplus.ui.base.BaseActivity
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.net.SocketTimeoutException
import java.util.*
import java.util.concurrent.TimeUnit
import javax.net.ssl.HostnameVerifier
import kotlin.collections.ArrayList

open class BaseApi : BaseCache() {


    companion object {
        val stethoInterceptor = StethoInterceptor()

        val serverDomain = "https://api.fearlessplus.net"

        val base: BaseApi = BaseApi()
        var retryApi: ArrayList<ApiCallBack<*>> = arrayListOf()
        fun retry() {
            retryApi.forEach {
                it.retryCount++
            }
            retryApi.clear()
        }
    }


    private fun getHttpClient(): OkHttpClient {

        val ts = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()).toString()

        val interceptors = ArrayList<Interceptor>()
        interceptors.add(Interceptor { chain ->
            val builder = chain.request().newBuilder()
            builder
                .addHeader("Content-Type", "application/json")
            chain.proceed(builder.build())
        })

        if (isDebugMode) {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            interceptors.add(interceptor)
            FearlessApplication.instance?.applicationContext?.let {
                interceptors.add(LocalResponseInterceptor(it))
            }
        }

        val httpFollowRedirects = false
        val sslbFollowRedirects = true
        val TIMEOUT = 15000L
        var GLOBAL_CONNECTION_POOL = ConnectionPool(5, 1, TimeUnit.MINUTES)

        val spec: ConnectionSpec = ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
            .tlsVersions(TlsVersion.TLS_1_2)
            .cipherSuites(
                CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256,
                CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256,
                CipherSuite.TLS_DHE_RSA_WITH_AES_128_GCM_SHA256)
            .build()

        val builder = OkHttpClient.Builder()
            .followRedirects(httpFollowRedirects)
            .followSslRedirects(sslbFollowRedirects)
            .connectTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
            .writeTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
            .hostnameVerifier(HostnameVerifier { hostname, session -> true })
            .readTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
            .connectionPool(GLOBAL_CONNECTION_POOL)
            .connectionSpecs(Collections.singletonList(spec))
//            .cache(provideCache())

        interceptors?.let {
            if (it.isNotEmpty()) {
                for (interceptor in interceptors) {
                    builder.addInterceptor(interceptor)
                }
            }
        }

        if (isDebugMode) {
            builder.addNetworkInterceptor(stethoInterceptor)
        }

//        builder.addInterceptor(provideOfflineCacheInterceptor())
//        builder.addNetworkInterceptor(provideCacheInterceptor())

        return builder.build()
    }


    fun <T> getService(
        serviceBaseUrl: String,
        service: Class<T>
    ): T {
        var baseUrl = serviceBaseUrl

        val gson = GsonBuilder()
            .enableComplexMapKeySerialization()
            .serializeNulls()
            .setLenient()
            .create()


        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(getHttpClient())
            .build()

        return retrofit.create(service)
    }

}

open class ApiCallBack<T>(var call: Call<T>) : Callback<T> {

    val REQUEST_SUCCESS_CODE = 200
    val OPTINAL_UPDATE = 209
    val FORCE_UPDATE = 0

    val BAD_REQUEST = 400
    val FORBIDDEN = 403
    val NOT_FOUND = 404
    val INTERNAL_SERVER_ERROR = 500

    val CONTENT_TYPE_ERROR = 9000
    val NETWORK_EXCEPTION = 9001
    val PARSE_EXCEPTION = 9002
    val INTERFACE_EXCEPTION = 9003

    var retryCount = 0
        set(value) {
            if (value < 3) {
                call.clone().send(this)
                field = value
            }
        }

    override fun onResponse(call: Call<T>, response: retrofit2.Response<T>) {
        val url = response.raw().request.url.toString()
        if (response.isSuccessful) {
            val subtype = response.raw().body?.contentType()?.subtype

            if ("json" == subtype) {
                try {

                    response.body()?.let {

                        if (it is BaseResponse) {

                            it.sync()
                            onComplete(it)

                            /*
                            val code = it.result.code
                            trace("@@@@@@@@@@ baseApi 22 ", code, it.result.code, response.body())
                            when (code) {
                                in 200..299 -> {
                                    it.sync()
                                    onComplete(it)
                                }

                                else -> {

                                }
                            }
                            */

                        }
                    }
                } catch (e: Exception) {
                    onFail(url, PARSE_EXCEPTION, e.message ?: "")
                }
            } else {
                onFail(
                    url,
                    CONTENT_TYPE_ERROR,
                    response.raw().body?.contentType()?.toString() ?: ""
                )
            }
        } else {
            onFail(url, INTERNAL_SERVER_ERROR, response.raw().toString())
        }

    }

    override fun onFailure(call: Call<T>, t: Throwable) {
        when (t) {
            is SocketTimeoutException -> {
//                showToast(getContext()?.resources?.getString(R.string.internet_connection_error)!!)

                val checkUrl = call.request().url.toString()
                var responseTime = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()).toString()

            }
            is IOException -> {

                /*
                try {
                    CGDialog(context = getContext()!!).set(
                        "R.string.notice", "R.string.internet_unstable_dialog_title_massage",
                        "R.string.empty", "R.string.ok",
                        null, null
                    ).show()
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
                */
            }
            else -> {
                onFail("", NETWORK_EXCEPTION, t.message ?: "")
            }
        }
        BaseActivity.getTopActivity()?.hideLoading()
    }

    open fun onComplete(body: T) {
    }

    open fun onFail(url: String, code: Int, message: String = "") {
        trace(
            "##BaseApi## onFail : ",
            url,
            code,
            message
        )
    }
}

