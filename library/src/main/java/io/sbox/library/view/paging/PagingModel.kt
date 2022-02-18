package io.sbox.library.view.paging

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.*
import io.sbox.library.extensions.globalContext
import kotlinx.coroutines.launch


/*
private val viewModel by lazy {
        ViewModelProvider(this, Factory(PagingViewModel())).get(PagingViewModel::class.java)
    }
 */
class Factory(val viewModel: ViewModel) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return viewModel as T
    }
}

enum class PagingModelType {
    HEADER, ITEM, SEPARATOR
}

sealed class PagingModel(val type: PagingModelType) {
    data class Item<V>(val value: V): PagingModel(PagingModelType.ITEM)
    data class Header<V>(val value: V): PagingModel(PagingModelType.HEADER)
    data class Separator<V>(val value: V? = null) : PagingModel(PagingModelType.SEPARATOR)
}


class PagingViewModel<V : Any> constructor(context: Fragment, pagingSource: PagingSource<V>): ViewModel() {

    init {

        context.lifecycleScope.launch {

        }

    }


    fun create(simpleType: Boolean = true) {

    }



}


/*
private val viewModel by viewModels<ChartViewModel>()

lifecycleScope.launch {
    viewModel.pagingData.collectLatest {
        pagingAdapter.submitData(it)
    }
}

class BGTubeViewModel : ViewModel() {

    val pageSize = net.fearlessplus.core.Config.pagedListPageSize
    fun getPagingData(tag: Int): Flow<PagingData<VideoItem>> {
        return Pager(PagingConfig(pageSize = pageSize)) {
            BGTubePagingSource(tag, pageSize)
        }.flow.cachedIn(viewModelScope)
    }

}

class ChartViewModel : ViewModel() {

    val pageSize = 20
    val pagingData =
        Pager(PagingConfig(pageSize = pageSize)) {
            ChartPagingSource(pageSize)
        }.flow.map { pagingData ->
            pagingData.map<ChartItem, SampleModel> { SampleModel.Data(it) }
                .insertHeaderItem(item = SampleModel.Header("Header!!!"))
                .insertFooterItem(item = SampleModel.Header("Footer"))
                .insertSeparators { before: SampleModel?, after: SampleModel? ->
                    if (before is SampleModel.Header || after is SampleModel.Header)
                        SampleModel.Separator
                    else
                        SampleModel.Separator2
                }

        }.cachedIn(viewModelScope)
}


 */