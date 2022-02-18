package net.fearlessplus.ui.main.information

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.leveloper.stickheader.StickyHeaderItemDecoration
import io.sbox.library.extensions.addEventReceiver
import io.sbox.library.extensions.scrollToPositionSpeed
import io.sbox.library.extensions.visible
import io.sbox.library.view.adapter.RecyclerAdapter
import io.sbox.library.view.item.SpaceItemDecoration
import io.sbox.library.view.paging.PagingAdapter
import kotlinx.android.synthetic.main.fragment_recyclerview_refresh.*
import kotlinx.android.synthetic.main.layout_recyclerview.*
import net.fearlessplus.BR
import net.fearlessplus.R
import net.fearlessplus.core.Config
import net.fearlessplus.core.EventType
import net.fearlessplus.databinding.CellChartListBinding
import net.fearlessplus.databinding.CellEventListBinding
import net.fearlessplus.extensions.toPx
import net.fearlessplus.model.EventModel
import net.fearlessplus.model.ScheduleModel
import net.fearlessplus.model.item.ChartItem
import net.fearlessplus.model.item.EventItem
import net.fearlessplus.network.Api
import net.fearlessplus.network.send
import net.fearlessplus.ui.base.BaseFragment

class InformationListFragment : BaseFragment() {
    companion object {
        @JvmStatic
        fun newInstance(position: Int?) =
            InformationListFragment().apply {
                arguments = bundleOf("position".to(position))
            }
    }

    private var position: Int = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            position = it.getInt("position")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_recyclerview_refresh, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        fragment_recyclerview_refresh_appbar.visible = false

        recyclerview_bg.setBackgroundResource(R.color.gray07)

        when (InformationCategory.values().get(position)) {
            InformationCategory.event -> {
                recyclerview.addItemDecoration(SpaceItemDecoration(20.toPx, 0.toPx))
                recyclerview.setPadding(0.toPx, 0, 0.toPx, 0)
            }
            InformationCategory.schedule -> {
//                recyclerview.setPadding(0, 20.toPx, 0, 0)
            }
            InformationCategory.chart -> {
                recyclerview.addItemDecoration(SpaceItemDecoration(20.toPx, 20.toPx))
                recyclerview.setPadding(20.toPx, 0, 20.toPx, 0)
            }
        }

        recyclerview.setLayoutManager(LinearLayoutManager(context))

        try {
            swipe_refresh_recyclerview.javaClass.getDeclaredField("mCircleView")?.let { f ->
                f.setAccessible(true)
                val img: ImageView = f.get(swipe_refresh_recyclerview) as ImageView
                img.setImageResource(R.drawable.ico_bg_logo)
            }
        } catch (e: Exception) {

        }

        addEventReceiver(EventType.NAVIGATION_RESELECTED) {
            recyclerview.scrollToPositionSpeed(0)
        }


        swipe_refresh_recyclerview.setOnRefreshListener {
            getContent()
        }
        getContent()
    }


    open fun getContent() {

        swipe_refresh_recyclerview.isRefreshing = false

        val animator: RecyclerView.ItemAnimator? = recyclerview.getItemAnimator()
        if (animator is SimpleItemAnimator) {
            animator.supportsChangeAnimations = false
        }

        when (InformationCategory.values().get(position)) {
            InformationCategory.event -> {
                Api.service.getEventList().send(this::setContent)
            }
            InformationCategory.schedule -> {
                Api.service.getScheduleList().send(this::setContent)
            }
            InformationCategory.chart -> {
                recyclerview.adapter = PagingAdapter<CellChartListBinding, ChartItem>(
                    R.layout.cell_chart_list, BR.data
                ).addPagingSource(
                    this,
                    ChartPagingSource(Config.pagedListPageSize),
                    Config.pagedListPageSize
                )

            }
        }
    }

    fun setContent(items: EventModel) {
        swipe_refresh_recyclerview.isRefreshing = false
        recyclerview?.let {
            if (items.data.list.size == 0) {

                recyclerview?.adapter?.let { adapter ->
                    if (adapter is RecyclerAdapter<*, *>) {
                        adapter.items?.clear()
                    }
                }
            } else {
                emptyview?.visible = false

                context?.let {
                    var adapter = RecyclerAdapter<CellEventListBinding, EventItem>(
                        it,
                        R.layout.cell_event_list,
                        BR.data
                    )
                    recyclerview.setAdapter(adapter)
                    adapter.addItem(items.data.list)
                }
            }
        }
    }


    fun setContent(items: ScheduleModel) {
        swipe_refresh_recyclerview.isRefreshing = false

        adapter = ScheduleAdapter(items.data.list)
        recyclerview.layoutManager = LinearLayoutManager(context)
        recyclerview.adapter = adapter
        recyclerview.addItemDecoration(StickyHeaderItemDecoration(getSectionCallback()))

        recyclerview.scrollToPosition(items.data.startIndex)
    }


    private lateinit var adapter: ScheduleAdapter
    private fun getSectionCallback(): StickyHeaderItemDecoration.SectionCallback {
        return object : StickyHeaderItemDecoration.SectionCallback {
            override fun isHeader(position: Int): Boolean {
                return adapter.isHeader(position)
            }

            override fun getHeaderLayoutView(list: RecyclerView, position: Int): View? {
                return adapter.getHeaderView(list, position)
            }
        }
    }


}