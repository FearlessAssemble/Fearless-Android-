package net.fearlessplus.model

import androidx.databinding.ObservableArrayList
import net.fearlessplus.model.base.BaseResponse
import net.fearlessplus.model.base.Result
import net.fearlessplus.model.item.ChartItem
import net.fearlessplus.model.item.PageInfoItem
import net.fearlessplus.model.item.VideoItem
import java.util.*

data class VideoModel(
    override var result: Result,
    var datatime: Date,
    var data: VideoData
) : BaseResponse() {

    override fun sync() {
    }

}

data class VideoData(
    var nextYn: String?,
    var pageInfo: PageInfoItem,
    var nextPageNum: Int?,
    var list: ObservableArrayList<VideoItem>
)
