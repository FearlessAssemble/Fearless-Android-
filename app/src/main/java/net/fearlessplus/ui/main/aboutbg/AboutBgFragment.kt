package net.fearlessplus.ui.main.aboutbg

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ObservableArrayList
import androidx.recyclerview.widget.*
import io.sbox.library.view.adapter.RecyclerAdapter
import io.sbox.library.view.item.SpaceItemDecoration
import kotlinx.android.synthetic.main.component_appbar.*
import kotlinx.android.synthetic.main.fragment_about_bg.*
import kotlinx.android.synthetic.main.item_recyclerview.*
import kotlinx.android.synthetic.main.item_recyclerview.recyclerview
import kotlinx.android.synthetic.main.layout_recyclerview.*
import net.fearlessplus.BR
import net.fearlessplus.R
import net.fearlessplus.databinding.CellAboutMemberBinding
import net.fearlessplus.databinding.CellAboutOfficialBinding
import net.fearlessplus.extensions.toPx
import net.fearlessplus.extensions.toStringFromRes
import net.fearlessplus.model.SnsInfoItem
import net.fearlessplus.network.Api
import net.fearlessplus.network.send
import net.fearlessplus.ui.base.BaseFragment

class AboutBgFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_recyclerview, container, false)
    }

    private val adapter = AboutBgAdapter()
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        appbar_title.setText(R.string.aboutbg)
        appbar.setAppbar(notification = true, setting = true)


        recyclerview.setHasFixedSize(false)

        val mLayoutManager = GridLayoutManager(context, 2)
        mLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (adapter.getItemViewType(position) == AboutBgAdapter.TYPE_MEMBER_SNS) {
                    1
                } else 2
            }
        }

        recyclerview_bg.setPadding(10.toPx, 0, 10.toPx, 0)
        recyclerview.setLayoutManager(mLayoutManager)

        getContentSns()

    }


    fun getContentSns() {
        Api.service.getSnsInfoList().send({
            adapter.setCorpSns(it.data.corp_sns)
            adapter.setMemberSns(it.data.member_sns)

            getContentSupport()
        })
    }

    fun getContentSupport() {
        Api.service.getAboutInfoList().send({
            adapter.setSupportSns(it.data.list)

            recyclerview.adapter = adapter
            adapter.notifyDataSetChanged()
        })
    }


}

