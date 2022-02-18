package net.fearlessplus.ui.main.bgtube

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import io.sbox.library.extensions.addEventReceiver
import io.sbox.library.extensions.scrollToPositionSpeed
import io.sbox.library.extensions.visible
import io.sbox.library.view.item.SpaceItemDecoration
import io.sbox.library.view.paging.PagingAdapter
import kotlinx.android.synthetic.main.fragment_recyclerview_refresh.*
import kotlinx.android.synthetic.main.layout_recyclerview.*
import net.fearlessplus.BR
import net.fearlessplus.R
import net.fearlessplus.core.Config
import net.fearlessplus.core.EventType
import net.fearlessplus.databinding.CellVideoListBinding
import net.fearlessplus.extensions.toPx
import net.fearlessplus.model.item.VideoItem
import net.fearlessplus.ui.base.BaseFragment

class BgTubeListFragment : BaseFragment() {


    companion object {
        @JvmStatic
        fun newInstance(tag: Int = 0) =
            BgTubeListFragment().apply {
                arguments = bundleOf("tag".to(tag))
            }
    }

    private var tag: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            tag = it.getInt("tag")
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

        if (resources.getBoolean(R.bool.is_smart_phone)) {
            recyclerview.addItemDecoration(SpaceItemDecoration(14.toPx, 12.toPx))
            recyclerview.setLayoutManager(LinearLayoutManager(context))
            recyclerview_bg.setPadding(20.toPx, 0, 20.toPx, 0)
        } else {
            val numberOfColumns = 2
            val mLayoutManager = StaggeredGridLayoutManager(
                numberOfColumns,
                StaggeredGridLayoutManager.VERTICAL
            )
            mLayoutManager.gapStrategy =
                StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS
            recyclerview.addItemDecoration(
                SpaceItemDecoration(
                    13.toPx,
                    24.toPx,
                    numberOfColumns
                )
            )
            recyclerview.setLayoutManager(mLayoutManager)
            recyclerview_bg.setPadding(20.toPx, 0, 20.toPx, 0)
        }

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

        recyclerview.adapter = PagingAdapter<CellVideoListBinding, VideoItem>(
            R.layout.cell_video_list, BR.data
        ).addPagingSource(
            this,
            BGTubePagingSource(tag, Config.pagedListPageSize),
            Config.pagedListPageSize
        )

    }

}