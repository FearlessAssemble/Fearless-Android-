package net.fearlessplus.core

import androidx.paging.PagedList

class Config {

    companion object {

        var pagedListPageSize = 100
        fun pagedListConfig(pageSize: Int = pagedListPageSize) =
            PagedList.Config.Builder()
                .setPageSize(pageSize)
                .setInitialLoadSizeHint(pageSize/10*8)
                .setPrefetchDistance(pageSize/10*8)
                .setEnablePlaceholders(false)
                .build()

    }
}