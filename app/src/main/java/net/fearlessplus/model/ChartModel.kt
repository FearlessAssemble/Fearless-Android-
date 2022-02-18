package net.fearlessplus.model

import androidx.databinding.ObservableArrayList
import net.fearlessplus.model.base.BaseResponse
import net.fearlessplus.model.base.Result
import net.fearlessplus.model.item.ChartItem
import net.fearlessplus.model.item.PageInfoItem
import java.util.*

data class ChartModel(
    override var result: Result,
    var datatime: Date,
    var data: ChartData
) : BaseResponse()

data class ChartData(
    var nextYn: String?,
    var pageInfo: PageInfoItem,
    var nextPageNum: Int?,
    var list: List<ChartItem>
)