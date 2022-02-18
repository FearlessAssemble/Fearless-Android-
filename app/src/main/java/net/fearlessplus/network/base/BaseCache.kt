package net.fearlessplus.network.base

import net.fearlessplus.extensions.globalContext
import net.fearlessplus.preferences.AppPreference
import okhttp3.Cache
import okhttp3.CacheControl
import okhttp3.Interceptor
import java.io.File
import java.util.concurrent.TimeUnit


open class BaseCache {


    fun provideCache(): Cache? {
        var cache: Cache? = null
        try {
            cache = Cache( File(globalContext?.cacheDir, "responses"), 10 * 1024 * 1024)
        } catch (e: Exception) {
        }
        return cache
    }

    fun provideCacheInterceptor(): Interceptor {
        return Interceptor { chain ->
            val response = chain.proceed( chain.request() )

            val cacheControl = CacheControl.Builder()
                .maxAge(2, TimeUnit.DAYS)
                .build()

            response.newBuilder()
                .header("Cache-Control", cacheControl.toString())
                .build()
        }
    }

    fun provideOfflineCacheInterceptor(): Interceptor {
        return Interceptor { chain ->
            var request = chain.request()
            if(AppPreference.isNetWorkAvailable) {
                request = request.newBuilder().cacheControl(CacheControl.FORCE_NETWORK).build()
            } else {
                val cacheControl = CacheControl.Builder()
//                    .onlyIfCached()
                    .maxStale(7, TimeUnit.DAYS)
                    .build()

                request = request.newBuilder()
                    .cacheControl(CacheControl.FORCE_CACHE)
//                    .cacheControl( cacheControl )
//                    .header("Cache-Control", cacheControl.toString())
                    .build()

            }
            val response = chain.proceed(request)

            response
        }
    }


}
