package net.fearlessplus.ui.main.gallery

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.os.bundleOf
import androidx.databinding.library.baseAdapters.BR
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import io.sbox.library.extensions.*
import io.sbox.library.view.item.SpaceItemDecoration
import io.sbox.library.view.paging.PagingAdapter
import kotlinx.android.synthetic.main.fragment_recyclerview_refresh.*
import kotlinx.android.synthetic.main.layout_recyclerview.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import net.fearlessplus.R
import net.fearlessplus.core.Config
import net.fearlessplus.core.EventType
import net.fearlessplus.databinding.CellGalleryListBinding
import net.fearlessplus.extensions.newFragment
import net.fearlessplus.extensions.toPx
import net.fearlessplus.model.GalleryContentModel
import net.fearlessplus.model.GalleryItem
import net.fearlessplus.ui.base.BaseFragment
import net.fearlessplus.ui.main.gallery.folder.GalleryFolderSource
import net.fearlessplus.ui.main.gallery.paging.GalleryAdapter
import net.fearlessplus.ui.main.gallery.paging.GalleryModel
import net.fearlessplus.ui.main.gallery.paging.GalleryViewModel
import net.fearlessplus.ui.main.gallery.viewer.GalleryBrowserFragment


@Suppress("DEPRECATION")
class GalleryListFragment : BaseFragment() {


    companion object {
        @JvmStatic
        fun newInstance(id: String = "") =
            GalleryListFragment().apply {
                arguments = bundleOf("id".to(id))
            }

        var allImages: List<GalleryItem> = listOf()

        var defaultType = true
    }

    private var id: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            id = it.getString("id", "")
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

        val numberOfColumns = if (resources.getBoolean(R.bool.is_smart_phone)) if(defaultType) 4 else 5  else 6
        val mLayoutManager = GridLayoutManager(context, numberOfColumns)

        mLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (pagingAdapter.getItemViewType(position) == GalleryAdapter.DATA) {
                    1
                } else numberOfColumns
            }
        }



        recyclerview.addItemDecoration(
            SpaceItemDecoration(
                8.toPx,
                0.toPx, /*24.toPx*/
                numberOfColumns
            )
        )
        recyclerview.setLayoutManager(mLayoutManager)
        recyclerview_bg.setPadding(20.toPx, 0, 20.toPx, 0)

        try {
            swipe_refresh_recyclerview.javaClass.getDeclaredField("mCircleView")?.let { f ->
                f.setAccessible(true)
                val img: ImageView = f.get(swipe_refresh_recyclerview) as ImageView
                img.setImageResource(R.drawable.ico_bg_logo)
            }
        } catch (e: Exception) {

        }

        val animator: RecyclerView.ItemAnimator? = recyclerview.getItemAnimator()
        if (animator is SimpleItemAnimator) {
            animator.supportsChangeAnimations = false
        }

        addEventReceiver(EventType.NAVIGATION_RESELECTED) {
            recyclerview.scrollToPositionSpeed(0)
        }

        addEventReceiver("GALLERY_TYPE") {

            trace("@@@@@@@@@@@@@@@  gg ", GalleryListFragment.defaultType)

            val numberOfColumns = if (resources.getBoolean(R.bool.is_smart_phone)) if(defaultType) 4 else 5  else 6
            val mLayoutManager = GridLayoutManager(context, numberOfColumns)

            mLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return if (pagingAdapter.getItemViewType(position) == GalleryAdapter.DATA) {
                        1
                    } else numberOfColumns
                }
            }
            recyclerview.setLayoutManager(mLayoutManager)


            val ft: FragmentTransaction = requireFragmentManager().beginTransaction()
            if (Build.VERSION.SDK_INT >= 26) {
                ft.setReorderingAllowed(false)
            }
            ft.detach(this).attach(this).commit()

