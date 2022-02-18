package net.fearlessplus.model

import io.sbox.library.extensions.trace
import net.fearlessplus.BuildConfig
import net.fearlessplus.model.base.BaseResponse
import net.fearlessplus.model.base.Result

data class AppInfoModel(
    override var result: Result,
    var data: AppInfoData
) : BaseResponse() {
    override fun sync() {
        super.sync()

        val currentVersionCode = BuildConfig.VERSION_CODE
        if(currentVersionCode < data.forceUpdatedCode) {
            data.isUpdateImmediate = true
        } else if(currentVersionCode < data.latestVersionCode) {
            data.isUpdateFlexible = true
        }
    }
}

data class AppInfoData(
    var forceUpdatedCode: Int,
    var latestVersionName: String,
    var latestVersionCode: Int,

    var isUpdateFlexible: Boolean = false,
    var isUpdateImmediate: Boolean = false
) {
    init {

        trace("@@@@@@@@@  app init")
    }
}
