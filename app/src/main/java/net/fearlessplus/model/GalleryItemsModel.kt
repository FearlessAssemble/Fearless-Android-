package net.fearlessplus.model

import android.content.Context
import android.view.View
import io.sbox.library.extensions.trace
import net.fearlessplus.extensions.newFragment
import net.fearlessplus.model.base.BaseResponse
import net.fearlessplus.model.base.Result
import net.fearlessplus.model.item.PageInfoItem
import net.fearlessplus.ui.main.gallery.GalleryListFragment
import net.fearlessplus.ui.main.gallery.viewer.GalleryBrowserFragment

data class GalleryItemsModel(
    override var result: Result,
    var data: GalleryItemsObject
) : BaseResponse() {

    override fun sync() {
        data.list?.forEach {
//            val id = it.id.replace("vskcn0t0eixrxs0kjr-qicwaan-c5g: ", "")
            val id = it.id
//            it.contentUrl = "https://drive.google.com/uc?id=" + id
            it.contentUrl = "https://drive.google.com/uc?export=view&id=" + id
            it.contentThumb = "https://drive.google.com/thumbnail?authuser=0&sz=w320&id=" + id
//            it.contentThumb = "lh3.googleusercontent.com/d/$id=w160?authuser=0"

            it.isVideoType = it.mimeType.startsWith("video/")
            it.isGifType = it.mimeType.startsWith("image/gif")

            it.contentType =
                if (it.isVideoType) GalleryContentType.VIDEO.index
                else if (it.isGifType) GalleryContentType.GIF.index
                else GalleryContentType.IMAGE.index

            it.eventDate = it.eventDate.split("T")[0]
            it.eventDateYear = it.eventDate.split("-")[0]
            it.eventDateMonth = it.eventDateYear + "-" + it.eventDate.split("-")[1]

        }


    }

}

data class GalleryItemsObject(
    var nextYn: String?,
    var pageInfo: PageInfoItem,
    var nextPageNum: Int?,
    val list: List<GalleryItem>
)

enum class GalleryContentType(var index: Int) {
    IMAGE(0), VIDEO(1), GIF(2)
}

data class GalleryItem(
    var seq: Int,
    var id: String,
    var name: String,
    var mimeType: String,
    var eventDate: String,

    var eventDateMonth: String,
    var eventDateYear: String,

    var contentUrl: String,
    var contentThumb: String,
    var isVideoType: Boolean = false,
    var isGifType: Boolean = false,
    var contentType: Int = 0
) {


    fun onClick(context: Context, view: View?) {

        contentUrl?.let {
//            context.browse(it)
            context.newFragment<GalleryBrowserFragment>(null)

        }

    }

}
