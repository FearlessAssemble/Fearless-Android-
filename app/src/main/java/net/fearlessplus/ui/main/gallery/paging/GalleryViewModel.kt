package net.fearlessplus.ui.main.gallery.paging

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import io.sbox.library.extensions.trace
import kotlinx.coroutines.flow.map
import net.fearlessplus.model.GalleryItem
import net.fearlessplus.ui.main.gallery.GalleryListFragment
import net.fearlessplus.ui.main.gallery.date.GalleryPagingSource

/*
private val viewModel by lazy {
        ViewModelProvider(this, Factory()).get(PagingViewModel::class.java)
    }
 */
class Factory : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return GalleryViewModel() as T
    }
}


sealed class GalleryModel(val type: SampleType) {
    data class Data(val value: GalleryItem): GalleryModel(SampleType.DATA)
    data class TopBottom(val title: String): GalleryModel(SampleType.TOP_BOTTOM)
    data class SeparatorDate(val date: String): GalleryModel(SampleType.SEPARATOR_DATE)
    object SeparatorLine: GalleryModel(SampleType.SEPARATOR_LINE)
}

enum class SampleType {
    TOP_BOTTOM, DATA, SEPARATOR_LINE, SEPARATOR_DATE
}

class GalleryViewModel : ViewModel() {
    val pageSize = net.fearlessplus.core.Config.pagedListPageSize
    val pagingData = Pager(PagingConfig(pageSize = pageSize)) {
            GalleryPagingSource(pageSize)
        }.flow.map { pagingData ->

            pagingData.map<GalleryItem, GalleryModel> { GalleryModel.Data(it) }
//                .insertHeaderItem(item = GalleryModel.TopBottom("쁘라우드 이용약관"))
//                .insertFooterItem(item = GalleryModel.TopBottom("- End Page -"))

                .insertSeparators { before: GalleryModel?, after: GalleryModel? ->
                    if (after is GalleryModel.TopBottom)
                        GalleryModel.SeparatorLine
                    else if (after is GalleryModel.Data) {

                        if(before !is GalleryModel.Data) {
                            if(GalleryListFragment.defaultType)
                                GalleryModel.SeparatorDate(after.value.eventDate)
                            else
                                GalleryModel.SeparatorDate(after.value.eventDateMonth)
                        } else if(before is GalleryModel.Data && GalleryListFragment.defaultType && !before.value.eventDate.equals(after.value.eventDate)) {
                            GalleryModel.SeparatorDate(after.value.eventDate)
                        } else if(before is GalleryModel.Data && !GalleryListFragment.defaultType && !before.value.eventDateMonth.equals(after.value.eventDateMonth)) {
                            GalleryModel.SeparatorDate(after.value.eventDateMonth)
                        } else {
                            null
                        }
                    }
                    else
                        null
                }


        }.cachedIn(viewModelScope)


    val pagingDataMonth = Pager(PagingConfig(pageSize = pageSize)) {
        GalleryPagingSource(pageSize)
    }.flow.map { pagingData ->


        trace("@@@@@@@@@@@ OK", GalleryListFragment.defaultType)

        pagingData.map<GalleryItem, GalleryModel> { GalleryModel.Data(it) }
//                .insertHeaderItem(item = GalleryModel.TopBottom("쁘라우드 이용약관"))
//                .insertFooterItem(item = GalleryModel.TopBottom("- End Page -"))
            .insertSeparators { before: GalleryModel?, after: GalleryModel? ->
                if (after is GalleryModel.TopBottom)
                    GalleryModel.SeparatorLine
                else if (after is GalleryModel.Data) {

                    if(before !is GalleryModel.Data) {
                        if(GalleryListFragment.defaultType)
                            GalleryModel.SeparatorDate(after.value.eventDate)
                        else
                            GalleryModel.SeparatorDate(after.value.eventDateMonth)
                    } else if(before is GalleryModel.Data && GalleryListFragment.defaultType && !before.value.eventDate.equals(after.value.eventDate)) {
                        GalleryModel.SeparatorDate(after.value.eventDate)
                    } else if(before is GalleryModel.Data && !GalleryListFragment.defaultType && !before.value.eventDateMonth.equals(after.value.eventDateMonth)) {
                        GalleryModel.SeparatorDate(after.value.eventDateMonth)
                    } else {
                        null
                    }
                }
                else
                    null
            }

    }.cachedIn(viewModelScope)
}


