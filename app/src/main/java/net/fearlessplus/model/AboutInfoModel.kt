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

data class AboutInfoModel(
    override var result: Result,
    var data: AboutInfoData
) : BaseResponse() {
    override fun sync() {
        super.sync()
        data.list.first().isTop = true
    }
}

data class AboutInfoData(
    var list: List<AboutInfoDataItem>
)

data class AboutInfoDataItem(
    val id: String,
    val title: String,
    val link: String,

    var isTop: Boolean = false
) {

    fun onClick(context: Context, view: View?) {

        link?.let {
            context.launchLink(it)
        }
    }

}