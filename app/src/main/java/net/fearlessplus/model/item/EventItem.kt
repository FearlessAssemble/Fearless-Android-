package net.fearlessplus.model.item

import android.content.Context
import android.view.View
import io.sbox.library.extensions.browse


data class EventItem(
    var id: Int,
    var title: String,
    var startTime: String,
    var endTime: String,
    var description: String,
    var thumbnailImg: String,
    var url: String,
    var regDtm: String,
    var viewStart: String,
    var viewEnd: String,
    var isInProgress: String,
    var viewCal: String,
    var limitDay: String,
    var shortDesc: String,
    var category: String
) {

    fun onClick(context: Context, view: View?) {

        url?.let {
            context.browse(it)
        }

    }

    fun date(): String {
        return "$viewStart ~ $viewEnd"
    }



}