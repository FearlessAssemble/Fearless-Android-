package net.fearlessplus.model

import androidx.databinding.ObservableArrayList
import net.fearlessplus.model.base.BaseResponse
import net.fearlessplus.model.base.Result
import net.fearlessplus.model.item.ChartItem
import net.fearlessplus.model.item.EventItem
import net.fearlessplus.model.item.PageInfoItem
import java.util.*

data class EventModel(
    override var result: Result,
    var datatime: Date,
    var data: EventData
) : BaseResponse()

data class EventData(
    var list: ObservableArrayList<EventItem>
)
