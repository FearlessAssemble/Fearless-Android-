package net.fearlessplus.model

import androidx.databinding.ObservableArrayList
import io.sbox.library.extensions.trace
import net.fearlessplus.model.base.BaseResponse
import net.fearlessplus.model.base.Result

data class GalleryContentModel(
    override var result: Result,
    var data: GalleryContentObject
) : BaseResponse() {

    override fun sync() {
    }

}

data class GalleryContentObject(val list: List<GalleryContentItem>)

data class GalleryContentItem(
    var id: String,
    var name: String,
    var path: String,
    var photoCnt: Int,
    var latestEventDate: String,
    var photos: ObservableArrayList<GalleryItem>
) {


}
