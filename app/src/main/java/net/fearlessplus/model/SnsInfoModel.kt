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

data class SnsInfoModel(
    override var result: Result,
    var data: SnsInfoData
) : BaseResponse() {
    override fun sync() {
        super.sync()

        data.corp_sns.forEach {
            it.snsIconResId = when(it.snsKind) {
                "instagram" -> R.drawable.ico_instagram
                "twitter" -> R.drawable.ico_twitter
                "post.naver" -> R.drawable.ico_naver
                "facebook" -> R.drawable.ico_facebook
                "vlive" -> R.drawable.ico_vlive
                else -> R.drawable.ico_youtube
            }
        }

        data.member_sns.forEach {
            it.snsList.forEach {
                it.snsIconResId = when(it.snsKind) {
                    "instagram" -> R.drawable.ico_small_instagram
                    "twitter" -> R.drawable.ico_small_twitter
                    "tiktok" -> R.drawable.ico_small_tictok
                    else -> R.drawable.ico_small_youtube
                }
            }


            it.name = it.userId
            when(it.name) {
                "MINYOUNG" -> {
                    it.imageRes = R.drawable.minyeong
                    it.profileImage = "https://cdn.fearlessplus.net/profile/img/minyeong.png"
                }
                "YUJEONG" -> {
                    it.imageRes = R.drawable.yujeong
                    it.profileImage = "https://cdn.fearlessplus.net/profile/img/yujeong.png"
                }
                "EUNJI" -> {
                    it.imageRes = R.drawable.eunji
                    it.profileImage = "https://cdn.fearlessplus.net/profile/img/eunji.png"
                }
                "YUNA" -> {
                    it.imageRes = R.drawable.yuna
                    it.profileImage = "https://cdn.fearlessplus.net/profile/img/yuna.png"
                }
            }
        }
    }
}

data class SnsInfoData(
    var corp_sns: ObservableArrayList<SnsInfoItem>,
    var member_sns: List<MemberInfoItem>
)

data class MemberInfoItem(
    val birthday: String,
    val position: String,
    val userId: String,

    var name: String,
    var profileImage: String,
    var imageRes: Int,

    var snsList: ObservableArrayList<SnsInfoItem>
)

data class SnsInfoItem(
    val snsKind: String,
    val snsUrl: String,

    var snsIconResId: Int
) {

    fun onClick(context: Context, view: View?) {

        snsUrl?.let {
            context.launchLink(it)
        }
    }

}