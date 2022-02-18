package net.fearlessplus.model

import android.content.Context
import android.view.View
import androidx.databinding.ObservableArrayList
import io.sbox.library.extensions.*
import net.fearlessplus.R
import net.fearlessplus.extensions.launchLink
import net.fearlessplus.extensions.toStringFromRes
import net.fearlessplus.model.base.BaseResponse
import net.fearlessplus.model.base.Result

data class PushHistoryModel(
    override var result: Result,
    var data: PushHistoryData
) : BaseResponse() {
    override fun sync() {
        super.sync()

        var tempDate = ""
        data.list.forEachIndexed { index, pushHistoryItem ->

            pushHistoryItem.date = pushHistoryItem.createdDate.getDate(DatePattern.TypeFull01).getFormatDate(DatePattern.TypeDate01)
            pushHistoryItem.time = pushHistoryItem.createdDate.getDate(DatePattern.TypeFull01).getFormatDate(DatePattern.TypeTime01)

            if(pushHistoryItem.date.equals(getCurrentDate())) {
                pushHistoryItem.date = R.string.today.toStringFromRes
            }

            if(pushHistoryItem.date.equals(tempDate)) {

            } else {
                tempDate = pushHistoryItem.date
                pushHistoryItem.visibleHeader = true
                pushHistoryItem.visibleLine = index > 0
            }
        }

    }
}

data class PushHistoryData(
    var list: ObservableArrayList<PushHistoryItem>
)

data class PushHistoryItem(
    val segment: String,
    val message: String,
    val url: String,
    val createdDate: String,

    var date: String,
    var time: String,
    var visibleHeader: Boolean = false,
    var visibleLine: Boolean = false
) {

    fun onClick(context: Context, view: View?) {

        url?.let {
            context.launchLink(it)
        }

    }



}