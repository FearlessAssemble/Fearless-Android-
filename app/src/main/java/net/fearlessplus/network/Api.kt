package net.fearlessplus.network

import net.fearlessplus.core.Config
import net.fearlessplus.network.base.ApiCallBack
import net.fearlessplus.network.base.BaseApi
import kotlinx.coroutines.*
import net.fearlessplus.model.*
import okhttp3.Response
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

class Api : BaseApi() {

    companion object {
        var service: ApiService = base.getService(serverDomain, ApiService::class.java)

        fun customService(url: String = "http://localhost"): ApiService {
            return base.getService(url, ApiService::class.java)
        }
    }

    interface ApiService {

        @GET("/android.json")
        fun getTest(): Call<DefaultModel>




        @GET("/app-api/v2/hotVideos/tags")
        fun getVideoTagsList(): Call<VideoTagModel>

        @GET("/app-api/v2/hotVideos")
        fun getHotVideoList(
            @Query(value = "tag_id") tag_id: Int,
            @Query(value = "size") size: Int = Config.pagedListPageSize,
            @Query(value = "page") page: Int = 1
        ): Call<VideoModel>

        @GET("/app-api/v2/schedules")
        fun getScheduleList(): Call<ScheduleModel>

        @GET("/app-api/v2/chart")
        fun getChartList(
            @Query(value = "size") size: Int = Config.pagedListPageSize,
            @Query(value = "page") page: Int = 1
        ): Call<ChartModel>

        @GET("/app-api/v2/events")
        fun getEventList(
            @Query(value = "category") category: String = "event"
        ): Call<EventModel>



        @GET("/app-api/v2/pushHistory")
        fun getPushHistoryList(): Call<PushHistoryModel>

        @GET("/app-api/v2/aboutInfo")
        fun getAboutInfoList(): Call<AboutInfoModel>

        @GET("/app-api/v2/sns_info")
        fun getSnsInfoList(): Call<SnsInfoModel>


        @GET("/app-api/v2/folders")
        fun getGalleryFolderList(
            @Query(value = "parentFolderId") parentFolderId: String): Call<GalleryFolderModel>

        @GET("/app-api/v2/folders")
        fun getGalleryFolderList(): Call<GalleryFolderModel>

        @GET("/app-api/v2/photos")
        fun getGalleryAllContents(
            @Query(value = "size") size: Int = Config.pagedListPageSize,
            @Query(value = "page") page: Int = 1
        ): Call<GalleryItemsModel>

        @GET("/app-api/v2/folders/{folderId}/photos")
        fun getGalleryContents(
            @Path("folderId") folderId: String): Call<GalleryContentModel>



    }
}



fun <T> Call<T>.send(callback: ApiCallBack<T>) {
    val job = Job()
    val scope = CoroutineScope(Dispatchers.Default + job)
    scope.launch {
        withContext(Dispatchers.IO) {
            enqueue(callback)
        }
    }
}

fun <T> Call<T>.send() {
    send(object : ApiCallBack<T>(this) {})
}

fun <T> Call<T>.send(complete: (T) -> (Unit), fail: (() -> Unit)? = null) {
    send(object : ApiCallBack<T>(this) {
        override fun onComplete(body: T) {
            super.onComplete(body)
            complete.invoke(body)
        }

        override fun onFail(url: String, code: Int, message: String) {
            super.onFail(url, code, message)
            fail?.apply {
                invoke()
            }
        }
    })
}

fun <T> Call<T>.sendWithMessage(
    complete: (T) -> (Unit),
    fail: ((String, Int, String) -> (Unit))? = null
) {
    send(object : ApiCallBack<T>(this) {
        override fun onComplete(body: T) {
            super.onComplete(body)
            complete.invoke(body)
        }

        override fun onFail(url: String, code: Int, message: String) {
            super.onFail(url, code, message)
            fail?.apply {
                invoke(url, code, message)
            }
        }
    })
}
