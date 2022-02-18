package net.fearlessplus.model

import android.content.Context
import android.view.View
import androidx.databinding.ObservableArrayList
import io.sbox.library.extensions.*
import net.fearlessplus.R
import net.fearlessplus.model.base.BaseResponse
import net.fearlessplus.model.base.Result
import net.fearlessplus.extensions.toStringFromRes
import java.lang.Exception
import java.util.*

data class ScheduleModel(
    override var result: Result,
    var datatime: Date,
    var data: ScheduleData
) : BaseResponse() {
    override fun sync() {
        super.sync()

        /*
        val list = data.list.reversed()
        data.list.clear()
        list.forEach {
            data.list.add(it)
        }
        */

        var tempMonth = ""
        var tempDay = ""
        data.list.forEachIndexed { index, item ->

            var date: Date? = null
            try {
                date = item.startLocalTime.getDate(DatePattern.TypeFull01)
                item.startTime = date.getFormatDate("HH:mm")
                item.endTime = item.endLocalTime.getDate(DatePattern.TypeFull01).getFormatDate("HH:mm")
                item.strDate = "${item.startTime} ~ ${item.endTime}"
            } catch (e: Exception) {
                try {
                    date = item.startLocalTime.getDate(DatePattern.TypeDate01)
                    item.strDate = R.string.all_day.toStringFromRes
                } catch (e: Exception) {

                }
            }

            item.month = date?.getFormatDate("MM")?:""
            item.day = date?.getFormatDate("dd")?:""

            if(!item.month.equals(tempMonth)) {
                item.isNewMonth = true
                tempMonth = item.month
            }
            if(!item.day.equals(tempDay)) {
                item.isNewDay = true
                item.isHeader = true
                tempDay = item.day
            }
            if(item.isHeader) {
                if(tempMonth.equals(getCurrentDate("MM")) && tempDay.equals(getCurrentDate("dd"))) {
                    data.startIndex = index
                }
            }


            val prefix = "https://"
            item.description.split(prefix).getOrNull(0)?.let {
                val link = item.description.replace(it, "")
                if (link.startsWith(prefix)) {
                    item.hasLink = true
                    item.linkUrl = link
                }
            }
        }

    }
}

data class ScheduleData(
    var list: ObservableArrayList<ScheduleItem>,
    var startIndex: Int = 0
)

data class ScheduleItem(
    var title: String,
    var startLocalTime: String,
    var endLocalTime: String,
    var allDay: String,
    var description: String,

    var month: String,
    var day: String,
    var startTime: String,
    var endTime: String,
    var strDate: String,
    var hasLink: Boolean = false,
    var linkUrl: String? = null,
    var isNewMonth: Boolean = false,
    var isNewDay: Boolean = false,
    var isHeader: Boolean = true

) {


    fun onClick(context: Context, view: View?) {
        linkUrl?.let {
            context.browse(it)
        }

    }

}
/*
        "scheduleId": "71529",
        "title": "Mnet '엠카운트다운'",
        "startTime": "2021-07-01T18:00:00+09:00",
        "endTime": "2021-07-01T19:00:00+09:00",
        "startLocalTime": "2021-07-01 18:00:00",
        "endLocalTime": "2021-07-01 19:00:00",
        "allDay": "N",
        "description": "",
        "image": "",
        "location": "",
        "categoryId": "11321",
        "name": null,
        "color": null,
        "timezone": "Asia/Seoul",
        "regYyyymm": null,
        "viewCal": null
 */