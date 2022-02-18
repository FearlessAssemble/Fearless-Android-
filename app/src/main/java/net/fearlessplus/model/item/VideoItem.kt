package net.fearlessplus.model.item

import android.content.Context
import android.view.View
import io.sbox.library.extensions.browse


data class VideoItem(
    var id: Int,
    var code: String,
    var title: String,
    var playTime: String,
    var uploadDate: String,
    var channelTitle: String,
    var tags: List<TagItem>,
    var thumbnailImg: String
) {

    fun onClick(context: Context, view: View?) {

        code?.let {
            context.browse("https://www.youtube.com/watch?v=$it")
        }

    }

    fun imageUrl(): String {
        return "https://img.youtube.com/vi/$code/hqdefault.jpg"
    }

}

data class TagItem(
    var id: Int,
    var title: String,
    var videoCount: String)