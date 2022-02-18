package net.fearlessplus.model

import net.fearlessplus.model.base.BaseResponse
import net.fearlessplus.model.base.Result

data class GalleryFolderModel(
    override var result: Result,
    var data: GalleryFolderObject
) : BaseResponse() {

    override fun sync() {

        data.list.forEach {
            var prefix = it.name.split("_").getOrNull(0)?:""
            if (prefix.isNotEmpty()) prefix = prefix + "_"
            it.name = it.name.replace(prefix, "")
        }

    }
}

data class GalleryFolderObject(val list: List<GalleryFolderItem>) {
    val menuList: List<GalleryFolderItem>
        get() {
            var value = arrayListOf(GalleryFolderItem("", "ALL", ""))
//            value.addAll(list)
            return value
        }
}


data class GalleryFolderItem(
    var id: String,
    var name: String,
    var path: String
)
