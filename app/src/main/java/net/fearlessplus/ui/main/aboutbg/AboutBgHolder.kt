package net.fearlessplus.ui.main.aboutbg

import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableArrayList
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import io.sbox.library.extensions.trace
import io.sbox.library.view.adapter.RecyclerAdapter
import kotlinx.android.synthetic.main.cell_about_member.view.*
import kotlinx.android.synthetic.main.cell_about_official.view.*
import net.fearlessplus.BR
import net.fearlessplus.R
import net.fearlessplus.databinding.*
import net.fearlessplus.extensions.toPx
import net.fearlessplus.model.AboutInfoDataItem
import net.fearlessplus.model.MemberInfoItem
import net.fearlessplus.model.SnsInfoItem

open class AboutBgHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    open fun bind(data: AboutBgAdapter.AboutBgData<Any>) {}
}


class AboutBgOfficialHolder(var binding: CellAboutOfficialBinding) : AboutBgHolder(binding.root) {
    override fun bind(data: AboutBgAdapter.AboutBgData<Any>) {

        binding.root.apply {

            data.value?.let { value ->
                if(value is ObservableArrayList<*>) {
                    try {
                        var items = value as ObservableArrayList<SnsInfoItem>
                        fragment_about_official_sns.adapter = RecyclerAdapter<CellAboutSnsBinding, SnsInfoItem>(
                            context,
                            R.layout.cell_about_sns,
                            BR.data
                        ).apply {
                            addItem(items)
                        }
                    } catch (e: Exception) {}
                }
            }
        }
    }
}

class AboutBgMemberHolder(var binding: CellAboutMemberBinding) : AboutBgHolder(binding.root) {
    override fun bind(data: AboutBgAdapter.AboutBgData<Any>) {

        binding.root.apply {

//            cell_member_sns_recyclerview.addItemDecoration(SectionItemDecoration())

            data.value?.let {

                if (it is MemberInfoItem) {
                    binding.data = it

                    it.snsList?.let {  value ->
                        if(value is ObservableArrayList<*>) {
                            try {
                                var items = value as ObservableArrayList<SnsInfoItem>
                                cell_member_sns_recyclerview.adapter = RecyclerAdapter<CellAboutMemberSnsBinding, SnsInfoItem>(
                                    context,
                                    R.layout.cell_about_member_sns,
                                    BR.data
                                ).apply {
                                    addItem(items)
                                }
                            } catch (e: Exception) {}
                        }
                    }
                }
            }
        }

    }

}

class AboutBgSupportHolder(var binding: CellAboutSupportBinding) : AboutBgHolder(binding.root) {
    override fun bind(data: AboutBgAdapter.AboutBgData<Any>) {
        data.value?.let {
            if(it is AboutInfoDataItem) binding.data = it
        }
    }
}

class SectionItemDecoration(val margin: Int = 0) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {

        val cnt = parent.adapter?.itemCount ?: 0
        val position = parent.getChildAdapterPosition(view)
        if (position == 0) {
            outRect.left = margin.toPx
        } else if (position == cnt - 1) {
            outRect.right = margin.toPx
        }
    }
}

class HomeLinearSnapHelper : LinearSnapHelper() {

    override fun findSnapView(layoutManager: RecyclerView.LayoutManager?): View? {

        var lm = layoutManager
        lm?.let {
            if (it is LinearLayoutManager) {
                if (it.findFirstCompletelyVisibleItemPosition() == 0) {
                    return it.getChildAt(0)
                } else if (it.findLastCompletelyVisibleItemPosition() == lm.itemCount - 1) {
                    return it.getChildAt(lm.itemCount - 1)
                }
            }
        }
        return super.findSnapView(layoutManager)
    }
}