//            getContent()
        }

        swipe_refresh_recyclerview.setOnRefreshListener {
            getContent()
        }
        getContent()
    }

    fun getContent() {
        if (id.isNotEmpty()) {
            getFolderContents(id)
        } else {
            getAllContent()
        }
    }


    private val viewModel by viewModels<GalleryViewModel>()
    private val pagingAdapter by lazy { GalleryAdapter(onClickItemListener) }
    open fun getAllContent() {
        context?.hideLoading()

        swipe_refresh_recyclerview.isRefreshing = false

/*
        recyclerview.adapter = PagingAdapter<CellContentListBinding, GalleryItem>(
            R.layout.cell_content_list, BR.data, BR.listener to onClickItemListener
        ).addPagingSource(
            this,
            GalleryPagingSource(Config.pagedListPageSize),
            Config.pagedListPageSize
        )
*/




        recyclerview.adapter = pagingAdapter/*.withLoadStateHeaderAndFooter(
            PagingAdapter.PagingLoadStateAdapter { pagingAdapter.retry() },
            PagingAdapter.PagingLoadStateAdapter { pagingAdapter.retry() }
        )*/

        lifecycleScope.launch {
            if(GalleryListFragment.defaultType) {
                viewModel.pagingData.collectLatest {
                    pagingAdapter.submitData(it)
                }
            } else {
                viewModel.pagingDataMonth.collectLatest {
                    pagingAdapter.submitData(it)
                }
            }
        }


    }


    interface OnClickItemListener {
        fun onClick(content: Context, item: GalleryItem)
    }


    var onClickItemListener = object : OnClickItemListener {
        override fun onClick(content: Context, item: GalleryItem) {
            recyclerview.adapter?.let { adapter ->
                if (adapter is PagingAdapter<*, *>) {
                    allImages = adapter.snapshot() as List<GalleryItem>
                    val position = allImages.indexOf(item)
                    context.newFragment<GalleryBrowserFragment>(
                        GalleryBrowserFragment.getBundle(
                            position
                        )
                    )
                } else if (adapter is ConcatAdapter) {
                    adapter.adapters.forEach {
                        if (it is GalleryAdapter) {
                            val snap = it.snapshot().filter { it is GalleryModel.Data }
                            val array = arrayListOf<GalleryItem>()
                            snap.forEach {
                                if(it is GalleryModel.Data) {
                                    array.add(it.value)
                                }
                            }
                            allImages = array.toList()

                            val position = allImages.indexOf(item)
                            context.newFragment<GalleryBrowserFragment>(
                                GalleryBrowserFragment.getBundle(
                                    position
                                )
                            )
                        }
                    }


                } else if (adapter is GalleryAdapter) {

                    val snap = adapter.snapshot().filter { it is GalleryModel.Data }
                    val array = arrayListOf<GalleryItem>()
                    snap.forEach {
                        if(it is GalleryModel.Data) {
                            array.add(it.value)
                        }
                    }
                    allImages = array.toList()

                    val position = allImages.indexOf(item)
                    context.newFragment<GalleryBrowserFragment>(
                        GalleryBrowserFragment.getBundle(
                            position
                        )
                    )
                }

            }

        }
    }


    fun getFolderContents(id: String) {
        trace("@@@@@@@@@@ id $id")



        swipe_refresh_recyclerview.isRefreshing = false


        recyclerview.adapter = PagingAdapter<CellGalleryListBinding, GalleryItem>(
            R.layout.cell_gallery_list, BR.data, BR.listener to onClickItemListener
        ).addPagingSource(
            this,
            GalleryFolderSource(id, Config.pagedListPageSize),
            Config.pagedListPageSize
        )

    }

    fun setFolderContents(items: GalleryContentModel) {

        trace("@@@@@@@@@  items $items")


        /*

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
                    var adapter = RecyclerAdapter<CellContentListBinding, GalleryItem>(
                        it,
                        R.layout.cell_content_list,
                        BR.data
                    )
                    recyclerview.setAdapter(adapter)


                    items.data.list.forEachIndexed { index, item ->
                        if (index == 0) {

                        } else {
                            items.data.list.getOrNull(0)?.photos?.addAll(item.photos)
                        }

                    }

                    adapter.addItem(items.data.list.get(0).photos)
                }
            }
        }

        */
    }


}