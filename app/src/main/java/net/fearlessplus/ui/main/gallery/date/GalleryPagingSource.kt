package net.fearlessplus.ui.main.gallery.date

import io.sbox.library.view.paging.PagingSource
import kotlinx.coroutines.suspendCancellableCoroutine
import net.fearlessplus.model.GalleryItem
import net.fearlessplus.model.GalleryItemsObject
import net.fearlessplus.network.Api
import net.fearlessplus.network.send
import javax.inject.Inject
import kotlin.coroutines.resume

class GalleryPagingSource @Inject constructor(val pageSize: Int) : PagingSource<GalleryItem>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, GalleryItem> {
        return try {

            suspendCancellableCoroutine { continuation ->
                val page = params.key ?: 1
                Api.service.getGalleryAllContents(pageSize, page).send({
                    var data = dataParsing(it.data)

                    continuation.resume(LoadResult.Page(
                        data = data.list,
                        prevKey = if (page == 1) null else page - 1,
                        nextKey = data.nextPageNum
                    ))
                })
            }

        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }


    fun dataParsing(data: GalleryItemsObject): GalleryItemsObject {
        var hasNext = false
        data.pageInfo?.apply {
            hasNext = total?:0 > page * size
        }
        data.nextPageNum = if (hasNext) data.pageInfo.page + 1 else null
        return data
    }

}