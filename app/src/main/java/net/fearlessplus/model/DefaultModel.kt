package net.fearlessplus.model

import net.fearlessplus.model.base.BaseResponse
import net.fearlessplus.model.base.Result
import com.google.gson.annotations.SerializedName
import java.util.*

data class DefaultModel(
    override var result: Result,
    var datatime: Date,
    /*@SerializedName("data") */
    var data: DefaultData
) : BaseResponse() {
}

data class DefaultData(var empty: Any?)