package io.sbox.library.view.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState


/*
class APPPagingSource @Inject constructor(val tag: Int = 0, val pageSize: Int = 100...) : PagingSource<V>() {
 */
open class PagingSource<V : Any> : PagingSource<Int, V>() {

    override fun getRefreshKey(state: PagingState<Int, V>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, V> {
        TODO("Not yet implemented")
        /*
        return try {
            suspendCancellableCoroutine { continuation ->
                val page = params.key ?: 1

                // Api.Request ->
                continuation.resume(LoadResult.Page(
                    data = listOf(),
                    prevKey = if (page == 1) null else page - 1,
                    nextKey = null
                ))

            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
        */
    }

    fun <R: Any>dataParsing(data: R): R {
        // next page check
        return data
    }
}