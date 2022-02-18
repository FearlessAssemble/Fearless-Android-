package net.fearlessplus.ui.main.aboutbg

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import io.sbox.library.extensions.trace
import net.fearlessplus.R
import net.fearlessplus.model.AboutInfoDataItem
import net.fearlessplus.model.MemberInfoItem
import net.fearlessplus.model.SnsInfoItem

class AboutBgAdapter : RecyclerView.Adapter<AboutBgHolder>() {

    companion object {
        val TYPE_OFFICIAL_SNS = 1
        val TYPE_MEMBER_SNS = 2
        val TYPE_SUPPORT_SNS = 3
    }


    data class AboutBgData<V>(val type: Int, val value: V)

    private val aboutList: ArrayList<AboutBgData<Any>> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AboutBgHolder {
        return when (viewType) {
            TYPE_OFFICIAL_SNS -> AboutBgOfficialHolder(
                DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.cell_about_official,
                    parent,
                    false
                )
            )
            TYPE_MEMBER_SNS -> AboutBgMemberHolder(
                DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.cell_about_member,
                    parent,
                    false
                )
            )

            else -> AboutBgSupportHolder(
                DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.cell_about_support,
                    parent,
                    false
                )
            )
        }

    }

    override fun onBindViewHolder(holder: AboutBgHolder, position: Int) {
        holder.bind(aboutList.get(position))
    }

    override fun getItemViewType(position: Int): Int {
        return aboutList.get(position).type
    }

    override fun getItemCount() = aboutList.size


    fun setCorpSns(list: List<SnsInfoItem>) {
        aboutList.add(AboutBgData(TYPE_OFFICIAL_SNS, list))
    }

    fun setMemberSns(list: List<MemberInfoItem>) {
        list.forEach {
            aboutList.add(AboutBgData(TYPE_MEMBER_SNS, it))
        }
    }

    fun setSupportSns(list: List<AboutInfoDataItem>) {
        list.forEach {
            aboutList.add(AboutBgData(TYPE_SUPPORT_SNS, it))
        }
    }

}