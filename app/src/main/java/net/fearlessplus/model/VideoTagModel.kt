package net.fearlessplus.model

import net.fearlessplus.model.base.BaseResponse
import net.fearlessplus.model.base.Result

data class VideoTagModel(
    override var result: Result,
    var data: List<VideoTagItem>
) : BaseResponse() {

    override fun sync() {
    }

}

data class VideoTagItem(
    var id: Int,
    var title: String,
    var videoCount: Int
)
