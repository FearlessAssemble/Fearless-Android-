package net.fearlessplus.ui.main.bgtube

import io.sbox.library.view.paging.PagingSource
import kotlinx.coroutines.suspendCancellableCoroutine
import net.fearlessplus.model.VideoData
import net.fearlessplus.model.item.VideoItem
import net.fearlessplus.network.Api
import net.fearlessplus.network.send
import javax.inject.Inject
import kotlin.coroutines.resume

class BGTubePagingSource @Inject constructor(val tag: Int = 0, val pageSize: Int) : PagingSource<VideoItem>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, VideoItem> {
        return try {

            suspendCancellableCoroutine { continuation ->
                val page = params.key ?: 1
                Api.service.getHotVideoList(tag, pageSize, page).send({
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

    fun dataParsing(data: VideoData): VideoData {
        val hasNext = data.nextYn.equals("Y")
        data.nextPageNum = if (hasNext) data.pageInfo.page + 1 else null
        return data
    }
}