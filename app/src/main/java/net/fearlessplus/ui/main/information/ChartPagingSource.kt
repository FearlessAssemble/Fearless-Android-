package net.fearlessplus.ui.main.information

import io.sbox.library.view.paging.PagingSource
import kotlinx.coroutines.suspendCancellableCoroutine
import net.fearlessplus.model.ChartData
import net.fearlessplus.model.item.ChartItem
import net.fearlessplus.network.Api
import net.fearlessplus.network.send
import javax.inject.Inject
import kotlin.coroutines.resume


class ChartPagingSource @Inject constructor(val pageSize: Int) : PagingSource<ChartItem>() {
//class ChartPagingSource @Inject constructor(val pageSize: Int) : PagingSource<Int, ChartItem>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ChartItem> {
        return try {

            suspendCancellableCoroutine { continuation ->
                val page = params.key ?: 1
                Api.service.getChartList(pageSize, page).send({
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

    fun dataParsing(data: ChartData): ChartData {
        val hasNext = data.nextYn.equals("Y")
        data.nextPageNum = if (hasNext) data.pageInfo.page + 1 else null
        return data
    }
}