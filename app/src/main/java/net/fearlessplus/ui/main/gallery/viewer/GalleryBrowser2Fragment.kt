package net.fearlessplus.ui.main.gallery.viewer

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import io.sbox.library.extensions.trace
import io.sbox.library.view.paging.PagingAdapter
import kotlinx.android.synthetic.main.fragment_gallery_browser.*
import kotlinx.android.synthetic.main.view_gallery_gif.view.*
import kotlinx.android.synthetic.main.view_gallery_image.view.*
import kotlinx.android.synthetic.main.view_gallery_image.view.gallery_thumb
import kotlinx.android.synthetic.main.view_gallery_video.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import net.fearlessplus.R
import net.fearlessplus.databinding.ViewGalleryGifBinding
import net.fearlessplus.databinding.ViewGalleryImageBinding
import net.fearlessplus.databinding.ViewGalleryVideoBinding
import net.fearlessplus.model.GalleryContentType
import net.fearlessplus.model.GalleryItem
import net.fearlessplus.ui.base.BaseFragment
import net.fearlessplus.ui.main.gallery.GalleryListFragment
import net.fearlessplus.ui.main.gallery.viewer.GalleryBrowser2Fragment.Companion.isStart
import net.fearlessplus.ui.main.gallery.viewer.GalleryBrowser2Fragment.Companion.selectPosition


class GalleryBrowser2Fragment : BaseFragment {


    companion object {

        fun newInstance(
            allImages: List<GalleryItem>,
            imagePosition: Int,
            anim: Context?
        ): GalleryBrowser2Fragment {
            return GalleryBrowser2Fragment(allImages, imagePosition, anim)
        }

        @JvmStatic
        fun newInstance(bundle: Bundle?) =
            GalleryBrowser2Fragment().apply {
                arguments = bundle
            }

        @JvmStatic
        fun getBundle(
            position: Int
        ) = bundleOf(
            "position" to position
        )

        var isStart = false
        var selectPosition = 0
    }


    private var allImages: List<GalleryItem> = listOf()
    private var animeContx: Context? = null

    constructor() {
        this.allImages = GalleryListFragment.allImages
        animeContx = context
    }

    constructor(allImages: List<GalleryItem>, imagePosition: Int, anim: Context?) {
        this.allImages = allImages
        selectPosition = imagePosition
        animeContx = anim
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_gallery_browser, container, false)
    }

    private val viewModel by activityViewModels<PagingAdapter.PagingViewModel<GalleryItem>>()

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            selectPosition = it.getInt("position", 0)
        }

        /*
        viewpager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
                isStart = false
//                this@GalleryBrowserFragment.dispatcherEvent("VIDEO_STATE_CHANAGE")
            }

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                selectPosition = position
                if (allImages.get(position).isVideoType) {
                    isStart = true
                }

                (viewpager.get(0) as RecyclerView).layoutManager?.findViewByPosition(position)?.gallery_video?.let { videoView ->
                    videoView.start()
                }
            }
        })
        viewpager?.adapter = PagerAdapter(allImages)
        */

        viewpager?.setCurrentItem(selectPosition, false)

    }





    class PagerAdapter(private val vo: List<GalleryItem>) :
        RecyclerView.Adapter<GalleryHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryHolder {
            return when (viewType) {
                GalleryContentType.VIDEO.index -> VideoViewHolder(
                    DataBindingUtil.inflate(
                        LayoutInflater.from(parent.context),
                        R.layout.view_gallery_video,
                        parent,
                        false
                    )
                )
                GalleryContentType.GIF.index -> GifViewHolder(
                    DataBindingUtil.inflate(
                        LayoutInflater.from(parent.context),
                        R.layout.view_gallery_gif,
                        parent,
                        false
                    )
                )
                else -> ImageViewHolder(
                    DataBindingUtil.inflate(
                        LayoutInflater.from(parent.context),
                        R.layout.view_gallery_image,
                        parent,
                        false
                    )
                )
            }
        }

        override fun onBindViewHolder(holder: GalleryHolder, position: Int) {
            holder.bind(vo.get(position), position)
        }

        override fun getItemViewType(position: Int): Int {
            return vo.get(position).contentType
        }

        override fun onViewRecycled(holder: GalleryHolder) {
            super.onViewRecycled(holder)
            holder.onDestroy()
        }

        override fun getItemCount(): Int = vo.size
    }

    open class GalleryHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        open fun bind(data: GalleryItem, position: Int) {
        }

        open fun onDestroy() {
        }
    }

    class VideoViewHolder(val binding: ViewGalleryVideoBinding) :
        GalleryHolder(binding.root) {
        init {
            binding.root.apply {
                val controller = MediaController(context)
                gallery_video.setMediaController(controller)
                gallery_video.requestFocus()
            }
        }

        override fun bind(data: GalleryItem, position: Int) {
            binding.data = data
            binding.root.gallery_video?.apply {
                setOnPreparedListener {
                    trace(
                        isStart && selectPosition == position
                    )
                    if (isStart && selectPosition == position) {
                        start()
                    }
                }
                setOnCompletionListener {
                }
                setVideoURI(Uri.parse(data.contentUrl))
                seekTo(0)
            }
        }


    }

    class ImageViewHolder(val binding: ViewGalleryImageBinding) :
        GalleryHolder(binding.root) {
        override fun bind(data: GalleryItem, position: Int) {
            binding.data = data

            binding.root.apply {

                Glide.with(this)
                    .asBitmap()
                    .load(data.contentUrl)
                    .apply(RequestOptions().fitCenter())
                    .listener(object : RequestListener<Bitmap> {
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Bitmap>?,
                            isFirstResource: Boolean
                        ): Boolean {
                            return false
                        }

                        override fun onResourceReady(
                            resource: Bitmap?,
                            model: Any?,
                            target: Target<Bitmap>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                            resource?.let {
                                CoroutineScope(Main).launch {
                                    gallery_thumb.visibility = View.GONE
                                    gallery_content.setImageBitmap(it)
                                }
                            }
                            return false
                        }
                    }).submit()
            }
        }

        override fun onDestroy() {
//            binding.root.destroy()
        }
    }

    class GifViewHolder(val binding: ViewGalleryGifBinding) :
        GalleryHolder(binding.root) {
        init {
            binding.root.apply {
            }
        }

        override fun bind(data: GalleryItem, position: Int) {
            binding.data = data

            binding.root.apply {
                Glide.with(gallery_gif)
                    .asGif()
                    .load(data.contentUrl)
                    .apply(RequestOptions().fitCenter())
                    .into(gallery_gif)
            }
        }
    }

}